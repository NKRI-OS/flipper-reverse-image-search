package gal.udc.fic.muei.tfm.dap.flipper.web.rest;

import com.codahale.metrics.annotation.Timed;
import gal.udc.fic.muei.tfm.dap.flipper.domain.Picture;
import gal.udc.fic.muei.tfm.dap.flipper.domain.User;
import gal.udc.fic.muei.tfm.dap.flipper.repository.PictureRepository;
import gal.udc.fic.muei.tfm.dap.flipper.repository.UserRepository;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto.PictureDTO;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto.PictureListDTO;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.mapper.PictureMapper;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing Picture.
 *
 * This file is part of Flipper Open Reverse Image Search.

 Flipper Open Reverse Image Search is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Flipper Open Reverse Image Search is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Flipper Open Reverse Image Search.  If not, see <http://www.gnu.org/licenses/>.
 */
@RestController
@RequestMapping("/api")
public class PictureResource {

    private final Logger log = LoggerFactory.getLogger(PictureResource.class);

    @Inject
    private PictureRepository pictureRepository;

    @Inject
    private PictureMapper pictureMapper;

    @Inject
    private UserRepository userRepository;

    /**
     * GET  /pictures -> get all the pictures.
     */
    @RequestMapping(value = "/pictures",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<PictureListDTO>> getAll(Pageable pageable) {
        log.debug("REST request to get all Pictures");
        try {
            Page<Picture> page = pictureRepository.findAllOrdered(pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pictures");
            return new ResponseEntity<>(page.getContent().stream()
                .map(pictureMapper::pictureToPictureListDTO)
                .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                .header("Failure", e.getMessage())
                .body(null);
        }
    }

    /**
     * GET  /pictures/:id -> get the "id" picture.
     */
    @RequestMapping(value = "/pictures/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PictureDTO> get(@PathVariable String id) {
        log.debug("REST request to get Picture : {}", id);
        return Optional.ofNullable(pictureRepository.findOne(UUID.fromString(id)))
            .map(pictureMapper::pictureToPictureDTO)
            .map(pictureDTO -> new ResponseEntity<>(
                pictureDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /pictures/byUser/:owner -> get all the pictures from a owner.
     */
    @RequestMapping(value = "/pictures/byUser/{owner}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<PictureListDTO>> getAllFromOwner(@PathVariable String owner, Pageable pageable) {
        log.debug("REST request to get all Pictures");
        Optional<User> user = userRepository.findOneByLogin(owner);
        if(user.isPresent())
        {
            try {
                /* get pictures from user */
                Page<Picture> page = pictureRepository.findByOwnerOrdered(owner, pageable);
                HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pictures/byUser/" + owner);
                return new ResponseEntity<>(page.getContent().stream()
                    .map(pictureMapper::pictureToPictureListDTO)
                    .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest()
                    .header("Failure", e.getMessage())
                    .body(null);
            }
        }

        // if user not found
        return ResponseEntity.badRequest()
            .header("Failure", "user not found")
            .body(null);
    }

    /**
     * SEARCH  /_search/pictures/:query -> search for the category corresponding
     * to the query.
     * Valid values for query: <i>searchText</i> or <i>field:searchText</i>
     * Valid wildcards for searchText: *, ?, -, !
     * Valid columns for query:
     *    title text (by default)
     *    description text
     *    created timestamp
     *    owner text
     *    modified timestamp
     *    modifiedby text
     *    autocolorcorrelogram list<double>
     *    autocolorcorrelogramasbase64 text
     *    cedd list<double>
     *    ceddasbase64 text
     *    colorhistogram list<double>
     *    colorhistogramasbase64 text
     *    colorlayout list<double>
     *    colorlayoutasbase64 text
     *    edgehistogram list<double>
     *    edgehistogramasbase64 text
     *    phog list<double>
     *    phogasbase64 text
     */
    @RequestMapping(value = "/_search/pictures/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public List<PictureListDTO> search(@PathVariable String query) {
        List<PictureListDTO> result = new ArrayList<>();

        /* convert to lower case */
        if(query.isEmpty()){
            query = "*";
        }else {
            query = query.toLowerCase();
        }

        result.addAll(pictureRepository.search(query).stream()
            .map(pictureMapper::pictureToPictureListDTO)
            .collect(Collectors.toList()));

        return result;
    }

}

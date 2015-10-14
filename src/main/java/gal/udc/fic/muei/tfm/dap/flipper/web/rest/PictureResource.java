package gal.udc.fic.muei.tfm.dap.flipper.web.rest;

import com.codahale.metrics.annotation.Timed;
import gal.udc.fic.muei.tfm.dap.flipper.domain.Picture;
import gal.udc.fic.muei.tfm.dap.flipper.domain.User;
import gal.udc.fic.muei.tfm.dap.flipper.repository.PictureRepository;
import gal.udc.fic.muei.tfm.dap.flipper.repository.UserRepository;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto.PictureDTO;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto.PictureListDTO;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.mapper.PictureMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
    public ResponseEntity<List<PictureListDTO>> getAll() {
        log.debug("REST request to get all Pictures");

        List<Picture> pictures = pictureRepository.findAll();

        List<PictureListDTO> pictureListDTOs = pictures.stream()
            .map(pictureMapper::pictureToPictureListDTO)
            .collect(Collectors.toList());
        return new ResponseEntity<>(pictureListDTOs, HttpStatus.OK);

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
    public ResponseEntity<List<PictureListDTO>> getAllFromOwner(@PathVariable String owner) {
        log.debug("REST request to get all Pictures");
        Optional<User> user = userRepository.findOneByLogin(owner);
        if(user.isPresent())
        {
            List<Picture> pictures = pictureRepository.findByOwner(owner);

            List<PictureListDTO> pictureListDTOs = pictures.stream()
                .map(pictureMapper::pictureToPictureListDTO)
                .collect(Collectors.toList());
            return new ResponseEntity<>(pictureListDTOs, HttpStatus.OK);

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

        result.addAll(pictureRepository.search(query).stream()
            .map(pictureMapper::pictureToPictureListDTO)
            .collect(Collectors.toList()));

        return result;
    }

}

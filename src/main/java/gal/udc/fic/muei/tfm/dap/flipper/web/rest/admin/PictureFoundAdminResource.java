package gal.udc.fic.muei.tfm.dap.flipper.web.rest.admin;

import com.codahale.metrics.annotation.Timed;
import gal.udc.fic.muei.tfm.dap.flipper.domain.PictureFound;
import gal.udc.fic.muei.tfm.dap.flipper.repository.PictureFoundRepository;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto.PictureFoundDTO;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.mapper.PictureFoundMapper;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST controller for managing PictureFound.
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
@RequestMapping("/api/admin")
public class PictureFoundAdminResource {

    private final Logger log = LoggerFactory.getLogger(PictureFoundAdminResource.class);

    @Inject
    private PictureFoundRepository pictureFoundRepository;

    @Inject
    private PictureFoundMapper pictureFoundMapper;

    /**
     * GET  /pictureFounds/byPictureSearch/:pictureSearch_id -> get pictures found by "pictureSearchId" picture search.
     */
    @RequestMapping(value = "/pictureFounds/byPictureSearch/{pictureSearch_id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PictureFoundDTO>> getByPictureSearch(@PathVariable String pictureSearch_id) {
        log.debug("REST request to get PictureFound from picture search with id : {}", pictureSearch_id);

        List<PictureFound> pictureFounds = pictureFoundRepository.findByPictureSearch_id(UUID.fromString(pictureSearch_id));

        List<PictureFoundDTO> pictureFoundDTO = pictureFounds.stream()
            .map(pictureFoundMapper::pictureFoundToPictureFoundDTO)
            .collect(Collectors.toList());
        return new ResponseEntity<>(pictureFoundDTO, HttpStatus.OK);

    }

    /**
     * GET  /pictureFounds/:pictureSearch_id/:picture_id -> get the "id" picture
     */
    @RequestMapping(value = "/pictureFounds/{pictureSearch_id}/{picture_id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PictureFoundDTO> get(@PathVariable String pictureSearch_id, @PathVariable String picture_id) {
        log.debug("REST request to get Picture : {}, {}", pictureSearch_id, picture_id);
        return Optional.ofNullable(pictureFoundRepository.findOne(UUID.fromString(pictureSearch_id), UUID.fromString(picture_id)))
            .map(pictureFoundMapper::pictureFoundToPictureFoundDTO)
            .map(pictureFoundDTO -> new ResponseEntity<>(
                pictureFoundDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /pictureFounds/:pictureSearch_id -> delete by the "pictureSearch_id" pictureFound.
     */
    @RequestMapping(value = "/pictureFounds/{pictureSearch_id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable String pictureSearch_id) {
        log.debug("REST request to delete PictureFound : {}", pictureSearch_id);
        pictureFoundRepository.deleteByPictureSearch(UUID.fromString(pictureSearch_id));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("pictureFound", pictureSearch_id.toString())).build();
    }
}

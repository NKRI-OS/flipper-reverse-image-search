package gal.udc.fic.muei.tfm.dap.flipper.web.rest.admin;

import com.codahale.metrics.annotation.Timed;
import gal.udc.fic.muei.tfm.dap.flipper.domain.Picture;
import gal.udc.fic.muei.tfm.dap.flipper.repository.PictureRepository;
import gal.udc.fic.muei.tfm.dap.flipper.service.PictureService;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto.PictureDTO;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto.PictureListDTO;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto.PictureUpdateDTO;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.mapper.PictureMapper;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;
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
@RequestMapping("/api/admin")
public class PictureAdminResource {

    private final Logger log = LoggerFactory.getLogger(PictureAdminResource.class);

    @Inject
    private PictureRepository pictureRepository;

    @Inject
    private PictureService pictureService;

    @Inject
    private PictureMapper pictureMapper;

    /**
     * PUT  /pictures -> Updates an existing picture.
     */
    @RequestMapping(value = "/pictures",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PictureDTO> update(@Valid @RequestBody PictureUpdateDTO pictureDTO) throws URISyntaxException {
        log.debug("REST request to update Picture : {}", pictureDTO);
        if (pictureDTO.getId() == null) {
            return ResponseEntity.badRequest()
                .header("Failure", "Picture must have an ID")
                .body(pictureMapper.pictureToPictureDTO(null));
        }

        Picture picture = pictureMapper.pictureUpdateDTOToPicture(pictureDTO);
        Picture result = pictureService.update(picture);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("picture", pictureDTO.getId().toString()))
                .body(pictureMapper.pictureToPictureDTO(result));
    }

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
     * DELETE  /pictures/:id -> delete the "id" picture.
     */
    @RequestMapping(value = "/pictures/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.debug("REST request to delete Picture : {}", id);
        try {
            pictureService.delete(UUID.fromString(id));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().header("Warning", "Cannot delete feature index from picture: " + e.getMessage()).body(null);
        }
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("picture", id.toString())).build();
    }
}

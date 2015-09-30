package gal.udc.fic.muei.tfm.dap.flipper.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.drew.imaging.ImageProcessingException;
import gal.udc.fic.muei.tfm.dap.flipper.domain.Picture;
import gal.udc.fic.muei.tfm.dap.flipper.repository.PictureRepository;
import gal.udc.fic.muei.tfm.dap.flipper.security.SecurityUtils;
import gal.udc.fic.muei.tfm.dap.flipper.service.PictureService;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto.PictureCreateDTO;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto.PictureDTO;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto.PictureListDTO;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto.PictureUpdateDTO;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.mapper.PictureMapper;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.util.HeaderUtil;
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
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
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
public class MyPictureResource {

    private final Logger log = LoggerFactory.getLogger(MyPictureResource.class);

    @Inject
    private PictureRepository pictureRepository;

    @Inject
    private PictureService pictureService;

    @Inject
    private PictureMapper pictureMapper;

    /**
     * POST  /mypictures -> Create a new picture.
     */
    @RequestMapping(value = "/mypictures",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PictureDTO> create(@Valid @RequestBody PictureCreateDTO pictureDTO) throws URISyntaxException {
        log.debug("REST request to save Picture : {}", pictureDTO);
        if (pictureDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new picture cannot already have an ID").body(null);
        }
        Picture picture = pictureMapper.pictureCreateDTOToPicture(pictureDTO);

        // If fails read metadata, response with warnings in the header
        try {
            /* create picture and metadata */
            Picture result = pictureService.create(picture);
            return ResponseEntity.created(new URI("/api/mypictures/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("picture", result.getId().toString()))
                .body(pictureMapper.pictureToPictureDTO(result));
        } catch (ImageProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().header("Warning", "Cannot process image from picture: "+e.getMessage()).body(null);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().header("Warning", "Cannot create feature index from picture: "+e.getMessage()).body(null);

        }

    }


    /**
     * POST  /mypictures -> Create a new picture from file directly
     * with MultipartFile
     */
    @RequestMapping(value = "/mypictures/upload",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PictureDTO> createFromFile(@RequestParam("pictureFile") MultipartFile pictureFile,
                                                     @RequestParam("title") String title,
                                                     @RequestParam(value = "description", required = false) String description) throws URISyntaxException {
        log.debug("REST request to save Picture : {}", title);


        try {
            Picture picture = new Picture();
            picture.setTitle(title);
            picture.setDescription(description);
            picture.setPictureFile(ByteBuffer.wrap(pictureFile.getBytes()));

                 /* create picture and metadata */
            Picture result = pictureService.create(picture);
            return ResponseEntity.created(new URI("/api/mypictures/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("picture", result.getId().toString()))
                .body(pictureMapper.pictureToPictureDTO(result));

        } catch (ImageProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().header("Warning", "Cannot process image from picture: "+e.getMessage()).body(null);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().header("Warning", "Cannot create feature index from picture: "+e.getMessage()).body(null);

        }

    }

    /**
     * PUT  /mypictures -> Updates an existing picture.
     */
    @RequestMapping(value = "/mypictures",
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
        String owner = pictureRepository.findOne(picture.getId()).getOwner();

        /* check if it is owner */
        String currentUser = SecurityUtils.getCurrentLogin();
        if( !owner.equalsIgnoreCase(currentUser) ){
            return ResponseEntity.badRequest()
                .header("Failure", "You are not the owner from this picture.")
                .body(pictureMapper.pictureToPictureDTO(null));
        }

        Picture result = pictureService.update(picture);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("picture", pictureDTO.getId().toString()))
                .body(pictureMapper.pictureToPictureDTO(result));
    }

    /**
     * GET  /mypictures -> get all the pictures from current user.
     */
    @RequestMapping(value = "/mypictures",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<PictureListDTO>> getAll(Pageable pageable) {
        log.debug("REST request to get all Pictures");

        try {
            Page<Picture> page = pictureRepository.findByOwnerOrdered(SecurityUtils.getCurrentLogin(), pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mypictures");

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
     * GET  /mypictures/:id -> get the "id" picture.
     */
    @RequestMapping(value = "/mypictures/{id}",
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
     * DELETE  /mypictures/:id -> delete the "id" picture.
     */
    @RequestMapping(value = "/mypictures/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.debug("REST request to delete Picture : {}", id);
        try {
            Picture picture = pictureRepository.findOne(UUID.fromString(id));
            if( !picture.getOwner().equalsIgnoreCase(SecurityUtils.getCurrentLogin()) ){
                return ResponseEntity.badRequest()
                    .header("Failure", "You are not the owner from this picture.")
                    .body(null);
            }

            pictureService.delete(UUID.fromString(id));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().header("Warning", "Cannot delete feature index from picture: " + e.getMessage()).body(null);
        }
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("picture", id.toString())).build();
    }

}

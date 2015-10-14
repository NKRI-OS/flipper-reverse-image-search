package gal.udc.fic.muei.tfm.dap.flipper.web.rest.admin;

import com.codahale.metrics.annotation.Timed;
import gal.udc.fic.muei.tfm.dap.flipper.domain.PictureSearch;
import gal.udc.fic.muei.tfm.dap.flipper.repository.PictureSearchRepository;
import gal.udc.fic.muei.tfm.dap.flipper.service.PictureSearchService;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto.PictureSearchDTO;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto.PictureSearchListDTO;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.mapper.PictureSearchMapper;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.util.HeaderUtil;
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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST controller for managing PictureSearch.
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
public class PictureSearchAdminResource {

    private final Logger log = LoggerFactory.getLogger(PictureSearchAdminResource.class);

    @Inject
    private PictureSearchRepository pictureSearchRepository;

    @Inject
    private PictureSearchMapper pictureSearchMapper;

    @Inject
    private PictureSearchService pictureSearchService;

    /**
     * GET  /pictureSearchs -> get all the pictureSearch.
     */
    @RequestMapping(value = "/pictureSearchs",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<PictureSearchListDTO>> getAll() {
        log.debug("REST request to get all PictureSearchs");

        List<PictureSearch> pictures = pictureSearchRepository.findAll();

        List<PictureSearchListDTO> pictureListDTOs = pictures.stream()
            .map(pictureSearchMapper::pictureSearchToPictureSearchListDTO)
            .collect(Collectors.toList());
        return new ResponseEntity<>(pictureListDTOs, HttpStatus.OK);
    }

    /**
     * GET  /pictureSearchs/:id -> get the "id" pictureSearch.
     */
    @RequestMapping(value = "/pictureSearchs/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PictureSearchDTO> get(@PathVariable String id) {
        log.debug("REST request to get PictureSearch : {}", id);

        PictureSearch pictureSearch = pictureSearchRepository.findOne(UUID.fromString(id));
        if(pictureSearch == null){
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        PictureSearchDTO pictureSearchDTO = pictureSearchMapper.pictureSearchToPictureSearchDTO(pictureSearch);

        return new ResponseEntity<>(pictureSearchDTO, HttpStatus.OK);
    }

    /**
     * DELETE  /pictureSearchs/:id -> delete the "id" pictureSearch.
     */
    @RequestMapping(value = "/pictureSearchs/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.debug("REST request to delete PictureSearch : {}", id);
        pictureSearchService.delete(UUID.fromString(id));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("pictureSearch", id.toString())).build();
    }

}

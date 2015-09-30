package gal.udc.fic.muei.tfm.dap.flipper.web.rest;

import com.codahale.metrics.annotation.Timed;
import gal.udc.fic.muei.tfm.dap.flipper.domain.PictureSearch;
import gal.udc.fic.muei.tfm.dap.flipper.repository.PictureSearchRepository;
import gal.udc.fic.muei.tfm.dap.flipper.service.PictureSearchService;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto.PictureFoundDTO;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto.PictureSearchCreateDTO;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto.PictureSearchDTO;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto.PictureSearchListDTO;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.mapper.PictureFoundMapper;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.mapper.PictureSearchMapper;
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
@RequestMapping("/api")
public class PictureSearchResource {

    private final Logger log = LoggerFactory.getLogger(PictureSearchResource.class);

    @Inject
    private PictureSearchMapper pictureSearchMapper;

    @Inject
    private PictureFoundMapper pictureFoundMapper;

    @Inject
    private PictureSearchRepository pictureSearchRepository;

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
    public ResponseEntity<List<PictureSearchListDTO>> getAll(Pageable pageable) {
        log.debug("REST request to get all PictureSearchs");
        try {
            Page<PictureSearch> page = pictureSearchRepository.findAllOrdered(pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pictureSearchs");
            return new ResponseEntity<>(page.getContent().stream()
                .map(pictureSearchMapper::pictureSearchToPictureSearchListDTO)
                .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                .header("Failure", e.getMessage())
                .body(null);
        }
    }

    /**
     * SEARCH /_search/pictureSearchs/ -> search for similar pictures
     * Upload a json picture search pictureFile as Base64
     * @return
     */
    @RequestMapping(
        value="/_search/pictureSearchs",
        method=RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public @ResponseBody ResponseEntity<PictureSearchDTO> search(@Valid @RequestBody
                                                                 PictureSearchCreateDTO pictureSearchCreateDTO) throws URISyntaxException {
        log.debug("REST request to search Pictures : {}", pictureSearchCreateDTO);

        try {
            PictureSearch result = pictureSearchService.searchByAllFeatures(pictureSearchCreateDTO.getPictureFile());
            return ResponseEntity.created(new URI("/api/pictureFound/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("pictureSearch", result.getId().toString()))
                .body(pictureSearchMapper.pictureSearchToPictureSearchDTO(result));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().header("Warning", "Cannot search from picture: " + e.getMessage()).body(null);
        }
    }

    /**
     * SEARCH /_search/pictureSearch/byFile/ -> search for similar pictures by input file
     * Upload a picture file as input file type
     * @return
     */
    @RequestMapping(
        value="/_search/pictureSearch/byFile",
        method=RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public @ResponseBody
    List<PictureFoundDTO> search(@RequestParam("file") MultipartFile file) throws URISyntaxException {

        try {
            return pictureSearchService.searchByAllFeaturesWithList(file.getBytes()).stream()
                .map(pictureFound -> pictureFoundMapper.pictureFoundToPictureFoundDTO(pictureFound))
                .collect(Collectors.toCollection(LinkedList::new));

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

}

package gal.udc.fic.muei.tfm.dap.flipper.web.rest;

import com.codahale.metrics.annotation.Timed;
import gal.udc.fic.muei.tfm.dap.flipper.domain.Metadata;
import gal.udc.fic.muei.tfm.dap.flipper.repository.MetadataRepository;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto.MetadataDTO;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.mapper.MetadataMapper;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST controller for managing Metadata.
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
public class MetadataResource {

    private final Logger log = LoggerFactory.getLogger(MetadataResource.class);

    @Inject
    private MetadataRepository metadataRepository;

    @Inject
    private MetadataMapper metadataMapper;

    /**
     * GET  /metadatas -> get all the metadatas.
     */
    @RequestMapping(value = "/metadatas",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<MetadataDTO>> getAll() {
        log.debug("REST request to get all Metadatas");

        List<Metadata> metadatas = metadataRepository.findAll();

        List<MetadataDTO> metadataDTOs = metadatas.stream()
            .map(metadataMapper::metadataToMetadataDTO)
            .collect(Collectors.toList());
        return new ResponseEntity<>(metadataDTOs, HttpStatus.OK);
    }

    /**
     * GET  /metadatas/byPicture/{picture_id} -> get all metadatas from picture
     */
    @RequestMapping(value = "/metadatas/byPicture/{picture_id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public List<MetadataDTO> getAllByPicture(@PathVariable String picture_id) {
        log.debug("REST request to get all Metadatas from a picture by id " + picture_id);
        return metadataRepository.findByPicture_id(UUID.fromString(picture_id)).stream()
            .map(metadata -> metadataMapper.metadataToMetadataDTO(metadata))
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * GET  /metadatas/:id -> get the "id" metadata.
     */
    @RequestMapping(value = "/metadatas/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MetadataDTO> get(@PathVariable String id) {
        log.debug("REST request to get Metadata : {}", id);
        return Optional.ofNullable(metadataRepository.findOne(UUID.fromString(id)))
            .map(metadataMapper::metadataToMetadataDTO)
            .map(metadataDTO -> new ResponseEntity<>(
                metadataDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * SEARCH  /_search/metadatas/:query -> search for the category corresponding
     * to the query.
     * Valid values for query: <i>searchText</i> or <i>field:searchText</i>
     * Valid wildcards for searchText: *, ?, -, !
     * Valid columns for query:
     *    description text (by default)
     *    directoryname text
     *    tagname text
     *    tagtype int
     *    title text
     */
    @RequestMapping(value = "/_search/metadatas/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Metadata> search(@PathVariable String query) {

        return metadataRepository.search(query);
    }
}

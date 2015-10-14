package gal.udc.fic.muei.tfm.dap.flipper.web.rest.admin;

import com.codahale.metrics.annotation.Timed;
import gal.udc.fic.muei.tfm.dap.flipper.domain.Metadata;
import gal.udc.fic.muei.tfm.dap.flipper.repository.MetadataRepository;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto.MetadataDTO;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.mapper.MetadataMapper;
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
@RequestMapping("/api/admin")
public class MetadataAdminResource {

    private final Logger log = LoggerFactory.getLogger(MetadataAdminResource.class);

    @Inject
    private MetadataRepository metadataRepository;

    @Inject
    private MetadataMapper metadataMapper;

    /**
     * GET  /metadatas -> get all the metadatas
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
     * DELETE  /metadatas/:id -> delete the "id" metadata.
     */
    @RequestMapping(value = "/metadatas/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.debug("REST request to delete Metadata : {}", id);

        Metadata metadata = metadataRepository.findOne(UUID.fromString(id));
        if(metadata == null){
            ResponseEntity.noContent().header("Failure", String.format("id %s not found.", id)).build();
        }

        metadataRepository.delete(metadata);

        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("metadata", id.toString())).build();
    }

}

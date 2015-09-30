package gal.udc.fic.muei.tfm.dap.flipper.web.rest.mapper;

import gal.udc.fic.muei.tfm.dap.flipper.domain.*;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto.MetadataDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Metadata and its DTO MetadataDTO.
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
@Mapper(componentModel = "spring", uses = {ByteMapper.class})
public interface MetadataMapper {

    MetadataDTO metadataToMetadataDTO(Metadata metadata);

    Metadata metadataDTOToMetadata(MetadataDTO metadataDTO);
}

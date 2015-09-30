package gal.udc.fic.muei.tfm.dap.flipper.web.rest.mapper;

import gal.udc.fic.muei.tfm.dap.flipper.domain.PictureSearch;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto.PictureSearchCreateDTO;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto.PictureSearchDTO;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto.PictureSearchListDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity PictureSearch and its DTO PictureSearchDTO.
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
@Mapper(componentModel = "spring", uses = {ByteMapper.class, DateMapper.class})
public interface PictureSearchMapper {

    PictureSearchDTO pictureSearchToPictureSearchDTO(PictureSearch pictureSearch);

    PictureSearch pictureSearchDTOToPictureSearch(PictureSearchDTO pictureSearchDTO);

    PictureSearch pictureSearchCreateDTOToPictureSearch(PictureSearchCreateDTO pictureSearchCreateDTO);

    PictureSearchListDTO pictureSearchToPictureSearchListDTO(PictureSearch pictureSearch);
}

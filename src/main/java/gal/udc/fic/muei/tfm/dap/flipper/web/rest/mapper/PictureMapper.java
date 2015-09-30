package gal.udc.fic.muei.tfm.dap.flipper.web.rest.mapper;

import gal.udc.fic.muei.tfm.dap.flipper.domain.Picture;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto.PictureCreateDTO;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto.PictureDTO;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto.PictureListDTO;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto.PictureUpdateDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity Picture and its DTO PictureDTO.
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
public interface PictureMapper {

    PictureDTO pictureToPictureDTO(Picture picture);

    Picture pictureCreateDTOToPicture(PictureCreateDTO pictureCreateDTO);

    Picture pictureUpdateDTOToPicture(PictureUpdateDTO pictureDTO);

    PictureListDTO pictureToPictureListDTO(Picture picture);
}

package gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


/**
 * A DTO for the PictureSearch entity.
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
public class PictureSearchListDTO implements Serializable {

    private UUID id;

    private String created;

    private List<UUID> pictureIdList;

    private String userLogin;

    /* 120px */
    private byte[] littlePictureFile;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PictureSearchListDTO pictureSearchDTO = (PictureSearchListDTO) o;

        if ( ! Objects.equals(id, pictureSearchDTO.id)) return false;

        return true;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public List<UUID> getPictureIdList() {
        return pictureIdList;
    }

    public void setPictureIdList(List<UUID> pictureIdList) {
        this.pictureIdList = pictureIdList;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public byte[] getLittlePictureFile() {
        return littlePictureFile;
    }

    public void setLittlePictureFile(byte[] littlePictureFile) {
        this.littlePictureFile = littlePictureFile;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PictureSearchDTO{" +
                "id=" + id +
                ", created='" + created + "'" +
                ", pictureIdList='" + pictureIdList + "'" +
                ", userLogin='" + userLogin + "'" +
                '}';
    }
}

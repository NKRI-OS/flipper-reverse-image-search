package gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


/**
 * A DTO for the Picture entity.
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
public class PictureDTO implements Serializable {

    private UUID id;

    @NotNull
    @Size(min = 3, max = 100)
    private String title;

    private String description;

    private byte[] pictureFile;

    @NotNull
    private String owner;
    /* 120px */
    private byte[] littlePictureFile;
    /* 480px */
    private byte[] mediumPictureFile;
    /* 1024px */
    private byte[] bigPictureFile;

    /* begin features */
    private List<Double> autocolorCorrelogram;
    private List<Double> cedd;
    private List<Double> colorHistogram;
    private List<Double> colorLayout;
    private List<Double> edgeHistogram;
    private List<Double> phog;
    /* end features*/

    public byte[] getLittlePictureFile() {
        return littlePictureFile;
    }

    public void setLittlePictureFile(byte[] littlePictureFile) {
        this.littlePictureFile = littlePictureFile;
    }

    public byte[] getMediumPictureFile() {
        return mediumPictureFile;
    }

    public void setMediumPictureFile(byte[] mediumPictureFile) {
        this.mediumPictureFile = mediumPictureFile;
    }

    public byte[] getBigPictureFile() {
        return bigPictureFile;
    }

    public void setBigPictureFile(byte[] bigPictureFile) {
        this.bigPictureFile = bigPictureFile;
    }

    public List<Double> getAutocolorCorrelogram() {
        return autocolorCorrelogram;
    }

    public void setAutocolorCorrelogram(List<Double> autocolorCorrelogram) {
        this.autocolorCorrelogram = autocolorCorrelogram;
    }

    public List<Double> getCedd() {
        return cedd;
    }

    public void setCedd(List<Double> cedd) {
        this.cedd = cedd;
    }

    public List<Double> getColorHistogram() {
        return colorHistogram;
    }

    public void setColorHistogram(List<Double> colorHistogram) {
        this.colorHistogram = colorHistogram;
    }

    public List<Double> getColorLayout() {
        return colorLayout;
    }

    public void setColorLayout(List<Double> colorLayout) {
        this.colorLayout = colorLayout;
    }

    public List<Double> getEdgeHistogram() {
        return edgeHistogram;
    }

    public void setEdgeHistogram(List<Double> edgeHistogram) {
        this.edgeHistogram = edgeHistogram;
    }

    public List<Double> getPhog() {
        return phog;
    }

    public void setPhog(List<Double> phog) {
        this.phog = phog;
    }

    private String modifiedBy;

    private String created;

    private String modified;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getPictureFile() {
        return pictureFile;
    }

    public void setPictureFile(byte[] pictureFile) {
        this.pictureFile = pictureFile;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PictureDTO pictureDTO = (PictureDTO) o;

        if ( ! Objects.equals(id, pictureDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PictureDTO{" +
                "id=" + id +
                ", title='" + title + "'" +
                ", description='" + description + "'" +
                ", owner='" + owner + "'" +
                ", modifiedBy='" + modifiedBy + "'" +
                ", created='" + created + "'" +
                ", modified='" + modified + "'" +
                '}';
    }

}

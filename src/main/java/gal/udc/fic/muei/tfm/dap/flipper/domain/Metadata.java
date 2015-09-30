package gal.udc.fic.muei.tfm.dap.flipper.domain;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.UUID;


/**
 * A Metadata entity
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
@Table(name = "metadata")
public class Metadata implements Serializable {

    @PartitionKey
    private UUID picture_id;

    @ClusteringColumn
    private UUID id;

    @NotNull
    private String directoryName;

    @NotNull
    private Integer tagType;

    @NotNull
    private String tagName;

    @NotNull
    @Pattern(regexp = "^[^<>%$]*$")
    private String description;

    @Size(min = 3, max = 100)
    private String title;

    private ByteBuffer pictureFile;

    /**
     * <Constructor>Empty constructor</Constructor>
     */
    public Metadata(){}

    /**
     * <Constructor>Create an initial Metadata</Constructor>
     * @param directoryName
     * @param tagType
     * @param tagName
     * @param description
     */
    public Metadata(String directoryName, Integer tagType, String tagName, String description) {
        this.directoryName = directoryName;
        this.tagType = tagType;
        this.tagName = tagName;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public Integer getTagType() {
        return tagType;
    }

    public void setTagType(Integer tagType) {
        this.tagType = tagType;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public UUID getPicture_id() {
        return picture_id;
    }

    public void setPicture_id(UUID picture_id) {
        this.picture_id = picture_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ByteBuffer getPictureFile() {
        return pictureFile;
    }

    public void setPictureFile(ByteBuffer pictureFile) {
        this.pictureFile = pictureFile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Metadata that = (Metadata) o;
        EqualsBuilder eb = new EqualsBuilder();

        eb.append(picture_id, that.getPicture_id());
        eb.append(directoryName, that.directoryName);
        eb.append(tagType, that.tagType);
        eb.append(tagName, that.getTagName());
        eb.append(title, that.title);

        return eb.isEquals();

    }

    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();

        hcb.append(picture_id);
        hcb.append(tagName);

        /* return "toHashCode", NOT "hashCode " */
        return hcb.toHashCode();
    }

    @Override
    public String toString() {
        return "Metadata{" +
            "directoryName='" + directoryName + '\'' +
            ", tagType=" + tagType +
            ", tagName='" + tagName + '\'' +
            ", description='" + description + '\'' +
            ", picture_id=" + picture_id +
            ", title='" + title + '\'' +
            '}';
    }
}

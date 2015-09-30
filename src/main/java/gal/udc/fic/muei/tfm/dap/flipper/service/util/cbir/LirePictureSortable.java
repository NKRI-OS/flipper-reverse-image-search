package gal.udc.fic.muei.tfm.dap.flipper.service.util.cbir;

import gal.udc.fic.muei.tfm.dap.flipper.domain.enumeration.FeatureEnumerate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A picture class to store temporal objects data from a found image in Apache Lucene with Lire
 * It stores the total score and a map with the features found and each score
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
public class LirePictureSortable implements Comparable<LirePictureSortable>{

    private UUID id;
    private float score;
    private Map<FeatureEnumerate, Float> descriptorList = new HashMap<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public float getScore() {
        return this.score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public void addScore(float score) {
        this.score += score;
    }

    public LirePictureSortable(UUID id, Float score, FeatureEnumerate fe) {
        this.id = id;
        this.score = score;
        this.descriptorList.put(fe, score);
    }

    public Map<FeatureEnumerate, Float> getDescriptor() {
        return descriptorList;
    }

    public void setDescriptor(Map<FeatureEnumerate, Float> descriptorList) {
        this.descriptorList = descriptorList;
    }

    public void addDescriptor(FeatureEnumerate descriptor, Float score) {
        this.descriptorList.put(descriptor, score);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LirePictureSortable)) return false;

        LirePictureSortable that = (LirePictureSortable) o;

        return getId().equals(that.getId());

    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    /**
     * Sort from min to max
     * NOTE: Lucene score for less values there is more similarity. Equals is value 0.0
     *
     * @param object
     * @return
     */
    @Override
    public int compareTo(LirePictureSortable object) {
        return Float.valueOf(this.getScore()).compareTo(Float.valueOf(object.getScore()));
    }
}

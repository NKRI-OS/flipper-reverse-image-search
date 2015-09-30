package gal.udc.fic.muei.tfm.dap.flipper.service.util.cbir;

import gal.udc.fic.muei.tfm.dap.flipper.domain.enumeration.FeatureEnumerate;

import java.util.List;


/**
 * A Feature descriptor class to store temporal objects in search and indexing images with Lire
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
public class Feature {

    private FeatureEnumerate descriptor;

    private List<Double> feature;

    private String featureAsBase64;

    /**
     * <constructor>Feature contructor with notempty attibutes</constructor>
     *
     * @param featureEnumerate
     * @param featureAsDoubleArray
     * @param featureAsBase64
     */
    public Feature(FeatureEnumerate featureEnumerate,
                   List<Double> featureAsDoubleArray,
                   String featureAsBase64) {

        this.feature = featureAsDoubleArray;
        this.descriptor = featureEnumerate;
        this.featureAsBase64 = featureAsBase64;
    }

    public FeatureEnumerate getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(FeatureEnumerate descriptor) {
        this.descriptor = descriptor;
    }

    public List<Double> getFeature() {
        return feature;
    }

    public void setFeature(List<Double> feature) {
        this.feature = feature;
    }

    public String getFeatureAsBase64() {
        return featureAsBase64;
    }

    public void setFeatureAsBase64(String featureAsBase64) {
        this.featureAsBase64 = featureAsBase64;
    }


}

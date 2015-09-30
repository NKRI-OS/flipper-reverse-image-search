package gal.udc.fic.muei.tfm.dap.flipper.domain.enumeration;

/**
 * Enum visual features used with Lire to extract, index and search images
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
public enum FeatureEnumerate {
    AutoColorCorrelogram("autocolorcorrelogram"),
    CEDD("cedd"),
    ColorLayout("colorlayout"),
    EdgeHistogram("edgehistogram"),
    ColorHistogram("colorhistogram"),
    PHOG("phog");

    private String text;

    FeatureEnumerate(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static FeatureEnumerate fromString(String text) {
        if (text != null) {
            for (FeatureEnumerate b : FeatureEnumerate.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }

    /**
    *    Object descriptors from Lire used in application
    */
    public Class getValueClass(){
        switch (this){
            case PHOG:
                return net.semanticmetadata.lire.imageanalysis.PHOG.class;
            case ColorLayout:
                return net.semanticmetadata.lire.imageanalysis.ColorLayout.class;

            case EdgeHistogram:
                return net.semanticmetadata.lire.imageanalysis.EdgeHistogram.class;

            case AutoColorCorrelogram:
                return net.semanticmetadata.lire.imageanalysis.AutoColorCorrelogram.class;

            case CEDD:
                return net.semanticmetadata.lire.imageanalysis.CEDD.class;

            case ColorHistogram:
                return net.semanticmetadata.lire.imageanalysis.SimpleColorHistogram.class;
        }

        return net.semanticmetadata.lire.imageanalysis.AutoColorCorrelogram.class;
    }

}

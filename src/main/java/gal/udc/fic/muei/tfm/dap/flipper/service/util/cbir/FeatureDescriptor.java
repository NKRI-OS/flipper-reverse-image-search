package gal.udc.fic.muei.tfm.dap.flipper.service.util.cbir;

import gal.udc.fic.muei.tfm.dap.flipper.domain.enumeration.FeatureEnumerate;
import gal.udc.fic.muei.tfm.dap.flipper.service.util.DataFormatUtil;
import net.semanticmetadata.lire.imageanalysis.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

/**
 *
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
public class FeatureDescriptor {


    public static Feature getColorHistogram(BufferedImage image) throws IOException {

        SimpleColorHistogram cl = new SimpleColorHistogram();
        cl.extract(image);
        return createFeature(cl, FeatureEnumerate.ColorHistogram);
    }

    public static Feature getColorLayout(BufferedImage image) throws IOException {
        ColorLayout cl = new ColorLayout();
        cl.extract(image);
        return createFeature(cl, FeatureEnumerate.ColorLayout);
    }

    public static Feature getEdgeHistogram(BufferedImage image) throws IOException {

        EdgeHistogram cl = new EdgeHistogram();
        cl.extract(image);
        return createFeature(cl, FeatureEnumerate.EdgeHistogram);
    }

    /**
     * AutoColorCorrelogram feature
     *
     * @param image
     * @return
     * @throws IOException
     * @link http://doi.ieeecomputersociety.org/10.1109/CVPR.1997.609412
     */
    public static Feature getAutoColorCorrelogram(BufferedImage image) throws IOException {

        AutoColorCorrelogram cl = new AutoColorCorrelogram();
        cl.extract(image);
        return createFeature(cl, FeatureEnumerate.AutoColorCorrelogram);
    }

    /**
     * PHoG feature
     *
     * @param image
     * @return
     * @throws IOException
     * @link http://www.robots.ox.ac.uk/~vgg/publications/2007/Bosch07/
     */
    public static Feature getPHoG(BufferedImage image) throws IOException {

        PHOG cl = new PHOG();
        cl.extract(image);
        return createFeature(cl, FeatureEnumerate.PHOG);
    }

    /**
     * CEDD feature
     *
     * @param image
     * @return
     * @throws IOException
     * @link http://www.robots.ox.ac.uk/~vgg/publications/2007/Bosch07/
     */
    public static Feature getCEDD(BufferedImage image) throws IOException {

        CEDD cl = new CEDD();
        cl.extract(image);
        return createFeature(cl, FeatureEnumerate.CEDD);
    }

    /**
     * Creates an object Feature temporal from a feature extractor from Lire
     *
     * @param lireFeature
     * @param featureEnumerate
     * @return
     */
    private static Feature createFeature(LireFeature lireFeature, FeatureEnumerate featureEnumerate) {

        byte[] asByte = lireFeature.getByteArrayRepresentation();

        List<Double> asList =  DataFormatUtil.serializeFromArrayToDouble(lireFeature.getDoubleHistogram());
        String asBase64 = Base64.getEncoder().encodeToString(asByte);

        return new Feature(featureEnumerate, asList, asBase64);

    }

}

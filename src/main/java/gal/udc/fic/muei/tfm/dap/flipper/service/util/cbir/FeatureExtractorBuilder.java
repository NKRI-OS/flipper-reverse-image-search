package gal.udc.fic.muei.tfm.dap.flipper.service.util.cbir;

import gal.udc.fic.muei.tfm.dap.flipper.domain.enumeration.FeatureEnumerate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for extracting & comparing MPEG-7 based CBIR descriptor ColorLayout
 *
 * @link https://github.com/locked-fg/JFeatureLib/wiki/FeaturesOverview
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
public class FeatureExtractorBuilder {

    /**
     * Create a list of features using Histogram methods and index the features.
     * Used RGB histogram, texture, direction and scale histograms.
     *
     * @param source
     * @return
     * @throws IOException
     * @link http://stackoverflow.com/questions/843972/image-comparison-fast-algorithm
     *
     */
    public static List<Feature> extract(byte[] source) throws IOException {

        ByteArrayInputStream in = new ByteArrayInputStream(source);
        BufferedImage bufferedImage = ImageIO.read(in);

        List<Feature> result = new ArrayList<>();

        /* create colorProcessor image */
        // ColorProcessor image = new ColorProcessor(bufferedImage);

        // Color Histogram RGB
        result.add(FeatureDescriptor.getColorHistogram(bufferedImage));

        // CEDD
        result.add(FeatureDescriptor.getCEDD(bufferedImage));

        // MPEG-7 color for extracting & comparing MPEG-7 based CBIR descriptor ColorLayout
        result.add(FeatureDescriptor.getColorLayout(bufferedImage));

        // MPEG-7 Edge histogram. For the texture direction histogram
        result.add(FeatureDescriptor.getEdgeHistogram(bufferedImage));

        // PHoG, Oriented: Pyramid Histograms of Oriented Gradients
        result.add(FeatureDescriptor.getPHoG(bufferedImage));

        // AutoColorCorrelogram: "Image Indexing Using Color Correlograms"
        result.add(FeatureDescriptor.getAutoColorCorrelogram(bufferedImage));

        return result;
    }

    /**
     * Extract only one feature
     *
     * @param source
     * @param feature enum FeatureEnumerate
     * @return
     * @throws IOException
     */
    public static Feature extract(byte[] source, FeatureEnumerate feature) throws IOException {

        try {
            /* create colorProcessor image */
            ByteArrayInputStream in = new ByteArrayInputStream(source);
            BufferedImage image = ImageIO.read(in);

            switch (feature) {
                case AutoColorCorrelogram:
                    return FeatureDescriptor.getAutoColorCorrelogram(image);
                case CEDD:
                    return FeatureDescriptor.getCEDD(image);
                case ColorLayout:
                    return FeatureDescriptor.getColorLayout(image);
                case EdgeHistogram:
                    return FeatureDescriptor.getEdgeHistogram(image);
                case ColorHistogram:
                    return FeatureDescriptor.getColorHistogram(image);
                case PHOG:
                    return FeatureDescriptor.getPHoG(image);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        // if extract cannot be made because problems with Lire support
        return null;
    }
}

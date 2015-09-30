package gal.udc.fic.muei.tfm.dap.flipper.service.util;


import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
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
public class MetadataExtactorUtils {

    private static final Logger log = LoggerFactory.getLogger(MetadataExtactorUtils.class);

    /**
     * Read metadata from picture
     *
     * @param source
     * @return
     * @throws ImageProcessingException
     * @throws IOException
     */
    public static Set<gal.udc.fic.muei.tfm.dap.flipper.domain.Metadata> readMetadata(byte[] source) throws ImageProcessingException, IOException {

        ByteArrayInputStream bis = new ByteArrayInputStream(source);

        Metadata metadata = ImageMetadataReader.readMetadata(bis);

        Set<gal.udc.fic.muei.tfm.dap.flipper.domain.Metadata> result = new HashSet<gal.udc.fic.muei.tfm.dap.flipper.domain.Metadata>();
        for (Directory directory : metadata.getDirectories()) {
            result.addAll(directory.getTags().stream().map(tag -> new gal.udc.fic.muei.tfm.dap.flipper.domain.Metadata(
                directory.getName(), tag.getTagType(), tag.getTagName(), tag.getDescription())).collect(Collectors.toList()));
        }

        return result;
    }
}

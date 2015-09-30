package gal.udc.fic.muei.tfm.dap.flipper.service;

import com.drew.imaging.ImageProcessingException;
import gal.udc.fic.muei.tfm.dap.flipper.config.LireConfiguration;
import gal.udc.fic.muei.tfm.dap.flipper.domain.Metadata;
import gal.udc.fic.muei.tfm.dap.flipper.domain.Picture;
import gal.udc.fic.muei.tfm.dap.flipper.domain.enumeration.FeatureEnumerate;
import gal.udc.fic.muei.tfm.dap.flipper.repository.*;
import gal.udc.fic.muei.tfm.dap.flipper.security.SecurityUtils;
import gal.udc.fic.muei.tfm.dap.flipper.service.util.MetadataExtactorUtils;
import gal.udc.fic.muei.tfm.dap.flipper.service.util.PictureScaleUtil;
import gal.udc.fic.muei.tfm.dap.flipper.service.util.cbir.Feature;
import gal.udc.fic.muei.tfm.dap.flipper.service.util.cbir.FeatureExtractorBuilder;
import gal.udc.fic.muei.tfm.dap.flipper.service.util.cbir.LireBuilder;
import net.semanticmetadata.lire.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

@Service
public class PictureService {

    private static final int MAX_RESULTS = 100;
    private static final int LIMIT_PER_WRITE = 100;
    private static final int MAX_WRITES = 25;
    private static final int CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR);

    private final Logger log = LoggerFactory.getLogger(PictureService.class);

    @Autowired
    private LireConfiguration lireConfiguration;

    @Inject
    private PictureFoundRepository pictureFoundRepository;

    @Inject
    private PictureRepository pictureRepository;

    @Inject
    private GeneralCounterRepository generalCounterRepository;
    @Inject
    private UserCounterRepository userCounterRepository;

    @Inject
    private MetadataRepository metadataRepository;

    /**
     * Create new picture with title, description and file
     *
     * @param picture
     * @return
     */
    public Picture create(Picture picture) throws ImageProcessingException, IOException {

        /* set picture date */
        picture.setCreated(new Date());

        /* set owner */
        String user = SecurityUtils.getCurrentLogin();
        picture.setOwner(user);

        /* increment year */
        generalCounterRepository.incrementPicture(CURRENT_YEAR);

        /* increment user picture */
        userCounterRepository.incrementPicture(user);

        /* Save */
        pictureRepository.save(picture);

        /* start async process */

        /* Scale images */
        scaleImage(picture);

        /* Create all features */
        createFeatures(picture);

        /* Create all metadata */
        createMetadata(picture);

        return picture;
    }

    /**
     * Scale pictures
     * @param picture
     * @throws IOException
     */
    @Async
    private void scaleImage(Picture picture) throws IOException {

        ByteArrayInputStream in = new ByteArrayInputStream(picture.getPictureFile().array());
        BufferedImage image = ImageIO.read(in);



        /* little */
        if (Math.max(image.getHeight(), image.getWidth()) > PictureScaleUtil.LITTLE_SIZE) {
            BufferedImage littleImg = ImageUtils.scaleImage(image, PictureScaleUtil.LITTLE_SIZE);
            picture.setLittlePictureFile(ByteBuffer.wrap(PictureScaleUtil.setBufferedImage(littleImg)));
        }else {
            picture.setLittlePictureFile(ByteBuffer.wrap(picture.getPictureFile().array()));
        }

        /* medium */
        if (Math.max(image.getHeight(), image.getWidth()) > PictureScaleUtil.MEDIUM_SIZE) {
            BufferedImage littleImg = ImageUtils.scaleImage(image, PictureScaleUtil.MEDIUM_SIZE);
            picture.setMediumPictureFile(ByteBuffer.wrap(PictureScaleUtil.setBufferedImage(littleImg)));
        }else {
            picture.setMediumPictureFile(ByteBuffer.wrap(picture.getPictureFile().array()));
        }

        /* big */
        if (Math.max(image.getHeight(), image.getWidth()) > PictureScaleUtil.BIG_SIZE) {
            BufferedImage littleImg = ImageUtils.scaleImage(image, PictureScaleUtil.BIG_SIZE);
            picture.setBigPictureFile(ByteBuffer.wrap(PictureScaleUtil.setBufferedImage(littleImg)));
        }else {
            picture.setBigPictureFile(ByteBuffer.wrap(picture.getPictureFile().array()));
        }

        /* update picture */
        pictureRepository.save(picture);
    }

    /**
     * Extract and create feature for picture
     *
     * @param picture
     */
    @Async
    private void createFeatures(Picture picture) throws IOException {

        // Index in Lucene
        LireBuilder.index(picture.getPictureFile().array(), picture.getId(), lireConfiguration.getConf());

        Feature autocolor = FeatureExtractorBuilder.extract(picture.getPictureFile().array(), FeatureEnumerate.AutoColorCorrelogram);
        if(autocolor != null){
            picture.setAutocolorCorrelogram(autocolor.getFeature());
            picture.setAutocolorCorrelogramAsBase64(autocolor.getFeatureAsBase64());
        }

        Feature cedd = FeatureExtractorBuilder.extract(picture.getPictureFile().array(), FeatureEnumerate.CEDD);
        if(cedd != null){
            picture.setCedd(cedd.getFeature());
            picture.setCeddAsBase64(cedd.getFeatureAsBase64());
        }


        Feature colorHistogram = FeatureExtractorBuilder.extract(picture.getPictureFile().array(), FeatureEnumerate.ColorHistogram);
        if(colorHistogram != null){
            picture.setColorHistogram(colorHistogram.getFeature());
            picture.setColorHistogramAsBase64(colorHistogram.getFeatureAsBase64());
        }

        Feature colorLayout = FeatureExtractorBuilder.extract(picture.getPictureFile().array(), FeatureEnumerate.ColorLayout);
        if(colorLayout != null){
            picture.setColorLayout(colorLayout.getFeature());
            picture.setColorLayoutAsBase64(colorLayout.getFeatureAsBase64());
        }

        Feature edgeHistogram = FeatureExtractorBuilder.extract(picture.getPictureFile().array(), FeatureEnumerate.EdgeHistogram);
        if(edgeHistogram != null){
            picture.setEdgeHistogram(edgeHistogram.getFeature());
            picture.setEdgeHistogramAsBase64(edgeHistogram.getFeatureAsBase64());
        }

        Feature phog = FeatureExtractorBuilder.extract(picture.getPictureFile().array(), FeatureEnumerate.PHOG);
        if(phog != null){
            picture.setPhog(phog.getFeature());
            picture.setPhogAsBase64(phog.getFeatureAsBase64());
        }

        /* update picture */
        pictureRepository.save(picture);
    }

    /**
     * Extract and create Metadata
     *
     * @param picture
     * @throws ImageProcessingException
     * @throws IOException
     */
    @Async
    private void createMetadata(Picture picture) throws ImageProcessingException, IOException {

        Set<Metadata> metadataSet = MetadataExtactorUtils.readMetadata(picture.getPictureFile().array());

        if (metadataSet.size() > LIMIT_PER_WRITE) {

            Set<Metadata>[] subsets = new Set[MAX_WRITES];
            Metadata[] array = metadataSet.toArray(new Metadata[metadataSet.size()]);

            for (int i = 1; i <= MAX_WRITES; i++) {
                int fromIndex = (i - 1) * metadataSet.size() / MAX_WRITES;
                int toIndex = i * metadataSet.size() / MAX_WRITES - 1;
                Set<Metadata> subHashSet = new HashSet<>();
                subHashSet.addAll(Arrays.asList(Arrays.copyOfRange(array, fromIndex, toIndex)));

                subsets[i - 1] = subHashSet;
            }

            // Avoid OutOfBand in Heap memory
            int writes = 0;
            for (Set<Metadata> subset : subsets) {
                metadataRepository.saveAll(subset, picture);
                writes++;
            }

            /* increment metadata for subsets */
            generalCounterRepository.incrementMetadata(writes, CURRENT_YEAR);
            userCounterRepository.incrementMetadata(writes, picture.getOwner());
        } else {
            metadataRepository.saveAll(metadataSet, picture);

            /* increment metadata for all metadata */
            generalCounterRepository.incrementMetadata(metadataSet.size(), CURRENT_YEAR);
            userCounterRepository.incrementMetadata(metadataSet.size(), picture.getOwner());
        }

    }

    /**
     * Update picture only title and description
     * Set modified date
     *
     * @param updated
     * @return
     */
    public Picture update(Picture updated) {

        /* find by id */
        Picture picture = pictureRepository.findOne(updated.getId());
        String oldTitle = picture.getTitle();
        String newTitle = updated.getTitle();

        /* update data */
        picture.setTitle(newTitle);
        picture.setDescription(updated.getDescription());

        /* set picture date */
        picture.setModified(new Date());

        /* set modifier user */
        picture.setModifiedBy(SecurityUtils.getCurrentLogin());

        /* save picture */
        pictureRepository.save(picture);

        if (oldTitle != newTitle) {
            updateMetadataAndFeatures(picture);
        }

        return picture;
    }

    /**
     * TODO perform update
     * @param picture
     */
    @Async
    private void updateMetadataAndFeatures(Picture picture) {
            metadataRepository.updateAllFromPicture(picture);
    }

    /**
     * Delete a picture and its metadata from a given uuid
     * @param uuid
     * @throws IOException
     */
    public void delete(UUID uuid) throws IOException {

        Picture picture = pictureRepository.findOne(uuid);

        /* delete picture */
        pictureRepository.delete(picture);

        /* delete extra info as asyncronous method */
        deleteMetadataAndLuceneIndex(uuid, picture.getOwner());

        generalCounterRepository.decrementPicture(CURRENT_YEAR);
        userCounterRepository.decrementPicture(picture.getOwner());
    }

    /**
     * Delete metadata information
     * @param uuid
     * @throws IOException
     */
    @Async
    private void deleteMetadataAndLuceneIndex(UUID uuid, String user) throws IOException {

        /* delete from Lucene */
        LireBuilder.delete(uuid, lireConfiguration.getConf());

        /* decrement metadata for all metadata */
        int total = metadataRepository.findByPicture_id(uuid).size();
        generalCounterRepository.decrementMetadata(total, CURRENT_YEAR);
        userCounterRepository.decrementMetadata(total, user);

        /* delete all features list from picture */
        metadataRepository.deleteFromPicture(uuid);
    }

    // TODO Add asynctask for every year to update counters;
}

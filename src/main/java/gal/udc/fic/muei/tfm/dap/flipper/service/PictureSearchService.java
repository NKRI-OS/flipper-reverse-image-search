package gal.udc.fic.muei.tfm.dap.flipper.service;

import gal.udc.fic.muei.tfm.dap.flipper.domain.Picture;
import gal.udc.fic.muei.tfm.dap.flipper.domain.PictureFound;
import gal.udc.fic.muei.tfm.dap.flipper.domain.PictureSearch;
import gal.udc.fic.muei.tfm.dap.flipper.domain.enumeration.FeatureEnumerate;
import gal.udc.fic.muei.tfm.dap.flipper.repository.*;
import gal.udc.fic.muei.tfm.dap.flipper.security.SecurityUtils;
import gal.udc.fic.muei.tfm.dap.flipper.service.util.PictureScaleUtil;
import gal.udc.fic.muei.tfm.dap.flipper.service.util.cbir.Feature;
import gal.udc.fic.muei.tfm.dap.flipper.service.util.cbir.FeatureExtractorBuilder;
import gal.udc.fic.muei.tfm.dap.flipper.service.util.cbir.LireBuilder;
import gal.udc.fic.muei.tfm.dap.flipper.service.util.cbir.LirePictureSortable;
import net.semanticmetadata.lire.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
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
@Service
public class PictureSearchService {

    private static final int MAX_RESULTS = 100;
    private static final int LIMIT_PER_WRITE = 50;
    private static final int MAX_WRITES = 20;
    private static final int CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR);
    private final Logger log = LoggerFactory.getLogger(PictureSearchService.class);

    @Inject
    private PictureSearchRepository pictureSearchRepository;

    @Inject
    private PictureFoundRepository pictureFoundRepository;

    @Inject
    private GeneralCounterRepository generalCounterRepository;

    @Inject
    private UserCounterRepository userCounterRepository;

    @Inject
    private PictureSearchCounterRepository pictureSearchCounterRepository;

    @Inject
    private PictureRepository pictureRepository;

    /**
     * Scale pictures
     * @param pictureSearch
     * @throws IOException
     */
    @Async
    private void scaleImage(PictureSearch pictureSearch) throws IOException {

        ByteArrayInputStream in = new ByteArrayInputStream(pictureSearch.getPictureFile().array());
        BufferedImage image = ImageIO.read(in);

        /* little */
        if (Math.max(image.getHeight(), image.getWidth()) > PictureScaleUtil.LITTLE_SIZE) {
            BufferedImage littleImg = ImageUtils.scaleImage(image, PictureScaleUtil.LITTLE_SIZE);
            pictureSearch.setLittlePictureFile(ByteBuffer.wrap(PictureScaleUtil.setBufferedImage(littleImg)));
        }else {
            pictureSearch.setLittlePictureFile(ByteBuffer.wrap(pictureSearch.getPictureFile().array()));
        }

    }

    /**
     * Create new picture search
     * @param source
     * @throws IOException
     */
    @Async
    private PictureSearch createPictureSearch(byte[] source) throws IOException {

        PictureSearch pictureSearch = new PictureSearch();
        pictureSearch.setCreated(new Date());
        pictureSearch.setPictureFile(ByteBuffer.wrap(source));

        String user = "anonymousUser";
        if(SecurityUtils.isAuthenticated()){
            user = SecurityUtils.getCurrentLogin();
            pictureSearch.setUserLogin(user);
        }

        /* scale picture */
        this.scaleImage(pictureSearch);

        /* save */
        pictureSearchRepository.save(pictureSearch);

        /* increment search */
        generalCounterRepository.incrementPictureSearch(CURRENT_YEAR);
        userCounterRepository.incrementPictureSearch(user);

        return pictureSearch;
    }

    /**
     * Set features to search
     *
     * @param pictureSearch
     * @throws IOException
     */
    private List<FeatureEnumerate> extractFeatures(PictureSearch pictureSearch) throws IOException {

        List<FeatureEnumerate> listFeaturesFromPictureSearch = new ArrayList<>();

        Feature autocolor = FeatureExtractorBuilder.extract(pictureSearch.getPictureFile().array(), FeatureEnumerate.AutoColorCorrelogram);
        if(autocolor != null){
            listFeaturesFromPictureSearch.add(FeatureEnumerate.AutoColorCorrelogram);
            pictureSearch.setAutocolorCorrelogram(autocolor.getFeature());
            pictureSearch.setAutocolorCorrelogramAsBase64(autocolor.getFeatureAsBase64());
        }

        Feature CEDD = FeatureExtractorBuilder.extract(pictureSearch.getPictureFile().array(), FeatureEnumerate.CEDD);
        if(CEDD != null){
            listFeaturesFromPictureSearch.add(FeatureEnumerate.CEDD);
            pictureSearch.setCedd(CEDD.getFeature());
            pictureSearch.setCeddAsBase64(CEDD.getFeatureAsBase64());
        }

        Feature colorHistogram = FeatureExtractorBuilder.extract(pictureSearch.getPictureFile().array(), FeatureEnumerate.ColorHistogram);
        if(colorHistogram != null) {
            listFeaturesFromPictureSearch.add(FeatureEnumerate.ColorHistogram);
            pictureSearch.setColorHistogram(colorHistogram.getFeature());
            pictureSearch.setColorHistogramAsBase64(colorHistogram.getFeatureAsBase64());
        }

        Feature colorLayout = FeatureExtractorBuilder.extract(pictureSearch.getPictureFile().array(), FeatureEnumerate.ColorLayout);
        if(colorLayout != null) {
            listFeaturesFromPictureSearch.add(FeatureEnumerate.ColorLayout);
            pictureSearch.setColorLayout(colorLayout.getFeature());
            pictureSearch.setColorLayoutAsBase64(colorLayout.getFeatureAsBase64());
        }

        Feature edgeHistogram = FeatureExtractorBuilder.extract(pictureSearch.getPictureFile().array(), FeatureEnumerate.EdgeHistogram);
        if(edgeHistogram != null) {
            listFeaturesFromPictureSearch.add(FeatureEnumerate.EdgeHistogram);
            pictureSearch.setEdgeHistogram(edgeHistogram.getFeature());
            pictureSearch.setEdgeHistogramAsBase64(edgeHistogram.getFeatureAsBase64());
        }

        Feature phog = FeatureExtractorBuilder.extract(pictureSearch.getPictureFile().array(), FeatureEnumerate.PHOG);
        if(phog != null){
            listFeaturesFromPictureSearch.add(FeatureEnumerate.PHOG);
            pictureSearch.setPhog(phog.getFeature());
            pictureSearch.setPhogAsBase64(phog.getFeatureAsBase64());
        }

        return listFeaturesFromPictureSearch;
    }

    /**
     * Search images and filter by total features
     *
     * @param source
     * @param featureEnumerates
     * @return
     */
    private List<LirePictureSortable> searchImages(byte[] source, List<FeatureEnumerate> featureEnumerates) throws IOException {

        List<LirePictureSortable> temp = LireBuilder.searchAllFeatures(source, MAX_RESULTS, featureEnumerates);
        return temp;

        //List<LirePictureSortable> result = new ArrayList<>();

        /* Only add if appears in min descriptor from the source or min */
        /*
        for(LirePictureSortable lp : temp)
        {
            // Greater or equal than the pictureSearch features number
            if(lp.getDescriptor().size() >= featureEnumerates.size()){
                result.add(lp);
            }
        }
        */

        //return result;
    }

    /**
     * Search pictures by feature query
     * @param source
     * @return
     */
    public PictureSearch searchByAllFeatures(byte[] source) throws IOException
    {
        /* create picture search */
        PictureSearch search = createPictureSearch(source);

        /* extract features */
        List<FeatureEnumerate> featureEnumerates = this.extractFeatures(search);
        if(featureEnumerates.size() == 0){
            return search;
        }

        /* search by pictures */
        List<LirePictureSortable> pictureIdsList = searchImages(source, featureEnumerates);

        /* create pictures found */
        createPicturesFound(search, pictureIdsList);

        return search;
    }

    /**
     * Search pictures by feature query
     * @param source
     * @return
     */
    public List<PictureFound> searchByAllFeaturesWithList(byte[] source) throws IOException
    {
        /* create picture search */
        PictureSearch search = createPictureSearch(source);

        /* extract features */
        List<FeatureEnumerate> featureEnumerates = this.extractFeatures(search);
        if(featureEnumerates.size() == 0)
        {
            return new ArrayList<>();
        }

        // Search all features
        List<LirePictureSortable> pictureIdsList = searchImages(source, featureEnumerates);

        /* create pictures found */
        return createPicturesFound(search, pictureIdsList);
    }

    @Async
    private List<PictureFound> createPicturesFound(PictureSearch pictureSearch, List<LirePictureSortable> pictureIdsList) {

        List<PictureFound> result = new ArrayList<>();
        List<UUID> uuids = pictureIdsList.stream().map(LirePictureSortable::getId).collect(Collectors.toList());
        UUID pictureSearchId = pictureSearch.getId();

        Picture picture = null;
        for(LirePictureSortable lp : pictureIdsList)
        {
            picture = pictureRepository.findOne(lp.getId());
            if(picture != null){
                result.add(createPictureFound(pictureSearchId, lp, picture));
            }
        }

        /* save pictures found */
        saveAllPicturesFound(result, pictureSearchId);

        return result;
    }

    private void saveAllPicturesFound(List<PictureFound> picturesFoundList, UUID pictureSearchId) {


        if (picturesFoundList.size() >= LIMIT_PER_WRITE) {

            // TODO review this solution
            List<PictureFound>[] subsets = new ArrayList[MAX_WRITES];
            PictureFound[] array = picturesFoundList.toArray(new PictureFound[picturesFoundList.size()]);

            for (int i = 1; i <= MAX_WRITES; i++) {
                int fromIndex = (i - 1) * picturesFoundList.size() / MAX_WRITES;
                int toIndex = i * picturesFoundList.size() / MAX_WRITES - 1;
                List<PictureFound> subHashSet = new ArrayList<>();
                subHashSet.addAll(Arrays.asList(Arrays.copyOfRange(array, fromIndex, toIndex)));

                subsets[i - 1] = subHashSet;
            }

            // Avoid OutOfBand in Heap memory
            for (List<PictureFound> subset : subsets) {
                pictureFoundRepository.saveAll(subset);

                /* increment picture found size */
                pictureSearchCounterRepository.incrementPictureSearch(subset.size(), pictureSearchId);
            }
        } else {
            pictureFoundRepository.saveAll(picturesFoundList);

            /* increment picture found size */
            pictureSearchCounterRepository.incrementPictureSearch(picturesFoundList.size(), pictureSearchId);
        }
    }

    private PictureFound createPictureFound(UUID pictureSearch_id, LirePictureSortable lp, Picture picture)
    {
        PictureFound result = new PictureFound();
        result.setPictureSearch_id(pictureSearch_id);
        result.setTotalScore(lp.getScore());
        result.setPicture(picture);

        /* set score for each feature */
        for(Map.Entry<FeatureEnumerate, Float> entry : lp.getDescriptor().entrySet()) {
            switch (entry.getKey()){
                case AutoColorCorrelogram:
                    result.setAutocolorCorrelogramScore(entry.getValue());
                    break;
                case CEDD:
                    result.setCeddScore(entry.getValue());
                    break;
                case ColorLayout:
                    result.setColorLayoutScore(entry.getValue());
                    break;
                case EdgeHistogram:
                    result.setEdgeHistogramScore(entry.getValue());
                    break;
                case ColorHistogram:
                    result.setColorHistogramScore(entry.getValue());
                    break;
                case PHOG:
                    result.setPhogScore(entry.getValue());
                    break;
            }
        }

        return result;
    }

    /**
     * removes pictures found from picture search and picture search
     * @param uuid
     */
    public void delete(UUID uuid)
    {
        PictureSearch pictureSearch = pictureSearchRepository.findOne(uuid);

        /* delete all pictures found from pictureSearch */
        pictureFoundRepository.deleteByPictureSearch(uuid);

        pictureSearchRepository.delete(pictureSearch);

        /* decrement picture search */
        generalCounterRepository.decrementPictureSearch(CURRENT_YEAR);
        userCounterRepository.decrementPictureSearch(pictureSearch.getUserLogin());

        /* decrement picture found from search */
        long totalFound = pictureSearchRepository.getTotalPicturesFound(uuid);
        pictureSearchCounterRepository.decrementPictureSearch(totalFound, uuid);
    }

    // TODO Add asynctask for every year to update counters;

}

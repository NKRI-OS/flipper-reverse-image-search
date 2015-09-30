package gal.udc.fic.muei.tfm.dap.flipper.service.util.cbir;

import gal.udc.fic.muei.tfm.dap.flipper.domain.enumeration.FeatureEnumerate;
import net.semanticmetadata.lire.DocumentBuilder;
import net.semanticmetadata.lire.DocumentBuilderFactory;
import net.semanticmetadata.lire.ImageSearchHits;
import net.semanticmetadata.lire.ImageSearcher;
import net.semanticmetadata.lire.impl.GenericFastImageSearcher;
import net.semanticmetadata.lire.utils.LuceneUtils;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 *
 * Lire indexer and search in Apache Lucene
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
public class LireBuilder {

    private static final Logger log = LoggerFactory.getLogger(LireBuilder.class);

    private static final String INDEX_DIR = "index";
    private static final int MIN_DESCRIPTORS = 4;

    /**
     * Index for each builder type
     * @param image
     * @param picture_id
     * @param prefix
     * @param builder
     * @param conf
     * @throws IOException
     */
    private static void luceneIndexer(BufferedImage image, UUID picture_id, String prefix, DocumentBuilder builder, IndexWriterConfig conf)
        throws IOException
    {
        File path = getPath(prefix);
        log.debug("creating indexed path " + path.getAbsolutePath());
        IndexWriter iw = new IndexWriter(FSDirectory.open(path), conf);

        try {
            Document document = builder.createDocument(image, picture_id.toString());
            iw.addDocument(document);

        } catch (Exception e) {
            System.err.println("Error reading image or indexing it.");
            e.printStackTrace();
        }

        // closing the IndexWriter
        iw.close();
    }

    /**
     * Search a picture
     *
     * @param imageBytes
     * @param maxHits
     * @param feature
     * @param pictures
     * @return
     * @throws IOException
     */
    public static List<LirePictureSortable> search(byte[] imageBytes, int maxHits, FeatureEnumerate feature,
                                                  List<LirePictureSortable> pictures) throws IOException
    {
        File path = getPath(feature.getText());
        log.debug("reading from indexed path " +path.getAbsolutePath());
        List<LirePictureSortable> result = new ArrayList<>();

        try{
            IndexReader ir = DirectoryReader.open(FSDirectory.open(path));
            ImageSearcher searcher = new GenericFastImageSearcher(maxHits, feature.getValueClass());

            // searching with a image file ...
            InputStream in = new ByteArrayInputStream(imageBytes);

            ImageSearchHits hits = searcher.search(in, ir);

            float score = 0.0F;
            for (int i = 0; i < hits.length(); i++) {
                score = hits.score(i);
                LirePictureSortable lp = new LirePictureSortable(
                    UUID.fromString(hits.doc(i).getValues(DocumentBuilder.FIELD_NAME_IDENTIFIER)[0]),
                    score, feature);

            /* check is its a picture was repeated by picture UUID */
                if(pictures.contains(lp))
                {
                    lp = pictures.get(pictures.indexOf(lp));
                    lp.addDescriptor(feature, score);
                    lp.addScore(score);
                }else {
                    result.add(lp);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Delete a term document
     * @param pictureId
     * @param conf
     * @throws IOException
     */
    public static void delete(UUID pictureId, IndexWriterConfig conf) throws IOException {

        /* create term to delete document */
        Term term = new Term(DocumentBuilder.FIELD_NAME_IDENTIFIER, pictureId.toString());

        for(FeatureEnumerate f : FeatureEnumerate.values()){
            deleteFromFeature(pictureId, term, f.getText(), conf);
        }
    }

    /**
     * Index a picture
     * @param source
     * @param picture_id
     * @param conf
     * @throws IOException
     */
    public static void index(byte[] source, UUID picture_id, IndexWriterConfig conf) throws IOException
    {
        ByteArrayInputStream in = new ByteArrayInputStream(source);
        BufferedImage image = ImageIO.read(in);

        // Creating an Lucene IndexWriter
        log.debug("Is Lucene configured? " + (conf == null));
        if(conf == null) {
            conf = new IndexWriterConfig(LuceneUtils.LUCENE_VERSION, new WhitespaceAnalyzer(LuceneUtils.LUCENE_VERSION));
            conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        }

        luceneIndexer(image, picture_id, FeatureEnumerate.AutoColorCorrelogram.getText(), DocumentBuilderFactory.getAutoColorCorrelogramDocumentBuilder(), conf);
        luceneIndexer(image, picture_id, FeatureEnumerate.CEDD.getText(), DocumentBuilderFactory.getCEDDDocumentBuilder(), conf);
        luceneIndexer(image, picture_id, FeatureEnumerate.ColorLayout.getText(), DocumentBuilderFactory.getColorLayoutBuilder(), conf);
        luceneIndexer(image, picture_id, FeatureEnumerate.EdgeHistogram.getText(), DocumentBuilderFactory.getEdgeHistogramBuilder(), conf);
        luceneIndexer(image, picture_id, FeatureEnumerate.ColorHistogram.getText(), DocumentBuilderFactory.getColorHistogramDocumentBuilder(), conf);
        luceneIndexer(image, picture_id, FeatureEnumerate.PHOG.getText(), DocumentBuilderFactory.getPHOGDocumentBuilder(), conf);

    }

    private static void deleteFromFeature(UUID pictureId, Term term, String prefix, IndexWriterConfig conf) throws IOException {

        File file = getPath(prefix);

        // Creating an Lucene IndexWriter
        log.debug("Is Lucene configured: " + (conf == null));
        if(conf == null) {
            conf = new IndexWriterConfig(LuceneUtils.LUCENE_VERSION, new WhitespaceAnalyzer(LuceneUtils.LUCENE_VERSION));
            conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        }
        IndexWriter iw = new IndexWriter(FSDirectory.open(file), conf);

        iw.deleteDocuments(term);

        iw.close();
    }


    private static File getPath(String prefix) throws IOException {
        File path = new File(INDEX_DIR + File.separator + prefix);
        if(!(path.exists() && path.isDirectory()) && !path.mkdirs())
        {
                throw new IOException(path.getName() + " not exists.");
        }

        return path;
    }

    /**
     * Search pictures that appear in all features
     *
     * @param source
     * @param maxHist
     * @param featureEnumerates
     * @return
     * @throws IOException
     */
    public static List<LirePictureSortable> searchAllFeatures(byte[] source, int maxHist, List<FeatureEnumerate> featureEnumerates) throws IOException {

        /* extract pictures sortable info from Lucene indexes */
        /* only from extract features from picture search */
        List<LirePictureSortable> result = new ArrayList<>();
        for(FeatureEnumerate feature : featureEnumerates)
        {
            result.addAll(LireBuilder.search(source, maxHist, feature, result));
        }

        /* save UUID from pictures ordered by score */
        Collections.sort(result);

        return result;
    }
}

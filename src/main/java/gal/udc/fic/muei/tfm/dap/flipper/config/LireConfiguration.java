package gal.udc.fic.muei.tfm.dap.flipper.config;

import net.semanticmetadata.lire.utils.LuceneUtils;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

/**
 * Configuration of web application with Servlet 3.0 APIs.
 */
@Configuration
public class LireConfiguration{

    private final Logger log = LoggerFactory.getLogger(LireConfiguration.class);

    private final String INDEX_DIR = "index";

    private IndexWriterConfig conf;

    private File getPath(String prefix) throws IOException {
        File path = new File(INDEX_DIR + File.separator + prefix);
        if(!(path.exists() && path.isDirectory()) && !path.mkdirs())
        {
            throw new IOException(path.getName() + " not exists.");
        }

        return path;
    }

    @PostConstruct
    public void init() throws IOException {
        log.info("Creating Lucene config");

        // Creating an Lucene IndexWriter
        IndexWriterConfig conf = new IndexWriterConfig(LuceneUtils.LUCENE_VERSION,
            new WhitespaceAnalyzer(LuceneUtils.LUCENE_VERSION));
        conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);

    }

    public IndexWriterConfig getConf() {
        return conf;
    }

    public void setConf(IndexWriterConfig conf) {
        this.conf = conf;
    }
}

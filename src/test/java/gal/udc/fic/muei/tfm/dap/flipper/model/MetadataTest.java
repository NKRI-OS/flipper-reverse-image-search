package gal.udc.fic.muei.tfm.dap.flipper.model;

import gal.udc.fic.muei.tfm.dap.flipper.AbstractCassandraTest;
import gal.udc.fic.muei.tfm.dap.flipper.Application;
import gal.udc.fic.muei.tfm.dap.flipper.domain.Metadata;
import gal.udc.fic.muei.tfm.dap.flipper.repository.MetadataRepository;
import org.assertj.core.api.StrictAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the MetadataRepository and MetadataService.
 *
 * @see MetadataRepository
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@IntegrationTest
public class MetadataTest extends AbstractCassandraTest {


    private static final String DEFAULT_DIRECTORY_NAME = "SAMPLE_TEXT";
    private static final Integer DEFAULT_TAG_TYPE = 1;
    private static final String DEFAULT_TAG_NAME = "SAMPLE_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";

    private static final String UPDATED_DIRECTORY_NAME = "UPDATED_TEXT";
    private static final Integer UPDATED_TAG_TYPE = 1;
    private static final String UPDATED_TAG_NAME = "UPDATED_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    private static final UUID DEFAULT_PICTURE_ID = UUID.randomUUID();
    private static final UUID UPDATED_PICTURE_ID = UUID.randomUUID();
    private static final String DEFAULT_TITLE = "SAMPLE_TEXT";
    private static final String UPDATED_TITLE = "UPDATED_TEXT";
    private static final byte[] DEFAULT_PICTURE_FILE = "SAMPLE_TEXT".getBytes();
    private static final byte[] UPDATED_PICTURE_FILE = "UPDATED_TEXT".getBytes();

    @Inject
    private MetadataRepository metadataRepository;

    private Metadata metadata;

    @PostConstruct
    public void setup() {

    }

    @Before
    public void initTest() {
        metadataRepository.deleteAll();
        metadata = new Metadata();
        metadata.setDirectoryName(DEFAULT_DIRECTORY_NAME);
        metadata.setTagType(DEFAULT_TAG_TYPE);
        metadata.setTagName(DEFAULT_TAG_NAME);
        metadata.setDescription(DEFAULT_DESCRIPTION);
        metadata.setPicture_id(DEFAULT_PICTURE_ID);
        metadata.setTitle(DEFAULT_TITLE);
        metadata.setPictureFile(ByteBuffer.wrap(DEFAULT_PICTURE_FILE));
    }

    private Metadata createObject(){
        metadata = new Metadata();
        metadata.setDirectoryName(DEFAULT_DIRECTORY_NAME);
        metadata.setTagType(DEFAULT_TAG_TYPE);
        metadata.setTagName(DEFAULT_TAG_NAME);
        metadata.setDescription(DEFAULT_DESCRIPTION);
        metadata.setPicture_id(DEFAULT_PICTURE_ID);
        metadata.setTitle(DEFAULT_TITLE);
        metadata.setPictureFile(ByteBuffer.wrap(DEFAULT_PICTURE_FILE));

        return metadata;
    }

    @Test
    public void createMetadata() throws Exception {
        int databaseSizeBeforeCreate = metadataRepository.findAll().size();

        // Create the Metadata
        metadataRepository.save(metadata);

        // Validate the Metadata in the database
        List<Metadata> metadatas = metadataRepository.findAll();
        assertThat(metadatas).hasSize(databaseSizeBeforeCreate + 1);

        /* find metadata */
        Metadata testMetadata = metadataRepository.findOne(metadata.getId());

        StrictAssertions.assertThat(testMetadata.getDirectoryName()).isEqualTo(DEFAULT_DIRECTORY_NAME);
        StrictAssertions.assertThat(testMetadata.getTagType()).isEqualTo(DEFAULT_TAG_TYPE);
        StrictAssertions.assertThat(testMetadata.getTagName()).isEqualTo(DEFAULT_TAG_NAME);
        StrictAssertions.assertThat(testMetadata.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        StrictAssertions.assertThat(testMetadata.getPicture_id()).isEqualTo(DEFAULT_PICTURE_ID);
        StrictAssertions.assertThat(testMetadata.getTitle()).isEqualTo(DEFAULT_TITLE);
        StrictAssertions.assertThat(testMetadata.getPictureFile()).isEqualTo(DEFAULT_PICTURE_FILE);
    }

    @Test
    public void deleteOneMetadata() throws Exception {
        Metadata deleteMetadata = this.createObject();
        int databaseSizeBeforeCreate = metadataRepository.findAll().size();

        // Create the Metadata
        metadataRepository.save(deleteMetadata);

        // Validate the Metadata in the database
        List<Metadata> metadatas = metadataRepository.findAll();
        assertThat(metadatas).hasSize(databaseSizeBeforeCreate + 1);

        /* delete metadata */
        metadataRepository.delete(deleteMetadata);

        // Validate the Metadata in the database
        metadatas = metadataRepository.findAll();
        assertThat(metadatas).hasSize(databaseSizeBeforeCreate - 1);

        // Find deleted metadata
        StrictAssertions.assertThat(metadataRepository.findOne(deleteMetadata.getId())).isEqualTo(null);
    }



}

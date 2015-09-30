package gal.udc.fic.muei.tfm.dap.flipper.web.rest;

import gal.udc.fic.muei.tfm.dap.flipper.AbstractCassandraTest;
import gal.udc.fic.muei.tfm.dap.flipper.Application;
import gal.udc.fic.muei.tfm.dap.flipper.domain.Metadata;
import gal.udc.fic.muei.tfm.dap.flipper.repository.MetadataRepository;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto.MetadataDTO;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.mapper.MetadataMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the MetadataResource REST controller.
 *
 * @see MetadataResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MetadataResourceTest extends AbstractCassandraTest {

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

    @Inject
    private MetadataMapper metadataMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restMetadataMockMvc;

    private Metadata metadata;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MetadataResource metadataResource = new MetadataResource();
        ReflectionTestUtils.setField(metadataResource, "metadataRepository", metadataRepository);
        ReflectionTestUtils.setField(metadataResource, "metadataMapper", metadataMapper);
        this.restMetadataMockMvc = MockMvcBuilders.standaloneSetup(metadataResource).setMessageConverters(jacksonMessageConverter).build();
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

    @Test
    public void createMetadata() throws Exception {
        int databaseSizeBeforeCreate = metadataRepository.findAll().size();

        // Create the Metadata
        MetadataDTO metadataDTO = metadataMapper.metadataToMetadataDTO(metadata);

        restMetadataMockMvc.perform(post("/api/metadatas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(metadataDTO)))
                .andExpect(status().isCreated());

        // Validate the Metadata in the database
        List<Metadata> metadatas = metadataRepository.findAll();
        assertThat(metadatas).hasSize(databaseSizeBeforeCreate + 1);
        Metadata testMetadata = metadatas.get(metadatas.size() - 1);
        assertThat(testMetadata.getDirectoryName()).isEqualTo(DEFAULT_DIRECTORY_NAME);
        assertThat(testMetadata.getTagType()).isEqualTo(DEFAULT_TAG_TYPE);
        assertThat(testMetadata.getTagName()).isEqualTo(DEFAULT_TAG_NAME);
        assertThat(testMetadata.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMetadata.getPicture_id()).isEqualTo(DEFAULT_PICTURE_ID);
        assertThat(testMetadata.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMetadata.getPictureFile()).isEqualTo(DEFAULT_PICTURE_FILE);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = metadataRepository.findAll().size();
        // set the field null
        metadata.setDirectoryName(null);

        // Create the Metadata, which fails.
        MetadataDTO metadataDTO = metadataMapper.metadataToMetadataDTO(metadata);

        restMetadataMockMvc.perform(post("/api/metadatas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(metadataDTO)))
                .andExpect(status().isBadRequest());

        List<Metadata> metadatas = metadataRepository.findAll();
        assertThat(metadatas).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = metadataRepository.findAll().size();
        // set the field null
        metadata.setTagName(null);

        // Create the Metadata, which fails.
        MetadataDTO metadataDTO = metadataMapper.metadataToMetadataDTO(metadata);

        restMetadataMockMvc.perform(post("/api/metadatas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(metadataDTO)))
                .andExpect(status().isBadRequest());

        List<Metadata> metadatas = metadataRepository.findAll();
        assertThat(metadatas).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkValueDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = metadataRepository.findAll().size();
        // set the field null
        metadata.setDescription(null);

        // Create the Metadata, which fails.
        MetadataDTO metadataDTO = metadataMapper.metadataToMetadataDTO(metadata);

        restMetadataMockMvc.perform(post("/api/metadatas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(metadataDTO)))
                .andExpect(status().isBadRequest());

        List<Metadata> metadatas = metadataRepository.findAll();
        assertThat(metadatas).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkPicture_idIsRequired() throws Exception {
        int databaseSizeBeforeTest = metadataRepository.findAll().size();
        // set the field null
        metadata.setPicture_id(null);

        // Create the Metadata, which fails.
        MetadataDTO metadataDTO = metadataMapper.metadataToMetadataDTO(metadata);

        restMetadataMockMvc.perform(post("/api/metadatas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(metadataDTO)))
                .andExpect(status().isBadRequest());

        List<Metadata> metadatas = metadataRepository.findAll();
        assertThat(metadatas).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllMetadatas() throws Exception {
        // Initialize the database
        metadataRepository.save(metadata);

        // Get all the metadatas
        restMetadataMockMvc.perform(get("/api/metadatas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(metadata.getId().toString())))
                .andExpect(jsonPath("$.[*].directoryName").value(hasItem(DEFAULT_DIRECTORY_NAME.toString())))
                .andExpect(jsonPath("$.[*].tagType").value(hasItem(DEFAULT_TAG_TYPE.toString())))
                .andExpect(jsonPath("$.[*].tagName").value(hasItem(DEFAULT_TAG_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].picture_id").value(hasItem(DEFAULT_PICTURE_ID.toString())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].pictureFile").value(hasItem(DEFAULT_PICTURE_FILE.toString())));
    }

    @Test
    public void getMetadata() throws Exception {
        // Initialize the database
        metadataRepository.save(metadata);

        // Get the metadata
        restMetadataMockMvc.perform(get("/api/metadatas/{id}", metadata.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(metadata.getId().toString()))
            .andExpect(jsonPath("$.[*].directoryName").value(hasItem(DEFAULT_DIRECTORY_NAME.toString())))
            .andExpect(jsonPath("$.[*].tagType").value(hasItem(DEFAULT_TAG_TYPE.toString())))
            .andExpect(jsonPath("$.[*].tagName").value(hasItem(DEFAULT_TAG_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.picture_id").value(DEFAULT_PICTURE_ID.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.pictureFile").value(DEFAULT_PICTURE_FILE.toString()));
    }

    @Test
    public void getNonExistingMetadata() throws Exception {
        // Get the metadata
        restMetadataMockMvc.perform(get("/api/metadatas/{id}", UUID.randomUUID().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateMetadata() throws Exception {
        // Initialize the database
        metadataRepository.save(metadata);

		int databaseSizeBeforeUpdate = metadataRepository.findAll().size();

        // Update the metadata
        metadata.setDirectoryName(UPDATED_DIRECTORY_NAME);
        metadata.setTagType(UPDATED_TAG_TYPE);
        metadata.setTagName(UPDATED_TAG_NAME);
        metadata.setDescription(UPDATED_DESCRIPTION);
        metadata.setPicture_id(UPDATED_PICTURE_ID);
        metadata.setTitle(UPDATED_TITLE);
        metadata.setPictureFile(ByteBuffer.wrap(UPDATED_PICTURE_FILE));

        MetadataDTO metadataDTO = metadataMapper.metadataToMetadataDTO(metadata);

        restMetadataMockMvc.perform(put("/api/metadatas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(metadataDTO)))
                .andExpect(status().isOk());

        // Validate the Metadata in the database
        List<Metadata> metadatas = metadataRepository.findAll();
        assertThat(metadatas).hasSize(databaseSizeBeforeUpdate);
        Metadata testMetadata = metadatas.get(metadatas.size() - 1);
        assertThat(testMetadata.getDirectoryName()).isEqualTo(UPDATED_DIRECTORY_NAME);
        assertThat(testMetadata.getTagType()).isEqualTo(UPDATED_TAG_TYPE);
        assertThat(testMetadata.getTagName()).isEqualTo(UPDATED_TAG_NAME);
        assertThat(testMetadata.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMetadata.getPicture_id()).isEqualTo(UPDATED_PICTURE_ID);
        assertThat(testMetadata.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMetadata.getPictureFile()).isEqualTo(UPDATED_PICTURE_FILE);
    }

    @Test
    public void deleteMetadata() throws Exception {
        // Initialize the database
        metadataRepository.save(metadata);

		int databaseSizeBeforeDelete = metadataRepository.findAll().size();

        // Get the metadata
        restMetadataMockMvc.perform(delete("/api/metadatas/{id}", metadata.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Metadata> metadatas = metadataRepository.findAll();
        assertThat(metadatas).hasSize(databaseSizeBeforeDelete - 1);
    }
}

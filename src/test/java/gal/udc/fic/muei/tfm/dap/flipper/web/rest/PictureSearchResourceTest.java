package gal.udc.fic.muei.tfm.dap.flipper.web.rest;

import gal.udc.fic.muei.tfm.dap.flipper.AbstractCassandraTest;
import gal.udc.fic.muei.tfm.dap.flipper.Application;
import gal.udc.fic.muei.tfm.dap.flipper.domain.PictureSearch;
import gal.udc.fic.muei.tfm.dap.flipper.domain.enumeration.FeatureEnumerate;
import gal.udc.fic.muei.tfm.dap.flipper.repository.PictureSearchRepository;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto.PictureSearchDTO;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.mapper.PictureSearchMapper;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PictureSearchResource REST controller.
 *
 * @see PictureSearchResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PictureSearchResourceTest extends AbstractCassandraTest {

    private static final byte[] DEFAULT_PICTURE_FILE = "SAMPLE_TEXT".getBytes();
    private static final byte[] UPDATED_PICTURE_FILE = "UPDATED_TEXT".getBytes();

    private static final Integer DEFAULT_MAX_HITS = 1;
    private static final Integer UPDATED_MAX_HITS = 2;
    private static final FeatureEnumerate DEFAULT_FEATURE_DESCRIPTOR = FeatureEnumerate.AutoColorCorrelogram;
    private static final FeatureEnumerate UPDATED_FEATURE_DESCRIPTOR = FeatureEnumerate.CEDD;

    private static final Date DEFAULT_CREATED = new Date();
    private static final Date UPDATED_CREATED = new Date();

    private static final String DEFAULT_USER_LOGIN = "SAMPLE_TEXT";
    private static final String UPDATED_USER_LOGIN = "UPDATED_TEXT";

    @Inject
    private PictureSearchRepository pictureSearchRepository;

    @Inject
    private PictureSearchMapper pictureSearchMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restPictureSearchMockMvc;

    private PictureSearch pictureSearch;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PictureSearchResource pictureSearchResource = new PictureSearchResource();
        ReflectionTestUtils.setField(pictureSearchResource, "pictureSearchRepository", pictureSearchRepository);
        ReflectionTestUtils.setField(pictureSearchResource, "pictureSearchMapper", pictureSearchMapper);
        this.restPictureSearchMockMvc = MockMvcBuilders.standaloneSetup(pictureSearchResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        pictureSearchRepository.deleteAll();
        pictureSearch = new PictureSearch();
        pictureSearch.setPictureFile(ByteBuffer.wrap(DEFAULT_PICTURE_FILE));
        pictureSearch.setCreated(DEFAULT_CREATED);
        pictureSearch.setUserLogin(DEFAULT_USER_LOGIN);
    }

    @Test
    public void createPictureSearch() throws Exception {
        int databaseSizeBeforeCreate = pictureSearchRepository.findAll().size();

        // Create the PictureSearch
        PictureSearchDTO pictureSearchDTO = pictureSearchMapper.pictureSearchToPictureSearchDTO(pictureSearch);

        restPictureSearchMockMvc.perform(post("/api/pictureSearchs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pictureSearchDTO)))
                .andExpect(status().isCreated());

        // Validate the PictureSearch in the database
        List<PictureSearch> pictureSearchs = pictureSearchRepository.findAll();
        assertThat(pictureSearchs).hasSize(databaseSizeBeforeCreate + 1);
        PictureSearch testPictureSearch = pictureSearchs.get(pictureSearchs.size() - 1);
        assertThat(testPictureSearch.getPictureFile()).isEqualTo(DEFAULT_PICTURE_FILE);
        assertThat(testPictureSearch.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testPictureSearch.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
    }

    @Test
    public void checkMaxHitsIsRequired() throws Exception {
        int databaseSizeBeforeTest = pictureSearchRepository.findAll().size();
        // set the field null


        // Create the PictureSearch, which fails.
        PictureSearchDTO pictureSearchDTO = pictureSearchMapper.pictureSearchToPictureSearchDTO(pictureSearch);

        restPictureSearchMockMvc.perform(post("/api/pictureSearchs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pictureSearchDTO)))
                .andExpect(status().isBadRequest());

        List<PictureSearch> pictureSearchs = pictureSearchRepository.findAll();
        assertThat(pictureSearchs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllPictureSearchs() throws Exception {
        // Initialize the database
        pictureSearchRepository.save(pictureSearch);

        // Get all the pictureSearchs
        restPictureSearchMockMvc.perform(get("/api/pictureSearchs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(pictureSearch.getId().toString())))
                .andExpect(jsonPath("$.[*].pictureFile").value(hasItem(DEFAULT_PICTURE_FILE.toString())))
                .andExpect(jsonPath("$.[*].maxHits").value(hasItem(DEFAULT_MAX_HITS)))
                .andExpect(jsonPath("$.[*].featureDescriptor").value(hasItem(DEFAULT_FEATURE_DESCRIPTOR.toString())))
                .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.getTime())))
                .andExpect(jsonPath("$.[*].userLogin").value(hasItem(DEFAULT_USER_LOGIN.toString())));
    }

    @Test
    public void getPictureSearch() throws Exception {
        // Initialize the database
        pictureSearchRepository.save(pictureSearch);

        // Get the pictureSearch
        restPictureSearchMockMvc.perform(get("/api/pictureSearchs/{id}", pictureSearch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(pictureSearch.getId().toString()))
            .andExpect(jsonPath("$.pictureFile").value(DEFAULT_PICTURE_FILE.toString()))
            .andExpect(jsonPath("$.maxHits").value(DEFAULT_MAX_HITS))
            .andExpect(jsonPath("$.featureDescriptor").value(DEFAULT_FEATURE_DESCRIPTOR.toString()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.getTime()))
            .andExpect(jsonPath("$.userLogin").value(DEFAULT_USER_LOGIN.toString()));
    }

    @Test
    public void getNonExistingPictureSearch() throws Exception {
        // Get the pictureSearch
        restPictureSearchMockMvc.perform(get("/api/pictureSearchs/{id}", UUID.randomUUID().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updatePictureSearch() throws Exception {
        // Initialize the database
        pictureSearchRepository.save(pictureSearch);

		int databaseSizeBeforeUpdate = pictureSearchRepository.findAll().size();

        // Update the pictureSearch

        pictureSearch.setCreated(UPDATED_CREATED);
        pictureSearch.setUserLogin(UPDATED_USER_LOGIN);

        PictureSearchDTO pictureSearchDTO = pictureSearchMapper.pictureSearchToPictureSearchDTO(pictureSearch);

        restPictureSearchMockMvc.perform(put("/api/pictureSearchs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pictureSearchDTO)))
                .andExpect(status().isOk());

        // Validate the PictureSearch in the database
        List<PictureSearch> pictureSearchs = pictureSearchRepository.findAll();
        assertThat(pictureSearchs).hasSize(databaseSizeBeforeUpdate);
        PictureSearch testPictureSearch = pictureSearchs.get(pictureSearchs.size() - 1);
        assertThat(testPictureSearch.getPictureFile()).isEqualTo(UPDATED_PICTURE_FILE);

        assertThat(testPictureSearch.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testPictureSearch.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
    }

    @Test
    public void deletePictureSearch() throws Exception {
        // Initialize the database
        pictureSearchRepository.save(pictureSearch);

		int databaseSizeBeforeDelete = pictureSearchRepository.findAll().size();

        // Get the pictureSearch
        restPictureSearchMockMvc.perform(delete("/api/pictureSearchs/{id}", pictureSearch.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PictureSearch> pictureSearchs = pictureSearchRepository.findAll();
        assertThat(pictureSearchs).hasSize(databaseSizeBeforeDelete - 1);
    }
}

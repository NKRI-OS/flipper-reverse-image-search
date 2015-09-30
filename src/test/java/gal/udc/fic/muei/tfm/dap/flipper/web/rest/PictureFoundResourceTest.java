package gal.udc.fic.muei.tfm.dap.flipper.web.rest;

import gal.udc.fic.muei.tfm.dap.flipper.AbstractCassandraTest;
import gal.udc.fic.muei.tfm.dap.flipper.Application;
import gal.udc.fic.muei.tfm.dap.flipper.domain.PictureFound;
import gal.udc.fic.muei.tfm.dap.flipper.repository.PictureFoundRepository;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto.PictureFoundDTO;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.mapper.PictureFoundMapper;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PictureFoundResource REST controller.
 *
 * @see PictureFoundResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PictureFoundResourceTest extends AbstractCassandraTest {


    private static final UUID DEFAULT_PICTURE_ID = UUID.randomUUID();
    private static final UUID UPDATED_PICTURE_ID = UUID.randomUUID();
    private static final String DEFAULT_TITLE = "SAMPLE_TEXT";
    private static final String UPDATED_TITLE = "UPDATED_TEXT";
    private static final String DEFAULT_OWNER = "SAMPLE_TEXT";
    private static final String UPDATED_OWNER = "UPDATED_TEXT";

    private static final UUID DEFAULT_PICTURE_SEARCH_ID = UUID.randomUUID();
    private static final UUID UPDATED_PICTURE_SEARCH_ID = UUID.randomUUID();

    private static final Float DEFAULT_AUTOCOLOR_CORRELOGRAM_SCORE = 1F;
    private static final Float UPDATED_AUTOCOLOR_CORRELOGRAM_SCORE = 2F;

    private static final Float DEFAULT_CEDD_SCORE = 1F;
    private static final Float UPDATED_CEDD_SCORE = 2F;

    private static final Float DEFAULT_COLOR_HISTOGRAM_SCORE = 1F;
    private static final Float UPDATED_COLOR_HISTOGRAM_SCORE = 2F;

    private static final Float DEFAULT_COLOR_LAYOUT_SCORE = 1F;
    private static final Float UPDATED_COLOR_LAYOUT_SCORE = 2F;

    private static final Float DEFAULT_EDGE_HISTOGRAM_SCORE = 1F;
    private static final Float UPDATED_EDGE_HISTOGRAM_SCORE = 2F;

    private static final Float DEFAULT_PHOG_SCORE = 1F;
    private static final Float UPDATED_PHOG_SCORE = 2F;

    private static final Date DEFAULT_CREATED = new Date();
    private static final Date UPDATED_CREATED = new Date();

    @Inject
    private PictureFoundRepository pictureFoundRepository;

    @Inject
    private PictureFoundMapper pictureFoundMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restPictureFoundMockMvc;

    private PictureFound pictureFound;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PictureFoundResource pictureFoundResource = new PictureFoundResource();
        ReflectionTestUtils.setField(pictureFoundResource, "pictureFoundRepository", pictureFoundRepository);
        ReflectionTestUtils.setField(pictureFoundResource, "pictureFoundMapper", pictureFoundMapper);
        this.restPictureFoundMockMvc = MockMvcBuilders.standaloneSetup(pictureFoundResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        pictureFoundRepository.deleteAll();
        pictureFound = new PictureFound();
        pictureFound.setPicture_id(DEFAULT_PICTURE_ID);
        pictureFound.setTitle(DEFAULT_TITLE);
        pictureFound.setOwner(DEFAULT_OWNER);
        pictureFound.setPictureSearch_id(DEFAULT_PICTURE_SEARCH_ID);
        pictureFound.setAutocolorCorrelogramScore(DEFAULT_AUTOCOLOR_CORRELOGRAM_SCORE);
        pictureFound.setCeddScore(DEFAULT_CEDD_SCORE);
        pictureFound.setColorHistogramScore(DEFAULT_COLOR_HISTOGRAM_SCORE);
        pictureFound.setColorLayoutScore(DEFAULT_COLOR_LAYOUT_SCORE);
        pictureFound.setEdgeHistogramScore(DEFAULT_EDGE_HISTOGRAM_SCORE);
        pictureFound.setPhogScore(DEFAULT_PHOG_SCORE);
        pictureFound.setCreated(DEFAULT_CREATED);
    }

    @Test
    public void createPictureFound() throws Exception {
        int databaseSizeBeforeCreate = pictureFoundRepository.findAll().size();

        // Create the PictureFound
        PictureFoundDTO pictureFoundDTO = pictureFoundMapper.pictureFoundToPictureFoundDTO(pictureFound);

        restPictureFoundMockMvc.perform(post("/api/pictureFounds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pictureFoundDTO)))
                .andExpect(status().isCreated());

        // Validate the PictureFound in the database
        List<PictureFound> pictureFounds = pictureFoundRepository.findAll();
        assertThat(pictureFounds).hasSize(databaseSizeBeforeCreate + 1);
        PictureFound testPictureFound = pictureFounds.get(pictureFounds.size() - 1);
        assertThat(testPictureFound.getPicture_id()).isEqualTo(DEFAULT_PICTURE_ID);
        assertThat(testPictureFound.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPictureFound.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testPictureFound.getPictureSearch_id()).isEqualTo(DEFAULT_PICTURE_SEARCH_ID);
        assertThat(testPictureFound.getAutocolorCorrelogramScore()).isEqualTo(DEFAULT_AUTOCOLOR_CORRELOGRAM_SCORE);
        assertThat(testPictureFound.getCeddScore()).isEqualTo(DEFAULT_CEDD_SCORE);
        assertThat(testPictureFound.getColorHistogramScore()).isEqualTo(DEFAULT_COLOR_HISTOGRAM_SCORE);
        assertThat(testPictureFound.getColorLayoutScore()).isEqualTo(DEFAULT_COLOR_LAYOUT_SCORE);
        assertThat(testPictureFound.getEdgeHistogramScore()).isEqualTo(DEFAULT_EDGE_HISTOGRAM_SCORE);
        assertThat(testPictureFound.getPhogScore()).isEqualTo(DEFAULT_PHOG_SCORE);
        assertThat(testPictureFound.getCreated()).isEqualTo(DEFAULT_CREATED);
    }

    @Test
    public void getAllPictureFounds() throws Exception {
        // Initialize the database
        pictureFoundRepository.save(pictureFound);

        // Get all the pictureFounds
        restPictureFoundMockMvc.perform(get("/api/pictureFounds"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].picture_id").value(hasItem(DEFAULT_PICTURE_ID.toString())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER.toString())))
                .andExpect(jsonPath("$.[*].pictureSearch_id").value(hasItem(DEFAULT_PICTURE_SEARCH_ID.toString())))
                .andExpect(jsonPath("$.[*].autocolorCorrelogramScore").value(hasItem(DEFAULT_AUTOCOLOR_CORRELOGRAM_SCORE.doubleValue())))
                .andExpect(jsonPath("$.[*].ceddScore").value(hasItem(DEFAULT_CEDD_SCORE.doubleValue())))
                .andExpect(jsonPath("$.[*].colorHistogramScore").value(hasItem(DEFAULT_COLOR_HISTOGRAM_SCORE.doubleValue())))
                .andExpect(jsonPath("$.[*].colorLayoutScore").value(hasItem(DEFAULT_COLOR_LAYOUT_SCORE.doubleValue())))
                .andExpect(jsonPath("$.[*].edgeHistogramScore").value(hasItem(DEFAULT_EDGE_HISTOGRAM_SCORE.doubleValue())))
                .andExpect(jsonPath("$.[*].phogScore").value(hasItem(DEFAULT_PHOG_SCORE.doubleValue())))
                .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())));
    }

    @Test
    public void getPictureFound() throws Exception {
        // Initialize the database
        pictureFoundRepository.save(pictureFound);

        // Get the pictureFound
        restPictureFoundMockMvc.perform(get("/api/pictureFounds/{pictureSearch_id}", pictureFound.getPictureSearch_id()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.picture_id").value(DEFAULT_PICTURE_ID.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER.toString()))
            .andExpect(jsonPath("$.pictureSearch_id").value(DEFAULT_PICTURE_SEARCH_ID.toString()))
            .andExpect(jsonPath("$.autocolorCorrelogramScore").value(DEFAULT_AUTOCOLOR_CORRELOGRAM_SCORE.doubleValue()))
            .andExpect(jsonPath("$.ceddScore").value(DEFAULT_CEDD_SCORE.doubleValue()))
            .andExpect(jsonPath("$.colorHistogramScore").value(DEFAULT_COLOR_HISTOGRAM_SCORE.doubleValue()))
            .andExpect(jsonPath("$.colorLayoutScore").value(DEFAULT_COLOR_LAYOUT_SCORE.doubleValue()))
            .andExpect(jsonPath("$.edgeHistogramScore").value(DEFAULT_EDGE_HISTOGRAM_SCORE.doubleValue()))
            .andExpect(jsonPath("$.phogScore").value(DEFAULT_PHOG_SCORE.doubleValue()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()));
    }

    @Test
    public void getNonExistingPictureFound() throws Exception {
        // Get the pictureFound
        restPictureFoundMockMvc.perform(get("/api/pictureFounds/{id}", UUID.randomUUID().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updatePictureFound() throws Exception {
        // Initialize the database
        pictureFoundRepository.save(pictureFound);

		int databaseSizeBeforeUpdate = pictureFoundRepository.findAll().size();

        // Update the pictureFound
        pictureFound.setPicture_id(UPDATED_PICTURE_ID);
        pictureFound.setTitle(UPDATED_TITLE);
        pictureFound.setOwner(UPDATED_OWNER);

        pictureFound.setPictureSearch_id(UPDATED_PICTURE_SEARCH_ID);
        pictureFound.setAutocolorCorrelogramScore(UPDATED_AUTOCOLOR_CORRELOGRAM_SCORE);
        pictureFound.setCeddScore(UPDATED_CEDD_SCORE);
        pictureFound.setColorHistogramScore(UPDATED_COLOR_HISTOGRAM_SCORE);
        pictureFound.setColorLayoutScore(UPDATED_COLOR_LAYOUT_SCORE);
        pictureFound.setEdgeHistogramScore(UPDATED_EDGE_HISTOGRAM_SCORE);
        pictureFound.setPhogScore(UPDATED_PHOG_SCORE);
        pictureFound.setCreated(UPDATED_CREATED);

        PictureFoundDTO pictureFoundDTO = pictureFoundMapper.pictureFoundToPictureFoundDTO(pictureFound);

        restPictureFoundMockMvc.perform(put("/api/pictureFounds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pictureFoundDTO)))
                .andExpect(status().isOk());

        // Validate the PictureFound in the database
        List<PictureFound> pictureFounds = pictureFoundRepository.findAll();
        assertThat(pictureFounds).hasSize(databaseSizeBeforeUpdate);
        PictureFound testPictureFound = pictureFounds.get(pictureFounds.size() - 1);
        assertThat(testPictureFound.getPicture_id()).isEqualTo(UPDATED_PICTURE_ID);
        assertThat(testPictureFound.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPictureFound.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testPictureFound.getPictureSearch_id()).isEqualTo(UPDATED_PICTURE_SEARCH_ID);
        assertThat(testPictureFound.getAutocolorCorrelogramScore()).isEqualTo(UPDATED_AUTOCOLOR_CORRELOGRAM_SCORE);
        assertThat(testPictureFound.getCeddScore()).isEqualTo(UPDATED_CEDD_SCORE);
        assertThat(testPictureFound.getColorHistogramScore()).isEqualTo(UPDATED_COLOR_HISTOGRAM_SCORE);
        assertThat(testPictureFound.getColorLayoutScore()).isEqualTo(UPDATED_COLOR_LAYOUT_SCORE);
        assertThat(testPictureFound.getEdgeHistogramScore()).isEqualTo(UPDATED_EDGE_HISTOGRAM_SCORE);
        assertThat(testPictureFound.getPhogScore()).isEqualTo(UPDATED_PHOG_SCORE);
        assertThat(testPictureFound.getCreated()).isEqualTo(UPDATED_CREATED);
    }

    @Test
    public void deletePictureFound() throws Exception {
        // Initialize the database
        pictureFoundRepository.save(pictureFound);

		int databaseSizeBeforeDelete = pictureFoundRepository.findAll().size();

        // Get the pictureFound
        restPictureFoundMockMvc.perform(delete("/api/pictureFounds/{pictureSearch_id}", pictureFound.getPictureSearch_id())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PictureFound> pictureFounds = pictureFoundRepository.findAll();
        assertThat(pictureFounds).hasSize(databaseSizeBeforeDelete - 1);
    }
}

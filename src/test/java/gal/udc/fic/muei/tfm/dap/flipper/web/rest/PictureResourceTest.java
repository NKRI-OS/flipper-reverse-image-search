package gal.udc.fic.muei.tfm.dap.flipper.web.rest;

import gal.udc.fic.muei.tfm.dap.flipper.AbstractCassandraTest;
import gal.udc.fic.muei.tfm.dap.flipper.Application;
import gal.udc.fic.muei.tfm.dap.flipper.domain.Picture;
import gal.udc.fic.muei.tfm.dap.flipper.repository.PictureRepository;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.dto.PictureDTO;
import gal.udc.fic.muei.tfm.dap.flipper.web.rest.mapper.PictureMapper;
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
 * Test class for the PictureResource REST controller.
 *
 * @see PictureResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PictureResourceTest extends AbstractCassandraTest {

    private static final String DEFAULT_TITLE = "SAMPLE_TEXT";
    private static final String UPDATED_TITLE = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";
    private static final byte[] DEFAULT_PICTURE_FILE = "SAMPLE_TEXT".getBytes();
    private static final byte[] UPDATED_PICTURE_FILE = "UPDATED_TEXT".getBytes();

    private static final Integer DEFAULT_FAVOURITES = 1;
    private static final Integer UPDATED_FAVOURITES = 2;

    private static final Integer DEFAULT_LIKES = 1;
    private static final Integer UPDATED_LIKES = 2;

    private static final Date DEFAULT_CREATED = new Date();
    private static final Date UPDATED_CREATED = new Date();

    private static final Date DEFAULT_MODIFIED = new Date();
    private static final Date UPDATED_MODIFIED = new Date();

    @Inject
    private PictureRepository pictureRepository;

    @Inject
    private PictureMapper pictureMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restPictureMockMvc;

    private Picture picture;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PictureResource pictureResource = new PictureResource();
        ReflectionTestUtils.setField(pictureResource, "pictureRepository", pictureRepository);
        ReflectionTestUtils.setField(pictureResource, "pictureMapper", pictureMapper);
        this.restPictureMockMvc = MockMvcBuilders.standaloneSetup(pictureResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        pictureRepository.deleteAll();
        picture = new Picture();
        picture.setTitle(DEFAULT_TITLE);
        picture.setDescription(DEFAULT_DESCRIPTION);
        picture.setPictureFile(ByteBuffer.wrap(DEFAULT_PICTURE_FILE));

        picture.setCreated(DEFAULT_CREATED);
        picture.setModified(DEFAULT_MODIFIED);
    }

    @Test
    public void createPicture() throws Exception {
        int databaseSizeBeforeCreate = pictureRepository.findAll().size();

        // Create the Picture
        PictureDTO pictureDTO = pictureMapper.pictureToPictureDTO(picture);

        restPictureMockMvc.perform(post("/api/pictures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pictureDTO)))
                .andExpect(status().isCreated());

        // Validate the Picture in the database
        List<Picture> pictures = pictureRepository.findAll();
        assertThat(pictures).hasSize(databaseSizeBeforeCreate + 1);
        Picture testPicture = pictures.get(pictures.size() - 1);
        assertThat(testPicture.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPicture.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPicture.getPictureFile()).isEqualTo(DEFAULT_PICTURE_FILE);

        assertThat(testPicture.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testPicture.getModified()).isEqualTo(DEFAULT_MODIFIED);
    }

    @Test
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = pictureRepository.findAll().size();
        // set the field null
        picture.setTitle(null);

        // Create the Picture, which fails.
        PictureDTO pictureDTO = pictureMapper.pictureToPictureDTO(picture);

        restPictureMockMvc.perform(post("/api/pictures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pictureDTO)))
                .andExpect(status().isBadRequest());

        List<Picture> pictures = pictureRepository.findAll();
        assertThat(pictures).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllPictures() throws Exception {
        // Initialize the database
        pictureRepository.save(picture);

        // Get all the pictures
        restPictureMockMvc.perform(get("/api/pictures"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(picture.getId().toString())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].pictureFile").value(hasItem(DEFAULT_PICTURE_FILE.toString())))
                .andExpect(jsonPath("$.[*].favourites").value(hasItem(DEFAULT_FAVOURITES)))
                .andExpect(jsonPath("$.[*].likes").value(hasItem(DEFAULT_LIKES)))
                .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.getTime())))
                .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.getTime())));
    }

    @Test
    public void getPicture() throws Exception {
        // Initialize the database
        pictureRepository.save(picture);

        // Get the picture
        restPictureMockMvc.perform(get("/api/pictures/{id}", picture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(picture.getId().toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.pictureFile").value(DEFAULT_PICTURE_FILE.toString()))
            .andExpect(jsonPath("$.favourites").value(DEFAULT_FAVOURITES))
            .andExpect(jsonPath("$.likes").value(DEFAULT_LIKES))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.getTime()))
            .andExpect(jsonPath("$.modified").value(DEFAULT_MODIFIED.getTime()));
    }

    @Test
    public void getNonExistingPicture() throws Exception {
        // Get the picture
        restPictureMockMvc.perform(get("/api/pictures/{id}", UUID.randomUUID().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updatePicture() throws Exception {
        // Initialize the database
        pictureRepository.save(picture);

		int databaseSizeBeforeUpdate = pictureRepository.findAll().size();

        // Update the picture
        picture.setTitle(UPDATED_TITLE);
        picture.setDescription(UPDATED_DESCRIPTION);
        picture.setPictureFile(ByteBuffer.wrap(UPDATED_PICTURE_FILE));

        picture.setCreated(UPDATED_CREATED);
        picture.setModified(UPDATED_MODIFIED);

        PictureDTO pictureDTO = pictureMapper.pictureToPictureDTO(picture);

        restPictureMockMvc.perform(put("/api/pictures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pictureDTO)))
                .andExpect(status().isOk());

        // Validate the Picture in the database
        List<Picture> pictures = pictureRepository.findAll();
        assertThat(pictures).hasSize(databaseSizeBeforeUpdate);
        Picture testPicture = pictures.get(pictures.size() - 1);
        assertThat(testPicture.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPicture.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPicture.getPictureFile()).isEqualTo(UPDATED_PICTURE_FILE);

        assertThat(testPicture.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testPicture.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    public void deletePicture() throws Exception {
        // Initialize the database
        pictureRepository.save(picture);

		int databaseSizeBeforeDelete = pictureRepository.findAll().size();

        // Get the picture
        restPictureMockMvc.perform(delete("/api/pictures/{id}", picture.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Picture> pictures = pictureRepository.findAll();
        assertThat(pictures).hasSize(databaseSizeBeforeDelete - 1);
    }
}

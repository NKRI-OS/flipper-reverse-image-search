package gal.udc.fic.muei.tfm.dap.flipper.repository;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import gal.udc.fic.muei.tfm.dap.flipper.domain.Picture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Cassandra repository for the Picture entity.
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
@Repository
public class PictureRepository {

    private static final int MAX_RESULTS = 10000;

    private final Logger log = LoggerFactory.getLogger(PictureRepository.class);

    @Inject
    private Session session;

    private Mapper<Picture> mapper;

    private PictureAccessor pictureAccesor;
    private GeneralCounterAccessor generalCounterAccessor;
    private UserCounterAccessor userCounterAccessor;

    private PreparedStatement findByIds;

    private PreparedStatement truncateStmt;

    @PostConstruct
    public void init() {
        MappingManager manager = new MappingManager (session);
        pictureAccesor = manager.createAccessor(PictureAccessor.class);
        generalCounterAccessor = manager.createAccessor(GeneralCounterAccessor.class);
        userCounterAccessor = manager.createAccessor(UserCounterAccessor.class);
        mapper = manager.mapper(Picture.class);
        findByIds = session.prepare("SELECT * from picture WHERE id IN ?;");
        truncateStmt = session.prepare("TRUNCATE picture");
    }

    /**
     * Find pictures by owner
     * @param owner
     * @return
     */
    public List<Picture> findByOwner(String owner){
        return pictureAccesor.findByOwner(owner).all();
    }

    /**
     * Using solr_query with start and count
     * Needs disable paging
     * @param pageable
     * @return
     */
    public Page<Picture> findByOwnerOrdered(String owner, UUID startId, Pageable pageable) {

        List<Picture> pictures = pictureAccesor.findByOwnerOrdered(owner, startId, pageable.getPageSize()).all();

        // Get total from owner
        long total = 0;
        if(userCounterAccessor.getPictureCounter(owner) != null)
        {
            total = userCounterAccessor.getPictureCounter(owner).one().getLong("picture_counter");
        }


        return new PageImpl<>(pictures, pageable, total);
    }

    /**
     * Find all pictures
     * @return
     */
    public List<Picture> findAll() {
        return pictureAccesor.findAll().all();
    }

    /**
     * Using solr_query with start and count
     * Needs disable paging
     * @param pageable
     * @return
     */
    public Page<Picture> findAllOrdered(UUID startId, Pageable pageable) {

        List<Picture> pictures = pictureAccesor.findAllOrdered(startId, pageable.getPageSize()).all();

        long total = generalCounterAccessor.getPictureCounter(Calendar.getInstance().get(Calendar.YEAR)).one().getLong("picture_counter");

        return new PageImpl<>(pictures, pageable, total);
    }

    /**
     * Returns a list of pictures from a given
     * from a retreived id picture list
     * @param idsList
     * @return
     */
    public List<Picture> findByIdsList(List<UUID> idsList) {

        List<Picture> pictures = new ArrayList<>(idsList.size());
        List<String> idsAsString = new ArrayList<>(idsList.size());
        idsAsString.addAll(idsList.stream().map(UUID::toString).collect(Collectors.toList()));

        session.execute(findByIds.bind(idsList)).all().stream().map(
            row -> {
                Picture picture = new Picture();
                picture.setId(row.getUUID("id"));
                picture.setTitle(row.getString("title"));
                picture.setDescription(row.getString("description"));
                picture.setOwner(row.getString("owner"));
                picture.setModifiedBy(row.getString("modifiedBy"));
                picture.setPictureFile(row.getBytes("pictureFile"));
                picture.setLittlePictureFile(row.getBytes("littlePictureFile"));
                picture.setMediumPictureFile(row.getBytes("mediumPictureFile"));
                picture.setBigPictureFile(row.getBytes("bigPictureFile"));
                picture.setCreated(row.getDate("created"));
                picture.setModified(row.getDate("modified"));
                picture.setAutocolorCorrelogramAsBase64(row.getString("autocolorCorrelogramAsBase64"));
                picture.setAutocolorCorrelogram(row.getList("autocolorCorrelogram", Double.class));
                picture.setCeddAsBase64(row.getString("ceddAsBase64"));
                picture.setCedd(row.getList("cedd", Double.class));
                picture.setColorHistogramAsBase64(row.getString("colorHistogramAsBase64"));
                picture.setColorHistogram(row.getList("colorHistogram", Double.class));
                picture.setColorLayoutAsBase64(row.getString("colorLayoutAsBase64"));
                picture.setColorLayout(row.getList("colorLayout", Double.class));
                picture.setEdgeHistogramAsBase64(row.getString("edgeHistogramAsBase64"));
                picture.setEdgeHistogram(row.getList("edgeHistogram", Double.class));
                picture.setPhogAsBase64(row.getString("phogAsBase64"));
                picture.setPhog(row.getList("phog", Double.class));
                return picture;
            }
        ).forEach(pictures::add);

        return pictures;

    }

    /**
     * Find one picture
     * @param id
     * @return
     */
    public Picture findOne(UUID id) {
        return mapper.get(id);
    }

    /**
     * Searchs a picture by a given query
     * in solr_query
     * @param query
     * @return
     */
    public List<Picture> search(String query)
    {
        return pictureAccesor.search(query).all();
    }

    /**
     * Create or update a picture
     * @param picture
     * @return
     */
    public Picture save(Picture picture) {
        if (picture.getId() == null) {
            picture.setId(UUID.randomUUID());
        }

        mapper.save(picture);
        return picture;
    }

    /**
     * Delete a picture from a given object picture
     * @param picture
     */
    public void delete(Picture picture) {
        mapper.delete(picture);
    }

    /**
     * Delete a picture from a given id
     * @param id
     */
    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt =  truncateStmt.bind();
        session.execute(stmt);
    }
}

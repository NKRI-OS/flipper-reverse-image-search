package gal.udc.fic.muei.tfm.dap.flipper.repository;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import gal.udc.fic.muei.tfm.dap.flipper.domain.Metadata;
import gal.udc.fic.muei.tfm.dap.flipper.domain.Picture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Cassandra repository for the Metadata entity.
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
public class MetadataRepository {

    private static final int LIMIT_PER_WRITE = 100;
    private static final int MAX_RESULTS = 10000;

    @Inject
    private Session session;

    private MetadataAccessor metadataAccessor;

    private  GeneralCounterAccessor generalCounterAccessor;

    private Mapper<Metadata> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    @PostConstruct
    public void init() {
        MappingManager manager = new MappingManager (session);
        metadataAccessor = manager.createAccessor(MetadataAccessor.class);
        generalCounterAccessor = manager.createAccessor(GeneralCounterAccessor.class);
        mapper = manager.mapper(Metadata.class);
        findAllStmt = session.prepare("SELECT * FROM metadata");
        truncateStmt = session.prepare("TRUNCATE metadata");
    }

    /**
     * Find all metadatas
     * @return
     */
    public List<Metadata> findAll() {
        return metadataAccessor.findAll().all();
    }

    /**
     * Using solr_query with start and count
     * Needs disable paging
     * @param pageable
     * @return
     */
    public Page<Metadata> findAllOrdered(UUID startId, Pageable pageable) {

        // Adds a position
        List<Metadata> metadatas = metadataAccessor.findAllOrdered(startId, pageable.getPageSize()).all();
        long total = generalCounterAccessor.getMetadataCounter(Calendar.getInstance().get(Calendar.YEAR)).one().getLong("metadata_counter");

        return new PageImpl<>(metadatas, pageable, total);
    }

    /**
     * Find all metadatas by picture
     * @param picture_id
     * @return
     */
    public List<Metadata> findByPicture_id(UUID picture_id) {
        return metadataAccessor.findByPicture_id(picture_id).all();
    }

    /**
     * Find one metadata
     * @param id
     * @return
     */
    public Metadata findOne(UUID id) {
        return metadataAccessor.findOne(id);
    }

    /**
     * Create or update one metadata
     * @param metadata
     * @return
     */
    public Metadata save(Metadata metadata) {
        if (metadata.getId() == null) {
            metadata.setId(UUID.randomUUID());
        }
        mapper.save(metadata);
        return metadata;
    }

    /**
     * Insert all metadata as a batch statement
     * @param metadataSet
     * @param picture
     */
    public void saveAll(Set<Metadata> metadataSet, Picture picture) {

        UUID picture_id = picture.getId();
        String title = picture.getTitle();
        ByteBuffer pictureFile = picture.getLittlePictureFile();

        PreparedStatement ps = session.prepare("INSERT INTO metadata (" +
            "id," +
            "directoryName," +
            "tagType," +
            "tagName," +
            "description," +
            "picture_id," +
            "title," +
            "pictureFile" +
            ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

        BatchStatement batch = new BatchStatement();
        for(Metadata metadata : metadataSet)
        {
            batch.add(ps.bind(UUID.randomUUID(),
                metadata.getDirectoryName(), metadata.getTagType(), metadata.getTagName(), metadata.getDescription(),
                picture_id, title, pictureFile));
        }

        /* execute batch multiple insert */
        session.execute(batch);
    }


    public void updateAllFromPicture(Picture picture) {

        UUID picture_id = picture.getId();
        String title = picture.getTitle();

        List<Metadata> updates = metadataAccessor.findByPicture_id(picture_id).all();
        List<UUID> uuidList = updates.stream().map(Metadata::getId).collect(Collectors.toList());

        for(UUID id: uuidList) {
            metadataAccessor.updateByPicture_id(id, picture_id, title);
        }
    }

    public List<Metadata> search(String query) {
        return metadataAccessor.search(query).all();
    }

    public void deleteFromPicture(UUID picture_id) {
        metadataAccessor.deleteByPicture_id(picture_id);
    }

    public void delete(Metadata metadata) {
        mapper.delete(metadata);
    }

    public void deleteAll() {
        BoundStatement stmt =  truncateStmt.bind();
        session.execute(stmt);
    }


}

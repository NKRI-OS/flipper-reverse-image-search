package gal.udc.fic.muei.tfm.dap.flipper.repository;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import gal.udc.fic.muei.tfm.dap.flipper.domain.PictureFound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Cassandra repository for the PictureFound entity.
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
public class PictureFoundRepository {

    private static final int MAX_RESULTS = 10000;
    @Inject
    private Session session;

    private Mapper<PictureFound> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement findAllByPictureStmt;

    private PreparedStatement truncateStmt;

    private PictureFoundAccessor pictureFoundAccessor;

    private PictureSearchCounterAccessor pictureSearchCounterAccessor;

    @PostConstruct
    public void init() {
        MappingManager manager = new MappingManager (session);
        pictureFoundAccessor = manager.createAccessor(PictureFoundAccessor.class);
        pictureSearchCounterAccessor = manager.createAccessor(PictureSearchCounterAccessor.class);
        mapper = manager.mapper(PictureFound.class);
        findAllStmt = session.prepare("SELECT * FROM pictureFound");
        findAllByPictureStmt = session.prepare("SELECT * FROM pictureFound WHERE picture_id = ?");
        truncateStmt = session.prepare("TRUNCATE pictureFound");
    }

    public List<PictureFound> findAll() {
        return pictureFoundAccessor.findAll().all();
    }

    public PictureFound findOne(UUID pictureSearch_id, UUID picture_id) {
        return pictureFoundAccessor.findOne(pictureSearch_id, picture_id);
    }

    /**
     * Find pictures found from a given picture id
     * @param picture_id
     * @return
     */
    public List<PictureFound> findByPicture(UUID picture_id) {

        //return pictureFoundAccessor.findByPicture(picture_id).all();
        List<PictureFound> result = new ArrayList<>();
        session.execute(findAllByPictureStmt.bind(picture_id)).all().stream().map(
            row -> {
                PictureFound pictureFound = new PictureFound();
                pictureFound.setPictureSearch_id(row.getUUID("pictureSearch_id"));
                pictureFound.setPicture_id(row.getUUID("picture_id"));
                pictureFound.setTitle(row.getString("title"));
                return pictureFound;
            }
        ).forEach(result::add);

        return  result;
    }

    /**
     * Find all picture found from a given picture search id
     * @param pictureSearch_id
     * @return
     */
    public List<PictureFound> findByPictureSearch_id(UUID pictureSearch_id) {

        return pictureFoundAccessor.findByPictureSearch(pictureSearch_id).all();
    }

    public PictureFound save(PictureFound pictureFound) {
        mapper.save(pictureFound);
        return pictureFound;
    }

    public void saveAll(List<PictureFound> picturesFoundList) {
        /*
        for(PictureFound pf : picturesFoundList){
            mapper.save(pf);
        }
        */
        PreparedStatement ps = session.prepare("INSERT INTO picturefound (" +
            "pictureSearch_id," +
            "picture_id," +
            "title," +
            "owner," +
            "created," +
            "littlePictureFile," +
            "mediumPictureFile," +
            "bigPictureFile," +
            "totalScore," +
            "autocolorCorrelogramScore," +
            "ceddScore," +
            "colorHistogramScore," +
            "colorLayoutScore," +
            "edgeHistogramScore," +
            "phogScore" +
            ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        BatchStatement batch = new BatchStatement();
        for(PictureFound p : picturesFoundList)
        {
            batch.add(ps.bind(
                p.getPictureSearch_id(),
                p.getPicture_id(),
                p.getTitle(),
                p.getOwner(),
                p.getCreated(),
                p.getLittlePictureFile(),
                p.getMediumPictureFile(),
                p.getBigPictureFile(),
                p.getTotalScore(),
                p.getAutocolorCorrelogramScore(),
                p.getCeddScore(),
                p.getColorHistogramScore(),
                p.getColorLayoutScore(),
                p.getEdgeHistogramScore(),
                p.getPhogScore()
            ));
        }

        /* execute batch multiple insert */
        session.execute(batch);
    }

    public void delete(PictureFound pictureFound) {
        mapper.delete(pictureFound);
    }

    public void deleteByPictureSearch(UUID pictureSearch_id) {
        pictureFoundAccessor.deleteByPictureSearch(pictureSearch_id);
    }

    public void deleteAll() {
        BoundStatement stmt =  truncateStmt.bind();
        session.execute(stmt);
    }

}

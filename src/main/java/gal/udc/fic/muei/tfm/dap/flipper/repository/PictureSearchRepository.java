package gal.udc.fic.muei.tfm.dap.flipper.repository;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import gal.udc.fic.muei.tfm.dap.flipper.domain.PictureSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * Cassandra repository for the PictureSearch entity.
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
public class PictureSearchRepository {

    private static final int MAX_RESULTS = 10000;

    @Inject
    private Session session;

    private Mapper<PictureSearch> mapper;

    private PreparedStatement findAllStmt;

    private PictureSearchAccesor pictureSearchAccesor;

    private GeneralCounterAccessor generalCounterAccessor;

    private PictureSearchCounterAccessor pictureSearchCounterAccessor;

    private PreparedStatement truncateStmt;

    @PostConstruct
    public void init() {
        MappingManager manager = new MappingManager (session);
        pictureSearchAccesor = manager.createAccessor(PictureSearchAccesor.class);
        generalCounterAccessor = manager.createAccessor(GeneralCounterAccessor.class);
        pictureSearchCounterAccessor = manager.createAccessor(PictureSearchCounterAccessor.class);
        mapper = manager.mapper(PictureSearch.class);
        findAllStmt = session.prepare("SELECT * FROM pictureSearch");
        truncateStmt = session.prepare("TRUNCATE pictureSearch");
    }

    public long getTotalPicturesFound(UUID pictureSearchId){
        return pictureSearchCounterAccessor.getPictureCounter(pictureSearchId).one().getLong("picturefound_counter");
    }

    /**
     * Find all searchs
     * @return
     */
    public List<PictureSearch> findAll() {
        return pictureSearchAccesor.findAll().all();
    }

    /**
     * Find all search with page by page iteration
     * @param pageable
     * @return
     */
    public Page<PictureSearch> findAllOrdered(Pageable pageable) {

        String query = String.format("{\"q\":\"*:*\", \"start\": %d, \"sort\":\"created DESC\"}", pageable.getOffset());
        List<PictureSearch> picturesSearch = pictureSearchAccesor.findAllOrdered(query, pageable.getPageSize()).all();

        long total = generalCounterAccessor.getPictureSearchCounter(Calendar.getInstance().get(Calendar.YEAR)).one().getLong("picturesearch_counter");

        return new PageImpl<>(picturesSearch, pageable, total);
    }

    public PictureSearch findOne(UUID id) {
        return mapper.get(id);
    }

    public PictureSearch save(PictureSearch pictureSearch) {
        if (pictureSearch.getId() == null) {
            pictureSearch.setId(UUID.randomUUID());
        }
        mapper.save(pictureSearch);
        return pictureSearch;
    }

    public void saveAll(List<PictureSearch> pictureSearches){

        PreparedStatement ps = session.prepare("INSERT INTO picturesearch (" +
            "id," +
            "created," +
            "picturefile," +
            "littlePictureFile," +
            "pictureidlist," +
            "userlogin" +

            "autocolorCorrelogramAsBase64," +
            "autocolorCorrelogram," +
            "ceddAsBase64," +
            "cedd," +
            "colorHistogramAsBase64," +
            "colorHistogram," +
            "colorLayoutAsBase64," +
            "colorLayout," +
            "edgeHistogramAsBase64," +
            "edgeHistogram," +
            "phogAsBase64," +
            "phog" +
            ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        BatchStatement batch = new BatchStatement();
        for(PictureSearch p : pictureSearches)
        {
            batch.add(ps.bind(UUID.randomUUID(), p.getCreated(),
                p.getPictureFile(), p.getLittlePictureFile(),
                p.getPictureIdList(), p.getUserLogin(),
                p.getAutocolorCorrelogramAsBase64(), p.getAutocolorCorrelogram(),
                p.getCeddAsBase64(), p.getCedd(),
                p.getColorHistogramAsBase64(), p.getColorHistogram(),
                p.getColorLayoutAsBase64(), p.getColorLayout(),
                p.getEdgeHistogramAsBase64(), p.getEdgeHistogram(),
                p.getPhogAsBase64(), p.getPhog()
                ));
            //batch.add(ps.bind(UUID.randomUUID(), p.getPictureFile(), p.getUserLogin()));
        }

        /* execute batch multiple insert */
        session.execute(batch);

    }

    public void delete(PictureSearch pictureSearch) {
        mapper.delete(pictureSearch);
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt =  truncateStmt.bind();
        session.execute(stmt);
    }

}

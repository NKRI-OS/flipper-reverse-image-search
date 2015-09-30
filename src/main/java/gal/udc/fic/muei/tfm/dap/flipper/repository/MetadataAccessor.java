package gal.udc.fic.muei.tfm.dap.flipper.repository;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import gal.udc.fic.muei.tfm.dap.flipper.domain.Metadata;

import java.util.UUID;

/**
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
@Accessor
public interface MetadataAccessor {

    @Query("SELECT * FROM metadata WHERE id = :id")
    Metadata findOne(@Param("id") UUID id);

    @Query("SELECT * FROM metadata WHERE solr_query = :query LIMIT :total")
    Result<Metadata> findAllOrdered(@Param("query") String query, @Param("total") int total);

    @Query("SELECT * FROM metadata")
    Result<Metadata> findAll();

    @Query("SELECT * FROM metadata WHERE picture_id = :picture_id LIMIT 100")
    Result<Metadata> findByPicture_id(@Param("picture_id") UUID picture_id);

    @Query("UPDATE metadata SET title = :title WHERE id = :id AND picture_id = :picture_id")
    void updateByPicture_id(@Param("id") UUID id, @Param("picture_id") UUID picture_id, @Param("title") String title);

    @Query("DELETE FROM metadata WHERE picture_id = :picture_id")
    void deleteByPicture_id(@Param("picture_id") UUID picture_id);

    @Query("SELECT * FROM metadata WHERE solr_query = :query LIMIT 100")
    Result<Metadata> search(@Param("query") String query);

    @Query("SELECT count(*) FROM metadata LIMIT 10000")
    Result<Metadata> count();
}

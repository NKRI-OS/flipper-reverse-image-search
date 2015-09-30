package gal.udc.fic.muei.tfm.dap.flipper.repository;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import gal.udc.fic.muei.tfm.dap.flipper.domain.Picture;

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
public interface PictureAccessor {

    @Query("SELECT * FROM picture WHERE id = :id")
    Picture findOne(@Param("id") UUID id);

    @Query("SELECT * FROM picture " +
        "WHERE solr_query = :query LIMIT :total")
    Result<Picture> findAllOrdered(@Param("query") String query, @Param("total") int total);

    @Query("SELECT * FROM picture LIMIT 100")
    Result<Picture> findAll();

    @Query("SELECT * FROM picture WHERE owner = :owner LIMIT 100")
    Result<Picture> findByOwner(@Param("owner") String owner);

    @Query("SELECT count(*) FROM picture WHERE owner = :owner LIMIT 10000")
    Result<Picture> countByOwner(@Param("owner") String owner);

    @Query("SELECT * FROM picture WHERE solr_query = :query LIMIT 100")
    Result<Picture> search(@Param("query") String query);

    @Query("SELECT count(*) FROM picture LIMIT 10000")
    Result<Picture> count();

}

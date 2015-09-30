package gal.udc.fic.muei.tfm.dap.flipper.repository;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import gal.udc.fic.muei.tfm.dap.flipper.domain.PictureFound;

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
public interface PictureFoundAccessor {

    @Query("SELECT * FROM pictureFound WHERE pictureSearch_id = :pictureSearch_id AND picture_id = :picture_id")
    PictureFound findOne(@Param("pictureSearch_id") UUID pictureSearch_id, @Param("picture_id") UUID picture_id);

    @Query("SELECT * FROM pictureFound LIMIT 100")
    Result<PictureFound> findAll();

    @Query("SELECT * FROM pictureFound WHERE solr_query = :query LIMIT :total")
    Result<PictureFound> findByPictureSearchOrdered(String query, int total);

    @Query("SELECT * FROM pictureFound WHERE pictureSearch_id = :pictureSearch_id LIMIT 100")
    Result<PictureFound> findByPictureSearch(@Param("pictureSearch_id") UUID pictureSearch_id);

    @Query("SELECT * FROM pictureFound WHERE solr_query = :query LIMIT 100")
    Result<PictureFound> search(@Param("query") String query);

    @Query("DELETE FROM pictureFound WHERE pictureSearch_id = :pictureSearch_id")
    void deleteByPictureSearch(@Param("pictureSearch_id") UUID pictureSearch_id);
}

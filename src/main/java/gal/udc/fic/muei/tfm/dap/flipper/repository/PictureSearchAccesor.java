package gal.udc.fic.muei.tfm.dap.flipper.repository;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import gal.udc.fic.muei.tfm.dap.flipper.domain.Picture;
import gal.udc.fic.muei.tfm.dap.flipper.domain.PictureSearch;

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
public interface PictureSearchAccesor {

    @Query("SELECT * FROM pictureSearch WHERE id = :id")
    PictureSearch findOne(@Param("id") UUID id);

    @Query("SELECT * FROM pictureSearch WHERE solr_query = :query LIMIT :total")
    Result<PictureSearch> findAllOrdered(@Param("query") String query, @Param("total") int total);


    @Query("SELECT * FROM pictureSearch LIMIT 100")
    Result<PictureSearch> findAll();
}

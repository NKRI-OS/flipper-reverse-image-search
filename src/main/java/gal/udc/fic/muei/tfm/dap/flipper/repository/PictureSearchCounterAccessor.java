package gal.udc.fic.muei.tfm.dap.flipper.repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;

import java.util.UUID;

/**
 * Accessor for general counter table
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
@Accessor
public interface PictureSearchCounterAccessor {

    @Query("SELECT picturefound_counter FROM picturesearch_counter WHERE pictureSearch_id = :pictureSearchId")
    ResultSet getPictureCounter(@Param("pictureSearchId") UUID pictureSearchId);

    @Query("UPDATE picturesearch_counter SET picturefound_counter=picturefound_counter + :count WHERE pictureSearch_id = :pictureSearchId")
    void incrementPictureSearch(@Param("count") long count, @Param("pictureSearchId") UUID pictureSearchId);

    @Query("UPDATE picturesearch_counter SET picturefound_counter=picturefound_counter - :count WHERE pictureSearch_id = :pictureSearchId")
    void decrementPictureSearch(@Param("count") long count, @Param("pictureSearchId") UUID pictureSearchId);

}

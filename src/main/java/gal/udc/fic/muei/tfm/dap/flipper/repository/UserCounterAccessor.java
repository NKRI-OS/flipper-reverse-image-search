package gal.udc.fic.muei.tfm.dap.flipper.repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;

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
public interface UserCounterAccessor {

    @Query("SELECT picture_counter FROM user_counter WHERE user = :user")
    ResultSet getPictureCounter(@Param("user") String user);

    @Query("SELECT metadata_counter FROM user_counter WHERE user = :user")
    ResultSet getMetadataCounter(@Param("user") String user);

    @Query("SELECT picturesearch_counter FROM user_counter WHERE user = :user")
    ResultSet getPictureSearchCounter(@Param("user") String user);

    @Query("UPDATE user_counter SET picture_counter=picture_counter+1 WHERE user = :user")
    void incrementPicture(@Param("user") String user);

    @Query("UPDATE user_counter SET metadata_counter=metadata_counter + :count WHERE user = :user")
    void incrementMetadata(@Param("count") long count, @Param("user") String user);

    @Query("UPDATE user_counter SET picturesearch_counter=picturesearch_counter+1 WHERE user = :user")
    void incrementPictureSearch(@Param("user") String user);

    @Query("UPDATE user_counter SET picture_counter=picture_counter-1 WHERE user = :user")
    void decrementPicture(@Param("user") String user);

    @Query("UPDATE user_counter SET metadata_counter=metadata_counter - :count WHERE user = :user")
    void decrementMetadata(@Param("count") long count, @Param("user") String user);

    @Query("UPDATE user_counter SET picturesearch_counter=picturesearch_counter-1 WHERE user = :user")
    void decrementPictureSearch(@Param("user") String user);
}

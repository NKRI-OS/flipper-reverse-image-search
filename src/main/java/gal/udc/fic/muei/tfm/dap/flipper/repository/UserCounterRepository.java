package gal.udc.fic.muei.tfm.dap.flipper.repository;

import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.MappingManager;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Cassandra repository for the user counter data entity.
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
public class UserCounterRepository {

    @Inject
    private Session session;

    private UserCounterAccessor userCounterAccessor;

    @PostConstruct
    public void init() {
        MappingManager manager = new MappingManager (session);
        userCounterAccessor = manager.createAccessor(UserCounterAccessor.class);
    }

    public void incrementPicture(String user){
        userCounterAccessor.incrementPicture(user);
    }

    public void incrementPictureSearch(String user){
        userCounterAccessor.incrementPictureSearch(user);
    }

    public void incrementMetadata(int count, String user){
        userCounterAccessor.incrementMetadata(count, user);
    }

    public void decrementPicture(String user){
        userCounterAccessor.decrementPicture(user);
    }

    public void decrementPictureSearch(String user){
        userCounterAccessor.decrementPictureSearch(user);
    }

    public void decrementMetadata(int count, String user){
        userCounterAccessor.decrementMetadata(count, user);
    }
}

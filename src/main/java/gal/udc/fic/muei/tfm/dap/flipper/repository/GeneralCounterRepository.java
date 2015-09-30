package gal.udc.fic.muei.tfm.dap.flipper.repository;

import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.MappingManager;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Cassandra repository for the GeneralCounter entity.
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
public class GeneralCounterRepository {

    @Inject
    private Session session;

    private GeneralCounterAccessor generalCounterAccessor;

    @PostConstruct
    public void init() {
        MappingManager manager = new MappingManager (session);
        generalCounterAccessor = manager.createAccessor(GeneralCounterAccessor.class);
    }

    public void incrementPicture(int year){
        generalCounterAccessor.incrementPicture(year);
    }

    public void incrementPictureSearch(int year){
        generalCounterAccessor.incrementPictureSearch(year);
    }

    public void incrementMetadata(int count, int year){
        generalCounterAccessor.incrementMetadata(count, year);
    }


    public void decrementPicture(int year){
        generalCounterAccessor.decrementPicture(year);
    }

    public void decrementPictureSearch(int year){
        generalCounterAccessor.decrementPictureSearch(year);
    }

    public void decrementMetadata(int count, int year){
        generalCounterAccessor.decrementMetadata(count, year);
    }
}

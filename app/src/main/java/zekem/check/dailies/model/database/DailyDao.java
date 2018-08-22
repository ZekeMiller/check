package zekem.check.dailies.model.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import zekem.check.dailies.model.Daily;

/**
 * @author Zeke Miller
 */
@Dao
public interface DailyDao {

    @Insert
    long insert( Daily daily );

    @Insert
    void insert( Daily... daily );

    @Insert
    void insert( List< Daily > dailies );

    @Update
    void update( Daily... daily );

    @Delete
    void delete( Daily daily );

    @Query( "DELETE FROM daily WHERE mId=:id " )
    void delete( int id );

    @Query( "SELECT * FROM daily" )
    LiveData< List< Daily > > getAll();

}

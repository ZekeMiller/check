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
public abstract class DailyDao {

    @Insert
    abstract long insert( Daily daily );

    @Insert
    abstract void insert( Daily... daily );

    @Insert
    abstract void insert( List< Daily > dailies );

    @Update
    abstract void update( Daily... daily );

    @Delete
    abstract void delete( Daily daily );

    @Query( "DELETE FROM daily WHERE mId=:id " )
    abstract void delete( int id );

    @Query( "SELECT * FROM daily" )
    abstract LiveData< List< Daily > > getAll();

}

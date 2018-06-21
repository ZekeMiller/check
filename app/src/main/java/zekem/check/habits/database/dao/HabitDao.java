package zekem.check.habits.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.support.annotation.NonNull;

import java.util.List;

import zekem.check.habits.Habit;

/**
 * @author Zeke Miller
 */
@Dao
public interface HabitDao {

    @Insert
    void insert( Habit... habit );

    @Insert
    void insert( List< Habit > habits );

    @Update
    void update( Habit... habit );

    @Delete
    void delete( Habit...  habit );

    @Delete
    void delete( List< Habit >  habit );

    @Query("DELETE FROM habit")
    void nukeTable();

    @Query( "SELECT * FROM habit" )
    LiveData< List< Habit > > getAll();

}

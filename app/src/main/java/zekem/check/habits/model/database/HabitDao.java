package zekem.check.habits.model.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import zekem.check.habits.model.Habit;

/**
 * @author Zeke Miller
 */
@Dao
public interface HabitDao {

    @Insert
    long insert( Habit habit );

    @Insert
    void insert( List< Habit > habits );

    @Update
    void update( Habit... habit );

    @Delete
    void delete( Habit...  habit );

    @Delete
    void delete( List< Habit >  habit );

    @Query( "DELETE FROM habit WHERE mId=:id" )
    void delete( int id );

    @Query( "SELECT * FROM habit" )
    LiveData< List< Habit > > getAll();

    @Query( "SELECT * FROM habit" )
    List< Habit > getAllSync();

    @Query( "SELECT * FROM habit WHERE mId=:id" )
    LiveData< Habit > getHabit( int id );

    @Query( "SELECT * FROM habit WHERE mId=:id" )
    Habit getHabitSync( int id );

    @Query( "DELETE FROM habit" )
    void nukeTable();


//    @Query( "SELECT mId FROM habit" )
//    List< Integer > getHabitIDs();

//    @Transaction
//    @Query( "SELECT * FROM habit" )
//    LiveData< List< HabitWithDays > > getAllWithDays();

//    @Transaction
//    @Query( "SELECT * FROM habit" )
//    List< HabitWithDays > getAllWithDaysSync();

//    @Transaction
//    @Query( "SELECT * FROM habit WHERE mId=:id" )
//    HabitWithDays getHabitWithDays( int id );

}

package zekem.check.habits.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;
import android.support.annotation.NonNull;

import java.util.List;

import zekem.check.habits.Habit;
import zekem.check.habits.HabitWithDays;

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

    @Query( "DELETE FROM habit WHERE id=:id" )
    void delete( int id );

    @Query( "SELECT * FROM habit" )
    LiveData< List< Habit > > getAll();

    @Query( "SELECT * FROM habit WHERE id=:id" )
    Habit getHabitByID( int id );

    @Query( "SELECT id FROM habit" )
    List< Integer > getHabitIDs();

    @Query( "DELETE FROM habit" )
    void nukeTable();

    @Transaction
    @Query( "SELECT * FROM habit" )
    LiveData< List< HabitWithDays > > getAllWithDays();

    @Transaction
    @Query( "SELECT * FROM habit WHERE id=:id" )
    HabitWithDays getHabitWithDays( int id );

}

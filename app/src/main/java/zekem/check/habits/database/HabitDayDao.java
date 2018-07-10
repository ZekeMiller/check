package zekem.check.habits.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;
import zekem.check.habits.HabitDay;

@Dao
public interface HabitDayDao {


    @Insert
    void insert( HabitDay... habitDays );

    @Insert
    void insert( List< HabitDay > habitDays );


    @Update
    void update( HabitDay... habitDays );

    @Update
    void update( List< HabitDay > habitDays );


    @Delete
    void delete( HabitDay... habitDays );


    @Query( "SELECT * FROM habitDay WHERE habitID=:habitID" )
    LiveData< List< HabitDay > > getDaysForHabit(int habitID );

    @Query( "SELECT * FROM habitDay WHERE date=:date")
    LiveData< List< HabitDay > > getHabitsForDay( String date );

    @Query( "SELECT * FROM habitDay WHERE date=:date AND habitID=:habitID" )
    HabitDay getDay( String date, int habitID );

    @Query( "SELECT * FROM habitDay WHERE dayID=:dayID" )
    HabitDay getDay( int dayID );

    @Query( "SELECT * FROM habitDay" )
    List< HabitDay > getAll();

}

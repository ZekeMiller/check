package zekem.check.habits.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import zekem.check.habits.HabitDay;

@Dao
public interface HabitDayDao {

    @Query( "SELECT * from habitday WHERE habitID=:habitID" )
    LiveData<List<HabitDay>> getDaysForHabit(int habitID );

    @Insert
    void insert( HabitDay habitDay );
}

package zekem.check.habits;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.joda.time.LocalDate;

/**
 * @author Zeke Miller
 */
@Dao
public interface HabitDayDAO {

    @Insert
    void insert( HabitDay habitDay );

    @Update
    void update( HabitDay habitDay );

    @Delete
    void delete( HabitDay habitDay );

    @Query( "SELECT * FROM habitday WHERE date = :localDate" )
    HabitDay getHabitDay( String localDate );

}
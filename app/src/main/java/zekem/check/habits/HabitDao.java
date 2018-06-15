package zekem.check.habits;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * @author Zeke Miller
 */
@Dao
public interface HabitDao {

    @Insert
    void insert(Habit... habit);

    @Insert
    void insert( List< Habit > habits );

    @Update
    void update(Habit habit);

    @Delete
    void delete(Habit habit);

    @Query( "SELECT * from habit" )
    List< Habit > getAll();

}

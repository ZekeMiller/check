package zekem.check.habits;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import java.util.List;

/**
 * @author Zeke Miller
 */
@Database( entities = {Habit.class}, version = 1, exportSchema = false )
public abstract class HabitDatabase extends RoomDatabase {

    private static HabitDatabase INSTANCE;
    private static final String DB_NAME = "habit_db";

    public abstract HabitDao habitDao();

    public static HabitDatabase getHabitDatabase(Context context) {
        if ( INSTANCE == null ) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), HabitDatabase.class, DB_NAME ).build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}

package zekem.check.habits.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import zekem.check.habits.Habit;
import zekem.check.habits.HabitDay;

/**
 * @author Zeke Miller
 */
@Database( entities = { Habit.class, HabitDay.class }, version = 1, exportSchema = false )
public abstract class HabitDatabase extends RoomDatabase {

    private static HabitDatabase INSTANCE;
    private static final String DB_NAME = "habit_db";

    public abstract HabitDao habitDao();
    public abstract HabitDayDao habitDayDao();

    public static HabitDatabase getHabitDatabase(Context context) {
        if ( INSTANCE == null ) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), HabitDatabase.class, DB_NAME ).allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}

package zekem.check.habits.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import zekem.check.habits.HabitDay;
import zekem.check.habits.database.dao.HabitDayDAO;

/**
 * @author Zeke Miller
 */
@Database( entities = {HabitDay.class}, version = 1, exportSchema = false )
public abstract class HabitDayDatabase extends RoomDatabase {

    private static HabitDayDatabase INSTANCE;

    abstract HabitDayDAO getHabitDayDAO();

    public static HabitDayDatabase getHabitDayDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(),
                            HabitDayDatabase.class, "user-database").build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}

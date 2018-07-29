package zekem.check.dailies.model.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import zekem.check.dailies.model.Daily;

/**
 * @author Zeke Miller
 */
@Database( entities = { Daily.class }, version = 1)
public abstract class DailyDatabase extends RoomDatabase {

}

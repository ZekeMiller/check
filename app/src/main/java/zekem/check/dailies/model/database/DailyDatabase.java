package zekem.check.dailies.model.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import zekem.check.dailies.model.Daily;

/**
 * @author Zeke Miller
 */
@Database( entities = { Daily.class }, version = 1, exportSchema = false )
public abstract class DailyDatabase extends RoomDatabase {

    // Singleton class variables

    private static final String DB_NAME = "daily_db";

    private static DailyDatabase sInstance;


    // Singleton methods

    public static DailyDatabase getDatabase( Context context ) {
        if ( sInstance == null ) {
            sInstance = Room.databaseBuilder( context.getApplicationContext(),
                    DailyDatabase.class, DB_NAME ).build();
            sInstance.mDatabaseExecutor = Executors.newSingleThreadExecutor();
            sInstance.mDailyDao = sInstance.dailyDao();
        }
        return sInstance;
    }

    public static void destroyInstance() {
        sInstance.mDailyDao = null;
        sInstance.mDatabaseExecutor = null;
        sInstance = null;
    }

    // Dao Accessors

    protected abstract DailyDao dailyDao();


    // Instance fields

    private ExecutorService mDatabaseExecutor;
    private DailyDao mDailyDao;


    public LiveData< List< Daily > > getAll() {
        return mDailyDao.getAll();
    }

    public void addGeneric() {
        mDatabaseExecutor.execute( () ->
            mDailyDao.insert( new Daily( String.valueOf( Math.random() ) ) )
        );
    }

}

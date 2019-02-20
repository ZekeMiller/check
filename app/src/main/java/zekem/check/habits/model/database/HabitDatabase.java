package zekem.check.habits.model.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import zekem.check.habits.model.Habit;
import zekem.check.habits.model.HabitDay;

/**
 * @author Zeke Miller
 */
@Database( entities = { Habit.class, }, version = 1, exportSchema = false )
public abstract class HabitDatabase extends RoomDatabase {

    // Singleton class variables

    private static final String DB_NAME = "habit_db";

    private static HabitDatabase sInstance;


    // Singleton methods

    public static HabitDatabase getDatabase( Context context ) {
        if ( sInstance == null ) {
            sInstance = Room.databaseBuilder( context.getApplicationContext(), HabitDatabase.class, DB_NAME ).build();
            sInstance.mDatabaseExecutor = Executors.newSingleThreadExecutor();
            sInstance.mHabitDao = sInstance.habitDao();
        }
        // sInstance.mDatabaseExecutor.execute( () -> sInstance.fillAllMissing() );
        return sInstance;
    }

    public static void destroyInstance() {
        sInstance.mHabitDao = null;
        sInstance.mDatabaseExecutor = null;
        sInstance = null;
    }


    // DAO Accessors

    protected abstract HabitDao habitDao();


    // Instance fields

    private ExecutorService mDatabaseExecutor;
    private HabitDao mHabitDao;


    // Instance methods

    public LiveData< Habit > getHabit( int habitId ) {
        fillMissingDates( habitId );
        return mHabitDao.getHabit( habitId );
    }

    public LiveData< List< Habit > > getAll() {
        fillAllMissing();
        return mHabitDao.getAll();
    }

    public void fillAllMissing() {
        mDatabaseExecutor.execute( () -> {

            List<Habit> habits = mHabitDao.getAllSync();

            if ( habits != null ) {
                for ( Habit habit : habits ) {
                    fillMissingDates( habit );
                }
            }
        } );
    }

    private void fillMissingDates( int habitId ) {
        mDatabaseExecutor.execute( () -> fillMissingDates( mHabitDao.getHabitSync( habitId ) ) );
    }

    private void fillMissingDates( Habit habit ) {

        LocalDate startDate = habit.getCreatedDate();
        LocalDate endDate = LocalDate.now().plusDays( 1 );  // add 1 so our loop exits after today

        for ( LocalDate date = startDate ; !date.equals( endDate ) ; date = date.plusDays( 1 ) ) {
            if ( !habit.hasDay( date ) ) {
                addDay( habit, date );
            }
        }
    }


    public void addHabit( String name, boolean minusActive, boolean plusActive ) {
        Habit habit = new Habit( name, minusActive, plusActive );
        insertHabit( habit );
    }


    public void insertHabit( @NonNull Habit habit ) {
        mDatabaseExecutor.execute( () -> {
            Long id = mHabitDao.insert( habit );
            addDay( id.intValue() );
        } );
    }

    public void addDay( int habitID ) {
        addDay( habitID, LocalDate.now() );
    }

    public void addDay( int habitID, @NonNull LocalDate date ) {
        mDatabaseExecutor.execute( () -> {
            Habit habit = mHabitDao.getHabitSync( habitID );
            if ( habit != null ) {
                addDay( habit, date );
            }
        });
    }

    public void addDay( @NonNull Habit habit ) {
        addDay( habit, LocalDate.now() );
    }

    public void addDay( @NonNull Habit habit, @NonNull LocalDate date ) {
        habit.addDay( date );
        updateHabitInDB( habit );
    }

    public void insertHabits( List< Habit > habits ) {
        mDatabaseExecutor.execute( () -> mHabitDao.insert( habits ) );
    }

    public void deleteHabit( int habitID ) {
        mDatabaseExecutor.execute( () -> mHabitDao.delete( habitID ) );
    }

    public void deleteHabit( Habit habit ) {
//        mDatabaseExecutor.execute(() -> habitDao().delete( habit ) );
        deleteHabit( habit.getId() );
    }

    public void plusHabitDay( HabitDay habitDay ) {
        plusHabitDay( habitDay.getHabitId(), habitDay.getDate() );
    }

    public void plusHabitDay( @NonNull Habit habit, @NonNull LocalDate date ) {
        plusHabitDay( habit.getId(), date );
    }

    public void plusHabitDay( int habitId, @NonNull LocalDate date ) {
        mDatabaseExecutor.execute( () -> {
            Habit habit = mHabitDao.getHabitSync( habitId );
            if ( habit != null ) {
                habit.plusDay( date );
                updateHabitInDB( habit );
            }
        } );
    }

    public void minusHabitDay( HabitDay habitDay ) {
        minusHabitDay( habitDay.getHabitId(), habitDay.getDate() );
    }

    public void minusHabitDay( @NonNull Habit habit, @NonNull LocalDate date ) {
        minusHabitDay( habit.getId(), date );
    }

    public void minusHabitDay( int habitId, @NonNull LocalDate date ) {
        mDatabaseExecutor.execute( () -> {
            Habit habit = mHabitDao.getHabitSync( habitId );
            if ( habit != null ) {
                habit.minusDay( date );
                updateHabitInDB( habit );
            }
        } );
    }

    public void updateHabitInDB( Habit habit ) {
        mDatabaseExecutor.execute( () -> mHabitDao.update( habit ) );
    }

}

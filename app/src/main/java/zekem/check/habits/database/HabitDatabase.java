package zekem.check.habits.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import zekem.check.habits.Habit;
import zekem.check.habits.HabitDay;
import zekem.check.habits.HabitWithDays;

/**
 * @author Zeke Miller
 */
@Database( entities = { Habit.class, HabitDay.class }, version = 1, exportSchema = false )
public abstract class HabitDatabase extends RoomDatabase {

    // Singleton class variables

    private static HabitDatabase INSTANCE;
    private static final String DB_NAME = "habit_db";

    // Singleton methods

    public static HabitDatabase getHabitDatabase( Context context ) {
        if ( INSTANCE == null ) {
            INSTANCE = Room.databaseBuilder( context.getApplicationContext(), HabitDatabase.class, DB_NAME ).build();
            INSTANCE.mDatabaseExecutor = Executors.newSingleThreadExecutor();
            INSTANCE.mHabitDao = INSTANCE.habitDao();
            INSTANCE.mHabitDayDao = INSTANCE.habitDayDao();
            INSTANCE.fillAllMissing();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE.mHabitDayDao = null;
        INSTANCE.mHabitDao = null;
        INSTANCE.mDatabaseExecutor = null;
        INSTANCE = null;
    }


    // DAO Accessors

    protected abstract HabitDao habitDao();
    protected abstract HabitDayDao habitDayDao();


    // Instance fields

    private ExecutorService mDatabaseExecutor;
    private HabitDao mHabitDao;
    private HabitDayDao mHabitDayDao;


    public HabitDao getHabitDao() {
        return mHabitDao;
    }

    public HabitDayDao getHabitDayDao() {
        return mHabitDayDao;
    }

    // Instance methods

    public void fillAllMissing() {
        mDatabaseExecutor.execute( () -> {

            List<HabitWithDays> habitsWithDays = mHabitDao.getAllWithDaysSync();

            if ( habitsWithDays != null ) {
                for ( HabitWithDays habitWithDays : habitsWithDays ) {
                    fillMissingDates( habitWithDays );
                }
            }
        } );
    }

    private void fillMissingDates( HabitWithDays habitWithDays ) {

        Habit habit = habitWithDays.getHabit();
        LocalDate startDate = habitWithDays.getHabit().getCreatedDate();
        LocalDate endDate = LocalDate.now().plusWeeks( 1 );

        for ( LocalDate date = startDate ; !date.equals( endDate ) ; date = date.plusDays( 1 ) ) {
            if ( habitWithDays.getForDate( date ) == null ) {
                addDay( habit, new LocalDate( date ) );
            }
        }
    }


    public void addHabit( String name, boolean minusActive, boolean plusActive ) {
        Habit habit = new Habit( name, minusActive, plusActive );
        insertHabit( habit );
    }


    public void insertHabit( Habit habit ) {
        mDatabaseExecutor.execute( () -> {
            Long id = mHabitDao.insert( habit );
            addDay( id.intValue() );
        } );
    }

    public void addDay( int habitID ) {
        addDay( habitID, LocalDate.now() );
    }

    public void addDay( int habitID, LocalDate date ) {
        mDatabaseExecutor.execute( () -> {
            Habit habit = mHabitDao.getHabitSync( habitID );
            if ( habit != null ) {
                addDay( habit, date );
            }
        });
    }

    public void addDay( Habit habit ) {
        addDay( habit, LocalDate.now() );
    }

    public void addDay( @NonNull Habit habit, @NonNull LocalDate date ) {
        mDatabaseExecutor.execute( () -> {
            if ( mHabitDayDao.getDay( date.toString(), habit.getId() ) == null ) {
                mHabitDayDao.insert( new HabitDay( habit, date ) );
            }
        } );
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
        plusHabitDay( habitDay.getDayID() );
    }

    public void plusHabitDay( int habitDayId ) {
        mDatabaseExecutor.execute( () -> {
            HabitDay habitDay = mHabitDayDao.getDay( habitDayId );
            if ( habitDay != null ) {
                incrementHabit( habitDay.getHabitID() );
                habitDay.incrementPlus();
                updateHabitDayInDB( habitDay );
            }
        } );
    }

    public void minusHabitDay( HabitDay habitDay ) {
        minusHabitDay( habitDay.getDayID() );
    }

    public void minusHabitDay( int habitDayId ) {
        mDatabaseExecutor.execute( () -> {
            HabitDay habitDay = mHabitDayDao.getDay( habitDayId );
            if ( habitDay != null ) {
                decrementHabit( habitDay.getHabitID() );
                habitDay.incrementMinus();
                updateHabitDayInDB( habitDay );
            }
        } );
    }

    public void incrementHabit( int id ) {
        mDatabaseExecutor.execute( () -> {
            Habit habit = mHabitDao.getHabitSync( id );
            if ( habit != null ) {
                incrementHabit( habit );
            }
        } );
    }

    public void incrementHabit( Habit habit ) {
        habit.increment();
        updateHabitInDB( habit );
    }

    public void decrementHabit( int id ) {
        mDatabaseExecutor.execute( () -> {
            Habit habit = mHabitDao.getHabitSync( id );
            if ( habit != null ) {
                decrementHabit( habit );
            }
        } );
    }

    public void decrementHabit( Habit habit ) {
        habit.decrement();
        updateHabitInDB( habit );
    }

    public void updateHabitInDB( Habit habit ) {
        mDatabaseExecutor.execute( () -> mHabitDao.update( habit ) );
    }

    public void updateHabitDayInDB( HabitDay habitDay ) {
        mDatabaseExecutor.execute( () -> mHabitDayDao.update( habitDay ) );
    }

    public void update( List< HabitDay > habitDays ) {
        mDatabaseExecutor.execute( () -> mHabitDayDao.update( habitDays ) );
    }

}

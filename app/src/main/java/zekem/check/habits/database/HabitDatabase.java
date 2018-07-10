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

/**
 * @author Zeke Miller
 */
@Database( entities = { Habit.class, HabitDay.class }, version = 1, exportSchema = false )
public abstract class HabitDatabase extends RoomDatabase {

    private static HabitDatabase INSTANCE;
    private static final String DB_NAME = "habit_db";

    public static HabitDatabase getHabitDatabase( Context context ) {
        if ( INSTANCE == null ) {
            INSTANCE = Room.databaseBuilder( context.getApplicationContext(), HabitDatabase.class, DB_NAME ).build();
            INSTANCE.mDatabaseExecutor = Executors.newSingleThreadExecutor();
        }
        return INSTANCE;
    }

    public abstract HabitDao habitDao();
    public abstract HabitDayDao habitDayDao();

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private ExecutorService mDatabaseExecutor;



    public void addHabit( String name ) {
        Habit habit = new Habit( name );
        insertHabit( habit );
    }


    public void insertHabit( Habit habit ) {
        mDatabaseExecutor.execute( () -> {
            Long id = habitDao().insert( habit );
            addDay( id.intValue() );
        } );
    }

    public void addDay( int habitID ) {
        addDay( habitID, LocalDate.now() );
    }

    public void addDay( int habitID, LocalDate date ) {
        mDatabaseExecutor.execute( () -> {
            Habit habit = habitDao().getHabitSync( habitID );
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
            if ( habitDayDao().getDay( date.toString(), habit.getId() ) == null ) {
                habitDayDao().insert( new HabitDay( habit, date ) );
            }
        } );
    }

    public void insertHabits( List< Habit > habits ) {
        mDatabaseExecutor.execute( () -> habitDao().insert( habits ) );
    }

    public void deleteHabit( int habitID ) {
        mDatabaseExecutor.execute( () -> habitDao().delete( habitID ) );
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
            HabitDay habitDay = habitDayDao().getDay( habitDayId );
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
            HabitDay habitDay = habitDayDao().getDay( habitDayId );
            if ( habitDay != null ) {
                decrementHabit( habitDay.getHabitID() );
                habitDay.incrementMinus();
                updateHabitDayInDB( habitDay );
            }
        } );
    }

    public void incrementHabit( int id ) {
        mDatabaseExecutor.execute( () -> {
            Habit habit = habitDao().getHabitSync( id );
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
            Habit habit = habitDao().getHabitSync( id );
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
        mDatabaseExecutor.execute( () -> habitDao().update( habit ) );
    }

    public void updateHabitDayInDB( HabitDay habitDay ) {
        mDatabaseExecutor.execute( () -> habitDayDao().update( habitDay ) );
    }

    public void update( List< HabitDay > habitDays ) {
        mDatabaseExecutor.execute( () -> habitDayDao().update( habitDays ) );
    }

}

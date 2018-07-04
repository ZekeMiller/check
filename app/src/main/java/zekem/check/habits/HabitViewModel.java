package zekem.check.habits;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import zekem.check.habits.database.HabitDatabase;
import zekem.check.habits.database.HabitDao;
import zekem.check.habits.database.HabitDayDao;

/**
 * @author Zeke Miller
 */
public class HabitViewModel extends AndroidViewModel
                implements HabitFragmentListener {


    // Fields

    @NonNull
    private final HabitDao habitDao;
    @NonNull
    private final HabitDayDao habitDayDao;
    private LiveData< List< HabitWithDays > > habitsWithDays;
    private final Object habitInstantiationLock = new Object();
    private ExecutorService databaseExecutor;

//    private ExecutorService uiExecutor;

    private MutableLiveData< Integer > habitDetailListener;
    private MutableLiveData< Boolean > newHabitPageListener;



    // Constructor

    public HabitViewModel( Application application ) {
        super( application );

        habitDetailListener = new MutableLiveData<>();
        newHabitPageListener = new MutableLiveData<>();

        habitDao = HabitDatabase.getHabitDatabase( getApplication() ).habitDao();
        habitDayDao = HabitDatabase.getHabitDatabase( getApplication() ).habitDayDao();
        databaseExecutor = Executors.newSingleThreadExecutor();

        databaseExecutor.execute( () -> {

            synchronized ( habitInstantiationLock ) {

                habitsWithDays = habitDao.getAllWithDays();

                // we can notify and unsync now because once the value is got, anyone can register

                habitInstantiationLock.notify();

            }

            List<Integer> habitIDs = habitDao.getHabitIDs();
            List<HabitWithDays> habitDays = habitsWithDays.getValue();

            // some habit IDs exist, some days exist, but they don't have the same amount
            // which means that some habits don't have a day for today
            if ( !(
                    habitDays == null ||
                    habitIDs == null ||
                    habitDays.size() == 0 ||
                    habitIDs.size() == 0 ||
                    habitDays.size() == habitIDs.size()
                ) ) {

                // remove all the habits for which a HabitDay for today exists
                for ( HabitWithDays habitDay : habitDays ) {
                    habitIDs.remove( habitDay.getHabit().getId() );
                }
                // make a day for all those who don't have one today
                for ( int habitID : habitIDs ) {
                    // any changes are notified in addDay
                    addDay( habitID );
                }
            }

        } );

    }



    // DB Access/Update Methods

    @NonNull
    public LiveData< Habit > getHabit( int id ) {
        MutableLiveData< Habit > liveData = new MutableLiveData<>();
        databaseExecutor.execute( () -> {
            Habit habit = habitDao.getHabitByID( id );
            liveData.postValue( habit );
        } );
        return liveData;
    }

    public void register( LifecycleOwner lifecycleOwner,
                          @NonNull Observer< List< HabitWithDays > > observer ) {

        databaseExecutor.execute( () -> {
            synchronized ( habitInstantiationLock ) {
                try {
                    if ( habitsWithDays == null ) {
                        habitInstantiationLock.wait();
                    }
                }
                catch ( InterruptedException e ) {
                    e.printStackTrace();
                }

                habitsWithDays.observe( lifecycleOwner, observer );
                habitInstantiationLock.notify();
            }
        } );
    }

    public void addHabitButton() {
        newHabitPageListener.postValue( true );
    }

    @Deprecated
    public void addHabit() {
        addHabit( String.valueOf( Math.random() * 100 ) );
    }

    public void addHabit( String name ) {
        Habit habit = new Habit( name );
        insertHabit( habit );
        newHabitPageListener.postValue( false );
    }


    private void insertHabit( Habit habit ) {
        databaseExecutor.execute( () -> {
            Long id = habitDao.insert( habit );
            addDay( id.intValue() );
        } );
    }

    public void addDay( int habitID ) {
        addDay( habitID, LocalDate.now() );
    }

    public void addDay( int habitID, LocalDate date ) {
        databaseExecutor.execute( () -> {
            Habit habit = habitDao.getHabitByID( habitID );
            if ( habit != null ) {
                addDay( habit, date );
            }
        });
    }

    public void addDay( Habit habit ) {
        addDay( habit, LocalDate.now() );
    }

    public void addDay( @NonNull Habit habit, @NonNull LocalDate date ) {
        databaseExecutor.execute( () -> {
            if ( habitDayDao.getDay( date.toString(), habit.getId() ) == null ) {
                habitDayDao.insert( new HabitDay( habit, LocalDate.now() ) );

            }
        } );
    }

    public void insertHabits( List< Habit > habits ) {
        databaseExecutor.execute( () -> habitDao.insert( habits ) );
    }

    public void deleteHabit( int habitID ) {
        databaseExecutor.execute( () -> habitDao.delete( habitID ) );
    }

    public void deleteHabit( Habit habit ) {
//        databaseExecutor.execute(() -> habitDao.delete( habit ) );
        deleteHabit( habit.getId() );
    }

    public void plusHabitDay( HabitDay habitDay ) {
        plusHabitDay( habitDay.getDayID() );
    }

    public void plusHabitDay( int habitDayId ) {
        databaseExecutor.execute( () -> {
            HabitDay habitDay = habitDayDao.getDay( habitDayId );
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
        databaseExecutor.execute( () -> {
            HabitDay habitDay = habitDayDao.getDay( habitDayId );
            if ( habitDay != null ) {
                decrementHabit( habitDay.getHabitID() );
                habitDay.incrementMinus();
                updateHabitDayInDB( habitDay );
            }
        } );
    }

    public void incrementHabit( int id ) {
        databaseExecutor.execute( () -> {
            Habit habit = habitDao.getHabitByID( id );
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
        databaseExecutor.execute( () -> {
            Habit habit = habitDao.getHabitByID( id );
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
        databaseExecutor.execute( () -> habitDao.update( habit ) );
    }

    public void updateHabitDayInDB( HabitDay habitDay ) {
        databaseExecutor.execute( () -> habitDayDao.update( habitDay ) );
    }



    public void registerDetail( LifecycleOwner lifecycleOwner,
                          @NonNull Observer< List< HabitDay > > observer, int habitID ) {
        databaseExecutor.execute( () ->
            habitDayDao.getDaysForHabit( habitID ).observe( lifecycleOwner, observer )
        );
    }


    public void viewHabitDetail( int id ) {
        habitDetailListener.postValue( id );
    }

    public LiveData< Integer > getHabitDetailListener() {
        return habitDetailListener;
    }

    public MutableLiveData< Boolean > getNewHabitPageListener() {
        return newHabitPageListener;
    }



    // Habit Listener Methods


    @Override
    public void onContentLongPress( HabitDay habitDay ) {
        if ( habitDay == null ) {
            return;
        }
        deleteHabit( habitDay.getHabitID() );
    }

    @Override
    public void onPlusPress( HabitDay habitDay ) {
        if ( habitDay == null ) {
            return;
        }
        plusHabitDay( habitDay.getDayID() );
    }

    @Override
    public void onMinusPress( HabitDay habitDay ) {
        if ( habitDay == null ) {
            return;
        }
        minusHabitDay( habitDay );
    }

    private void sleep( int seconds ) {
        try {
            TimeUnit.SECONDS.sleep( seconds );
        }
        catch ( InterruptedException e ) {
            e.printStackTrace();
        }
    }

}

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

import zekem.check.habits.database.HabitDatabase;
import zekem.check.habits.database.HabitDao;
import zekem.check.habits.database.HabitDayDao;

/**
 * @author Zeke Miller
 */
public class HabitViewModel extends AndroidViewModel
                implements HabitFragmentListener {


    // Fields

    private final HabitDao habitDao;
    private final HabitDayDao habitDayDao;
    private LiveData< List< HabitWithDays > > habitsWithDays;
    private final Object habitInstantiationLock = new Object();
    private ExecutorService databaseExecutor;
    private ExecutorService uiExecutor;

    private MutableLiveData< Integer > habitDetail = new MutableLiveData<>();



    // Constructor

    public HabitViewModel( Application application ) {
        super( application );

        habitDao = HabitDatabase.getHabitDatabase( getApplication() ).habitDao();
        habitDayDao = HabitDatabase.getHabitDatabase( getApplication() ).habitDayDao();
        databaseExecutor = Executors.newSingleThreadExecutor();

        databaseExecutor.execute( () -> {
            synchronized ( habitInstantiationLock ) {

                // habitsWithDays = habitDayDao.getHabitsForDay(LocalDate.now().toString());
                habitsWithDays = habitDao.getAllWithDays();
                List<Integer> habitIDs = habitDao.getHabitIDs();
                List<HabitWithDays> habitDays = habitsWithDays.getValue();

                // some habit IDs exist, some days exist, but they don't have the same amount
                // which means that some habits don't have a day for today
                if ( !(habitDays == null ||
                        habitIDs == null ||
                        habitDays.size() == 0 ||
                        habitIDs.size() == 0 ||
                        habitDays.size() == habitIDs.size() )
                        ) {

                    // remove all the habits for which a HabitDay for today exists
                    for ( HabitWithDays habitDay : habitDays ) {
                        habitIDs.remove( habitDay.getHabit().getId() );
                    }
                    // make a day for all those who don't have one today
                    for ( int habitID : habitIDs ) {
                        addDay(habitID, null);
                    }
                }


                habitInstantiationLock.notify();
            }
        } );

    }



    // DB Access/Update Methods

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


    public void addHabit() {
        Habit habit = new Habit( String.valueOf( Math.random() ) );
        insertHabit( habit );
    }


    public void insertHabit( Habit habit ) {
        databaseExecutor.execute( () -> {
            long id = habitDao.insert( habit );
//            addDay( (int) id, null );
        } );
    }

    public void addDay( int habitID, Runnable runnable ) {
        addDay( habitID, runnable, LocalDate.now() );
    }

    public void addDay( int habitID, Runnable runnable, LocalDate date ) {
        databaseExecutor.execute( () -> {
            Habit habit = habitDao.getHabitByID( habitID );
            if ( habit != null ) {
                addDay( habit, runnable, date );
            }
        });
    }

    public void addDay( Habit habit, Runnable runnable ) {
        addDay( habit, runnable, LocalDate.now() );
    }

    public void addDay( Habit habit, Runnable runnable, LocalDate date ) {
        databaseExecutor.execute( () -> {
            if ( habitDayDao.getDay( date.toString(), habit.getId() ) == null ) {
                habitDayDao.insert( new HabitDay( habit, LocalDate.now() ) );

                if ( runnable != null ) {
                    runnable.run();
                }
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
        databaseExecutor.execute(() -> habitDao.delete( habit ) );
    }

    public void plusHabitDay( HabitDay habitDay ) {
        habitDay.incrementPlus();
        incrementHabit( habitDay.getHabitID() );
        updateHabitDayInDB( habitDay );
    }

    public void minusHabitDay( HabitDay habitDay ) {
        habitDay.incrementMinus();
        decrementHabit( habitDay.getHabitID());
        updateHabitDayInDB( habitDay );
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
        habitDetail.postValue( id );
    }

    public LiveData< Integer > getDetail() {
        return habitDetail;
    }

//    public void fetchHabit( @NonNull HabitDay habitDay, @Nullable Runnable postExecute ) {
//        databaseExecutor.execute( () ->
//            {
//                try {
//                    TimeUnit.SECONDS.sleep( 1 );
//                }
//                catch ( InterruptedException e ) {
//                    e.printStackTrace();
//                }
//
//                habitDay.setHabit( habitDao.getHabitByID( habitDay.getHabitID() ) );
//
//                if ( postExecute != null ) {
////                    uiExecutor.execute(postExecute);
//                    postExecute.run();
//                }
//            }
//        );
//    }




    // Habit Listener Methods


    @Override
    public void onContentLongPress( HabitDay habitDay ) {
        deleteHabit( habitDay.getHabitID() );
    }

    @Override
    public void onPlusPress( HabitDay habitDay ) {
        plusHabitDay( habitDay );
    }

    @Override
    public void onMinusPress( HabitDay habitDay ) {
        minusHabitDay( habitDay );
    }
}

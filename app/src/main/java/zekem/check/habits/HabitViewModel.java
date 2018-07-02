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

    private final HabitDao habitDao;
    private final HabitDayDao habitDayDao;
    private LiveData< List< HabitDay > > habitsToday;
    private final Object habitInstantiationLock = new Object();
    private ExecutorService executorService;

    private MutableLiveData< Integer > habitDetail = new MutableLiveData<>();



    // Constructor

    public HabitViewModel( Application application ) {
        super( application );

        habitDao = HabitDatabase.getHabitDatabase( getApplication() ).habitDao();
        habitDayDao = HabitDatabase.getHabitDatabase( getApplication() ).habitDayDao();
        executorService = Executors.newSingleThreadExecutor();

        executorService.execute( () -> {
            synchronized ( habitInstantiationLock ) {

                for (HabitDay habitDay : habitDayDao.getAll()) {
                    // repopulate Habit field ( ignored by Room )
                    if (habitDay.getHabit() == null) {
                        habitDay.setHabit(habitDao.getHabitByID(habitDay.getHabitID()));
                    }
                }

                habitsToday = habitDayDao.getHabitsForDay(LocalDate.now().toString());
                List<Integer> habitIDs = habitDao.getHabitIDs();
                List<HabitDay> habitDays = habitsToday.getValue();

                // some habit IDs exist, some days exist, but they don't have the same amount
                // which means that some habits don't have a day for today
                if (!(habitDays == null ||
                        habitIDs == null ||
                        habitDays.size() == 0 ||
                        habitIDs.size() == 0 ||
                        habitDays.size() == habitIDs.size()
                )) {
                    for (HabitDay habitDay : habitDays) {
                        habitIDs.remove(habitDay.getHabitID());
                    }
                    for (int habitID : habitIDs) {
                        addDay(habitID);
                    }
                }


                habitInstantiationLock.notify();
            }
        } );

    }



    // DB Access/Update Methods

    public void register( LifecycleOwner lifecycleOwner,
                          @NonNull Observer< List< HabitDay > > observer ) {

        executorService.execute( () -> {
            synchronized ( habitInstantiationLock ) {
                try {
                    if ( habitsToday == null ) {
                        habitInstantiationLock.wait();
                    }
                }
                catch ( InterruptedException e ) {
                    e.printStackTrace();
                }
                habitsToday.observe( lifecycleOwner, observer );
                habitInstantiationLock.notify();
            }
        } );
    }


    public void addHabit() {
        Habit habit = new Habit( String.valueOf( Math.random() ) );
        insertHabit( habit );
    }


    public void insertHabit( Habit habit ) {
        executorService.execute( () -> {
            long id = habitDao.insert( habit );
            addDay( (int) id );
        } );
    }

    public void addDay( int habitID ) {
        executorService.execute( () -> {
            Habit habit = habitDao.getHabitByID( habitID );
            if ( habit != null ) {
                addDay( habit );
            }
        });
    }

    public void addDay( Habit habit ) {
        executorService.execute( () -> {
            if ( habitDayDao.getDay( LocalDate.now().toString(), habit.getId() ) == null ) {
                habitDayDao.insert( new HabitDay( habit, LocalDate.now() ) );
            }
        } );
    }

    public void insertHabits( List< Habit > habits ) {
        executorService.execute( () -> habitDao.insert( habits ) );
    }

    public void deleteHabit( int habitID ) {
        executorService.execute( () -> habitDao.delete( habitID ) );
    }

    public void deleteHabit( Habit habit ) {
        executorService.execute(() -> habitDao.delete( habit ) );
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
        executorService.execute( () -> {
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
        executorService.execute( () -> {
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
        executorService.execute( () -> habitDao.update( habit ) );
    }

    public void updateHabitDayInDB( HabitDay habitDay ) {
        executorService.execute( () -> habitDayDao.update( habitDay ) );
    }



    public void registerDetail( LifecycleOwner lifecycleOwner,
                          @NonNull Observer< List< HabitDay > > observer, int habitID ) {
        executorService.execute( () ->
            habitDayDao.getDaysForHabit( habitID ).observe( lifecycleOwner, observer )
        );
    }


    public void viewHabitDetail( int id ) {
        habitDetail.postValue( id );
    }

    public LiveData< Integer > getDetail() {
        return habitDetail;
    }



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

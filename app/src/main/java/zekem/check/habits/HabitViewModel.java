package zekem.check.habits;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import zekem.check.habits.database.HabitDatabase;
import zekem.check.habits.database.dao.HabitDao;

/**
 * @author Zeke Miller
 */
public class HabitViewModel extends AndroidViewModel
                implements HabitPageFragment.HabitFragmentListener {


    // Fields

    private final HabitDao habitDao;
    private LiveData< List< Habit > > habits;
    private ExecutorService executorService;



    // Constructor

    public HabitViewModel( Application application ) {
        super( application );
        habitDao = HabitDatabase.getHabitDatabase( getApplication() ).habitDao();
        executorService = Executors.newSingleThreadExecutor();

        executorService.execute( () -> {
            habits = habitDao.getAll();
            if ( habits.getValue() == null || habits.getValue().size() == 0 ) {
                habitDao.nukeTable();
                habitDao.insert( new Habit( "-3" ) );
                try {
                    TimeUnit.SECONDS.sleep( 10 );
                }
                catch ( InterruptedException e ) {
                    e.printStackTrace();
                }
                habitDao.insert( Habit.HabitDummy.HABITS_BIG );
            }
        } );

    }



    // DB Access/Update Methods


    public LiveData<List<Habit>> getHabits() {
        return habits;
    }

    public void insertHabit( Habit habit ) {
        executorService.execute( () -> habitDao.insert( habit ) );
    }

    public void insertHabits( List< Habit > habits ) {
        executorService.execute( () -> habitDao.insert( habits ) );
    }

    public void deleteHabit( Habit habit ) {
        executorService.execute(() -> habitDao.delete(habit) );
    }

    public void incrementHabit( Habit habit ) {
        habit.increment();
        updateHabitInDB( habit );
    }

    public void decrementHabit( Habit habit ) {
        habit.decrement();
        updateHabitInDB( habit );
    }

    public void updateHabitInDB( Habit habit ) {
        executorService.execute( () -> habitDao.update( habit ) );
    }




    // Habit Listener Methods


    @Override
    public void onContentLongPress( Habit habit ) {
        deleteHabit( habit );
    }

    @Override
    public void onPlusPress( Habit habit ) {
        incrementHabit( habit );
    }

    @Override
    public void onMinusPress( Habit habit ) {
        decrementHabit( habit );
    }
}

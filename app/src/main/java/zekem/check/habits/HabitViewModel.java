package zekem.check.habits;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

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
                implements HabitPageFragment.OnListFragmentInteractionListener {


    // Fields

    private final HabitDao habitDao;
    private List< Habit > habits = null;
    private ExecutorService executorService;



    // Constructor

    public HabitViewModel( Application application ) {
        super( application );
        habitDao = HabitDatabase.getHabitDatabase( getApplication() ).habitDao();
        executorService = Executors.newSingleThreadExecutor();
        // executorService.execute( () -> habits.postValue( habitDao.getAll() ) );
        executorService.execute( () -> {
            habits = habitDao.getAll();
        } );

        // only will occur when first booting, so just wait and make sure we get the information
        try {
            executorService.awaitTermination( 10, TimeUnit.MILLISECONDS );
        }
        catch ( InterruptedException e ) {
            e.printStackTrace();
        }
    }



    // DB Access/Update Methods


    @NonNull
    public List< Habit > getHabits() {
    // public LiveData<List<Habit>> getHabits() {
        // if ( habits.getValue() == null || habits.getValue().size() == 0 ) {
        if ( habits == null || habits.size() == 0 ) {
            insertHabits( Habit.HabitDummy.HABITS );
        }
        return habits;
    }

    public void insertHabit( Habit habit ) {
        executorService.execute( () -> habitDao.insert( habit ) );
    }

    public void insertHabits( List< Habit > habits ) {
        executorService.execute( () -> habitDao.insert( habits ) );
    }

    public void deleteHabit( Habit habit ) {
        executorService.execute(() -> habitDao.delete(habit));
    }

    public void incrementHabit( Habit habit ) {
        habit.increment();
        updateHabit( habit );
    }

    public void decrementHabit( Habit habit ) {
        habit.decrement();
        updateHabit( habit );
    }

    public void updateHabit( Habit habit ) {
        executorService.execute( () -> {
            habitDao.update( habit );
            // habits.postValue( habitDao.getAll() );
            habits = habitDao.getAll();
        } );
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

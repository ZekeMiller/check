package zekem.check.habits;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Zeke Miller
 */
public class HabitViewModel extends AndroidViewModel {


    private final HabitDao habitDao;
    private final MutableLiveData< List< Habit > > habits = new MutableLiveData<>();
    private ExecutorService executorService;


    public HabitViewModel( Application application ) {
        super( application );
        habitDao = HabitDatabase.getHabitDatabase( getApplication() ).habitDao();
        executorService = Executors.newSingleThreadExecutor();
        executorService.execute( () -> habits.postValue( habitDao.getAll() ) );
    }

    @NonNull
    public LiveData<List<Habit>> getHabits() {
        if ( habits.getValue() == null || habits.getValue().size() == 0 ) {
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
            habits.postValue( habitDao.getAll() );
        } );
    }





}

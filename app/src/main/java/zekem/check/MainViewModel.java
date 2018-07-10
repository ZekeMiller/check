package zekem.check;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import zekem.check.habits.Habit;
import zekem.check.habits.database.HabitDatabase;

/**
 * @author Zeke Miller
 */
public class MainViewModel extends AndroidViewModel {

    private HabitDatabase mHabitDatabase;

    public MainViewModel( Application application ) {
        super( application );
        mHabitDatabase = HabitDatabase.getHabitDatabase( getApplication() );

    }

    public LiveData< Habit > getHabit( int habitId ) {
        return mHabitDatabase.getHabitDao().getHabit( habitId );
    }
}

package zekem.check;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import zekem.check.habits.database.HabitDatabase;

/**
 * @author Zeke Miller
 */
public class MainViewModel extends AndroidViewModel {

    private HabitDatabase mHabitDatabase;

    public MainViewModel( Application application ) {
        super( application );

    }
}

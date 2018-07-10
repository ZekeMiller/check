package zekem.check.habits.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import zekem.check.habits.HabitObservables;
import zekem.check.habits.database.HabitDatabase;
import zekem.check.habits.listener.NewHabitPageListener;

/**
 * @author Zeke Miller
 */
public class NewHabitViewModel extends AndroidViewModel implements NewHabitPageListener {

    private HabitDatabase mHabitDatabase;
    private HabitObservables mHabitObservables;

    public NewHabitViewModel( Application application ) {
        super( application );
        mHabitDatabase = HabitDatabase.getHabitDatabase( getApplication() );
        mHabitObservables = HabitObservables.getInstance();
    }

    @Override
    public void onSubmitPress( String name ) {
        mHabitDatabase.addHabit( name );
        mHabitObservables.showHabitPage();
    }
}

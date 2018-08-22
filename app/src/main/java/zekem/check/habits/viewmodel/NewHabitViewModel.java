package zekem.check.habits.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import zekem.check.habits.model.database.HabitDatabase;
import zekem.check.habits.viewmodel.listener.NewHabitPageListener;

/**
 * @author Zeke Miller
 */
public class NewHabitViewModel extends AndroidViewModel implements NewHabitPageListener {

    private HabitDatabase mHabitDatabase;
    private HabitObservables mHabitObservables;

    public NewHabitViewModel( Application application ) {
        super( application );
        mHabitDatabase = HabitDatabase.getDatabase( getApplication() );
        mHabitObservables = HabitObservables.getInstance();
    }

    @Override
    public void onSubmitPress( String name, boolean minusActive, boolean plusActive ) {
        mHabitDatabase.addHabit( name, minusActive, plusActive );
        mHabitObservables.showHabitPage();
    }
}

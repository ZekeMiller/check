package zekem.check.habits.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import zekem.check.habits.HabitObservables;
import zekem.check.habits.database.HabitDatabase;
import zekem.check.habits.listener.HabitDeleteDialogListener;

/**
 * @author Zeke Miller
 */
public class DeleteHabitViewModel extends AndroidViewModel implements HabitDeleteDialogListener {

    private HabitDatabase mHabitDatabase;
    private HabitObservables mHabitObservables;

    public DeleteHabitViewModel( Application application ) {
        super( application );
        mHabitDatabase = HabitDatabase.getHabitDatabase( getApplication() );
        mHabitObservables = HabitObservables.getInstance();
    }


    @Override
    public void delete( int habitId ) {
        mHabitDatabase.deleteHabit( habitId );
        mHabitObservables.showHabitPage();
    }

    @Override
    public void cancel() {

    }

}

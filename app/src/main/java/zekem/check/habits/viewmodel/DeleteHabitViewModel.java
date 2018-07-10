package zekem.check.habits.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;

import zekem.check.habits.DeleteHabitDialogFragment;
import zekem.check.habits.HabitObservables;
import zekem.check.habits.database.HabitDatabase;
import zekem.check.habits.listeners.HabitDeleteDialogListener;

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

//        deleteHabit( habitId );
//        showHabitPage();
        mHabitDatabase.deleteHabit( habitId );
        mHabitObservables.showHabitPage();
    }

    @Override
    public void cancel() {

    }

}

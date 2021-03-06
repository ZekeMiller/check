package zekem.check.habits.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import zekem.check.habits.model.Habit;
import zekem.check.habits.model.HabitDay;
import zekem.check.habits.model.database.HabitDatabase;
import zekem.check.habits.viewmodel.listener.HabitDetailListener;

/**
 * @author Zeke Miller
 */
public class HabitDetailViewModel extends AndroidViewModel implements HabitDetailListener {


    private final HabitObservables mHabitObservables;
    private final HabitDatabase mHabitDatabase;


    public HabitDetailViewModel( Application application ) {
        super( application );
        mHabitObservables = HabitObservables.getInstance();
        mHabitDatabase = HabitDatabase.getDatabase( getApplication() );
    }


    @Override
    public void onDetailToolbarButton( int habitId ) {
        triggerDeleteDialog( habitId );
    }


    @Override
    public LiveData< Habit > getHabit( int habitId ) {
        return mHabitDatabase.getHabit( habitId );
    }

    private void triggerDeleteDialog( int habitId ) {
        mHabitObservables.triggerDeleteDialog( habitId );
    }

    @Override
    public void press( HabitDay habitDay ) {
//        mHabitDatabase.plusHabitDay( habitDay );
        mHabitDatabase.plusHabitDay( habitDay.getHabitId(), habitDay.getDate() );
    }

    @Override
    public void longPress( HabitDay habitDay ) {
//        mHabitDatabase.minusHabitDay( habitDay );
        mHabitDatabase.minusHabitDay( habitDay.getHabitId(), habitDay.getDate() );
    }
}

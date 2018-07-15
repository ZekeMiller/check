package zekem.check.habits.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import zekem.check.habits.Habit;
import zekem.check.habits.HabitDay;
import zekem.check.habits.HabitObservables;
import zekem.check.habits.database.HabitDatabase;
import zekem.check.habits.listener.HabitDetailListener;

/**
 * @author Zeke Miller
 */
public class HabitDetailViewModel extends AndroidViewModel implements HabitDetailListener {


    private final HabitObservables mHabitObservables;
    private final HabitDatabase mHabitDatabase;


    public HabitDetailViewModel( Application application ) {
        super( application );
        mHabitObservables = HabitObservables.getInstance();
        mHabitDatabase = HabitDatabase.getHabitDatabase( getApplication() );
    }


    @Override
    public void onDetailToolbarButton( int habitId ) {
        triggerDeleteDialog( habitId );
    }


    @Override
    public LiveData< Habit > getHabit( int habitId ) {
        return mHabitDatabase.getHabitDao().getHabit( habitId );
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

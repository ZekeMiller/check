package zekem.check.habits.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import zekem.check.habits.HabitDay;
import zekem.check.habits.HabitObservables;
import zekem.check.habits.database.HabitDatabase;
import zekem.check.habits.database.HabitDayDao;
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

    @NonNull
    @Override
    public LiveData< List< HabitDay > > getDaysForDetail( int habitId ) {
        return mHabitDatabase.getHabitDayDao().getDaysForHabit( habitId );
    }


    private void triggerDeleteDialog( int habitId ) {
        mHabitObservables.triggerDeleteDialog( habitId );
    }

    @Override
    public void press( HabitDay habitDay ) {
        mHabitDatabase.plusHabitDay( habitDay );
    }

    @Override
    public void longPress( HabitDay habitDay ) {
        mHabitDatabase.minusHabitDay( habitDay );
    }
}

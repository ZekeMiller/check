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
    private final HabitDayDao mHabitDayDao;


    public HabitDetailViewModel( Application application ) {
        super( application );
        mHabitObservables = HabitObservables.getInstance();
        mHabitDayDao = HabitDatabase.getHabitDatabase( getApplication() ).getHabitDayDao();
    }


    @Override
    public void onDetailToolbarButton( int habitId ) {
        triggerDeleteDialog( habitId );
    }

    @NonNull
    @Override
    public LiveData< List< HabitDay > > getDaysForDetail( int habitId ) {
        return mHabitDayDao.getDaysForHabit( habitId );
    }


    private void triggerDeleteDialog( int habitId ) {
        mHabitObservables.triggerDeleteDialog( habitId );
    }
}

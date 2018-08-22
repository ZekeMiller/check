package zekem.check.habits.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.List;

import zekem.check.habits.model.Habit;
import zekem.check.habits.model.HabitDay;
import zekem.check.habits.model.database.HabitDatabase;
import zekem.check.habits.viewmodel.listener.HabitFragmentListener;

/**
 * @author Zeke Miller
 */
public class HabitPageViewModel extends AndroidViewModel implements HabitFragmentListener {


    // Fields

    @NonNull private final HabitDatabase mHabitDatabase;
    @NonNull private final HabitObservables mHabitObservables;

    // Constructor

    public HabitPageViewModel( Application application ) {

        super( application );

        mHabitDatabase = HabitDatabase.getDatabase( getApplication() );
        mHabitObservables = HabitObservables.getInstance();

    }




    // Habit Listener Methods

    @Override
    public void onToolbarAddButtonPress() {
        showNewHabitPage();
    }


    @Override
    public void onContentLongPress( HabitDay habitDay ) {
        triggerDeleteDialog( habitDay.getHabitId() );
    }

    @Override
    public void onContentShortPress( int habitId ) {
        viewHabitDetail( habitId );
    }

    @Override
    public void onPlusPress( HabitDay habitDay ) {
        if ( habitDay != null ) {
            mHabitDatabase.plusHabitDay( habitDay );
        }
    }

    @Override
    public void onMinusPress( HabitDay habitDay ) {
        if ( habitDay != null ) {
            mHabitDatabase.minusHabitDay( habitDay );
        }
    }

    @Override
    public void onMissingDay( Habit habit, LocalDate date ) {
        mHabitDatabase.addDay( habit, date );
    }

    @Override
    public LiveData< List< Habit > > getHabits() {
        return mHabitDatabase.getAll();
    }



    private void viewHabitDetail( int id ) {
        mHabitObservables.viewHabitDetail( id );
    }


    private void triggerDeleteDialog( int habitId ) {
        mHabitObservables.triggerDeleteDialog( habitId );
    }


    private void showNewHabitPage() {
        mHabitObservables.showNewHabitPage();
    }

}

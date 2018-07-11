package zekem.check.habits.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.List;

import zekem.check.habits.Habit;
import zekem.check.habits.HabitDay;
import zekem.check.habits.HabitObservables;
import zekem.check.habits.HabitWithDays;
import zekem.check.habits.database.HabitDatabase;
import zekem.check.habits.listener.HabitFragmentListener;

/**
 * @author Zeke Miller
 */
public class HabitViewModel extends AndroidViewModel implements  HabitFragmentListener {


    // Fields

    @NonNull private final HabitDatabase mHabitDatabase;
    @NonNull private final HabitObservables mHabitObservables;

    // Constructor

    public HabitViewModel( Application application ) {

        super( application );

        mHabitDatabase = HabitDatabase.getHabitDatabase( getApplication() );
        mHabitDatabase.fillAllMissing();
        mHabitObservables = HabitObservables.getInstance();

    }




    // Habit Listener Methods

    @Override
    public void onToolbarAddButtonPress() {
        showNewHabitPage();
    }


    @Override
    public void onContentLongPress( HabitDay habitDay ) {
        triggerDeleteDialog( habitDay.getHabitID() );
    }

    @Override
    public void onContentShortPress( int habitId ) {
        viewHabitDetail( habitId );
    }

    @Override
    public void onPlusPress( HabitDay habitDay ) {
        if ( habitDay == null ) {
            return;
        }
        mHabitDatabase.plusHabitDay( habitDay.getDayID() );
    }

    @Override
    public void onMinusPress( HabitDay habitDay ) {
        if ( habitDay == null ) {
            return;
        }
        mHabitDatabase.minusHabitDay( habitDay.getDayID() );
    }

    @Override
    public void onMissingDay( Habit habit, LocalDate date ) {
        mHabitDatabase.addDay( habit, date );
    }

    @Override
    public LiveData< List< HabitWithDays > > getHabitsWithDays() {
        return mHabitDatabase.getHabitDao().getAllWithDays();
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

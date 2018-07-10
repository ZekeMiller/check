package zekem.check.habits.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import zekem.check.habits.Habit;
import zekem.check.habits.HabitDay;
import zekem.check.habits.HabitObservables;
import zekem.check.habits.HabitWithDays;
import zekem.check.habits.database.HabitDatabase;
import zekem.check.habits.database.HabitDao;
import zekem.check.habits.database.HabitDayDao;
import zekem.check.habits.listeners.HabitFragmentListener;

/**
 * @author Zeke Miller
 */
public class HabitViewModel extends AndroidViewModel implements  HabitFragmentListener {


    // Fields

//    @NonNull private final HabitDao mHabitDao;
//    @NonNull private final HabitDayDao mHabitDayDao;
    @NonNull private final HabitDatabase mHabitDatabase;
    @NonNull private final ExecutorService mDatabaseExecutor;
    @NonNull private final HabitObservables mHabitObservables;

    // Constructor

    public HabitViewModel( Application application ) {
        super( application );

//        mHabitDao = HabitDatabase.getHabitDatabase( getApplication() ).habitDao();
//        mHabitDayDao = HabitDatabase.getHabitDatabase( getApplication() ).habitDayDao();

        mHabitDatabase = HabitDatabase.getHabitDatabase( getApplication() );
        mDatabaseExecutor = Executors.newSingleThreadExecutor();
        mHabitObservables = HabitObservables.getInstance();

        mDatabaseExecutor.execute( () -> {

            List< HabitWithDays > habitsWithDays = mHabitDatabase.habitDao().getAllWithDaysSync();

            if ( habitsWithDays != null ) {
                for ( HabitWithDays habitWithDays : habitsWithDays ) {
                    fillMissingDates( habitWithDays );
                }
            }
        } );

    }

    private void fillMissingDates( HabitWithDays habitWithDays ) {

        Habit habit = habitWithDays.getHabit();
        LocalDate startDate = habitWithDays.getHabit().getCreatedDate();
        LocalDate endDate = LocalDate.now().plusWeeks( 1 );

        for ( LocalDate date = startDate ; !date.equals( endDate ) ; date = date.plusDays( 1 ) ) {
            if ( habitWithDays.getForDate( date ) == null ) {
//                habitWithDays.addDate( date, new HabitDay( habit, date ) );
                mHabitDatabase.addDay( habit, new LocalDate( date ) );
            }
        }
//        mDatabaseExecutor.execute( () -> mHabitDayDao.update( habitWithDays.getHabitDays() ) );
//        mDatabaseExecutor.execute( () -> mHabitDatabase.habitDayDao().update( habitWithDays.getHabitDays() ) );
    }



    // DB Access/Update Methods

    private void viewHabitDetail( int id ) {
        mHabitObservables.viewHabitDetail( id );
    }




    private void triggerDeleteDialog( int habitId ) {
        mHabitObservables.triggerDeleteDialog( habitId );
    }

    private void showHabitPage() {
         mHabitObservables.showHabitPage();
    }

    private void showNewHabitPage() {
        mHabitObservables.showNewHabitPage();
    }




    @NonNull
    public LiveData< Habit > getHabitAsync( int id ) {
        return mHabitDatabase.habitDao().getHabit( id );
//        return mHabitDao.getHabit( id );
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
//        plusHabitDay( habitDay.getDayID() );
    }

    @Override
    public void onMinusPress( HabitDay habitDay ) {
        if ( habitDay == null ) {
            return;
        }
        mHabitDatabase.minusHabitDay( habitDay.getDayID() );
//        minusHabitDay( habitDay );
    }

    @Override
    public void onMissingDay( Habit habit, LocalDate date ) {
//        addDay( habit, date );
        mHabitDatabase.addDay( habit, date );
    }

    @Override
    public LiveData< List< HabitWithDays > > getHabitsWithDays() {
        return mHabitDatabase.habitDao().getAllWithDays();
//        return mHabitDao.getAllWithDays();
    }

}

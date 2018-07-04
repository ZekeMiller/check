package zekem.check.habits;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import zekem.check.habits.database.HabitDatabase;
import zekem.check.habits.database.HabitDao;
import zekem.check.habits.database.HabitDayDao;
import zekem.check.habits.listeners.HabitDeleteDialogListener;
import zekem.check.habits.listeners.HabitDetailFragmentListener;
import zekem.check.habits.listeners.HabitFragmentListener;
import zekem.check.habits.listeners.NewHabitPageListener;

/**
 * @author Zeke Miller
 */
public class HabitViewModel extends AndroidViewModel
                implements  HabitFragmentListener,
                            HabitDetailFragmentListener,
                            NewHabitPageListener,
                            HabitDeleteDialogListener {


    // Fields

    @NonNull private final Object mHabitInstantiationLock = new Object();

    @NonNull private final HabitDao mHabitDao;
    @NonNull private final HabitDayDao mHabitDayDao;
    @NonNull private final ExecutorService mDatabaseExecutor;

    private LiveData< List< HabitWithDays > > mHabitsWithDays;


//    private ExecutorService uiExecutor;

    private MutableLiveData< Integer > mShowHabitDetailListener;
    private MutableLiveData< Boolean > mNewHabitPageListener;
    private MutableLiveData< Integer > mDetailDeleteListener;



    // Constructor

    public HabitViewModel( Application application ) {
        super( application );

        mShowHabitDetailListener = new MutableLiveData<>();
        mNewHabitPageListener = new MutableLiveData<>();
        mDetailDeleteListener = new MutableLiveData<>();

        mHabitDao = HabitDatabase.getHabitDatabase( getApplication() ).habitDao();
        mHabitDayDao = HabitDatabase.getHabitDatabase( getApplication() ).habitDayDao();
        mDatabaseExecutor = Executors.newSingleThreadExecutor();

        mDatabaseExecutor.execute( () -> {

            synchronized ( mHabitInstantiationLock ) {

                mHabitsWithDays = mHabitDao.getAllWithDays();

                // we can notify and unsync now because once the value is got, anyone can register

                mHabitInstantiationLock.notify();

            }

            List<Integer> habitIDs = mHabitDao.getHabitIDs();
            List<HabitWithDays> habitDays = mHabitsWithDays.getValue();

            // some habit IDs exist, some days exist, but they don't have the same amount
            // which means that some habits don't have a day for today
            if ( !(
                    habitDays == null ||
                    habitIDs == null ||
                    habitDays.size() == 0 ||
                    habitIDs.size() == 0 ||
                    habitDays.size() == habitIDs.size()
                ) ) {

                // remove all the habits for which a HabitDay for today exists
                for ( HabitWithDays habitDay : habitDays ) {
                    habitIDs.remove( habitDay.getHabit().getId() );
                }
                // make a day for all those who don't have one today
                for ( int habitID : habitIDs ) {
                    // any changes are notified in addDay
                    addDay( habitID );
                }
            }

        } );

    }



    // DB Access/Update Methods

    private void viewHabitDetail( int id ) {
        mShowHabitDetailListener.postValue( id );
    }

    private void addHabit( String name ) {
        Habit habit = new Habit( name );
        insertHabit( habit );
        mNewHabitPageListener.postValue( false );
    }


    private void insertHabit( Habit habit ) {
        mDatabaseExecutor.execute( () -> {
            Long id = mHabitDao.insert( habit );
            addDay( id.intValue() );
        } );
    }

    private void addDay( int habitID ) {
        addDay( habitID, LocalDate.now() );
    }

    private void addDay( int habitID, LocalDate date ) {
        mDatabaseExecutor.execute( () -> {
            Habit habit = mHabitDao.getHabitByID( habitID );
            if ( habit != null ) {
                addDay( habit, date );
            }
        });
    }

    private void addDay( Habit habit ) {
        addDay( habit, LocalDate.now() );
    }

    private void addDay( @NonNull Habit habit, @NonNull LocalDate date ) {
        mDatabaseExecutor.execute( () -> {
            if ( mHabitDayDao.getDay( date.toString(), habit.getId() ) == null ) {
                mHabitDayDao.insert( new HabitDay( habit, LocalDate.now() ) );
            }
        } );
    }

    private void insertHabits( List< Habit > habits ) {
        mDatabaseExecutor.execute( () -> mHabitDao.insert( habits ) );
    }

    private void deleteHabit( int habitID ) {
        mDatabaseExecutor.execute( () -> mHabitDao.delete( habitID ) );
    }

    private void deleteHabit( Habit habit ) {
//        mDatabaseExecutor.execute(() -> mHabitDao.delete( habit ) );
        deleteHabit( habit.getId() );
    }

    private void plusHabitDay( HabitDay habitDay ) {
        plusHabitDay( habitDay.getDayID() );
    }

    private void plusHabitDay( int habitDayId ) {
        mDatabaseExecutor.execute( () -> {
            HabitDay habitDay = mHabitDayDao.getDay( habitDayId );
            if ( habitDay != null ) {
                incrementHabit( habitDay.getHabitID() );
                habitDay.incrementPlus();
                updateHabitDayInDB( habitDay );
            }
        } );
    }

    private void minusHabitDay( HabitDay habitDay ) {
        minusHabitDay( habitDay.getDayID() );
    }

    private void minusHabitDay( int habitDayId ) {
        mDatabaseExecutor.execute( () -> {
            HabitDay habitDay = mHabitDayDao.getDay( habitDayId );
            if ( habitDay != null ) {
                decrementHabit( habitDay.getHabitID() );
                habitDay.incrementMinus();
                updateHabitDayInDB( habitDay );
            }
        } );
    }

    private void incrementHabit( int id ) {
        mDatabaseExecutor.execute( () -> {
            Habit habit = mHabitDao.getHabitByID( id );
            if ( habit != null ) {
                incrementHabit( habit );
            }
        } );
    }

    private void incrementHabit( Habit habit ) {
        habit.increment();
        updateHabitInDB( habit );
    }

    private void decrementHabit( int id ) {
        mDatabaseExecutor.execute( () -> {
            Habit habit = mHabitDao.getHabitByID( id );
            if ( habit != null ) {
                decrementHabit( habit );
            }
        } );
    }

    private void decrementHabit( Habit habit ) {
        habit.decrement();
        updateHabitInDB( habit );
    }

    private void updateHabitInDB( Habit habit ) {
        mDatabaseExecutor.execute( () -> mHabitDao.update( habit ) );
    }

    private void updateHabitDayInDB( HabitDay habitDay ) {
        mDatabaseExecutor.execute( () -> mHabitDayDao.update( habitDay ) );
    }




    @NonNull
    public LiveData< Habit > getHabitAsync( int id ) {
        MutableLiveData< Habit > liveData = new MutableLiveData<>();
        mDatabaseExecutor.execute( () -> {
            Habit habit = mHabitDao.getHabitByID( id );
            liveData.postValue( habit );
        } );
        return liveData;
    }

    public LiveData< Integer > getShowHabitDetailListener() {
        return mShowHabitDetailListener;
    }

    public LiveData< Boolean > getNewHabitPageListener() {
        return mNewHabitPageListener;
    }

    public LiveData< Integer > getDetailDeleteListener() {
        return mDetailDeleteListener;
    }



    // Habit Listener Methods

    @Override
    public void onToolbarAddButtonPress() {
        mNewHabitPageListener.postValue( true );
    }


    @Override
    public void onContentLongPress( HabitDay habitDay ) {
        if ( habitDay == null ) {
            return;
        }
        deleteHabit( habitDay.getHabitID() );
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
        plusHabitDay( habitDay.getDayID() );
    }

    @Override
    public void onMinusPress( HabitDay habitDay ) {
        if ( habitDay == null ) {
            return;
        }
        minusHabitDay( habitDay );
    }

    @Override
    public void onMissingDay( Habit habit, LocalDate date ) {
        addDay( habit, date );
    }

    @Override
    public LiveData< LiveData< List< HabitWithDays > > > getHabitsWithDaysWhenReady() {

        MutableLiveData< LiveData< List< HabitWithDays > > > liveData = new MutableLiveData<>();

        mDatabaseExecutor.execute( () -> {
            synchronized ( mHabitInstantiationLock ) {
                try {
                    if ( mHabitsWithDays == null ) {
                        mHabitInstantiationLock.wait();
                    }
                }
                catch ( InterruptedException e ) {
                    e.printStackTrace();
                }

                liveData.postValue( mHabitsWithDays );
                mHabitInstantiationLock.notify();
            }
        } );

        return liveData;
    }


    // Habit Detail Listener Methods

    @Override
    public void onDetailToolbarButton( int habitId ) {
        mDetailDeleteListener.postValue( habitId );
    }

    @NonNull
    @Override
    public LiveData< LiveData< List< HabitDay > > > getDaysForDetailAsync( int habitId ) {
        MutableLiveData< LiveData< List< HabitDay > > > liveData = new MutableLiveData<>();
        mDatabaseExecutor.execute( () -> {
            LiveData< List< HabitDay > > habitDays;
            habitDays = mHabitDayDao.getDaysForHabit( habitId );
            liveData.postValue( habitDays );
        } );

        return liveData;
    }



    // New Habit Page Listener Methods

    @Override
    public void onSubmitPress( String name ) {
        addHabit( name );
    }


    // Delete Habit Dialog Listener Methods

    @Override
    public void delete( int habitId ) {
        deleteHabit( habitId );
        mDetailDeleteListener.postValue( null );
    }

    @Override
    public void cancel() {

    }

}

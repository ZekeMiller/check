package zekem.check.habits;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Zeke Miller
 */
public class HabitObservables {

    private static HabitObservables sInstance;

    public static HabitObservables getInstance() {
        if ( sInstance == null ) {
            sInstance = new HabitObservables();
        }
        return sInstance;
    }


//    private final MutableLiveData< Integer > mShowHabitDetailListener;
//    private final MutableLiveData< Void > mNewHabitPageListener;
//    private final MutableLiveData< Integer > mDetailDeleteListener;
//    private final MutableLiveData< Void > mShowHabitPageListener;

    private final List< Observer< Integer > > mDeleteObservers;
    private final List< Observer< Integer > > mDetailObservers;
    private final List< Observer< Void > > mNewHabitObservers;
    private final List< Observer< Void > > mShowHabitPageObservers;

    private HabitObservables() {
//        mDetailDeleteListener = new MutableLiveData<>();
//        mNewHabitPageListener = new MutableLiveData<>();
//        mShowHabitDetailListener = new MutableLiveData<>();
//        mShowHabitPageListener = new MutableLiveData<>();
        mDeleteObservers = new LinkedList<>();
        mDetailObservers = new LinkedList<>();
        mNewHabitObservers = new LinkedList<>();
        mShowHabitPageObservers = new LinkedList<>();
    }

    public void registerDelete( Observer< Integer > observer ) {
        mDeleteObservers.add( observer );
    }

    public void unregisterDelete( Observer< Integer > observer ) {
        mDeleteObservers.remove( observer );
    }

    public void registerDetail( Observer< Integer > observer ) {
        mDetailObservers.add( observer );
    }

    public void unRegisterDetail( Observer< Integer > observer ) {
        mDetailObservers.remove( observer );
    }

    public void registerNewHabit( Observer< Void > observer ) {
        mNewHabitObservers.add( observer );
    }

    public void unregisterNewHabit( Observer< Void > observer ) {
        mNewHabitObservers.remove( observer );
    }

    public void registerShowHabitPage( Observer< Void > observer ) {
        mShowHabitPageObservers.add( observer );
    }

    public void unregisterShowHabitPage( Observer< Void > observer ) {
        mShowHabitPageObservers.remove( observer );
    }

//    public LiveData< Void > getShowHabitPageListener() {
//        return mShowHabitPageListener;
//    }
//
//    public LiveData< Integer > getDetailDeleteListener() {
//        return mDetailDeleteListener;
//    }
//
//    public LiveData< Integer > getShowHabitDetailListener() {
//        return mShowHabitDetailListener;
//    }
//
//    public LiveData< Void > getNewHabitPageListener() {
//        return mNewHabitPageListener;
//    }


    public void triggerDeleteDialog( int habitId ) {
//        mDetailDeleteListener.postValue( habitId );
        for ( Observer< Integer > observer : mDeleteObservers ) {
            observer.update( habitId );
        }
    }

    public void viewHabitDetail( int habitId ) {
//        mShowHabitDetailListener.postValue( habitId );
        for ( Observer< Integer > observer : mDetailObservers ) {
            observer.update( habitId );
        }
    }

    public void showHabitPage() {
//        mShowHabitPageListener.postValue( null );
        for ( Observer< Void > observer : mShowHabitPageObservers ) {
            observer.update( null );
        }
    }

    public void showNewHabitPage() {
//        mNewHabitPageListener.postValue( null );
        for ( Observer< Void > observer : mNewHabitObservers ) {
            observer.update( null );
        }
    }


    public interface Observer<T> {
        void update( T data );
    }
}

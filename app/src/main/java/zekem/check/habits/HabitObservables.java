package zekem.check.habits;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

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


    private final MutableLiveData< Integer > mShowHabitDetailListener;
    private final MutableLiveData< Void > mNewHabitPageListener;
    private final MutableLiveData< Integer > mDetailDeleteListener;
    private final MutableLiveData< Void > mShowHabitPageListener;

    private HabitObservables() {
        mDetailDeleteListener = new MutableLiveData<>();
        mNewHabitPageListener = new MutableLiveData<>();
        mShowHabitDetailListener = new MutableLiveData<>();
        mShowHabitPageListener = new MutableLiveData<>();
    }

    public LiveData< Void > getShowHabitPageListener() {
        return mShowHabitPageListener;
    }

    public LiveData< Integer > getDetailDeleteListener() {
        return mDetailDeleteListener;
    }

    public LiveData< Integer > getShowHabitDetailListener() {
        return mShowHabitDetailListener;
    }

    public LiveData< Void > getNewHabitPageListener() {
        return mNewHabitPageListener;
    }


    public void triggerDeleteDialog( int habitId ) {
        mDetailDeleteListener.postValue( habitId );
    }

    public void showHabitPage() {
        mShowHabitPageListener.postValue( null );
    }

    public void showNewHabitPage() {
        mNewHabitPageListener.postValue( null );
    }

    public void viewHabitDetail( int habitId ) {
        mShowHabitDetailListener.postValue( habitId );
    }
}

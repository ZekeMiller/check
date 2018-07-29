package zekem.check.habits.viewmodel;

import android.arch.lifecycle.Observer;

import java.util.LinkedList;
import java.util.List;

/**
 * A singleton class used to observe and onChanged some things in the UI
 *
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


    private final List< Observer< Integer > > mDeleteObservers;
    private final List< Observer< Integer > > mDetailObservers;
    private final List< Observer< Void > > mNewHabitObservers;
    private final List< Observer< Void > > mShowHabitPageObservers;


    private HabitObservables() {
        mDeleteObservers = new LinkedList<>();
        mDetailObservers = new LinkedList<>();
        mNewHabitObservers = new LinkedList<>();
        mShowHabitPageObservers = new LinkedList<>();
    }


    // register/unregister

    public void registerDelete( Observer< Integer > observer ) {
        if ( observer != null && !mDeleteObservers.contains( observer ) ) {
            mDeleteObservers.add( observer );
        }
    }

    public void unregisterDelete( Observer< Integer > observer ) {
        mDeleteObservers.remove( observer );
    }

    public void registerDetail( Observer< Integer > observer ) {
        if ( observer != null && !mDetailObservers.contains( observer ) ) {
            mDetailObservers.add( observer );
        }
    }

    public void unRegisterDetail( Observer< Integer > observer ) {
        mDetailObservers.remove( observer );
    }

    public void registerNewHabit( Observer< Void > observer ) {
        if ( observer != null && !mNewHabitObservers.contains( observer ) ) {
            mNewHabitObservers.add( observer );
        }
    }

    public void unregisterNewHabit( Observer< Void > observer ) {
        mNewHabitObservers.remove( observer );
    }

    public void registerShowHabitPage( Observer< Void > observer ) {
        if ( observer != null && !mShowHabitPageObservers.contains( observer ) ) {
            mShowHabitPageObservers.add( observer );
        }
    }

    public void unregisterShowHabitPage( Observer< Void > observer ) {
        mShowHabitPageObservers.remove( observer );
    }


    // updates

    public void triggerDeleteDialog( int habitId ) {
        for ( Observer< Integer > observer : mDeleteObservers ) {
            observer.onChanged( habitId );
        }
    }

    public void viewHabitDetail( int habitId ) {
        for ( Observer< Integer > observer : mDetailObservers ) {
            observer.onChanged( habitId );
        }
    }

    public void showHabitPage() {
        for ( Observer< Void > observer : mShowHabitPageObservers ) {
            observer.onChanged( null );
        }
    }

    public void showNewHabitPage() {
        for ( Observer< Void > observer : mNewHabitObservers ) {
            observer.onChanged( null );
        }
    }
}

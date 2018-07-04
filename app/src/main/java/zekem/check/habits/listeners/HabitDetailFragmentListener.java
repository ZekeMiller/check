package zekem.check.habits.listeners;

import android.arch.lifecycle.LiveData;

import java.util.List;

import zekem.check.habits.HabitDay;

/**
 * @author Zeke Miller
 */
public interface HabitDetailFragmentListener {

    void onDetailToolbarButton( int habitId );

    LiveData< LiveData< List< HabitDay > > > getDaysForDetailAsync( int habitId );

}

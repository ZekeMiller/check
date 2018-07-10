package zekem.check.habits.listeners;

import android.arch.lifecycle.LiveData;

import java.util.List;

import zekem.check.habits.HabitDay;

/**
 * @author Zeke Miller
 */
public interface HabitDetailListener {

    void onDetailToolbarButton( int habitId );

    LiveData< List< HabitDay > > getDaysForDetail( int habitId );

}

package zekem.check.habits.listener;

import android.arch.lifecycle.LiveData;

import zekem.check.habits.Habit;
import zekem.check.habits.HabitDay;

/**
 * @author Zeke Miller
 */
public interface HabitDetailListener {

    void onDetailToolbarButton( int habitId );

    LiveData< Habit > getHabit( int habitId );

    void press( HabitDay habitDay );

    void longPress( HabitDay habitDay );

}

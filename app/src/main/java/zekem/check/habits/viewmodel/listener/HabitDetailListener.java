package zekem.check.habits.viewmodel.listener;

import android.arch.lifecycle.LiveData;

import zekem.check.habits.model.Habit;
import zekem.check.habits.model.HabitDay;

/**
 * @author Zeke Miller
 */
public interface HabitDetailListener {

    void onDetailToolbarButton( int habitId );

    LiveData< Habit > getHabit( int habitId );

    void press( HabitDay habitDay );

    void longPress( HabitDay habitDay );

}

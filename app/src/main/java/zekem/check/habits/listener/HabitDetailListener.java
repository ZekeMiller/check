package zekem.check.habits.listener;

import android.arch.lifecycle.LiveData;

import java.util.List;

import zekem.check.habits.HabitDay;
import zekem.check.habits.database.HabitDatabase;

/**
 * @author Zeke Miller
 */
public interface HabitDetailListener {

    void onDetailToolbarButton( int habitId );

    LiveData< List< HabitDay > > getDaysForDetail( int habitId );

    void press( HabitDay habitDay );

    void longPress( HabitDay habitDay );

}

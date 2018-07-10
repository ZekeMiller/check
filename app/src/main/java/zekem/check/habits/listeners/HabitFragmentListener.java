package zekem.check.habits.listeners;

import android.arch.lifecycle.LiveData;

import org.joda.time.LocalDate;

import java.util.List;

import zekem.check.habits.Habit;
import zekem.check.habits.HabitDay;
import zekem.check.habits.HabitWithDays;

/**
 * Interface for the ViewModel to listen to interactions with a habit
 *
 * @author Zeke Miller
 */
public interface HabitFragmentListener {

    void onToolbarAddButtonPress();

    void onContentLongPress( HabitDay habitDay );

    void onContentShortPress( int habitId );

    void onPlusPress( HabitDay habitDay );

    void onMinusPress( HabitDay habitDay );

    void onMissingDay( Habit habit, LocalDate date );

    LiveData< List< HabitWithDays > > getHabitsWithDays();

}
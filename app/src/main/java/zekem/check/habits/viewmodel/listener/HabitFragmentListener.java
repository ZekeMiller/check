package zekem.check.habits.viewmodel.listener;

import android.arch.lifecycle.LiveData;

import org.joda.time.LocalDate;

import java.util.List;

import zekem.check.habits.model.Habit;
import zekem.check.habits.model.HabitDay;

/**
 * Interface for the ViewModel to listen to interactions with a habit
 *
 * @author Zeke Miller
 */
public interface HabitFragmentListener {

    void onToolbarAddButtonPress();

    void onContentShortPress( int habitId );

    void onContentLongPress( HabitDay habitDay );

    void onPlusPress( HabitDay habitDay );

    void onMinusPress( HabitDay habitDay );

    void onMissingDay( Habit habit, LocalDate date );

    LiveData< List< Habit > > getHabits();

}
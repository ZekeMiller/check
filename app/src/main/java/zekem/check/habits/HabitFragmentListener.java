package zekem.check.habits;

/**
 * Interface for the ViewModel to listen to interactions with a habit
 *
 * @author Zeke Miller
 */
public interface HabitFragmentListener {

    void onContentLongPress( Habit habit );

    void onPlusPress( Habit habit );

    void onMinusPress( Habit habit );
}
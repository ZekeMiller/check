package zekem.check.habits;

/**
 * Interface for the ViewModel to listen to interactions with a habit
 *
 * @author Zeke Miller
 */
public interface HabitFragmentListener {

    void onContentLongPress( HabitDay habitDay );

    void onPlusPress( HabitDay habitDay );

    void onMinusPress( HabitDay habitDay );

}
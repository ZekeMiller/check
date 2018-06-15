package zekem.check.habits;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zeke Miller
 */
@Entity
public class Habit {

    @NonNull
    @PrimaryKey
    private String title;


    public Habit( String title ) {

        this.title = title;
    }



    public String getTitle() {

        return title;
    }



    public static class HabitDummy {

        public static final List< Habit > HABITS = new ArrayList<>();
        public static final int AMOUNT = 20;

        static {
            for ( int i = 0 ; i <= AMOUNT ; i++ ) {
                HABITS.add( new Habit( Integer.toString( i ) ) );
            }
        }


    }

}

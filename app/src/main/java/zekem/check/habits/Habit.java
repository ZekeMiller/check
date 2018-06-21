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
    @PrimaryKey (autoGenerate = true)
    private int id;

    private String title;
    private int info;


    public Habit( String title ) {

        this.title = title;
        this.info = Integer.parseInt( title );
    }



    @NonNull
    public int getId() {

        return id;
    }

    public void setId( @NonNull int id ) {

        this.id = id;
    }


    public String getTitle() {

        return title;
    }

    public void setTitle( String title ) {

        this.title = title;
    }


    public int getInfo() {

        return info;
    }

    public void setInfo( int info ) {

        this.info = info;
    }


    public void increment() {
        info++;
    }

    public void decrement() {
        info--;
    }


    public static class HabitDummy {

        public static final List< Habit > HABITS = new ArrayList<>();
        public static final List< Habit > HABITS_BIG = new ArrayList<>();
        public static final int AMOUNT = 20;

        static {
            for ( int i = 0 ; i <= AMOUNT ; i++ ) {
                HABITS.add( new Habit( Integer.toString( i ) ) );
            }
            for ( int i = 0 ; i <= AMOUNT * AMOUNT * AMOUNT ; i++ ) {
                HABITS_BIG.add( new Habit( Integer.toString( i ) ) );
            }
        }


    }

    public boolean sameContents( Habit habit ) {
        return this.equals( habit ) && this.title.equals( habit.title ) && this.info == habit.info;
    }

    @Override
    public boolean equals( Object other ) {
        if ( other instanceof Habit ) {
            Habit otherHabit = (Habit) other;
            return this.id == otherHabit.id;
        }
        return false;
    }

}

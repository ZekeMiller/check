package zekem.check.habits;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import org.joda.time.LocalDate;

import zekem.check.habits.database.DateConverters;

/**
 * @author Zeke Miller
 */
@Entity
@TypeConverters( DateConverters.class )
public class Habit {

    private static final int NUM_RANKS = 5;

    @PrimaryKey(autoGenerate = true)
    private int id;

    private LocalDate createdDate;

    private String title;
    private int totalPlus;
    private int totalMinus;


    public Habit( String title ) {

        this.title = title;
        createdDate = LocalDate.now();
        totalMinus = 0;
        totalPlus = 0;
    }



    public int getId() {
        return id;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public String getTitle() {
        return title;
    }

    public int getTotalMinus() {
        return totalMinus;
    }

    public int getTotalPlus() {
        return totalPlus;
    }


    public void setId( int id ) {
        this.id = id;
    }

    public void setCreatedDate( LocalDate createdDate ) {
        this.createdDate = createdDate;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public void setTotalMinus(int totalMinus) {
        this.totalMinus = totalMinus;
    }

    public void setTotalPlus(int totalPlus) {
        this.totalPlus = totalPlus;
    }


    public void increment() {
        totalPlus++;
    }

    public void decrement() {
        totalMinus++;
    }

    public int getRank() {
//        return NUM_RANKS * ( totalPlus / ( totalPlus + totalMinus ) ) + 1;
        double total = totalMinus + totalPlus;
        double ratio = totalPlus / total;
        int scaledRounded = (int) ( ratio * NUM_RANKS );
        int adjust = scaledRounded + 1;
        return adjust;
    }



    public boolean sameContents( Habit habit ) {
        return this.equals( habit ) && this.title.equals( habit.title ) &&
                this.totalMinus == habit.totalMinus &&
                this.totalPlus == habit.totalPlus;
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

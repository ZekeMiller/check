package zekem.check.habits;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import static android.arch.persistence.room.ColumnInfo.INTEGER;

/**
 * @author Zeke Miller
 */
@Entity
public class Habit {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    private int totalPlus;
    private int totalMinus;


    public Habit( String title ) {

        this.title = title;
        totalMinus = 0;
        totalPlus = 0;
    }



    public int getId() {

        return id;
    }

    public void setId( int id ) {

        this.id = id;
    }

//    public List<HabitDay> getHabitDays() {
//        return habitDays;
//    }
//
//    public void setHabitDays(List<HabitDay> habitDays) {
//        this.habitDays = habitDays;
//    }

    public String getTitle() {
        return title;
    }

    public int getTotalMinus() {
        return totalMinus;
    }

    public int getTotalPlus() {
        return totalPlus;
    }


    public void setTitle(String title ) {
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

package zekem.check.habits;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import org.joda.time.LocalDate;

import zekem.check.habits.database.DateConverters;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * @author Zeke Miller
 */
@Entity (   foreignKeys = @ForeignKey(  entity = Habit.class,
                                        parentColumns = "id",
                                        childColumns = "habitID",
                                        onDelete = CASCADE ),
            indices = { @Index( "habitID" ) } )
@TypeConverters( DateConverters.class )
public class HabitDay {

    // these fields should never change after being created
    @PrimaryKey(autoGenerate = true)
    private int dayID;
    private int habitID;
    private LocalDate date;

    // these fields can change
    private int plusCount;
    private int minusCount;



    public HabitDay() {}

    public HabitDay( Habit habit, LocalDate date ) {
        this.habitID = habit.getId();
        this.date = date;
        plusCount = 0;
        minusCount = 0;
    }



    public void incrementPlus() {
        plusCount++;
    }

    public void incrementMinus() {
        minusCount++;
    }


    public int getDayID() {
        return dayID;
    }

    public int getHabitID() {
        return habitID;
    }

    public int getMinusCount() {
        return minusCount;
    }

    public int getPlusCount() {
        return plusCount;
    }

    public LocalDate getDate() {
        return date;
    }


    public void setDayID( int dayID ) {
        this.dayID = dayID;
    }

    public void setDate( LocalDate date ) {
        this.date = date;
    }

    public void setHabitID( int habitID) {
        this.habitID = habitID;
    }

    public void setPlusCount(int plusCount ) {
        this.plusCount = plusCount;
    }

    public void setMinusCount( int minusCount ) {
        this.minusCount = minusCount;
    }

    public boolean sameContents( HabitDay other ) {
        return
//                this.equals( other ) &&
//                this.habitID == other.habitID &&
                this.plusCount == other.plusCount &&
                this.minusCount == other.minusCount;
    }

    public boolean sameId( HabitDay other ) {
        return this.dayID == other.dayID;
    }

    @Override
    public boolean equals( Object other ) {
        if ( other instanceof HabitDay ) {
            HabitDay otherDay = (HabitDay) other;
            return this.sameId( otherDay ) && this.sameContents( otherDay );
        }
        return false;
    }


}

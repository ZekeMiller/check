package zekem.check.habits;

import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.RoomWarnings;
import android.arch.persistence.room.TypeConverter;
import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * @author Zeke Miller
 */
@Entity (   foreignKeys = @ForeignKey(  entity = Habit.class,
                                        parentColumns = "id",
                                        childColumns = "habitID",
                                        onDelete = CASCADE ),
            indices = { @Index( "habitID" ) } )
public class HabitDay {

    @PrimaryKey(autoGenerate = true)
    private int dayID;
    private int habitID;

    private String date;
    private int plusCount;
    private int minusCount;

    @Ignore
    private Habit habit;


    public HabitDay() {}

    public HabitDay( Habit habit, LocalDate date ) {
        this.habit = habit;
        this.habitID = habit.getId();
        this.date = date.toString();
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

    @Nullable
    public Habit getHabit() {
        return habit;
    }

    public int getMinusCount() {

        return minusCount;
    }

    public int getPlusCount() {

        return plusCount;
    }

    public String getDate() {

        return date;
    }


    public void setDayID( int dayID ) {
        this.dayID = dayID;
    }

    public void setDate( String date ) {

        this.date = date;
    }

    public void setHabitID(int habitID) {
        this.habitID = habitID;
    }

    public void setHabit(Habit habit) {
        this.habit = habit;
    }

    public void setPlusCount(int plusCount ) {
        this.plusCount = plusCount;
    }

    public void setMinusCount( int minusCount ) {
        this.minusCount = minusCount;
    }

    public String getTitle() {
        if ( habit == null ) {
            return "loading";
        }
        else {
            return habit.getTitle();
        }
    }



    @TypeConverter
    public String dateToString(LocalDate localDate) {
         return localDate.toString();
    }


}

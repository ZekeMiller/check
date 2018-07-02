package zekem.check.habits;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
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
    private int id;
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
//        this.habitID = habit.getId();
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


    public int getId() {
        return id;
    }

    public int getHabitID() {
        return habitID;
    }

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


    public void setId(int id) {
        this.id = id;
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
            return "placeholder text";
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

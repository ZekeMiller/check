package zekem.check.habits;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;

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

    @PrimaryKey ( autoGenerate = true )
    private int id;

    private int habitID;
    private String date = LocalDate.now().toString();

    private int plusCount = 0;
    private int minusCount = 0;

    public HabitDay() {
    }

    public HabitDay( int habitID, LocalDate date ) {
        this.habitID = habitID;
        this.date = date.toString();
    }



    public void incrementPlus() {
        plusCount++;
    }

    public void incrementMinus() {
        minusCount++;
    }


    public void setId( int id) {
        this.id = id;
    }

    public void setDate( String date ) {

        this.date = date;
    }

    public void setHabitID(int habitID) {
        this.habitID = habitID;
    }

    public void setPlusCount( int plusCount ) {
        this.plusCount = plusCount;
    }

    public void setMinusCount( int minusCount ) {
        this.minusCount = minusCount;
    }


    public int getId() {
        return id;
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

    public String getDate() {

        return date;
    }


    @TypeConverter
    public String dateToString(LocalDate localDate) {
         return localDate.toString();
    }


}

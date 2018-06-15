package zekem.check.habits;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

/**
 * @author Zeke Miller
 */
@Entity
public class HabitDay {

    @NonNull
    @PrimaryKey
    private String date = LocalDate.now().toString();

    private int plusCount = 0;
    private int minusCount = 0;

    public void incrementPlus() {
        plusCount++;
    }

    public void incrementMinus() {
        minusCount++;
    }

    public void setPlusCount( int plusCount ) {
        this.plusCount = plusCount;
    }

    public void setMinusCount( int minusCount ) {
        this.minusCount = minusCount;
    }

    public void setDate( @NonNull String date ) {

        this.date = date;
    }

    public int getMinusCount() {

        return minusCount;
    }

    public int getPlusCount() {

        return plusCount;
    }

    @NonNull
    public String getDate() {

        return date;
    }


    @TypeConverter
    public String dateToTimestamp(LocalDate localDate) {
        return date.toString();
    }


}

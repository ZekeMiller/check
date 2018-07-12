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
                                        parentColumns = "mId",
                                        childColumns = "mHabitId",
                                        onDelete = CASCADE ),
            indices = { @Index( "mHabitId" ) } )
@TypeConverters( DateConverters.class )
public class HabitDay {

    // these fields should never change after being created
    @PrimaryKey(autoGenerate = true)
    private int mDayId;
    private int mHabitId;
    private LocalDate mDate;

    // these fields can change
    private int mPlusCount;
    private int mMinusCount;



    public HabitDay() {}

    public HabitDay( Habit habit, LocalDate date ) {
        this.mHabitId = habit.getId();
        this.mDate = date;
        mPlusCount = 0;
        mMinusCount = 0;
    }



    public void incrementPlus() {
        mPlusCount++;
    }

    public void incrementMinus() {
        mMinusCount++;
    }


    public int getDayId() {
        return mDayId;
    }

    public int getHabitId() {
        return mHabitId;
    }

    public LocalDate getDate() {
        return mDate;
    }

    public int getMinusCount() {
        return mMinusCount;
    }

    public int getPlusCount() {
        return mPlusCount;
    }



    public void setDayId( int dayID ) {
        this.mDayId = dayID;
    }

    public void setHabitId( int habitID) {
        this.mHabitId = habitID;
    }

    public void setDate( LocalDate date ) {
        this.mDate = date;
    }

    public void setPlusCount(int plusCount ) {
        this.mPlusCount = plusCount;
    }

    public void setMinusCount( int minusCount ) {
        this.mMinusCount = minusCount;
    }



    public boolean sameContents( HabitDay other ) {
        return
//                this.equals( other ) &&
//                this.mHabitId == other.mHabitId &&
                this.mPlusCount == other.mPlusCount &&
                this.mMinusCount == other.mMinusCount;
    }

    public boolean sameId( HabitDay other ) {
        return this.mDayId == other.mDayId;
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

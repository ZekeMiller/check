package zekem.check.habits;

import android.arch.persistence.room.TypeConverters;

import org.joda.time.LocalDate;

import zekem.check.habits.database.Converters;

/**
 * @author Zeke Miller
 */
@TypeConverters( Converters.class )
public class HabitDay {

    // these fields should never change after being created
    private int mHabitId;
    private LocalDate mDate;

    // these fields can change
    private int mPlusCount;
    private int mMinusCount;



    public HabitDay( int habitId, LocalDate date ) {

        this.mHabitId = habitId;
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
        return this.equals( other )
                && this.mPlusCount == other.mPlusCount
                && this.mMinusCount == other.mMinusCount;
    }

    @Override
    public boolean equals( Object other ) {
        if ( other != null && other instanceof HabitDay ) {
            HabitDay otherDay = (HabitDay) other;
            return this.mHabitId == otherDay.mHabitId
                    && this.mDate == otherDay.mDate;
        }
        return false;
    }


}

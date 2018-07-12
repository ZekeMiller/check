package zekem.check.habits;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import org.joda.time.LocalDate;

import zekem.check.habits.database.DateConverters;

// TODO order by column

/**
 * @author Zeke Miller
 */
@Entity
@TypeConverters( DateConverters.class )
public class Habit {

    @PrimaryKey(autoGenerate = true)
    private int mId;

    private LocalDate mCreatedDate;

    private String mTitle;

    private int mTotalPlus;
    private int mTotalMinus;

//    private int mMinusStreak;
//    private int mPlusStreak;
//
//    private LocalDate mLastMinusDate;
//    private LocalDate mLastPlusDate;

    private boolean mMinusActive;
    private boolean mPlusActive;


    public Habit(String title, boolean minusActive, boolean plusActive ) {

        this.mTitle = title;

        this.mMinusActive = minusActive;
        this.mPlusActive = plusActive;

        mCreatedDate = LocalDate.now();

        mTotalMinus = 0;
        mTotalPlus = 0;

//        mMinusStreak = 0;
//        mPlusStreak = 0;

    }



    public int getId() {
        return mId;
    }

    public LocalDate getCreatedDate() {
        return mCreatedDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getTotalMinus() {
        return mTotalMinus;
    }

    public int getTotalPlus() {
        return mTotalPlus;
    }

//    public int getMinusStreak() {
//        return mMinusStreak;
//    }
//
//    public int getPlusStreak() {
//        return mPlusStreak;
//    }
//
//    public LocalDate getLastMinusDate() {
//        return mLastMinusDate;
//    }
//
//    public LocalDate getLastPlusDate() {
//        return mLastPlusDate;
//    }

    public boolean isMinusActive() {
        return mMinusActive;
    }

    public boolean isPlusActive() {
        return mPlusActive;
    }



    public void setId(int id ) {
        this.mId = id;
    }

    public void setCreatedDate( LocalDate createdDate ) {
        this.mCreatedDate = createdDate;
    }

    public void setTitle( String title ) {
        this.mTitle = title;
    }

    public void setTotalMinus(int totalMinus) {
        this.mTotalMinus = totalMinus;
    }

    public void setTotalPlus(int totalPlus) {
        this.mTotalPlus = totalPlus;
    }

//    public void setMinusStreak( int minusStreak ) {
//        this.mMinusStreak = minusStreak;
//    }
//
//    public void setPlusStreak( int plusStreak ) {
//        this.mPlusStreak = plusStreak;
//    }
//
//    public void setLastMinusDate( LocalDate lastMinusDate ) {
//        this.mLastMinusDate = lastMinusDate;
//    }
//
//    public void setLastPlusDate( LocalDate lastPlusDate ) {
//        this.mLastPlusDate = lastPlusDate;
//    }

    public void setMinusActive( boolean minusActive ) {
        this.mMinusActive = minusActive;
    }

    public void setPlusActive( boolean plusActive ) {
        this.mPlusActive = plusActive;
    }

    public void increment() {
        mTotalPlus++;
    }

    public void decrement() {
        mTotalMinus++;
    }


    public boolean sameContents( Habit habit ) {
        return this.equals( habit ) && this.mTitle.equals( habit.mTitle ) &&
                this.mTotalMinus == habit.mTotalMinus &&
                this.mTotalPlus == habit.mTotalPlus;
    }

    @Override
    public boolean equals( Object other ) {
        if ( other instanceof Habit ) {
            Habit otherHabit = (Habit) other;
            return this.mId == otherHabit.mId;
        }
        return false;
    }

}

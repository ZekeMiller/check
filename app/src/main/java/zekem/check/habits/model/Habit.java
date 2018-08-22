package zekem.check.habits.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zekem.check.habits.model.database.HabitConverters;

// TODO order by column

/**
 * A class to represent a Habit and all the data that has been provided for it (each day)
 *
 * @author Zeke Miller
 */
@Entity
@TypeConverters( HabitConverters.class )
public class Habit {

    // static variables
    @Ignore private static final int THRESHOLD_MINUS_THREE = -20;
    @Ignore private static final int THRESHOLD_MINUS_TWO = -10;
    @Ignore private static final int THRESHOLD_MINUS_ONE = -2;
    @Ignore private static final int THRESHOLD_PLUS_ONE = 1;
    @Ignore private static final int THRESHOLD_PLUS_TWO = 8;
    @Ignore private static final int THRESHOLD_PLUS_THREE = 15;
    @Ignore private static final int BAD_VALUE = Integer.MAX_VALUE;

    // constant fields (can't label final due to room, but these should never change, ever)
    @PrimaryKey(autoGenerate = true)
    private int mId;
    private LocalDate mCreatedDate;
    private boolean mMinusActive;
    private boolean mPlusActive;


    // fields that can change
    private String mTitle;
    private Map< LocalDate, HabitDay> mDayMap;

    // sorting fields
    @Ignore private List< HabitDay > mHabitDays;

    @Ignore private Comparator< HabitDay > mDaySort;
//    @Ignore private Comparator< Habit > mHabitSort;
    // TODO habit sort should be somewhere else, probably HabitViewModel

    /**
     * basic constructor
     * @param title the Habit's name
     * @param minusActive if the minus Button is active
     * @param plusActive if the plus button is active
     */
    public Habit( String title, boolean minusActive, boolean plusActive ) {

        this.mTitle = title;
        this.mMinusActive = minusActive;
        this.mPlusActive = plusActive;

        mCreatedDate = LocalDate.now();
        mDayMap = new HashMap<>();
        mHabitDays = new ArrayList<>();

        // default daySort is just by date
        mDaySort = ( day1, day2 ) -> day1.getDate().compareTo( day2.getDate() );

        // default habitSort is just by id
//        mHabitSort = ( habit1, habit2 ) ->  Integer.compare( habit1.mId, habit2.mId );


    }



    // getters

    public int getId() {
        return mId;
    }

    public LocalDate getCreatedDate() {
        return mCreatedDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public boolean isMinusActive() {
        return mMinusActive;
    }

    public boolean isPlusActive() {
        return mPlusActive;
    }

    public Map< LocalDate, HabitDay > getDayMap() {
        return mDayMap;
    }

    public List< HabitDay > getHabitDays() {
        return mHabitDays;
    }


    // setters

    public void setId( int id ) {
        this.mId = id;
    }

    public void setCreatedDate( LocalDate createdDate ) {
        this.mCreatedDate = createdDate;
    }

    public void setTitle( String title ) {
        this.mTitle = title;
    }

    public void setMinusActive( boolean minusActive ) {
        this.mMinusActive = minusActive;
    }

    public void setPlusActive( boolean plusActive ) {
        this.mPlusActive = plusActive;
    }

    public void setDayMap( Map< LocalDate, HabitDay > dayMap ) {
        this.mDayMap = dayMap;
        updateList();
    }

//    public void setDaySort( Comparator<HabitDay> sort ) {
//        this.mDaySort = sort;
//    }


    // methods to modify and check more complex things

    /**
     * adds a day for the given date if one doesn't already exist and isn't before creation date
     * @param date the date to add a day for
     */
    public void addDay( @NonNull LocalDate date ) {
        // date doesn't exist and isn't before the created date
        if ( mDayMap.get( date ) == null && ! date.isBefore( mCreatedDate ) ) {
            mDayMap.put( date, new HabitDay( mId, date ) );
            updateList();
        }
    }

    /**
     * checks if a day exists already
     * @param date the date to check
     * @return bool if the day exists
     */
    public boolean hasDay( LocalDate date ) {
        return mDayMap.containsKey( date );
    }

    /**
     * returns the HabitDay instance for a given date
     * @param date the date to retrieve the HabitDay for
     * @return HabitDay associated with certain day
     */
    public HabitDay getDay( LocalDate date ) {
        return mDayMap.get( date );
    }

    /**
     * updates list, currently a stub until I implement custom sorts
     */
    private void updateList() {
        // does nothing current since we don't sort
        this.mHabitDays = new ArrayList<>( mDayMap.values() );
        Collections.sort( this.mHabitDays, mDaySort );
    }


    /**
     * minus button action for a HabitDay
     * @param date the date to hit minus for
     */
    public void minusDay( LocalDate date ) {
        HabitDay habitDay = mDayMap.get( date );
        if ( habitDay != null ) {
            habitDay.incrementMinus();
        }
    }

    /**
     * gets total minus count by adding them all up
     * @return int sum of minuses for days
     */
    public int getTotalMinus() {
        int count = 0;
        for ( HabitDay habitDay : mDayMap.values() ) {
            count += habitDay.getMinusCount();
        }
        return count;
    }

    /**
     * hits plus for a HabitDay associated with a given date
     * @param date the date to plus
     */
    public void plusDay( LocalDate date ) {
        HabitDay habitDay = mDayMap.get( date );
        if ( habitDay != null ) {
            habitDay.incrementPlus();
        }
    }

    /**
     * gets total plus count by adding them all up
     * @return int sum of pluses for days
     */
    public int getTotalPlus() {
        int count = 0;
        for ( HabitDay habitDay : mDayMap.values() ) {
            count += habitDay.getPlusCount();
        }
        return count;
    }


    /**
     * gets total grade
     * @return int overall grade
     */
    public int getGrade() {
        return getGrade( getTotalPlus(), getTotalMinus() );
    }

    /**
     * gets grade for a specific day or BAD_VALUE for nonexistent date
     * @param date the date to get grade for
     * @return int grade
     */
    public int getGrade( LocalDate date ) {
        if ( date != null && mDayMap.get( date ) != null ) {
            return getGrade( mDayMap.get( date ) );
        }
        else {
            return BAD_VALUE;
        }
    }

    /**
     * gets grade for specific date, except from a HabitDay instance instead of date
     * @param habitDay the day to grade
     * @return int the grade
     */
    public int getGrade( @NonNull HabitDay habitDay ) {
        return getGrade( habitDay.getPlusCount(), habitDay.getMinusCount() );
    }


    // TODO maybe replace with ratios so day grading is fairer?
    /**
     * gets the grade from a plus value and minus value
     * @param plus the plus count to grade
     * @param minus the minus count to grade
     * @return int grade
     */
    @SuppressWarnings( "ConstantConditions" )
    private int getGrade( int plus, int minus) {
        int value = plus - minus;

        if ( value > 0 ) {
            if ( value >= THRESHOLD_PLUS_THREE ) {
                return 3;
            }
            else if ( value >= THRESHOLD_PLUS_TWO ) {
                return 2;
            }
            else if ( value >= THRESHOLD_PLUS_ONE ) {
                return 1;
            }
        }
        else if ( value < 0 ) {
            if ( value <= THRESHOLD_MINUS_THREE ) {
                return -3;
            }
            else if ( value <= THRESHOLD_MINUS_TWO ) {
                return -2;
            }
            else if ( value <= THRESHOLD_MINUS_ONE ) {
                return -1;
            }
        }
        return 0;
    }


    /**
     * calculates a minus streak.  Current day will add if it was minused, but a streak can be
     * had without the current day being minused.  Calculated every time to accommodate
     * retroactive changes
     * @return int the number of days of consecutive minuses
     */
    public int calculateMinusStreak() {
        HabitDay today = mDayMap.get( LocalDate.now() );
        int todayCount = ( today != null && today.getMinusCount() > 0 ) ? 1 : 0;
        for ( int i = 1 ; ; i++ ) {
            HabitDay day = mDayMap.get( LocalDate.now().minusDays( i ) );
            if ( day == null || day.getMinusCount() == 0 ) {
                return i + todayCount - 1;
            }
        }
    }


    /**
     * calculates a plus streak.  Current day will add if it was plussed, but a streak can be
     * had without the current day being plussed.  Calculated every time to accommodate
     * retroactive changes
     * @return int the number of days of consecutive minuses
     */
    public int calculatePlusStreak() {
        HabitDay today = mDayMap.get( LocalDate.now() );
        int todayCount = ( today != null && today.getPlusCount() > 0 ) ? 1 : 0;
        for ( int i = 1 ; ; i++ ) {
            HabitDay day = mDayMap.get( LocalDate.now().minusDays( i ) );
            if ( day == null || day.getPlusCount() == 0 ) {
                return i + todayCount - 1;
            }
        }
    }


    /**
     * checks if another habit has the same contents as this one (intended for use with DiffUtil)
     * @param habit the other habit to check
     * @return boolean if they have the same contents
     */
    public boolean sameContents( Habit habit ) {
        return this.equals( habit )
                && this.mTitle.equals( habit.mTitle )
                && this.mDayMap.equals( habit.mDayMap );
    }

    /**
     * simple equals method, true iff other is a Habit and the IDs are equal
     * @param other the object to compare
     * @return boolean if equal
     */
    @Override
    public boolean equals( Object other ) {
        if ( other != null && other instanceof Habit ) {
            Habit otherHabit = (Habit) other;
            return this.mId == otherHabit.mId;
        }
        return false;
    }

}

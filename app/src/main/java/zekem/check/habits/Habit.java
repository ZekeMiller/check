package zekem.check.habits;

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

import zekem.check.habits.database.Converters;

// TODO order by column

/**
 * @author Zeke Miller
 */
@Entity
@TypeConverters( Converters.class )
public class Habit {

    @Ignore private static final int THRESHOLD_MINUS_THREE = -20;
    @Ignore private static final int THRESHOLD_MINUS_TWO = -10;
    @Ignore private static final int THRESHOLD_MINUS_ONE = -2;
    @Ignore private static final int THRESHOLD_PLUS_ONE = 1;
    @Ignore private static final int THRESHOLD_PLUS_TWO = 8;
    @Ignore private static final int THRESHOLD_PLUS_THREE = 15;
    @Ignore private static final int BAD_VALUE = Integer.MAX_VALUE;

    @PrimaryKey(autoGenerate = true)
    private int mId;

    private LocalDate mCreatedDate;

    private String mTitle;

    private boolean mMinusActive;
    private boolean mPlusActive;

    private Map< LocalDate, HabitDay > mDayMap;
    @Ignore private List< HabitDay > mHabitDays;
    @Ignore private Comparator< HabitDay > sort;

    public Habit(String title, boolean minusActive, boolean plusActive ) {

        this.mTitle = title;

        this.mMinusActive = minusActive;
        this.mPlusActive = plusActive;

        mCreatedDate = LocalDate.now();
        mDayMap = new HashMap<>();
        mHabitDays = new ArrayList<>();
        sort = ( o1, o2 ) -> o1.getDate().compareTo( o2.getDate() );

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

    public void setSort( Comparator<HabitDay> sort ) {
        this.sort = sort;
    }


    public void addDay( LocalDate date ) {
        if ( mDayMap.get( date ) == null ) {
            mDayMap.put( date, new HabitDay( mId, date ) );
            updateList();
        }
    }

    public boolean hasDay( LocalDate date ) {
        return mDayMap.containsKey( date );
    }

    public HabitDay getDay( LocalDate date ) {
        return mDayMap.get( date );
    }

    private void updateList() {
        this.mHabitDays = new ArrayList<>( mDayMap.values() );
        Collections.sort( this.mHabitDays, sort );
    }

    public void minusDay( LocalDate date ) {
        HabitDay habitDay = mDayMap.get( date );
        if ( habitDay != null ) {
            habitDay.incrementMinus();
        }
    }

    public int getTotalMinus() {
        int count = 0;
        for ( HabitDay habitDay : mDayMap.values() ) {
            count += habitDay.getMinusCount();
        }
        return count;
    }


    public void plusDay( LocalDate date ) {
        HabitDay habitDay = mDayMap.get( date );
        if ( habitDay != null ) {
            habitDay.incrementPlus();
        }
    }

    public int getTotalPlus() {
        int count = 0;
        for ( HabitDay habitDay : mDayMap.values() ) {
            count += habitDay.getPlusCount();
        }
        return count;
    }

    public int getGrade() {
        return getGrade( getTotalPlus(), getTotalMinus() );
    }

    public int getGrade( @NonNull LocalDate date ) {
        if ( mDayMap.get( date ) != null ) {
            return getGrade( mDayMap.get( date ) );
        }
        else {
            return BAD_VALUE;
        }
    }

    public int getGrade( @NonNull HabitDay habitDay ) {

        return getGrade( habitDay.getPlusCount(), habitDay.getMinusCount() );
    }


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


    public boolean sameContents( Habit habit ) {
        return this.equals( habit ) && this.mTitle.equals( habit.mTitle );
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

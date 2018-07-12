package zekem.check.habits;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Relation;
import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import java.util.HashMap;
import java.util.List;

public class HabitWithDays {

    private static final int THRESHOLD_MINUS_THREE = -20;
    private static final int THRESHOLD_MINUS_TWO = -10;
    private static final int THRESHOLD_MINUS_ONE = -2;
    private static final int THRESHOLD_PLUS_ONE = 1;
    private static final int THRESHOLD_PLUS_TWO = 8;
    private static final int THRESHOLD_PLUS_THREE = 15;


    @Embedded
    private Habit mHabit;

    @Relation( parentColumn = "mId", entityColumn = "mHabitId" )
    private List< HabitDay > mHabitDays;

    @Ignore
    private HashMap< LocalDate, HabitDay > mHabitDaysDateMap = null;


    public Habit getHabit() {
        return mHabit;
    }


    public List<HabitDay> getHabitDays() {
        return mHabitDays;
    }


    public void setHabit(Habit habit) {
        this.mHabit = habit;
    }


    public void setHabitDays(List<HabitDay> habitDays) {
        this.mHabitDays = habitDays;
        mapUpdate();
    }

    private void mapUpdate() {

        if ( mHabitDaysDateMap == null || ( mHabitDaysDateMap.size() < mHabitDays.size() ) ) {
            mHabitDaysDateMap = new HashMap<>();
            for ( HabitDay habitDay : mHabitDays ) {
                mHabitDaysDateMap.put( habitDay.getDate(), habitDay );
            }
        }
    }


    @Nullable
    public HabitDay getForDate( LocalDate date ) {
        mapUpdate();
        return mHabitDaysDateMap.get( date );
    }


    public boolean sameHabitId( HabitWithDays other ) {
        return this.mHabit.getId() == other.mHabit.getId();
    }


    public int getValue() {
        int value = mHabit.getTotalPlus() - mHabit.getTotalMinus();

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
        HabitDay today = mHabitDaysDateMap.get( LocalDate.now() );
        int todayCount = ( today != null && today.getPlusCount() > 0 ) ? 1 : 0;
        for ( int i = 1 ; ; i++ ) {
            HabitDay day = mHabitDaysDateMap.get( LocalDate.now().minusDays( i ) );
            if ( day == null || day.getPlusCount() == 0 ) {
                return i + todayCount - 1;
            }
        }
    }

    public int calculateMinusStreak() {
        HabitDay today = mHabitDaysDateMap.get( LocalDate.now() );
        int todayCount = ( today != null && today.getMinusCount() > 0 ) ? 1 : 0;
        for ( int i = 1 ; ; i++ ) {
            HabitDay day = mHabitDaysDateMap.get( LocalDate.now().minusDays( i ) );
            if ( day == null || day.getMinusCount() == 0 ) {
                return i + todayCount - 1;
            }
        }
    }


    @Override
    public boolean equals( Object other ) {
        if ( other instanceof HabitWithDays ) {
            HabitWithDays otherWithDays = (HabitWithDays) other;
            return this.mHabit.equals( otherWithDays.mHabit) &&
                    this.mHabitDays.equals( otherWithDays.mHabitDays);
        }
        return false;
    }
}

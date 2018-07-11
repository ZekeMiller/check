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

    @Relation( parentColumn = "id", entityColumn = "habitID" )
    private List< HabitDay > mHabitDays;

    @Ignore
    private HashMap< LocalDate, HabitDay > habitDaysDateMap = null;


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
        habitDaysDateMap = new HashMap<>();
        for ( HabitDay habitDay : habitDays ) {
            habitDaysDateMap.put( habitDay.getDate(), habitDay );
        }
    }


    @Nullable
    public HabitDay getForDate( LocalDate date ) {
        if ( habitDaysDateMap == null || ( habitDaysDateMap.size() < mHabitDays.size() ) ) {
            setHabitDays(mHabitDays);
        }
        return habitDaysDateMap.get( date );
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

package zekem.check.habits;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Relation;
import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import java.util.HashMap;
import java.util.List;

public class HabitWithDays {

    @Embedded
    private Habit habit;

    @Relation( parentColumn = "id", entityColumn = "habitID" )
    private List< HabitDay > habitDays;

    @Ignore
    private HashMap< LocalDate, HabitDay > habitDaysDateMap = null;


    public Habit getHabit() {
        return habit;
    }

    public List<HabitDay> getHabitDays() {
        return habitDays;
    }

    public void setHabit(Habit habit) {
        this.habit = habit;
    }

    public void setHabitDays(List<HabitDay> habitDays) {
        this.habitDays = habitDays;
        habitDaysDateMap = new HashMap<>();
        for ( HabitDay habitDay : habitDays ) {
            habitDaysDateMap.put( habitDay.getDate(), habitDay );
        }
    }

    @Nullable
    public HabitDay getForDate( LocalDate date ) {
        if ( habitDaysDateMap == null || ( habitDaysDateMap.size() < habitDays.size() ) ) {
            setHabitDays( habitDays );
        }
        return habitDaysDateMap.get( date );
    }

    public void addDate( LocalDate date, HabitDay habitDay ) {
        habitDaysDateMap.put( date, habitDay );
    }

    public boolean sameHabitId( HabitWithDays other ) {
        return this.habit.getId() == other.habit.getId();
    }

    @Override
    public boolean equals( Object other ) {
        if ( other instanceof HabitWithDays ) {
            HabitWithDays otherWithDays = (HabitWithDays) other;
            return this.habit.equals( otherWithDays.habit ) &&
                    this.habitDays.equals( otherWithDays.habitDays );
        }
        return false;
    }
}

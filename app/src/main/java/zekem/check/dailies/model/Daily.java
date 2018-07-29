package zekem.check.dailies.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import org.joda.time.LocalDate;

import java.util.Map;

import zekem.check.dailies.model.scheduling.Scheduler;
import zekem.check.habits.model.database.HabitConverters;

/**
 * @author Zeke Miller
 */
@Entity
@TypeConverters( HabitConverters.class )
public class Daily {

    @PrimaryKey ( autoGenerate = true )
    private int mId;

    private String mTitle;
    private Scheduler mScheduler;
    private Map< LocalDate, Boolean > mDays;


    
    public Daily( String title ) {
        this.mTitle = title;
    }


    // getters

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public Scheduler getScheduler() {
        return mScheduler;
    }

    public Map< LocalDate, Boolean > getDays() {
        return mDays;
    }


    // setters

    public void setId( int id ) {
        this.mId = id;
    }

    public void setTitle( String title ) {
        this.mTitle = title;
    }

    public void setScheduler( Scheduler scheduler ) {
        this.mScheduler = scheduler;
    }

    public void setDays( Map< LocalDate, Boolean > days ) {
        this.mDays = days;
    }

}

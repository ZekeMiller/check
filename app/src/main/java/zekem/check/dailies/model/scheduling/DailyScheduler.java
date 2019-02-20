package zekem.check.dailies.model.scheduling;

import org.joda.time.LocalDate;

/**
 * @author Zeke Miller
 */
public class DailyScheduler extends Scheduler {

    private LocalDate mLastCompleted;

    public DailyScheduler() {
        mLastCompleted = LocalDate.now();
    }

    @Override
    public boolean isDue( LocalDate date ) {
        return mLastCompleted == null || mLastCompleted.isBefore( date );
    }

    @Override
    public void completeDay( LocalDate date ) {
        if ( date != null && ( mLastCompleted == null || mLastCompleted.isBefore( date ) ) ) {
            mLastCompleted = date;
        }
    }

    @Override
    public void uncompleteDay( LocalDate date ) {
        // TODO figure out how to handle this well (stack of dates? scan backwards?)
    }

    @Override
    public String toString() {
        if ( mLastCompleted == null ) {
            return "DailyScheduler no date";
        }
        return String.format( "DailyScheduler %s", mLastCompleted.toString() );
    }
}

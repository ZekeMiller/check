package zekem.check.dailies.model.scheduling;

import org.joda.time.LocalDate;

/**
 * Generic Scheduler interface, all we need to do is ask if it's ready for a given day
 *
 * @author Zeke Miller
 */
public abstract class Scheduler {

    // TODO make a way to store this in Room (possibly abstract, might want a static builder for
    // rebuilding, need to figure out all the options I have and what fields are needed

    protected String type = getClass().getName();

    /**
     * checks if the scheduler is due on a certain date
     * @param date the date to check on (probably today but could be for past-checking)
     * @return whether or not the task this scheduler checks should be marked due on the date
     */
    abstract boolean isDue( LocalDate date );

    /**
     * marks this scheduler as completed for a given day
     * @param date the date the scheduler should mark as complete
     */
    abstract void completeDay( LocalDate date );

    /**
     * marks this scheduler as incomplete for a given day (an undo function for an accidental press)
     * @param date the date to 'uncomplete'
     */
    abstract void uncompleteDay( LocalDate date );

}

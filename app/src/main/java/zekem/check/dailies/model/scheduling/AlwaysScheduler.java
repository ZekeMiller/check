package zekem.check.dailies.model.scheduling;

import org.joda.time.LocalDate;

/**
 * scheduled for as often as possible
 *
 * @author Zeke Miller
 */
public class AlwaysScheduler extends Scheduler {

    public AlwaysScheduler() {}

    @Override
    public boolean isDue( LocalDate date ) {
        return true;
    }

    @Override
    public void completeDay( LocalDate date ) {
        // Never changes scheduling
    }

    @Override
    public void uncompleteDay( LocalDate date ) {
        // Never changes scheduling
    }

    @Override
    public String toString() {
        return "AlwaysSched";
    }
}

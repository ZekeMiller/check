package zekem.check.dailies.model.scheduling;

import org.joda.time.LocalDate;

/**
 * scheduled for as often as possible
 *
 * @author Zeke Miller
 */
public class AlwaysScheduler implements Scheduler {

    public AlwaysScheduler() {}

    @Override
    public boolean isReady( LocalDate date ) {
        return true;
    }

}

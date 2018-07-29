package zekem.check.dailies.model.scheduling;

import org.joda.time.LocalDate;

/**
 * Generic Scheduler interface, all we need to do is ask if it's ready for a given day
 *
 * @author Zeke Miller
 */
public interface Scheduler {

    boolean isReady( LocalDate date );

}

package nandcat.model;

/**
 * This class represents a listener on the Clock. It notifies all implementing classes
 * about changes in the Clock.
 */
public interface ClockListener {

    /**
     * Called on implementing classes if clock cycle changed.
     * @param clock Clock caused the tick.
     */
    void clockTicked(Clock clock);
}

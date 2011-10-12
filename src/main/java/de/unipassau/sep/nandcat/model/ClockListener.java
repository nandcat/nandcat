package de.unipassau.sep.nandcat.model;

/**
 * This class represents a listener on the Clock. It notifies all implementing classes
 * about changes in the Clock.
 * 
 * @version 0.1
 * 
 */
public interface ClockListener {

    /**
     * {@inheritDoc}
     */
    void clockTicked(Clock clock); // TODO clock wird an setState() von Port, dann von Connection und dann Port
                                   // TODO weitergereicht.
}

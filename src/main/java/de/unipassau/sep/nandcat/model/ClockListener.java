package de.unipassau.sep.nandcat.model;

/**
 * Clock listener.
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

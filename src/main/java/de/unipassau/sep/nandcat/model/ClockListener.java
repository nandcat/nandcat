package de.unipassau.sep.nandcat.model;

/**
 * Clock listener.
 * 
 * @version 0.1
 * 
 */
public interface ClockListener {

    void clockTicked(Clock clock); // clock wird an setState() von Port, dann von Connection und dann Port
                                   // weitergereicht.
}

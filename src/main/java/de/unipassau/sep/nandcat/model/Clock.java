package de.unipassau.sep.nandcat.model;

import java.util.Set;

/**
 * Clock.
 * 
 * @version 0.1
 */
public class Clock {
    private int cycle;

    private Set<ClockListener> listeners;

    public void simulate() {

    }

    public void addListener(ClockListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ClockListener listener) {
        listeners.remove(listener);
    }

}

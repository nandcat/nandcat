package de.unipassau.sep.nandcat.model.element;

import java.util.Set;

import de.unipassau.sep.nandcat.model.Clock;
import de.unipassau.sep.nandcat.model.ClockListener;

/**
 * Circuit.
 * 
 * @version 0.1
 * 
 */
public class Circuit implements ClockListener, Element {

    // TODO private Model model; ? For have reference to clock.
    // unnecessary if Clock is singleton.

    public Set<Element> getStartingElements() {
        return null;
    }

    public void setName(String name) {
        // TODO Auto-generated method stub

    }

    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    public void clockTicked(Clock clock) {
        // TODO Auto-generated method stub

    }

    // Circuit contains more elements than module
    // Is called from the clock - only once.
    public boolean calculate() {
        return false;
    }
}

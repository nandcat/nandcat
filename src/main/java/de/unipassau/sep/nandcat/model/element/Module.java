package de.unipassau.sep.nandcat.model.element;

import java.util.Set;

import de.unipassau.sep.nandcat.model.ClockListener;

/**
 * Module.
 * 
 * @version 0.1
 * 
 */
public interface Module extends ClockListener, Element {
    /**
     * Calculates the result.
     * 
     * @return True iff calculation was successful.
     */
    boolean getResult();

    /**
     * Gets incoming ports.
     * 
     * @return incoming ports.
     */
    Set<Port> getInPorts();

    /**
     * Gets outgoing ports.
     * 
     * @return outgoing ports.
     */
    Set<Port> getOutPorts();
}

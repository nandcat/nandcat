package de.unipassau.sep.nandcat.model.check;

import de.unipassau.sep.nandcat.model.element.Circuit;

/**
 * Circuit check.
 * 
 * @version 0.1
 * 
 */
public interface CircuitCheck {
    /**
     * 
     * @return
     */
    boolean isActive();

    /**
     * 
     * @param active
     * @return
     */
    boolean setActive(boolean active);

    /**
     * 
     * @param circuit
     * @return
     */
    boolean test(Circuit circuit);

    /**
     * 
     * @param l
     */
    void addListener(CheckListener l);

    /**
     * 
     * @param l
     */
    void removeListener(CheckListener l);
}

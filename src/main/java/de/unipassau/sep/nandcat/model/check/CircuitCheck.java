package de.unipassau.sep.nandcat.model.check;

import de.unipassau.sep.nandcat.model.element.Circuit;

/**
 * Circuit check interface.
 * 
 * All checks implement this interface. An implementing check is able to check the circuit for errors.
 * 
 * @version 0.1
 */
public interface CircuitCheck {

    /**
     * Gets the state of the check inside the model.
     * @return True iff check is active.
     */
    boolean isActive();

    /**
     * Sets the checks state.
     * @param active True to set check active
     * @return True iff activation was successful.
     */
    boolean setActive(boolean active);

    /**
     * Tests the given circuit.
     * @param circuit Circuit to check.
     * @return True iff check was successful.
     */
    boolean test(Circuit circuit);

    /**
     * Adds a listener to observe the check.
     * @param l Listener to inform.
     */
    void addListener(CheckListener l);

    /**
     * Removes a given listener from the check.
     * @param l Listener to remove.
     */
    void removeListener(CheckListener l);
}

package nandcat.model.check;

import java.util.LinkedHashSet;
import java.util.Set;
import nandcat.model.check.CheckEvent.State;
import nandcat.model.element.Circuit;
import nandcat.model.element.Connection;
import nandcat.model.element.Element;

/**
 * IllegalConnectionCheck.
 * 
 * Checks if all connections only connect in-Ports with out-Ports.
 */
public class IllegalConnectionCheck implements CircuitCheck {

    /**
     * Listeners for this check.
     */
    Set<CheckListener> listener;

    /**
     * Check is active or not.
     */
    private boolean active;

    /**
     * Constructor for IllegalConnectionCheck. By default the check is active.
     */
    public IllegalConnectionCheck() {
        listener = new LinkedHashSet<CheckListener>();
        active = true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isActive() {
        return active;
    }

    /**
     * {@inheritDoc}
     */
    public boolean setActive(boolean active) {
        return this.active = active;
    }

    /**
     * {@inheritDoc}
     */
    public boolean test(Circuit circuit) {
        Set<Element> elements = new LinkedHashSet<Element>();
        informListeners(State.RUNNING, elements);
        for (Connection c : circuit.getConnections()) {
            if (c.getInPort() == null) {
                elements.add(c);
                informListeners(State.FAILED, elements);
                return false;
            } else if (c.getOutPort() == null) {
                elements.add(c);
                informListeners(State.FAILED, elements);
                return false;
            } else if (c.getInPort().getModule() == null) {
                elements.add(c);
                informListeners(State.FAILED, elements);
                return false;
            } else if (c.getOutPort().getModule() == null) {
                elements.add(c);
                informListeners(State.FAILED, elements);
                return false;
            } else if (c.getInPort().isOutPort() && c.getOutPort().isOutPort()) {
                elements.add(c);
                informListeners(State.FAILED, elements);
                return false;
            } else if (!c.getInPort().isOutPort() && !c.getOutPort().isOutPort()) {
                elements.add(c);
                informListeners(State.FAILED, elements);
                return false;
            }
        }
        informListeners(State.SUCCEEDED, elements);
        return true;
    }

    /**
     * Notifies the Classes implementing the CheckListener interface about a change in this Check.
     * 
     * @param state
     *            State of the check.
     * @param elements
     *            Elements causing a fail in the check. Empty if the check started or succeeded.
     */
    private void informListeners(State state, Set<Element> elements) {
        // Event informing listeners that check has started.
        CheckEvent e = new CheckEvent(State.RUNNING, elements, this);
        for (CheckListener l : listener) {
            l.checkChanged(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void addListener(CheckListener l) {
        listener.add(l);
    }

    /**
     * {@inheritDoc}
     */
    public void removeListener(CheckListener l) {
        listener.remove(l);
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return "Prüft ob ausgehende Ports nur mit eingehenden Ports verbunden sind und umgekehrt";
    }
}

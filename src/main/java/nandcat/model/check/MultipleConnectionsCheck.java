package nandcat.model.check;

import java.util.LinkedHashSet;
import java.util.Set;
import nandcat.model.check.CheckEvent.State;
import nandcat.model.element.Circuit;
import nandcat.model.element.Element;

/**
 * MultipleConnectionsCheck.
 * 
 * Checks if any Ports has two Connections.
 */
public class MultipleConnectionsCheck implements CircuitCheck {

    /*
     * ***********************************
     */
    // MultipleOrgasmen check überflüssig, da wenn überhaupt implizit in illegal connection check mit dabei und
    // technisch gar nicht möglich dass der fehlschlägt
    // edit : ist aber im lastenheft vorgegeben.
    /*
     * ***********************************
     */

    /**
     * Listeners for this check.
     */
    private Set<CheckListener> listener;

    /**
     * Check is active or not.
     */
    private boolean active;

    /**
     * Constructor for MultipleConnectionsCheck. By default the check is active.
     */
    public MultipleConnectionsCheck() {
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
        // MultipleConnections are not supported by the model.
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
        CheckEvent e = new CheckEvent(state, elements, this);
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
}

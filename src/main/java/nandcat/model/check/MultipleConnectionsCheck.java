package nandcat.model.check;

import java.util.LinkedHashSet;
import java.util.Set;
import nandcat.model.element.Circuit;

/**
 * MultipleConnectionsCheck.
 * 
 * Checks if any Ports has two Connections.
 */
public class MultipleConnectionsCheck implements CircuitCheck {

    /*
     * ***********************************
     */
    // TODO MultipleOrgasmen check überflüssig, da wenn überhaupt implizit in illegal connection check mit dabei und
    // technisch gar nicht möglich dass der fehlschlägt
    /*
     * ***********************************
     */

    /**
     * Listeners for this check.
     */
    Set<CheckListener> listener;

    /**
     * Check is active or not.
     */
    boolean active;

    public MultipleConnectionsCheck() {
        listener = new LinkedHashSet<CheckListener>();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isActive() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean setActive(boolean active) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean test(Circuit circuit) {
        // TODO Auto-generated method stub
        return false;
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

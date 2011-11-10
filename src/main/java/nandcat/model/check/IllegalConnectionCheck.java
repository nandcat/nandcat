package nandcat.model.check;

import java.util.Set;
import nandcat.model.element.Circuit;
import nandcat.model.element.Connection;

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
    boolean active;

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
        //TODO
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean test(Circuit circuit) {
        for (Connection c : circuit.getConnections()) {
            if (c.getInPort() == null) {
                return false;
            } else if (c.getOutPort() == null) {
                return false;
            } else if (c.getInPort().getModule() == null) {
                return false;
            } else if (c.getOutPort().getModule() == null) {
                return false;
            } else if (c.getInPort().isOutPort() && c.getOutPort().isOutPort()) {
                return false;
            } else if (!c.getInPort().isOutPort() && !c.getOutPort().isOutPort() ) {
                return false;
            }
        }
        return true;
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

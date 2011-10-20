package nandcat.model.check;

import nandcat.model.element.Circuit;

/**
 * IllegalConnectionCheck.
 * 
 * Checks if all connections only connect in-Ports with out-Ports.
 */
public class IllegalConnectionCheck implements CircuitCheck {

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
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     */
    public void removeListener(CheckListener l) {
        // TODO Auto-generated method stub
    }
}

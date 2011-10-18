package de.unipassau.sep.nandcat.model.check;

import de.unipassau.sep.nandcat.model.element.Circuit;

/**
 * Sourcecheck.
 * 
 * Checks if all elements are (in)directly connected to a source.
 * @version 0.1
 * 
 */
public class SourceCheck implements CircuitCheck {

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

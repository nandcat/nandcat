package nandcat.model.check;

import java.util.Set;
import nandcat.model.element.Circuit;

/**
 * CountCheck.
 * 
 * Checks if a given amount of elements exceeded, which could result in performance issues.
 */
public class CountCheck implements CircuitCheck {
    
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

package nandcat.model.check;

import java.util.Set;
import nandcat.model.element.Circuit;
import nandcat.model.element.Module;
import nandcat.model.element.Port;

/**
 * OrphanCheck.
 * 
 * Checks if elements are without connection to other elements.
 */
public class OrphanCheck implements CircuitCheck {
    
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
        // TODO missing a small snippet of (correct!) code!
        for (Module m : circuit.getModules()) {
            for (Port p : m.getInPorts()) {
                if (p.getConnection().getNextModule() != null) {
                    return false;
                }
            }
            for (Port p : m.getOutPorts()) {
                if (p.getConnection().getNextModule() != null) {
                    return false;
                }
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

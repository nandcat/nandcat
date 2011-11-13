package nandcat.model.check;

import java.util.HashSet;
import java.util.Set;
import nandcat.model.element.Circuit;
import nandcat.model.element.Module;
import nandcat.model.element.Port;

/**
 * OrphanCheck.
 * 
 * Checks if elements are without connection to other elements.
 * 
 * @version 4
 */
public class OrphanCheck implements CircuitCheck {

    /**
     * Listeners for this check.
     */
    private Set<CheckListener> listener;

    /**
     * Check is active or not.
     */
    private boolean active;

    public OrphanCheck() {
        listener = new HashSet<CheckListener>();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isActive() {
        // TODO Auto-generated method stub
        return active;
    }

    /**
     * {@inheritDoc}
     */
    public boolean setActive(boolean active) {
        // TODO Auto-generated method stub
        return this.active = active;
    }

    /**
     * {@inheritDoc}
     */
    public boolean test(Circuit circuit) {
        for (Module m : circuit.getModules()) {
            boolean current = false;
            for (Port p : m.getInPorts()) {
                if (p.getConnection().getNextModule() != null) {
                    current = true;
                }
            }
            for (Port p : m.getOutPorts()) {
                if (p.getConnection().getNextModule() != null) {
                    current = true;
                }
            }
            if (!current) {
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
    
    public String toString() {
        return "Test auf Verwaisung";
    }
}

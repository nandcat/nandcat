package nandcat.model.check;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import nandcat.model.check.CheckEvent.State;
import nandcat.model.element.Circuit;
import nandcat.model.element.Element;
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

    /**
     * Constructor for OrphanCheck. By default the check is active.
     */
    public OrphanCheck() {
        listener = new HashSet<CheckListener>();
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
        
        // Event informing listeners that check has started.
        CheckEvent e = new CheckEvent(State.RUNNING, elements, this);
        for (CheckListener l : listener) {
            l.checkChanged(e);
        }
        for (Module m : circuit.getModules()) {
            boolean current = false;
//            for (Port p : m.getInPorts()) {
//                if (p.getConnection().getNextModule() != null) {
//                    current = true;
//                }
//            }
//            for (Port p : m.getOutPorts()) {
//                if (p.getConnection().getNextModule() != null) {
//                    current = true;
//                }
//            }
            if (!current) {
                
                // If the test fails fire CheckEvent and add the module which caused the failure to the event.
                e = new CheckEvent(State.FAILED, elements, this);
                elements.add(m);
                for (CheckListener l : listener) {
                    l.checkChanged(e);
                }
                return false;
            }
        }
        
        // If everything went fine fire CheckEvent with empty set.
        e = new CheckEvent(State.SUCCEEDED, elements, this);
        for (CheckListener l : listener) {
            l.checkChanged(e);
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

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return "Test auf Verwaisung";
    }
}

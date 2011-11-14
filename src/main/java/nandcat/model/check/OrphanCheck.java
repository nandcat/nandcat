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
        
        informListeners(State.RUNNING, elements);
        for (Module m : circuit.getModules()) {
            boolean current = false;
            for (Port p : m.getInPorts()) {
                if (p.getConnection() != null) {
                    current = true;
                }
            }
            for (Port p : m.getOutPorts()) {
                if (p.getConnection() != null) {
                    current = true;
                }
            }
            if (!current) {
                elements.add(m);
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

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return "Test auf Verwaisung";
    }
}

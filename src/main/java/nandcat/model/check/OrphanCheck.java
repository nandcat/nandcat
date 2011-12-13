package nandcat.model.check;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
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
        this.active = active;
        return active;
    }

    /**
     * {@inheritDoc}
     */
    public boolean test(Circuit circuit) {
        Set<Element> elements = new LinkedHashSet<Element>();

        informListeners(State.RUNNING, elements);
        Set<Module> modules = circuit.getModules();
        Iterator<Module> iter = circuit.getModules().iterator();
        // Start with any Module you can get
        Module start = null;
        if (iter.hasNext()) {
            start = (Module) iter.next();
        }
        Queue<Module> queue = new LinkedList<Module>();
        // Remember if a Port has already been visited.
        Set<Port> visited = new HashSet<Port>();
        if (start != null) {
            queue.offer(start);
        }
        // Put all preceding and following Modules of the current Module into the queue. Mark the Ports of the
        // Modules as visited, and remove the Modules from the set of all Modules of the circuit.
        while (!queue.isEmpty()) {
            Module current = queue.poll();
            modules.remove(current);
            for (Port p : current.getInPorts()) {
                if (!visited.contains(p) && (p.getConnection() != null)) {
                    queue.add(p.getConnection().getPreviousModule());
                    visited.add(p);
                }
            }
            for (Port p : current.getOutPorts()) {
                if (!visited.contains(p) && (p.getConnection() != null)) {
                    queue.add(p.getConnection().getNextModule());
                    visited.add(p);
                }
            }
        }
        // If the set of all Modules of the circuit is empty it means every Module has been visited and is somehow
        // connected to the first one. Otherwise there are orphans.
        if (!modules.isEmpty()) {
            elements.addAll(modules);
            informListeners(State.FAILED, elements);
            return false;
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
}

package nandcat.model.check;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import nandcat.model.element.Circuit;
import nandcat.model.element.Module;
import nandcat.model.element.Port;

/**
 * FeedbackCheck.
 * 
 * Checks if there are cycles inside the circuit.
 */
public class FeedbackCheck implements CircuitCheck {

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
     * {@inheritDoc} <br>This Check will fail if it is not possible to run trhough a Queue of this circuit, where the first
     * Module adds the next modules, and visit every module only once
     */
    public boolean test(Circuit circuit) {
        HashSet<Module> visited = new HashSet<Module>();
        Queue<Module> q = new LinkedList<Module>();
        q.addAll(circuit.getStartingModules());
        while (!q.isEmpty()) {
            Module current = q.poll();
            if (visited.contains(current)) {
                return false;
            }
            visited.add(current);
            for (Port p : current.getOutPorts()) {
                if (p.getConnection() != null) {
                    q.add(p.getConnection().getNextModule());
                    
                }
            }
        }
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

package nandcat.model.check;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import nandcat.model.element.Circuit;
import nandcat.model.element.Module;
import nandcat.model.element.Port;

/**
 * SourceCheck.
 * 
 * Checks if all elements are (in)directly connected to a source.
 */
public class SourceCheck implements CircuitCheck {
    
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
        /*
         * idea is: start at starting elements. go with the flow and well, you should visit every module....
         */
        Set<Module> visited = new HashSet<Module>();
        Set<Module> all = new HashSet<Module>();
        Queue<Module> q = new LinkedList<Module>();
        q.addAll(circuit.getStartingModules());
        q.addAll(circuit.getModules());
        while (!q.isEmpty()) {
            Module current = q.poll();
            all.remove(current);

            /*
             * without visited set the amount of queue-loops might go up not only to over 9000, but straight through the roof to
             * infinity if there is a feedback. and this check should work even if feedback check fails, kthxbye.
             */
            if (!visited.contains(current)) {
                for (Port p : current.getOutPorts()) {
                    if (p.getConnection() != null) {
                        q.add(p.getConnection().getNextModule());
                    }
                }
            }
        }
        return all.isEmpty();
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

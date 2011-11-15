package nandcat.model.check;

import java.util.HashSet;
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
     * Constructor for SourceCheck. By default the check is active.
     */
    public SourceCheck() {
        listener = new LinkedHashSet<CheckListener>();
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
        Set<Element> elements = new HashSet<Element>();
        informListeners(State.RUNNING, elements);
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
             * without visited set the amount of queue-loops might go up not only to over 9000, but straight through the
             * roof to infinity if there is a feedback. and this check should work even if feedback check fails,
             * kthxbye.
             */
            if (!visited.contains(current)) {
                for (Port p : current.getOutPorts()) {
                    if (p.getConnection() != null) {
                        q.add(p.getConnection().getNextModule());
                    }
                }
            }
        }
        if (all.isEmpty()) {
            informListeners(State.SUCCEEDED, elements);
            return true;
        }
        elements.addAll(all);
        informListeners(State.FAILED, elements);
        return false;
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
        return "Prüfen ob alle Elemente indirekt mit einer Quelle verbunden sind.";
    }
}
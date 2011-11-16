package nandcat.model.check;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import nandcat.model.check.CheckEvent.State;
import nandcat.model.element.Circuit;
import nandcat.model.element.Element;
import nandcat.model.element.ImpulseGenerator;
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
    private boolean active;

    /**
     * Constructor for FeedbackCheck. By default its active.
     */
    public FeedbackCheck() {
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
     * {@inheritDoc} This Check will fail if it is not possible to run through a Queue of this circuit, where the first
     * Module adds the next modules, and visit every module only once
     */
    public boolean test(Circuit circuit) {
        Set<Element> elements = new LinkedHashSet<Element>();
        informListeners(State.RUNNING, elements);
        HashSet<Port> visited = new HashSet<Port>();
        Queue<Port> q = new LinkedList<Port>();

        for (Port port : getStartingPorts(circuit)) {
            q.add(port);
            visited.clear();
            while (!q.isEmpty()) {
                Port current = q.poll();
                if (visited.contains(current)) {

                    // If the test fails add the Module which caused the fail to the CheckEvent.
                    elements.add(current.getModule());
                    informListeners(State.FAILED, elements);
                    return false;
                }
                visited.add(current);

                /*
                 * When the port has a connection the outgoing ports of the following module are added to the queue. If
                 * there is no further connection an ending module of the circuit is reached. If there are still
                 * elements in the queue the algorithm starts from the beginning and the visited set has to be reset.
                 */
                if (current.getConnection() != null) {
                    q.addAll(current.getConnection().getOutPort().getModule().getOutPorts());
                } else {
                    // visited.clear();
                }
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
     * Returns the "first" Ports in this Circuit, meaning the ports from the "first" modules in the circuit.
     * 
     * @return List<Module> containing the starting Modules of this Circuit.
     */
    private List<Port> getStartingPorts(Circuit circuit) {
        List<Port> result = new LinkedList<Port>();
        for (Module m : circuit.getStartingModules()) {
            result.addAll(m.getOutPorts());
        }
        return result;
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

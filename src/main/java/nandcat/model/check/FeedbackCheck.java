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
        HashSet<Module> hasNeighbour = new HashSet<Module>();
        Queue<Module> q = new LinkedList<Module>();
        if (getStartModules(circuit).isEmpty() && !circuit.getModules().isEmpty()) {
            elements.addAll(circuit.getModules());
            informListeners(State.FAILED, elements);
            return false;
        } else {
            q.addAll(getStartModules(circuit));
        }

        while (!q.isEmpty()) {
            Module current = q.poll();
            if (hasNeighbour.contains(current)) {

                // If the test fails add the Module which caused the fail to the CheckEvent.
                elements.add(current);
                informListeners(State.FAILED, elements);
                return false;
            }

            for (Port p : current.getOutPorts()) {
                if (p.getConnection() != null) {
                    q.add(p.getConnection().getNextModule());
                    hasNeighbour.add(current);
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
     * Returns the "first" Modules in this Circuit. "First" modules are those which do not have any ingoing connection.
     * 
     * @return List<Module> containing the starting Modules of this Circuit.
     */
    private List<Module> getStartModules(Circuit circuit) {
        List<Module> result = new LinkedList<Module>();
        for (Element e : circuit.getElements()) {
            if (e instanceof Module) {
                boolean isStartModule = false;
                Module m = (Module) e;
                if (m instanceof ImpulseGenerator) {
                    isStartModule = true;
                } else {
                    isStartModule = true;
                    for (Port p : m.getInPorts()) {
                        if (p.getConnection() != null) {
                            isStartModule = false;
                        }
                    }
                }
                if (isStartModule) {
                    result.add(m);
                }
            }
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

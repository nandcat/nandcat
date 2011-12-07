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
import nandcat.model.element.Lamp;
import nandcat.model.element.Module;
import nandcat.model.element.Port;

/**
 * SourceCheck.
 * 
 * Checks if all elements are (in)directly connected to a source.
 */
public class SinkCheck implements CircuitCheck {

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
    public SinkCheck() {
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
        Queue<Module> q = new LinkedList<Module>();
        List<Module> startModules = getStartModules(circuit);
        if (!startModules.isEmpty()) {
            q.addAll(startModules);
        } else {
            for (Module m : circuit.getModules()) {
                if (m instanceof Lamp) {
                    informListeners(State.SUCCEEDED, elements);
                    return true;
                } else {
                    elements.addAll(circuit.getModules());
                    informListeners(State.FAILED, elements);
                    return false;
                }
            }
        }
        q.addAll(getStartModules(circuit));
        while (!q.isEmpty()) {
            Module current = q.poll();
            /*
             * without visited set the amount of queue-loops might go up not only to over 9000, but straight through the
             * roof to infinity if there is a feedback. and this check should work even if feedback check fails,
             * kthxbye.
             */
            if (!visited.contains(current)) {
                visited.add(current);
                boolean isLast = true;
                for (Port p : current.getOutPorts()) {
                    if (p.getConnection() != null) {
                        isLast = false;
                        q.add(p.getConnection().getNextModule());
                    }
                }

                // If the current module does not have a connection to a following module it represents a sink of
                // the circuit and therefore has to be a lamp.
                if (isLast) {
                    if (!(current instanceof Lamp)) {
                        elements.add(current);
                        informListeners(State.FAILED, elements);
                        return false;
                    }
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

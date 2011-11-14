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
        Set<Element> elements = new LinkedHashSet<Element>();
        informListeners(State.RUNNING, elements);

        Set<Module> visited = new HashSet<Module>();
        Set<Module> all = new HashSet<Module>();
        Queue<Module> q = new LinkedList<Module>();
        q.addAll(getEndingModules(circuit));
        while (!q.isEmpty()) {
            Module current = q.poll();
            all.remove(current);

            if (!visited.contains(current)) {
                boolean isLast = true;
                for (Port p : current.getInPorts()) {
                    if (p.getConnection() != null) {
                        isLast = false;
                        q.add(p.getConnection().getPreviousModule());
                    }
                }
                if (isLast) {
                    if (!(current instanceof ImpulseGenerator)) {
                        all.add(current);
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
     * Returns the "last" Modules in this Circuit.
     * 
     * @return List<Module> containing the starting Modules of this Circuit.
     */
    public List<Module> getEndingModules(Circuit circuit) {
        List<Module> result = new LinkedList<Module>();
        for (Element e : circuit.getElements()) {
            if (e instanceof Module) {
                boolean isEndingModule = false;
                Module m = (Module) e;
                if (m instanceof Lamp) {
                    isEndingModule = true;
                } else {
                    for (Port p : m.getOutPorts()) {
                        if (p.getConnection() == null) {
                            isEndingModule = true;
                            break;
                        }
                    }
                }
                if (isEndingModule) {
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

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return "Prüft ob alle Bausteine indirekt mit einem Taktgeber verbunden sind";
    }
}

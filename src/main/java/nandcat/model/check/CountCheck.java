package nandcat.model.check;

import java.util.LinkedHashSet;
import java.util.Set;
import nandcat.model.check.CheckEvent.State;
import nandcat.model.element.Circuit;
import nandcat.model.element.Element;

/**
 * CountCheck.
 * 
 * Checks if a given amount of elements exceeded, which could result in performance issues.
 */
public class CountCheck implements CircuitCheck {

    /**
     * Number of gates at which user will be warned.
     */
    private static final int THRESHOLD = 1000;

    /**
     * Listeners for this check.
     */
    private Set<CheckListener> listener;

    /**
     * Check is active or not.
     */
    private boolean active;

    /**
     * Constructor for CountCheck. By default the check is active.
     */
    public CountCheck() {
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

        // Event informing listeners that check has started.
        CheckEvent e = new CheckEvent(State.RUNNING, elements, this);
        for (CheckListener l : listener) {
            l.checkChanged(e);
        }
        if (countElems(circuit) > THRESHOLD) {
            e = new CheckEvent(State.FAILED, elements, this);
            for (CheckListener l : listener) {
                l.checkChanged(e);
            }
            return false;
        }
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
     * Counts all Elements (and thanks to recursion also all sub-elements) in this circuit.
     * 
     * @param c
     *            Circuit that needs to be checked
     * @return number of elements
     */
    private int countElems(Circuit c) {
        int zaehlchen = 0;
        for (Element e : c.getElements()) {
            if (e instanceof Circuit) {
                zaehlchen += countElems((Circuit) e);
            } else {
                zaehlchen++;
            }
        }
        return zaehlchen;
    }
}

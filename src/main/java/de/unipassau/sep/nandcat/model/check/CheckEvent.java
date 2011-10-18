package de.unipassau.sep.nandcat.model.check;

import java.util.Set;
import de.unipassau.sep.nandcat.model.element.Element;

/**
 * @(#) CheckEvent.java
 */
public class CheckEvent {

    /**
     * Possbile states of the check.
     */
    public enum state {
        RUNNING, SUCCEEDED, FAILED, UNKNOWN
    }

    /**
     * State of the check.
     */
    private state state;

    /**
     * List of elements that may have caused the check to fail. If these cannot be identified, this list may be empty.
     */
    private Set<Element> elements;

    /**
     * Check firing this event.
     */
    private CircuitCheck source;

    /**
     * Default constructor.
     * 
     * @param state
     *            state of the check
     * @param elements
     *            Set<Element> causing the state
     * @param source
     *            CircuitCheck firing the event
     * 
     */
    public CheckEvent(state state, Set<Element> elements, CircuitCheck source) {
        this.state = state;
        this.elements = elements;
        this.source = source;
    }

    /**
     * Return CircuitCheck that fired this event.
     * 
     * @return CircuitCheck firing this event
     */
    public CircuitCheck getSource() {
        return source;
    }

    public state getState() {
        return state;
    }

    public Set<Element> getElements() {
        return elements;
    }
}

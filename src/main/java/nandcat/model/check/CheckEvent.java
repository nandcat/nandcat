package nandcat.model.check;

import java.util.Set;
import nandcat.model.element.Element;

/**
 * CheckEvent the Event fired when something changes in the Clock.
 */
public class CheckEvent {

    /**
     * Possbile states of the check.
     */
    public static enum State {
        /**
         * Running.
         */
        RUNNING,
        /**
         * Test was successfull.
         */
        SUCCEEDED,
        /**
         * Test Failed.
         */
        FAILED,
        /**
         * Test not initialized.
         */
        UNKNOWN
    }

    /**
     * State of the check.
     */
    private State state;

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
    public CheckEvent(State state, Set<Element> elements, CircuitCheck source) {
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

    /**
     * Gets the state of the check.
     * 
     * @return State of the check.
     */
    public State getState() {
        return state;
    }

    /**
     * Gets affected elements if check fails.
     * 
     * @return Affected elements.
     */
    public Set<Element> getElements() {
        return elements;
    }
}

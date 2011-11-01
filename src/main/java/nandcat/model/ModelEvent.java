package nandcat.model;

import java.util.Set;
import nandcat.model.check.CircuitCheck;
import nandcat.model.element.Element;

/**
 * Modelevent object fired by Model to inform its listeners about state changes (Elements/Checks/Simulation/... changed)
 */
public class ModelEvent {

    /**
     * Elements relevant for the fired event.
     */
    private Set<Element> elements;

    /**
     * CicuitChecks relevant for the fired event.
     */
    private Set<CircuitCheck> checks;

    /**
     * Message for the fired event.
     */
    private String message;

    /**
     * Return CircuitChecks relevant for the event.
     * 
     * @return Set<CircuitCheck> of relevant checks
     */
    public Set<CircuitCheck> getChecks() {
        return checks;
    }

    /**
     * Set CircuitChecks relevant for the fired event.
     * 
     * @param checks
     *            Set<CircuitCheck> relevant for the event
     */
    public void setChecks(Set<CircuitCheck> checks) {
        this.checks = checks;
    }

    /**
     * Return the message for the event.
     * 
     * @return String containing the event's message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the message for the event.
     * 
     * @param message
     *            String to set the event's message to
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Get elements for the event.
     * 
     * @return Set<Element> elements relevant for the event
     */
    public Set<Element> getElements() {
        return elements;
    }

    /**
     * Set elements for the event.
     * 
     * @param elements
     *            Set<Element> for the event
     */
    public void setElements(Set<Element> elements) {
        this.elements = elements;
    }
}

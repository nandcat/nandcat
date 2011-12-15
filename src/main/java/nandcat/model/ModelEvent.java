package nandcat.model;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import nandcat.model.check.CircuitCheck;
import nandcat.model.element.DrawElement;

/**
 * Modelevent object fired by Model to inform its listeners about state changes (Elements/Checks/Simulation/... changed)
 */
public class ModelEvent {

    /**
     * Boolean for tests.
     */
    private boolean checksPassed;

    /**
     * File used for export and import events.
     */
    private File file;

    /**
     * UUid of the circuit.
     */
    private String circuitUuid;

    /**
     * Used to mark that the export was a export using a drawer.
     */
    private boolean isDrawerExport = false;

    /**
     * Elements relevant for the fired event.
     */
    private Set<DrawElement> elements;

    /**
     * CicuitChecks relevant for the fired event.
     */
    private Set<CircuitCheck> checks;

    /**
     * Message for the fired event.
     */
    private String message;

    /**
     * Default constructor.
     */
    public ModelEvent() {
    }

    /**
     * Constructor for initialization of the ModelEvent with a Module.
     * 
     * @param element
     *            DrawElement to initialize elements with.
     */
    public ModelEvent(DrawElement element) {
        elements = new HashSet<DrawElement>();
        elements.add(element);
    }

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
     * Get checksPassed.
     * 
     * @return boolean relevant for the event
     */
    public boolean allChecksPassed() {
        return checksPassed;
    }

    /**
     * Set checksPassed to b.
     * 
     * @param b
     *            new value of checksPassed
     */
    public void setChecksPassed(boolean b) {
        checksPassed = b;
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
    public Set<DrawElement> getElements() {
        return elements;
    }

    /**
     * Set elements for the event.
     * 
     * @param elements
     *            Set<Element> for the event
     */
    public void setElements(Set<DrawElement> elements) {
        this.elements = elements;
    }

    /**
     * Gets the file of the event.
     * 
     * @return File of the event.
     */
    public File getFile() {
        return file;
    }

    /**
     * Sets the file for the event.
     * 
     * @param file
     *            File to set.
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Checks if the export was made using a drawer.
     * 
     * @return True if export was made using a drawer.
     */
    public boolean isDrawerExport() {
        return isDrawerExport;
    }

    /**
     * Sets if the export was made using a drawer.
     * 
     * @param isDrawerExport
     *            if exporter was a drawer.
     */
    public void setDrawerExport(boolean isDrawerExport) {
        this.isDrawerExport = isDrawerExport;
    }

    /**
     * Gets the circuits UUID.
     * 
     * @return the circuits UUID.
     */
    public String getCircuitUuid() {
        return circuitUuid;
    }

    /**
     * Sets the circuits UUID.
     * 
     * @param circuitUuid
     *            the circuit UUID to set
     */
    public void setCircuitUuid(String circuitUuid) {
        this.circuitUuid = circuitUuid;
    }
}

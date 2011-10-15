package de.unipassau.sep.nandcat.model;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Set;
import de.unipassau.sep.nandcat.model.check.CircuitCheck;
import de.unipassau.sep.nandcat.model.element.Circuit;
import de.unipassau.sep.nandcat.model.element.Element;
import de.unipassau.sep.nandcat.model.element.Module;
import de.unipassau.sep.nandcat.model.element.Port;

/**
 * The model class is a wrapper for the logic of the program.
 * 
 * @version 0.1
 */
public class Model {

    // TODO Implements clocklistener anonymously.
    /**
     * A set of checks which can be performed on the current circuit before starting the simulation.
     */
    private Set<CircuitCheck> checks;

    /**
     * A set of all model listeners on the model. The listener informs the implementing class about changes in the
     * model.
     */
    private Set<ModelListener> listeners;

    /**
     * The current circuit of the model.
     */
    private Circuit circuit;

    /**
     * The current clock instance used for simulation.
     */
    private Clock clock;

    /**
     * The constructor for the model class.
     */
    public Model() {
        // TODO implement
    }

    /**
     * Start the selected checks on the current circuit.
     */
    public void startChecks() {
        // TODO implement
        // for (CircuitCheck check : checks) {
        // if (check.isActive()) {
        // check.test(circuit);
        // }
        // }
    }

    /**
     * Set a given check on the circuit to active or inactive.
     * 
     * @param check
     *            The CircuitCheck to be set to active.
     * @param isActive
     *            TRUE if the check is set to active, FALSE otherwise.
     */
    // TODO public method? why?
    public void setCheckActive(CircuitCheck check, boolean isActive) {
        // TODO implement
        // check.setActive(isActive);
    }

    /**
     * Load an existing file into the program. The file has to be imported by an importer.
     * 
     * @param fileName
     *            String defining the name of the file to be loaded.
     */
    public void loadFile(String fileName) {
        // TODO implement
        // Importer anstoßen.
    }

    /**
     * Save a file to the system.
     * 
     * @param fileName
     *            String defining the name of the file to be saved.
     */
    public void saveFile(String fileName) {
        // TODO implement
        // Exporter anstoßen.
    }

    /**
     * Adds a listener to the set of listeners, which will be notified using events.
     * 
     * @param l
     *            Modellistener
     */
    public void addListener(ModelListener l) {
        // listeners.add(l);
    }

    /**
     * Removes a listener from the set of listeners.
     * 
     * @param l
     *            Modellistener
     */
    public void removeListener(ModelListener l) {
        // listeners.remove(l);
    }

    /**
     * Get a set of elements at a specific location.
     * 
     * @param point
     *            Point containing the x- and y-coordinate.
     * @return Set of Elements at the given location.
     */
    public Set<Element> getElementsAt(Point point) {
        // Set<Element> elementsAt = new HashSet<Element>();
        // for (Element element : circuit.getElements()) {
        // if (element.getRectangle.contains(point) {
        // TODO Get position of Element
        // elementsAt.add(element);
        // }
        return null;
        // TODO implement
    }

    /**
     * Gets the current clock.
     * 
     * @return Clock used for simulation.
     */
    public Clock getClock() {
        // TODO Man braucht ne Clock auf die man sich registrieren kann
        return clock;
    }

    /**
     * Select elements from the circuit. An element is selected when it lies within a given rectangle.
     * 
     * @param rect
     *            The Rectangle defining the zone where elements are selected.
     * @return A Set of the selected elements.
     */
    public Set<Element> selectElements(Rectangle rect) {
        // TODO implement
        // Set<Element> selectedElements = new HashSet<Element>();
        // for (Element element : circuit.getElements()) {
        // if (rect.contains(element.getRectangle)){
        // selectedElements.add(element);
        // }
        // }
        return null;
    }

    /**
     * Get all elements from the current circuit.
     * 
     * @return A Set of all elements.
     */
    // TODO Check if needed. You can call circuit.getElements()
    public Set<Element> getElements() {
        // TODO implement
        return null;
    }

    /**
     * Start the simulation on the current circuit. The starting elements are registered at the clock and compute their
     * output.
     */
    public void startSimulation() {
        // TODO implement
        // for (Element element : circuit.getStartingElements()) {
        // clock.register(element)
        // }
    }

    /**
     * Remove all objects from the current circuit.
     */
    public void clearCircuit() {
        // TODO implement
        // circuit.getElements().clear();
    }

    /**
     * Adds a Connection between two Ports to this Model.
     * 
     * @param inPort
     *            the Port carrying the input Signal of the Connection
     * @param outPort
     *            the Port carrying the output Signal of the Connection
     */
    public void addConnection(Port inPort, Port outPort) {
        // TODO Fuege die connection auch in die Gatter ein?
        // inPort.setconnection(...)
        circuit.addConnection(inPort, outPort);
    }

    /**
     * Adds a new Module to the Model.
     * 
     * @param m
     *            the Module to be added
     * @param p
     *            the Location of the Module
     */
    public void addModule(Module m, Point p) {
        circuit.addModule(m, p);
    }
}

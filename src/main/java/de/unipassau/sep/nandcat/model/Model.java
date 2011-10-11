package de.unipassau.sep.nandcat.model;

import java.util.Set;

import de.unipassau.sep.nandcat.model.check.CircuitCheck;
import de.unipassau.sep.nandcat.model.element.Circuit;

/**
 * Model.
 * 
 * @version 0.1
 * 
 */
public class Model {
    // TODO Implements clocklistener anonym.
    private Set<CircuitCheck> checks;
    private Set<ModelListener> listeners;

    private Circuit circuit;

    public Model() {

    }

    public void getStartingElements() {

    }

    public void startChecks() {

    }

    // TODO public method? why?
    public void setCheckActive(CircuitCheck check, boolean isActive) {

    }

    public void loadFile(String fileName) {

    }

    public void saveFile(String fileName) {

    }

    /**
     * Adds a listener to the set of listeners, which will be notified using events.
     * @param l Modellistener
     */
    public void addListener(ModelListener l) {
        listeners.add(l);
    }

    /**
     * Removes a listener from the set of listeners.
     * @param l  Modellistener
     */
    public void removeListener(ModelListener l) {
        listeners.remove(l);
    }

    public void getElementsAtLocation() {

    }

    // TODO missing parameter?
    public void selectElements() {

    }

    public void getElements() {

    }

    public void startSimulation() {

    }

}

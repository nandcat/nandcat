package de.unipassau.sep.nandcat.model.element;

import java.util.Set;
import de.unipassau.sep.nandcat.model.Clock;
import de.unipassau.sep.nandcat.model.ClockListener;

/**
 * Circuit.
 * 
 * @version 0.1
 * 
 */
public class Circuit implements ClockListener, Element {

    /**
     * Contains the Elements in this circuit.
     */
    private Set<Element> elements;

    /**
     * Returns the "first" Elements in this Circuit. These 
     *
     * @return
     */
    public Set<Element> getStartingElements() {
        return null;
    }

    public void setName(String name) {
        // TODO Auto-generated method stub
    }

    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    public void clockTicked(Clock clock) {
        // TODO Auto-generated method stub
    }

    // TODO Circuit contains more elements than module
    // TODO Is called from the clock - only once.
    public boolean calculate() {
        return false;
    }

    /**
     * Getter for Elements.
     * 
     * @return The Elements
     */
    public Set<Element> getElements() {
        return elements;
    }

    /**
     * Setter for Elements.
     * 
     * @param elements
     *            The Elements to set
     */
    // TODO check if needed. Maybe for importing.
    public void setElements(Set<Element> elements) {
        this.elements = elements;
    }

    /**
     * Add an Element to Set of Elements.
     * 
     * @param element
     *            The Element to be added.
     */
    public void addElement(Element element) {
        elements.add(element);
    }

    /**
     * Remove a given Element from Set of Elements.
     * 
     * @param element
     *            Element to be removed.
     */
    public void removeElement(Element element) {
        elements.remove(element);
    }
}

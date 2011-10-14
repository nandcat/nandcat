package de.unipassau.sep.nandcat.model.element;

import java.awt.Point;
import java.util.Set;
import de.unipassau.sep.nandcat.model.Clock;
import de.unipassau.sep.nandcat.model.ClockListener;

/**
 * Circuit.
 * 
 * @version 0.1
 * 
 */
public class Circuit implements ClockListener, Module {

    /**
     * The name of this Circuit.
     */
    private String name;

    /**
     * Contains the Elements in this circuit.
     */
    private Set<Element> elements;

    /**
     * Returns the "first" Elements in this Circuit.
     *
     * @return Set<Element> containing the starting Elements of this Circuit.
     */
    public Set<Element> getStartingElements() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void clockTicked(Clock clock) {
        // TODO Auto-generated method stub
    }

//    // TODO Circuit contains more elements than module
//    // TODO Is called from the clock - only once.
//    public boolean calculate() {
//        return false;
//    }

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

// TODO prüfen ob notwendig. Obsolet imho.
//    /**
//     * Add an Element to Set of Elements.
//     * 
//     * @param element
//     *            The Element to be added.
//     */
//    public void addElement(Element element) {
//        elements.add(element);
//    }
//
//    /**
//     * Remove a given Element from Set of Elements.
//     * 
//     * @param element
//     *            Element to be removed.
//     */
//    public void removeElement(Element element) {
//        elements.remove(element);
//    }

    /**
     * Adds a connection between two ports to this Circuit.
     *
     * @param inPort Port the Connection will get attached to
     * @param outPort Port the Connection will get attached to
     */
    public void addConnection(Port inPort, Port outPort) {
        Connection connection = new Connection(inPort, outPort);
        elements.add(connection);
    }

    /**
     * Adds a Module to the Circuit.
     * 
     * @param m Module to add
     * @param p Point specifying the location of the Module
     */
    public void addModule(Module m, Point p) {
        elements.add(m);
        m.setLocation(p);
    }

    /**
     * {@inheritDoc}
     */
    public Set<Port> getInPorts() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Set<Port> getOutPorts() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void setLocation(Point p) {
        // TODO Auto-generated method stub
    }
}

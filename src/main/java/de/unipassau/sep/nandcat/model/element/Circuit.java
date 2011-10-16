package de.unipassau.sep.nandcat.model.element;

import java.awt.Point;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
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
     * Contains the Location in this Circuit.
     */
    private Point location;

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
    public Set<Module> getStartingModules() {
        Set<Module> result = new LinkedHashSet<Module>();
        for (Element e : elements) {
            if (e instanceof Module) {
                Module m = (Module) e;
                boolean isStartingModule = false;
                for (Port p : m.getInPorts()) {
                    if (p.getConnection() == null) {
                        break;
                    }
                    isStartingModule = true;
                }
                if (isStartingModule) {
                    result.add(m);
                }
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    public void clockTicked(Clock clock) {
        // TODO Auto-generated method stub
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
     * Adds a connection between two ports to this Circuit.
     * 
     * @param inPort
     *            Port the Connection will get attached to
     * @param outPort
     *            Port the Connection will get attached to
     * @return the added Connection
     */
    public Connection addConnection(Port inPort, Port outPort) {
        Connection connection = new Connection(inPort, outPort);
        elements.add(connection);
        return connection;
    }

    /**
     * Adds a Module to the Circuit.
     * 
     * @param m
     *            Module to add
     * @param p
     *            Point specifying the location of the Module
     */
    public void addModule(Module m, Point p) {
        elements.add(m);
        m.setLocation(p);
    }

    /**
     * {@inheritDoc}
     */
    public List<Port> getInPorts() {
        List<Port> result = new LinkedList<Port>();
        for (Module m : getStartingModules()) {
            for (Port p : m.getInPorts()) {
                result.add(p);
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    // FIXME implement correctly
    public List<Port> getOutPorts() {
        List<Port> result = new LinkedList<Port>();
        for (Element e : elements) {
            if (e instanceof Module) {
                for (Port p : ((Module) e).getOutPorts()) {
                    result.add(p);
                }
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public void setLocation(Point p) {
        this.location = p;
    }

    /**
     * {@inheritDoc}
     */
    public Point getLocation() {
        return location;
    }

    /**
     * Remove element from circuit.
     * 
     * @param e
     *            Element to remove
     */
    public void removeElement(Element e) {
        if (e == null) {
            return;
        }
        if (e instanceof Connection) {
            Connection c = (Connection) e;
            c.getInPort().setConnection(null);
            c.getOutPort().setConnection(null);
            elements.remove(c);
        }
        if (e instanceof Module) {
            Module m = (Module) e;
            for (Port p : m.getInPorts()) {
                if (p.getConnection() != null) {
                    removeElement(p.getConnection());
                }
            }
            for (Port p : m.getOutPorts()) {
                if (p.getConnection() != null) {
                    removeElement(p.getConnection());
                }
            }
            elements.remove(m);
        }
    }
}

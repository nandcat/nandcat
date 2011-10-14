package de.unipassau.sep.nandcat.model.element;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import de.unipassau.sep.nandcat.model.Clock;

/**
 * Lamp implementation.
 * 
 * @version 0.1
 * 
 */
public class Lamp implements Module {

    /**
     * Point specifying the Location of the Gate.
     */
    private Point location;

    /**
     * Lamp's name.
     */
    private String name;

    /**
     * Lamp's port.
     */
    private Port inPort;

    /**
     * Lamp's state.
     */
    private boolean state;

    /**
     * Default constructor.
     */
    public Lamp() {
        state = false;
        inPort = new Port(this);
    }

    /**
     * Set lamp's name.
     * 
     * @param name
     *            String to set lamp's name to
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return lamp's name.
     * 
     * @return String representing lamp's name
     */
    public String getName() {
        return name;
    }

    /**
     * Return out ports. (always empty)
     * 
     * @return Set containing all outgoing ports
     */
    public List<Port> getOutPorts() {
        return new LinkedList<Port>();
    }

    /**
     * Return in ports.
     * 
     * @return Set containing all incoming ports
     */
    public List<Port> getInPorts() {
        LinkedList<Port> ports = new LinkedList<Port>();
        ports.add(inPort);
        return ports;
    }

    /**
     * Set incoming ports.
     * 
     * @param inPorts
     *            Set containing incoming ports to set
     */
    public void setInPorts(List<Port> inPorts) {
        if (inPorts != null && inPorts.size() == 1) {
            this.inPort = inPorts.iterator().next();
        }
    }

    /**
     * Get state of lamp.
     * 
     * @return state of the lamp
     */
    public boolean getState() {
        return state;
    }

    /**
     * {@inheritDoc}
     */
    public void clockTicked(Clock clock) {
        this.state = inPort.getState();
    }

    /**
     * {@inheritDoc}
     */
    public void setLocation(Point p) {
        location = p;
    }

    /**
     * {@inheritDoc}
     */
    public Point getLocation() {
        return location;
    }
}

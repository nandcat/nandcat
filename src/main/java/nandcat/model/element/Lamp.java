package nandcat.model.element;

import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import nandcat.model.Clock;

/**
 * Lamp implementation. The lamp is shiny if the input signal is true. It has no outPorts.
 */
public class Lamp implements Module {

    /**
     * Default serial version uid.
     */
    private static final long serialVersionUID = 1L;

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
     * Rectangle specifying the Lamp's shape.
     */
    private Rectangle rectangle;

    /**
     * Lamp is selected (or not).
     */
    private boolean selected;

    /**
     * /** Default constructor.
     */
    public Lamp() {
        state = false;
        rectangle = new Rectangle(EXTENT, EXTENT);
        inPort = new Port(this);
        inPort.setState(false, null);
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
    public Rectangle getRectangle() {
        return rectangle;
    }

    /**
     * {@inheritDoc}
     */
    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    /**
     * {@inheritDoc}
     */
    public void setSelected(boolean b) {
        selected = b;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        String x = this.getClass().getSimpleName() + "(" + getRectangle().x + "/" + getRectangle().y + ") ";
        x += "(In) ";
        if (inPort.getRectangle() != null) {
            x += inPort.getRectangle().x + "/" + inPort.getRectangle().y + ", ";
        }
        return x;
    }
}

package nandcat.model.element;

import java.awt.geom.Line2D;
import nandcat.model.Clock;

/**
 * Connection between two Modules. Propagates the signal from the first Module to the second. Is responsible for
 * registering the next Module on the clock.
 */
public class Connection implements Element {

    /**
     * Default serial version uid.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Connection's name.
     */
    private String name;

    /**
     * port (a module's <b>outPort</b>) the connection is attached to. Set to one of the modules's outPorts.
     */
    private Port inPort;

    /**
     * port (a module's <b>inPort</b>) the connection is attached to. Set to one of the modules's inPorts.
     */
    private Port outPort;

    /**
     * Connection's state.
     */
    private boolean state;

    /**
     * Connection's selection-state.
     */
    private boolean selected;

    /**
     * Create and attach new connection.Note that this will also set the port's connection reference.
     * 
     * @param inPort
     *            Port (a module's <b>outPort</b>) the connection is attached to. Set to one of the modules's outPorts.
     *            May not be null.
     * @param outPort
     *            Port (a module's <b>inPort</b>) the connection is attached to. Set to one of the modules's inPorts.
     *            May not be null.
     */
    public Connection(Port inPort, Port outPort) {
        if (inPort == null || outPort == null) {
            throw new IllegalArgumentException("neither in nor outport of a connection may be null");
        }
        this.inPort = inPort;
        this.outPort = outPort;
        inPort.setConnection(this);
        outPort.setConnection(this);
        state = false;
    }

    /**
     * Set connection's name.
     * 
     * @param name
     *            String to set connection's name to
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return connection's name.
     * 
     * @return String representing connection's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets connection's state.
     * 
     * @param state
     *            state to set
     * @param clock
     *            Clock that has strikken(!)
     */
    public void setState(boolean state, Clock clock) {
        this.state = state;
        if (clock != null) {
            clock.addListener(this);
        }
    }

    /**
     * Return connection's state.
     * 
     * @return the state
     */
    public boolean getState() {
        return state;
    }

    /**
     * Return the next module (the Connection is attached to). Next: going from one element's outPort to the other
     * element's inPort
     * 
     * @return Module whose inPort is attached to this connection
     */
    public Module getNextModule() {
        if (outPort == null) {
            return null;
        }
        return outPort.getModule();
    }

    /**
     * Return the previous module (the Connection is attached to). Previous: going from one element's inPort to the
     * other element's outPort
     * 
     * @return Module whose outPort is attached to this connection
     */
    public Module getPreviousModule() {
        return inPort.getModule();
    }

    /**
     * Getter for inPort.
     * 
     * @return Port inPort
     */
    public Port getInPort() {
        return inPort;
    }

    /**
     * Getter for outPort.
     * 
     * @return Port outPort
     */
    public Port getOutPort() {
        return outPort;
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
    public void clockTicked(Clock clock) {
        if (getNextModule() != null) {
            outPort.setState(state, clock);
            if (clock != null) {
                clock.addListener(getNextModule());
            }
        }
    }

    /**
     * Return Line-object representing this connection (derived from attached in/outPorts).
     * 
     * @return Line2D representing the connection's shape
     */
    public Line2D getLine() {
        // TODO exact enough? -> maybe better: public Point inPort.getModuleBoundary()
        return new Line2D.Double(inPort.getRectangle().getLocation(), outPort.getRectangle().getLocation());
    }
}

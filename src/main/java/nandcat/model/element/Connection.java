package nandcat.model.element;

import nandcat.model.Clock;

/**
 * Connection between two Modules. Propagates the signal from the first Module to the second. Is responsible for
 * registering the next Module on the clock.
 * 
 * @version 0.1
 * 
 */
public class Connection implements Element {

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
     * Default constructor.
     */
    public Connection() {
    }

    /**
     * Create and attach new connection.
     * 
     * @param inPort
     *            Port (a module's <b>outPort</b>) the connection is attached to. Set to one of the modules's outPorts
     * @param outPort
     *            Port (a module's <b>inPort</b>) the connection is attached to. Set to one of the modules's inPorts
     */
    public Connection(Port inPort, Port outPort) {
        this.inPort = inPort;
        this.outPort = outPort;
        inPort.setConnection(this);
        outPort.setConnection(this);
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
        if (getNextModule() != null) {
            outPort.setState(getState(), clock);
            if (clock != null) {
                clock.addListener(getNextModule());
            }
        }
    }

    /**
     * Return connection's state.
     * 
     * @return the state
     */
    public boolean getState() {
        if (inPort == null) {
            return false;
        }
        return inPort.getState();
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
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSelected() {
        // TODO Auto-generated method stub
        return false;
    }
}

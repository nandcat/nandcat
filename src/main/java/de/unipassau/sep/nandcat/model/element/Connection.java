package de.unipassau.sep.nandcat.model.element;

import de.unipassau.sep.nandcat.model.Clock;

/**
 * Connection. // TODO konstruktor fehlt
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
     * Connection's state.
     */
    private boolean state;
    /**
     * port (of type <b>outPort</b>) the connection is attached to. Set to one of the modules's outPorts.
     */
    private Port inPort;
    /**
     * port (of type <b>inPort</b>) the connection is attached to. Set to one of the modules's inPorts.
     */
    private Port outPort;

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
        clock.addListener(getNextModule());
    }

    /**
     * Return connection's state.
     * 
     * @return the state
     */
    public boolean getState() {
        return state;
    }

    // FIXME weg DAMIT! Des Teufels Tand!
    /**
     * Set connection's state.
     * 
     * @param state
     *            to set
     */
    public void setState1(boolean state) {
        this.state = state;
    }

    /**
     * Return the next module (the Connection is attached to). Next: going from one element's outPort to the other
     * element's inPort
     * 
     * @return Module whose inPort is attached to this connection
     */
    public Module getNextModule() {
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
}

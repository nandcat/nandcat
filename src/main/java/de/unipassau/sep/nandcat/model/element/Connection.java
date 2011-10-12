package de.unipassau.sep.nandcat.model.element;

import de.unipassau.sep.nandcat.model.Clock;

/**
 * Connection.
 * 
 * @version 0.1
 * 
 */
public class Connection implements Element { // Connection meldet Baustein bei Clock an! Über Port.

    /**
     * Connection's name.
     */
    private String name;
    /**
     * Connection's state.
     */
    private boolean state;
    /**
     * port (of type <u><b>outPort</b></u>) the connection is attached to.
     * Set to on of the modules's outPorts.
     */
    private Port inPort;
    /**
     * port (of type <u><b>inPort</b></u>) the connection is attached to.
     * Set to on of the modules's inPorts.
     */
    private Port outPort;

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
     * Sets connection's state.
     * 
     * @param state
     *            state to set
     * @param clock
     *            Clock that has strikken(!)
     */
    public void setState(boolean state, Clock clock) {
        this.state = state;
        clock.addListener(inPort.getGate());
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
     * Set connection's state.
     * 
     * @param state
     *            to set
     */
    public void setState(boolean state) {
        this.state = state;
    }

    /**
     * Return connection's inPort.
     * 
     * @return connection's inPort
     */
    private Port getInPort() {
        return inPort;
    }

    /**
     * Return connection's outPort.
     * 
     * @return connection's outPort
     */
    private Port getOutPort() {
        return outPort;
    }

    /**
     * Return the next element (the Connection is attached to).
     * Next: going from one element's outPort to the other element's inPort
     * 
     * @return Gate
     */
    public Gate getNextElement() {
        return outPort.getGate();
    }
}

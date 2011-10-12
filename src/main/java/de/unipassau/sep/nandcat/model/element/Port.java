package de.unipassau.sep.nandcat.model.element;

import de.unipassau.sep.nandcat.model.Clock;

/**
 * Port.
 * 
 * @version 0.1
 */
public class Port {

    /**
     * Port's state.
     */
    private boolean state;
    /**
     * Gate this port is attached to.
     */
    private final Gate gate;
    /**
     * Connection attached to port.
     */
    private Connection connection;

    /**
     * Default constructor.
     * 
     * @param gate
     *            Gate to attach port to
     */
    public Port(Gate gate) {
        this.gate = gate;
    }

    /**
     * Get port gate.
     * 
     * @return Gate belonging to this port
     */
    public Gate getGate() {
        return gate;
    }

    /**
     * Set state of port.
     * 
     * @param state
     *            state to set
     * @param clock
     *            Clock that has strikken(!)
     */
    public void setState(boolean state, Clock clock) {
        this.state = state;
        getConnection().setState(state, clock);
    }

    /**
     * Get state of port.
     * 
     * @return state of the port
     */
    public boolean getState() {
        return false;
    }

    /**
     * Return attached connection.
     * 
     * @return Connection attached to this port
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Attach connection to port.
     * 
     * @param connection
     *            Connection to attach to port
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Return wether port is outport or not.
     * 
     * @return port is outport or not
     */
    public boolean isOutPort() {
        return gate.getOutPorts().contains(this);
    }
}

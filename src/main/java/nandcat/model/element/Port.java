package nandcat.model.element;

import java.awt.Rectangle;
import nandcat.model.Clock;

/**
 * A Port exists in Modules only. Connection's are attached to the module's Ports. It's responsible to propagate the
 * signal from the connection to the Module.
 */
public class Port {

    /**
     * Port's state.
     */
    private boolean state;

    /**
     * Representing the bounds of the Port. The Rectangle's coordinates are <b>not relative</b> to the Module this Port
     * is attached to, but absolute. This Rectangle should have a default value, so
     */
    private Rectangle bounds;

    /**
     * Returns the Rectangle representing the bounds of the Port. The Rectangle's coordinates are <b>not relative</b> to
     * the Module this Port is attached to, but absolute Coordinates on the drawing area.
     * 
     * @return Rectangle representing the bounds of the Port
     */
    public Rectangle getRectangle() {
        return bounds;
    }

    /**
     * Set the Rectangle representing the bounds of the Port. The Rectangle's coordinates are <b>not relative</b> to the
     * Module this Port is attached to, but absolute Coordinates on the drawing area.
     * 
     * @param position
     */
    public void setRectangle(Rectangle position) {
        this.bounds = position;
    }

    /**
     * Module this port is attached to.
     */
    private final Module module;

    /**
     * Connection attached to port. Note that this reference may be null.
     */
    private Connection connection;

    /**
     * Default constructor.
     * 
     * @param module
     *            Module to attach port to
     */
    public Port(Module module) {
        this.module = module;
        this.bounds = locateOnStandardPosition();
    }

    /**
     * Get port module.
     * 
     * @return Module belonging to this port
     */
    public Module getModule() {
        return module;
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
        if (isOutPort() && getConnection() != null) {
            getConnection().setState(state, clock);
        }
    }

    /**
     * Get state of port.
     * 
     * @return state of the port
     */
    public boolean getState() {
        return state;
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
     * @return true if port is outport or false if port is inport
     */
    public boolean isOutPort() {
        return module.getOutPorts().contains(this);
    }

    /**
     * Calculates the standard position on this port during the construction. Note that this can lead to immense and
     * horrific errors and unexpected behaviour if the Module is smaller than the size of the according list of ports.
     * 
     * @return Rectangle specifying the standard Location of this Port
     */
    private Rectangle locateOnStandardPosition() {

        // rectangle containing the standard size, but not position of the port
        Rectangle result = new Rectangle(-1, -1, 1, 1);

        // index starts at 0, so +1. first port is number 1. derp.
        int numberOfPort = isOutPort() ? module.getOutPorts().indexOf(this) : module.getInPorts().indexOf(this) + 1;

        // amount of ports on this side
        int amountOfPorts = isOutPort() ? module.getOutPorts().size() : module.getInPorts().size();

        /*
         * one port: position at half the height two ports: position at one third, etcpp +1
         */
        int yOffset = module.getRectangle().height / (amountOfPorts + 1);

        result.y = module.getRectangle().y + (yOffset * numberOfPort);
        result.x = module.getRectangle().x + (isOutPort() ? 0 : module.getRectangle().width - result.width);

        return result;
    }
}

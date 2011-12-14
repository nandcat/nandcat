package nandcat.model.element;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import nandcat.model.Clock;

/**
 * A Port exists in Modules only. Connection's are attached to the module's Ports. It's responsible to propagate the
 * signal from the connection to the Module.
 */
public class Port implements Serializable {

    /**
     * Default serial version uid.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Port's state.
     */
    private boolean state;

    /**
     * Port's annotation got from connected lamp or impulse generator.
     */
    private String annotation;

    /**
     * Representing the bounds of the Port. The Rectangle's coordinates are <b>not relative</b> to the Module this Port
     * is attached to, but absolute. This Rectangle should have a default value, so
     */
    private Rectangle bounds = new Rectangle();

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
     *            Rectangle to set
     */
    public void setRectangle(Rectangle position) {
        if (position == null) {
            throw new IllegalArgumentException();
        }
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
     * Default constructor. It won't layout the port(s) in the containing Module. See/call
     * <code>locateOnStandardPosition()</code> for the glourious details.
     * 
     * @param module
     *            Module to attach port to
     */
    protected Port(Module module) {
        this.module = module;
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
     * Return center of Port.
     * 
     * @return Point of the center
     */
    public Point getCenter() {
        return new Point(bounds.x + (bounds.width / 2), bounds.y + (bounds.height / 2));
    }

    /**
     * Gets the string representation of the port.
     * 
     * @return String representation.
     */
    public String toString() {
        if (bounds == null) {
            return getModule().getClass().getSimpleName() + "(" + this.getState() + ")";
        }
        // beware of getState
        return "[" + this.getState() + "]";
    }

    /**
     * Gets the annotation of the port.
     * 
     * @return the annotation
     */
    public String getAnnotation() {
        return annotation;
    }

    /**
     * Sets the annotation of the port.
     * 
     * @param annotation
     *            the annotation to set
     */
    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }
}

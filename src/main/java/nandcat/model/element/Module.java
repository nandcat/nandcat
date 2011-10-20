package nandcat.model.element;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import nandcat.model.ClockListener;

/**
 * Module interface. A Module is an Element with Ports. They can be connected by Connections.
 */
public interface Module extends ClockListener, Element {

    /**
     * Gets incoming ports.
     * 
     * @return incoming ports.
     */
    List<Port> getInPorts();

    /**
     * Gets outgoing ports.
     * 
     * @return List of outgoing ports.
     */
    List<Port> getOutPorts();

    /**
     * Set the Location of this Module.
     * 
     * @param p
     *            Point containing the new Location of this Module.
     */
    void setLocation(Point p);

    /**
     * Get the Location of this Module.
     * 
     * @return Point containing the Location of this Module
     */
    Point getLocation();

    /**
     * Return the module's rectangle.
     * 
     * @return Rectangle representing the module's shape.
     */
    Rectangle getRectangle();

    /**
     * Set the module's rectangle.
     * 
     * @param rectangle
     *            Rectangle to set module's shape to.
     */
    void setRectangle(Rectangle rectangle);
}

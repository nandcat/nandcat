package nandcat.model.element;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

/**
 * Module interface. A Module is an Element with Ports. They can be connected by Connections.
 */
public interface Module extends Element, DrawModule {

    /**
     * Default extent for the rectangle of Modules.
     */
    int EXTENT = 100;

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
     * Set the Location of this Module. The Location is the TopLeft corner.
     * 
     * @deprecated use setRectangle(Rectangle rectangle) instead
     * @param p
     *            Point containing the new Location of this Module.
     */
    void setLocation(Point p);

    /**
     * Get the Location of this Module. The Location is the TopLeft corner.
     * 
     * @deprecated use getRectangle() instead
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

package nandcat.model.element;

import java.awt.Rectangle;
import java.util.List;

/**
 * Module interface. A Module is an Element with Ports. They can be connected by Connections.
 */
public interface Module extends Element, DrawModule {

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

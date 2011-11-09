package nandcat.model.element;

import java.awt.Rectangle;
import java.util.List;

/**
 * RoModule interface. A read-only Module is an Element with Ports. They can be connected by Connections.
 */
public interface DrawModule extends DrawElement {

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
}

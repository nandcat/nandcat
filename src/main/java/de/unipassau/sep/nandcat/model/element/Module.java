package de.unipassau.sep.nandcat.model.element;

import java.awt.Point;
import java.util.List;
import de.unipassau.sep.nandcat.model.ClockListener;

/**
 * Module interface.
 * 
 * @version 0.1
 * 
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
}

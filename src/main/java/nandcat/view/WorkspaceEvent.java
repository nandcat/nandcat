package nandcat.view;

import java.awt.Point;

/**
 * Workspace event.
 * 
 * Holds information about the occurred event.
 */
public class WorkspaceEvent {

    /**
     * Point where the Event happened.
     */
    private Point location;

    /**
     * getter for the location Point.
     * 
     * @return Point location where the Event happend
     */
    public Point getLocation() {
        return location;
    }

    /**
     * setter, set where the event happend.
     * 
     * @param p
     *            Point where the Event happened.
     */
    public void setLocation(Point p) {
        location = p;
    }
}

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
     * Integer representing which Button has been pressed.
     */
    private int button;

    /**
     * Boolean representing if SHIFT Key is pressed.
     */
    private boolean isShiftDown;

    /**
     * Integer representing number of "clicks" scrolled.
     */
    private int wheelRotation;

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

    /**
     * setter, set which button has been pressed.
     * 
     * @param i
     *            int Number of Button.
     */
    public void setButton(int i) {
        button = i;
    }

    /**
     * getter of the Button-Number.
     * 
     * @return int Number of Button.
     */
    public int getButton() {
        return button;
    }

    /**
     * setter, set whether the Shift Key is pressed or not.
     * 
     * @param b
     *            boolean down or not
     */
    public void setShiftDown(boolean b) {
        isShiftDown = b;
    }

    /**
     * getter of Shift down or not.
     * 
     * @return boolean Shift pressed or not.
     */
    public boolean isShiftDown() {
        return isShiftDown;
    }

    /**
     * Set the number of "clicks" the mouse wheel was rotated.
     * 
     * @param wheelRotation
     *            int Number of "clicks".
     */
    public void setWheelRotation(int wheelRotation) {
        this.wheelRotation = wheelRotation;
    }

    /**
     * Returns the number of "clicks" the mouse wheel was rotated.
     * 
     * @return negative values if the mouse wheel was rotated up/away from the user, and positive values if the mouse
     *         wheel was rotated down/ towards the user
     */
    public int getWheelRotation() {
        return wheelRotation;
    }
}

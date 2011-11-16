package nandcat.view;

/**
 * Interface WorkspaceListener defines the methods the Listener offers and must be implemented.
 */
public interface WorkspaceListener {

    /**
     * Invoked when the mouse button has been clicked (pressed and released) on a component.
     * 
     * @param e
     *            WorkspaceEvent containing informations about the Location.
     */
    void mouseClicked(WorkspaceEvent e);

    /**
     * Invoked when the mouse button has been released on a component.
     * 
     * @param e
     *            WorkspaceEvent containing informations about the Location.
     */
    void mouseReleased(WorkspaceEvent e);

    /**
     * Invoked when the mouse button has been pressed on a component.
     * 
     * @param e
     *            WorkspaceEvent containing informations about the Location.
     */
    void mousePressed(WorkspaceEvent e);

    /**
     * Invoked when the mouse button has been dragged (pressed and moved) on a component.
     * 
     * @param e
     *            WorkspaceEvent containing informations about the Location.
     */
    void mouseDragged(WorkspaceEvent e);

    /**
     * Invoked when the mouse button has been moved.
     * 
     * @param e
     *            WorkspaceEvent containing informations about the Location.
     */
    void mouseMoved(WorkspaceEvent e);

    /**
     * Invoked when Close Button has been pressed.
     * 
     * @param e
     *            WorkspaceEvent.
     */
    void windowClosing(WorkspaceEvent e);
}

package nandcat.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.JPanel;
import nandcat.model.ModelEvent;

/**
 * Workspace.
 * 
 * @version 0.1
 * 
 */
public class Workspace extends JPanel {

    /**
     * Default serial version uid.
     */
    private static final long serialVersionUID = 1L;

    /**
     * ElementDrawer handles drawing of elements on the workspace.
     */
    private ElementDrawer elementDrawer;

    /**
     * View instance.
     */
    private View view;

    /**
     * A set of all workspace listeners on the workspace. The listener informs the implementing class about changes of
     * the workspace.
     */
    private Set<WorkspaceListener> listeners = new LinkedHashSet<WorkspaceListener>();

    /**
     * MouseListener of the Workspace on itself.
     */
    private MouseAdapter mouseListener;

    /**
     * Constructs the workspace.
     */
    public Workspace() {
        setupWorkspace();
        mouseListener = new MouseAdapter() {
            // TODO implement
        };
    }

    /**
     * set up the Workspace.
     */
    private void setupWorkspace() {
        // TODO Auto-generated method stub
    }

    /**
     * Redraws the workspace with its elements.
     * 
     * @param e
     *            ModelEvent with the elements to be redrawed.
     */
    public void redraw(ModelEvent e) {
        // TODO Wo kommen die Elements her?
    }

    /**
     * Adds a listener to the collection of listeners, which will be notified.
     * 
     * @param l
     *            WorkspaceListener to add.
     */
    public void addListener(WorkspaceListener l) {
        listeners.add(l);
    }

    /**
     * Removes a listener from the collection of listeners.
     * 
     * @param l
     *            WorkspaceListener to remove.
     */
    public void removeListener(WorkspaceListener l) {
        listeners.remove(l);
    }

    /**
     * Notifies listeners of mouse click events on the workspace.
     */
    private void notifyMouseClicked(MouseEvent altE) {
        WorkspaceEvent e = new WorkspaceEvent();
        e.setLocation(altE.getPoint());
        for (WorkspaceListener l : listeners) {
            l.mouseClicked(e);
        }
    }

    /**
     * Notifies listeners of mouse pressed events on the workspace.
     */
    private void notifyMousePressed(MouseEvent altE) {
    }

    /**
     * Notifies listeners of mouse released events on the workspace.
     */
    private void notifyMouseReleased(MouseEvent altE) {
    }

    /**
     * Notifies listeners of mouse moved events on the workspace.
     */
    private void notifyMouseMoved(MouseEvent altE) {
    }

    /**
     * Notifies listeners of mouse dragged events on the workspace.
     */
    private void notifyMouseDragged(MouseEvent altE) {
    }
}

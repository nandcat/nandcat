package de.unipassau.sep.nandcat.view;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Workspace.
 * 
 * @version 0.1
 * 
 */
public class Workspace {

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
     * Redraws the workspace.
     */
    public void redraw() {
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
    private void notifyMouseClicked() {
        WorkspaceEvent e = new WorkspaceEvent();
        // e.setLocation(p);
        // TODO Fehlender Parameter -> Übergabe des ursprünglichen Mousevents..?
        for (WorkspaceListener l : listeners) {
            l.mouseClicked(e);
        }
    }

    /**
     * Notifies listeners of mouse pressed events on the workspace.
     */
    private void notifyMousePressed() {
    }

    /**
     * Notifies listeners of mouse released events on the workspace.
     */
    private void notifyMouseReleased() {
    }

    /**
     * Notifies listeners of mouse moved events on the workspace.
     */
    private void notifyMouseMoved() {
    }

    /**
     * Notifies listeners of mouse dragged events on the workspace.
     */
    private void notifyMouseDragged() {
    }
}

package de.unipassau.sep.nandcat.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import de.unipassau.sep.nandcat.view.View;
import de.unipassau.sep.nandcat.view.WorkspaceEvent;
import de.unipassau.sep.nandcat.view.WorkspaceListener;

/**
 * The ViewTool is responsible for moving the display range over the Workspace.
 * 
 * @version 0.4
 * 
 */
public class ViewTool implements Tool {

    /**
     * Current View instance.
     */
    private View view = null;

    /**
     * Current Controller instance.
     */
    private Controller controller;

    /**
     * Icon representation of the Tool.
     */
    private ImageIcon icon; // TODO icon setzen

    /**
     * String representation of the Tool.
     */
    private String represent; // TODO beschreibung schreiben

    /**
     * ActionListerner of the Tool on the Buttons.
     */
    private ActionListener buttonListener;

    /**
     * WorkspaceListener of the Tools on the Workspace.
     */
    private WorkspaceListener workspaceListener;

    /**
     * Constructs the ViewTool.
     * 
     * @param controller
     *            Controller component of the application.
     */
    public ViewTool(Controller controller) {
        this.controller = controller;
        view = controller.getView();
    }

    /**
     * {@inheritDoc}
     */
    public void setActive(boolean active) {
        if (active) {
            if (workspaceListener == null) {
                view.getWorkspace().addListener(workspaceListener = new WorkspaceListener() {

                    @Override
                    public void mouseReleased(WorkspaceEvent e) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void mousePressed(WorkspaceEvent e) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void mouseMoved(WorkspaceEvent e) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void mouseDragged(WorkspaceEvent e) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void mouseClicked(WorkspaceEvent e) {
                        // TODO Auto-generated method stub
                    }
                });
            } else {
                view.getWorkspace().addListener(workspaceListener);
            }
        } else {
            view.getWorkspace().removeListener(workspaceListener);
        }
    }

    /**
     * {@inheritDoc}
     */
    public ActionListener getListener() {
        if (buttonListener != null) {
            return buttonListener;
        } else {
            buttonListener = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    // TODO Auto-generated method stub
                }
            };
        }
        return buttonListener;
    }

    /**
     * {@inheritDoc}
     */
    public String getText() {
        return represent;
    }

    /**
     * {@inheritDoc}
     */
    public ImageIcon getIcon() {
        return icon;
    }
}

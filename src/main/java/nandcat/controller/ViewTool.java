package nandcat.controller;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import nandcat.view.View;
import nandcat.view.WorkspaceEvent;
import nandcat.view.WorkspaceListener;
import nandcat.view.WorkspaceListenerAdapter;

/**
 * The ViewTool is responsible for moving the display range over the Workspace.
 */
public class ViewTool implements Tool {

    /**
     * Current View instance.
     */
    private View view = null;

    /**
     * Reference to this Tool.
     */
    private Tool viewTool;

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
    private List<String> represent = new LinkedList<String>() {

        /**
         * Default uid.
         */
        private static final long serialVersionUID = 1L;
        {
            add("move");
        }
    };

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
        viewTool = this;
        view.addViewPortListener(new ChangeListener() {

            // if size is changed it my happen that former invisible elements come in sight.
            public void stateChanged(ChangeEvent e) {
                view.giveViewPortRect();
//                view.getWorkspace().redraw();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void setActive(boolean active) {
        if (active) {
            if (workspaceListener == null) {
                workspaceListener = new WorkspaceListenerAdapter() {

                    private Point offset;
                    private int dx;
                    private int dy;

                    public void mousePressed(WorkspaceEvent e) {
                        offset = e.getLocation();
                    }

                    public void mouseDragged(WorkspaceEvent e) {
                        // divide by 1.5 so it isn't soo fast
                        dx = (int) ((e.getLocation().x - offset.x)/1.5);
                        dy = (int) ((e.getLocation().y - offset.y)/1.5);
                        view.setViewportPosition(dx,dy);
                        offset = e.getLocation();
                        // redraw new elements in sight
//                        view.giveViewPortRect();
//                        view.getWorkspace().redraw();
                    }
                };
            }
            view.getWorkspace().addListener(workspaceListener);
        } else {
            view.getWorkspace().removeListener(workspaceListener);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, ActionListener> getFunctionalities() {
        buttonListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                controller.requestActivation(viewTool);
            }
        };
        Map<String, ActionListener> map = new HashMap<String, ActionListener>();
        for (String functionality : represent) {
            map.put(functionality, buttonListener);
        }
        return map;
    }

    /**
     * {@inheritDoc}
     */
    public ImageIcon getIcon() {
        return icon;
    }
}

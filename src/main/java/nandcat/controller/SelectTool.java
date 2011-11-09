package nandcat.controller;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import nandcat.model.Model;
import nandcat.view.ElementDrawer;
import nandcat.view.View;
import nandcat.view.WorkspaceEvent;
import nandcat.view.WorkspaceListener;
import nandcat.view.WorkspaceListenerAdapter;

/**
 * The SelectTool is responsible for selecting and moving Elements on the Workspace.
 */
public class SelectTool implements Tool {

    /**
     * Current Model instance.
     */
    private Model model;

    /**
     * Current Controller instance.
     */
    private Controller controller;

    /**
     * Current View instance.
     */
    private View view;

    /**
     * Icon representation of the Tool.
     */
    private ImageIcon icon; // TODO icon setzen

    /**
     * String representation of the Tool.
     */
    private List<String> represent; // TODO beschreibung schreiben

    /**
     * ActionListerner of the Tool on the Buttons.
     */
    private ActionListener buttonListener;

    /**
     * WorkspaceListener of the Tool.
     */
    private WorkspaceListener workspaceListener;

    private Rectangle rect = new Rectangle();

    /**
     * Constructs the SelectTool.
     * 
     * @param controller
     *            Controller component of the application.
     */
    public SelectTool(Controller controller) {
        this.controller = controller;
        this.view = controller.getView();
        this.model = controller.getModel();
        represent = new LinkedList<String>();
        represent.add("select");
    }

    /**
     * {@inheritDoc}
     */
    public void setActive(boolean active) {
        if (active) {
            setListeners();
        } else {
            removeListeners();
        }
    }

    /**
     * Sets a WorkspaceListener on the Workspace.
     */
    private void setListeners() {
    if (workspaceListener == null) {
            workspaceListener = new WorkspaceListenerAdapter() {
                @Override
                public void mousePressed(WorkspaceEvent e) {
                    rect = new Rectangle(e.getLocation());
                }
                public void mouseDragged(WorkspaceEvent e) {
                    selectElements(rect, e.getLocation());
                }
            };
        }
        view.getWorkspace().addListener(workspaceListener);
    }

    /**
     * Removes the Listener from the Workspace.
     */
    private void removeListeners() {
        view.getWorkspace().removeListener(workspaceListener);
    }

    /**
     * Set the Elements within the rectangle as selected and paints the selection rectangle on the workspace.
     */
    private void selectElements(Rectangle rect, Point point) {
        ElementDrawer drawer = view.getDrawer();
        
        int xCoord = Math.min(rect.x, point.x);
        int yCoord = Math.min(rect.y, point.y);
        int width = Math.abs(rect.x - point.x);
        int height = Math.abs(rect.y - point. y);
        rect.setBounds(xCoord, yCoord, width, height);

        drawer.draw(rect);
        model.selectElements(rect);
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, ActionListener> getFunctionalities() {
        buttonListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if(e.getActionCommand().equals("select")) {
                    activateTool();
                }
            }
        };
        Map<String, ActionListener> map = new HashMap<String, ActionListener>();
        for (String functionality : represent) {
            map.put(functionality, buttonListener);
        }
        return map;
        
    }
    private void activateTool() {
        controller.requestActivation(this);
    }

    /**
     * {@inheritDoc}
     */
    public ImageIcon getIcon() {
        return icon;
    }
}

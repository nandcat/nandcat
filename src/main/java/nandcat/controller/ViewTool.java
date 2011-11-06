package nandcat.controller;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import nandcat.model.Model;
import nandcat.model.element.Element;
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
     * Current Model instance.
     */
    private Model model;

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
        model = controller.getModel();
        viewTool = this;
        view.addViewPortListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                view.giveViewPortRect();
                List<Element> elem = model.getElements();
                Set<Element> elements = new HashSet<Element>();
                elements.addAll(elem);
                view.getWorkspace().redraw(elements);
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

                    public void mousePressed(WorkspaceEvent e) {
                        offset = e.getLocation();
                    }

                    public void mouseDragged(WorkspaceEvent e) {
                        // move ViewPort
                        int x = (int) (e.getLocation().getX() - offset.getX());
                        int y = (int) (e.getLocation().getY() - offset.getY());
                        Point p = new Point(x, y);
                        view.setViewportPosition(p);
                        // redraw new elements in sight
                        view.giveViewPortRect();
                        List<Element> elem = model.getElements();
                        Set<Element> elements = new HashSet<Element>();
                        elements.addAll(elem);
                        view.getWorkspace().redraw(elements);
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

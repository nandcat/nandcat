package nandcat.controller;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import nandcat.model.Model;
import nandcat.model.ViewModule;
import nandcat.model.element.DrawElement;
import nandcat.model.element.Module;
import nandcat.model.element.Port;
import nandcat.view.ElementDrawer;
import nandcat.view.View;
import nandcat.view.WorkspaceEvent;
import nandcat.view.WorkspaceListener;
import nandcat.view.WorkspaceListenerAdapter;

/**
 * The CreateTool is responsible for the creation of new Modules and Connections. They will be displayed on the Workspace and added
 * to the Model.
 */
public class CreateTool implements Tool {

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
    private List<String> represent;

    /**
     * ActionListerner of the Tool on the Buttons.
     */
    private ActionListener buttonListener;

    /**
     * WorkspaceListener of the Tool.
     */
    private WorkspaceListener workspaceListener;

    /**
     *
     */
    private ViewModule selectedModule;

    /**
     * Tolerance used if mouse clicked.
     */
    private static final Dimension MOUSE_TOLERANCE = new Dimension(2, 2);

    /**
     * Port representing the source of a new Connection.
     * NULL if the user did not click on an Element to create a Connection.
     */
    private Port sourcePort;

    /**
     * Constructs the SelectTool.
     * 
     * @param controller
     *            Controller component of the application. It contains the view and model component of the application.
     */
    public CreateTool(Controller controller) {
        this.controller = controller;
        this.view = controller.getView();
        this.model = controller.getModel();
        represent = new LinkedList<String>();
        represent.add("createButton");
        represent.add("selectModule");
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
                public void mouseClicked(WorkspaceEvent e) {
                    createElement(e.getLocation());
                }
            };
        }
        view.getWorkspace().addListener(workspaceListener);
    }

    /**
     * Creates a new Element at the given Point.
     */
    private void createElement(Point point) {
        Set<DrawElement> elementsAt = model.getDrawElementsAt(new Rectangle(point, MOUSE_TOLERANCE));

        if (elementsAt.isEmpty()) {
            if (selectedModule != null) {
                model.addModule(selectedModule, point);
            }
        } else {
            Module toConnect = null;
            for (DrawElement element : elementsAt) {
                if (element instanceof Module) {
                    toConnect = (Module) element;
                }
            }
            if (sourcePort == null) {
                /*
                ElementDrawer drawer = view.getDrawer();
                sourcePort = drawer.getPortAt(new Rectangle(point), toConnect);*/
                sourcePort = model.getPortAt(new Rectangle(point, MOUSE_TOLERANCE));
            } else {
                Port targetPort = model.getPortAt(new Rectangle(point, MOUSE_TOLERANCE));
                model.addConnection(sourcePort, targetPort);
                sourcePort = null;
            }
        }
    }

    /**
     * Removes the Listener from the Workspace.
     */
    private void removeListeners() {
        view.getWorkspace().removeListener(workspaceListener);
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, ActionListener> getFunctionalities() {
        buttonListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("createButton")) {

                    // User first has to click on CreateButton before he can add a module to the workspace
                    activateTool();
                } else if (e.getActionCommand().equals("selectModule")) {
                  if (e.getSource() instanceof JComboBox) {
                      selectedModule = (ViewModule) ((JComboBox) e.getSource()).getSelectedItem();
                  }
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

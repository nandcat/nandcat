package nandcat.controller;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
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
import nandcat.model.element.Port;
import nandcat.view.View;
import nandcat.view.WorkspaceEvent;
import nandcat.view.WorkspaceListener;
import nandcat.view.WorkspaceListenerAdapter;

/**
 * The CreateTool is responsible for the creation of new Modules and Connections. They will be displayed on the
 * Workspace and added to the Model.
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
    private ImageIcon icon;

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
    private static final Dimension GATE_TOLERANCE = new Dimension(60, 40);

    /**
     * Tolerance used if mouse clicked.
     */
    private static final Dimension MOUSE_TOLERANCE = new Dimension(10, 10);

    /**
     * Port representing the source of a new Connection. NULL if the user did not click on an Element to create a
     * Connection.
     */
    private Port sourcePort;

    /**
     * A line drawn to see where the connection will be set to.
     */
    private Line2D connectionPreview;

    /**
     * Boolean indicating if the tool is already active. Avoids double activation of it.
     */
    private boolean isYetActive = false;

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
            isYetActive = true;
        } else {
            removeListeners();
            isYetActive = false;
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

                @Override
                public void mouseMoved(WorkspaceEvent e) {
                    moveCursor(e.getLocation());
                }
            };
        }
        view.getWorkspace().addListener(workspaceListener);
    }

    /**
     * Creates a new Element at the given Point.
     */
    private void createElement(Point point) {

        Set<DrawElement> elementsAt = model.getDrawElementsAt(new Rectangle(point, GATE_TOLERANCE));

        // First check if the user clicked on an empty space on the workspace. This means they want to create a new
        // module.
        if (elementsAt.isEmpty()) {

            if (sourcePort != null) {
                sourcePort = null;
                view.getWorkspace().redraw();
            } else if (selectedModule == null) {
                selectedModule = model.getViewModules().get(0);
                model.addModule(selectedModule, point);
            } else {
                model.addModule(selectedModule, point);
            }
        } else {
            point.x -= MOUSE_TOLERANCE.height / 2;
            point.y -= MOUSE_TOLERANCE.width / 2;
            if (sourcePort == null) {
                if (model.getPortAt(new Rectangle(point, MOUSE_TOLERANCE)) != null) {
                    sourcePort = model.getPortAt(new Rectangle(point, MOUSE_TOLERANCE));
                    connectionPreview = new Line2D.Double();
                    connectionPreview.setLine(sourcePort.getCenter(), sourcePort.getCenter());
                    view.getWorkspace().redraw(connectionPreview);
                }
            } else {
                if (model.getPortAt(new Rectangle(point, MOUSE_TOLERANCE)) != null) {
                    Port targetPort = model.getPortAt(new Rectangle(point, MOUSE_TOLERANCE));
                    if (!sourcePort.isOutPort() && targetPort.isOutPort()) {
                        model.addConnection(targetPort, sourcePort);
                        sourcePort = null;
                    } else if (sourcePort.isOutPort() && !targetPort.isOutPort()) {
                        model.addConnection(sourcePort, targetPort);
                        sourcePort = null;
                    }
                }
            }
        }
    }

    /**
     * Change the cursor when its moved. Show error symbol if the cursor is at a position, where an inport would be
     * connected with another inport and vice versa. Draw a preview of the connection to be set by the user.
     * 
     * @param point
     *            Point representing the location of the mouse cursor.
     */
    private void moveCursor(Point point) {
        if (sourcePort != null) {

            // Draw a preview of the connection
            connectionPreview.setLine(sourcePort.getCenter(), point);
            view.getWorkspace().redraw(connectionPreview);

            point.x -= MOUSE_TOLERANCE.height / 2;
            point.y -= MOUSE_TOLERANCE.width / 2;

            // Inform the user if the underlying port cannot be connected to the chosen port.
            Set<DrawElement> elements = model.getDrawElementsAt(new Rectangle(point, MOUSE_TOLERANCE));
            Port portAt = model.getPortAt(new Rectangle(point, MOUSE_TOLERANCE));
            if (!elements.isEmpty() && portAt != null) {
                if ((sourcePort.isOutPort() && portAt.isOutPort()) || (!sourcePort.isOutPort() && !portAt.isOutPort())) {
                    view.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                }
            } else {
                view.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        } else {
            view.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /**
     * Removes the Listener from the Workspace.
     */
    private void removeListeners() {
        view.getWorkspace().removeListener(workspaceListener);
        sourcePort = null;
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, ActionListener> getFunctionalities() {
        buttonListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("createButton") || e.getActionCommand().equals("selectModule")) {
                    if (!isYetActive) {
                        activateTool();
                    }
                    if (e.getSource() instanceof JComboBox) {
                        selectedModule = (ViewModule) ((JComboBox) e.getSource()).getSelectedItem();
                    }
                } else if (e.getActionCommand().equals("new")) {
                    model.clearCircuit();
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

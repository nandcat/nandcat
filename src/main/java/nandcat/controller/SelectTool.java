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
import nandcat.model.Model;
import nandcat.model.element.DrawElement;
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
     * Rectangle used for selecting elements.
     */
    private Rectangle rect = new Rectangle();

    /**
     * Tolerance used if mouse clicked.
     */
    private static final Dimension MOUSE_TOLERANCE = new Dimension(1, 1);

    /**
     * Size of one Grid-Cell height and width.
     */
    private static final int GRID_SIZE = 20;

    /**
     * Boolean representing if grid is shown or not.
     */
    private boolean gridActive = false;

    /**
     * Point representing where we started to drag with the Mouse.
     */
    private Point startPoint;

    /**
     * Boolean representing if at least one Element has been selected yet.
     */
    private boolean notEmpty = false;

    /**
     * Boolean representing if we want to select or to drag.
     */
    private boolean isSelect = false;

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
        represent.add("grid");
    }

    /**
     * {@inheritDoc}
     */
    public void setActive(boolean active) {
        if (active) {
            setListeners();
        } else {
            removeListeners();
            model.deselectAll();
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
                    Set<DrawElement> elements = model
                            .getDrawElementsAt(new Rectangle(e.getLocation(), MOUSE_TOLERANCE));
                    startPoint = e.getLocation();
                    if (elements.isEmpty()) {
                        rect = new Rectangle(e.getLocation());
                        isSelect = true;
                    } else { // Mouse over a Module.
                        if (notEmpty) { // Some Modules selected yet
                            boolean oneSelected = false;
                            for (DrawElement element : elements) {
                                if (element.isSelected()) {
                                    oneSelected = true;
                                }
                            }
                            if (oneSelected) {
                                isSelect = false;
                            } else {
                                model.deselectAll();
                                notEmpty = model.selectElements(new Rectangle(e.getLocation(), MOUSE_TOLERANCE));
                                isSelect = false;
                            }
                        } else {
                            model.deselectAll();
                            notEmpty = model.selectElements(new Rectangle(e.getLocation(), MOUSE_TOLERANCE));
                            isSelect = false;
                        }
                    }
                }

                @Override
                public void mouseReleased(WorkspaceEvent e) {
                    if (gridActive) {
                        model.adaptToGrid(GRID_SIZE);
                    }
                    rect = null;
                    view.getWorkspace().redraw(rect);
                }

                @Override
                public void mouseDragged(WorkspaceEvent e) {
                    if (isSelect) {
                        selectElements(e.getLocation());
                    } else {
                        moveElements(e.getLocation());
                    }
                }

                @Override
                public void mouseClicked(WorkspaceEvent e) {
                    model.deselectAll();
                    notEmpty = model.selectElements(new Rectangle(e.getLocation(), MOUSE_TOLERANCE));
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
        model.deselectAll();
    }

    /**
     * Set the Elements within the rectangle as selected and paints the selection rectangle on the workspace.
     * 
     * @param point
     *            Point representing a edge of the Rectangle.
     */
    private void selectElements(Point point) {
        model.deselectAll();
        rect.setFrameFromDiagonal(point, this.startPoint);
        view.getWorkspace().redraw(rect);
        notEmpty = model.selectElements(rect);
    }

    /**
     * Move the selected elements to the current location of the mouse.
     * 
     * @param point
     *            Point indicating the mouse position.
     */
    private void moveElements(Point point) {
        Point offset = new Point();
        offset.x = (startPoint.x - point.x);
        offset.y = (startPoint.y - point.y);
        model.moveBy(offset);
        this.startPoint = point;
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, ActionListener> getFunctionalities() {
        buttonListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("select")) {
                    activateTool();
                } else if (e.getActionCommand().equals("grid")) {
                    boolean b = gridActive;
                    gridActive = !b;
                    activateTool();
                    view.getWorkspace().setGridEnable(gridActive, GRID_SIZE);
                    if (gridActive) {
                        model.adaptAllToGrid(GRID_SIZE);
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

    /**
     * This method requests activation at the Controller.
     */
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

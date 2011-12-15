package nandcat.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Line2D;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.swing.JPanel;
import nandcat.model.Model;
import nandcat.model.element.AndGate;
import nandcat.model.element.Circuit;
import nandcat.model.element.Connection;
import nandcat.model.element.DrawElement;
import nandcat.model.element.FlipFlop;
import nandcat.model.element.IdentityGate;
import nandcat.model.element.ImpulseGenerator;
import nandcat.model.element.Lamp;
import nandcat.model.element.Module;
import nandcat.model.element.NotGate;
import nandcat.model.element.OrGate;

/**
 * Workspace.
 * 
 * The Space the user works on, here he can place the Elements. It holds the ElementDrawer and is says him which
 * elements to be painted. It also is responsible for notifying the Classes which have implemented the
 * WorkspaceListener.
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
     * A set of all workspace listeners on the workspace. The listener informs the implementing class about changes of
     * the workspace.
     */
    private Set<WorkspaceListener> listeners = new LinkedHashSet<WorkspaceListener>();

    /**
     * MouseListener of the Workspace on itself.
     */
    private MouseAdapter mouseListener;

    /**
     * Listener of the Workspace on itself on Mouse.
     */
    private MouseWheelListener mouseWheelListener;

    /**
     * Rectangle representing the Position and Boundaries of the ViewPort.
     */
    private Rectangle viewPortRect;

    /**
     * Model instance.
     */
    private Model model;

    /**
     * Rectangle to draw while selecting Elements.
     */
    private Rectangle selectRect;

    /**
     * Line2D to draw while trying to place a connection.
     */
    private Line2D connectLine;

    /**
     * View instance.
     */
    private View view;

    /**
     * WindowAdapter listening on Window Events of the view.
     */
    private WindowAdapter windowListener;

    /**
     * Boolean representing if grid is active or not.
     */
    private boolean grid = false;

    /**
     * Integer representing the Size of a Grid Cell.
     */
    private int gridSize;

    /**
     * Representing if a Shadow of a Element is drawn or not.
     */
    private boolean shadow;

    /**
     * Constructs the workspace.
     * 
     * @param model
     *            Model instance.
     * @param view
     *            View instance.
     */
    public Workspace(Model model, View view) {
        this.model = model;
        this.view = view;
        setupWorkspace();
        mouseListener = new MouseAdapter() {

            public void mouseExited(MouseEvent e) {
                selectRect = null;
                connectLine = null;
                repaint();
            }

            public void mouseMoved(MouseEvent e) {
                notifyMouseMoved(e);
            }

            public void mouseDragged(MouseEvent e) {
                notifyMouseDragged(e);
            }

            public void mousePressed(MouseEvent e) {
                notifyMousePressed(e);
            }

            public void mouseReleased(MouseEvent e) {
                notifyMouseReleased(e);
            }

            public void mouseClicked(MouseEvent e) {
                notifyMouseClicked(e);
            }
        };
        mouseWheelListener = new MouseWheelListener() {

            public void mouseWheelMoved(MouseWheelEvent e) {
                notifyMouseWheelMoved(e);
            }
        };
        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);
        this.addMouseWheelListener(mouseWheelListener);
        windowListener = new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                notifyWindowClosing(e);
            }
        };
        view.addWindowListener(windowListener);
    }

    /**
     * set up the Workspace.
     */
    private void setupWorkspace() {
        this.elementDrawer = new StandardElementDrawer();
    }

    /**
     * Redraws the workspace with its elements. By calling the repaint() method.
     */
    public void redraw() {
        repaint();
    }

    /**
     * Redraws the workspace with its elements. By calling the repaint() method. And sets a rectangle which represents
     * the selecting area.
     * 
     * @param rect
     *            Rectangle size and position of the "seeking-rectangle".
     */
    public void redraw(Rectangle rect, boolean shadow) {
        this.shadow = shadow;
        this.selectRect = rect;
        repaint();
    }

    /**
     * Redraws the workspace with its elements. By calling the repaint() method. And sets a connectLine which represents
     * the Connection which the user tries to place but is not established yet.
     * 
     * @param line
     *            Line2D line which represents the connectionLine while trying to set the connection.
     */
    public void redraw(Line2D line) {
        this.connectLine = line;
        repaint();
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
     * Returns the current ElementDrawer.
     * 
     * @return elementDrawer, ElementDrawer
     */
    public ElementDrawer getDrawer() {
        return elementDrawer;
    }

    /**
     * Sets the ElementDrawer.
     * 
     * @param drawer
     *            ElementDrawer, Class implementing the ElementDrawer interface.
     */
    public void setDrawer(ElementDrawer drawer) {
        this.elementDrawer = drawer;
    }

    /**
     * Sets the Rectangle representing the Boundaries and Position of the ViewPort.
     * 
     * @param rect
     *            Rectangle to be set.
     */
    public void setViewPortRect(Rectangle rect) {
        this.viewPortRect = rect;
        // if size of visible part is bigger than the workspace by itself it must be extended
        if (viewPortRect.getWidth() >= this.getWidth()) {
            view.setWorkspaceWidth((int) viewPortRect.getWidth());
        }
        if (viewPortRect.getHeight() >= this.getHeight()) {
            view.setWorkspaceHeight((int) viewPortRect.getHeight());
        }
    }

    /**
     * Setter for the Grid.
     * 
     * @param grid
     *            Boolean if grid is enabeld or disabled.
     * @param gridSize
     *            integer the size of one Grid Cell
     */
    public void setGridEnable(boolean grid, int gridSize) {
        this.grid = grid;
        this.gridSize = gridSize;
        repaint();
    }

    /**
     * Getter if grid is enabled or not.
     * 
     * @return boolean true if grid is active false else.
     */
    public boolean getGridEnable() {
        return grid;
    }

    /**
     * Getter for the Size of the Grid.
     * 
     * @return Int the Size of one Grid-Cell.
     */
    public int getGridSize() {
        return gridSize;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paint(g2);
        if (grid) {
            g2.setColor(Color.lightGray);
            for (int i = gridSize; i < this.getWidth(); i += gridSize) {
                g2.drawLine(i, 0, i, this.getHeight());
            }
            for (int i = gridSize; i < this.getWidth(); i += gridSize) {
                g2.drawLine(0, i, this.getWidth(), i);
            }
        }
        elementDrawer.setGraphics(g2);
        List<DrawElement> elementsToDraw = model.getDrawElements();
        List<Connection> cachedConnections = new LinkedList<Connection>();
        for (DrawElement elem : elementsToDraw) {
            if (isInView(elem)) {
                if (elem instanceof Connection) {
                    cachedConnections.add((Connection) elem);
                } else if (elem instanceof AndGate) {
                    elementDrawer.draw((AndGate) elem);
                } else if (elem instanceof FlipFlop) {
                    elementDrawer.draw((FlipFlop) elem);
                } else if (elem instanceof Circuit) {
                    elementDrawer.draw((Circuit) elem);
                } else if (elem instanceof ImpulseGenerator) {
                    elementDrawer.draw((ImpulseGenerator) elem);
                } else if (elem instanceof IdentityGate) {
                    elementDrawer.draw((IdentityGate) elem);
                } else if (elem instanceof NotGate) {
                    elementDrawer.draw((NotGate) elem);
                } else if (elem instanceof OrGate) {
                    elementDrawer.draw((OrGate) elem);
                } else if (elem instanceof Lamp) {
                    elementDrawer.draw((Lamp) elem);
                }
            }
        }
        for (Connection connection : cachedConnections) {
            elementDrawer.draw(connection);
        }
        if (selectRect != null) {
            if (shadow) {
                g2.setColor(Color.gray);
                g2.drawRect(selectRect.x, selectRect.y, selectRect.width, selectRect.height);
            } else {
                elementDrawer.draw(selectRect);
            }
            g2.setColor(Color.black);
        }
        if (connectLine != null) {
            elementDrawer.draw(connectLine);
        }
    }

    /**
     * Notifies listeners of mouse click events on the workspace.
     * 
     * @param altE
     *            the MouseEvent received from the MouseListener.
     */
    private void notifyMouseClicked(MouseEvent altE) {
        WorkspaceEvent e = new WorkspaceEvent();
        e.setLocation(altE.getPoint());
        e.setButton(altE.getButton());
        e.setShiftDown(altE.isShiftDown());
        for (WorkspaceListener l : listeners) {
            l.mouseClicked(e);
        }
    }

    /**
     * Notifies listeners of mouse pressed events on the workspace.
     * 
     * @param altE
     *            the MouseEvent received from the MouseListener.
     */
    private void notifyMousePressed(MouseEvent altE) {
        WorkspaceEvent e = new WorkspaceEvent();
        e.setLocation(altE.getPoint());
        e.setButton(altE.getButton());
        e.setShiftDown(altE.isShiftDown());
        for (WorkspaceListener l : listeners) {
            l.mousePressed(e);
        }
    }

    /**
     * Notifies listeners of mouse released events on the workspace.
     * 
     * @param altE
     *            the MouseEvent received from the MouseListener.
     */
    private void notifyMouseReleased(MouseEvent altE) {
        WorkspaceEvent e = new WorkspaceEvent();
        e.setLocation(altE.getPoint());
        e.setShiftDown(altE.isShiftDown());
        selectRect = null;
        connectLine = null;
        for (WorkspaceListener l : listeners) {
            l.mouseReleased(e);
        }
    }

    /**
     * Notifies listeners of mouse moved events on the workspace.
     * 
     * @param altE
     *            the MouseEvent received from the MouseListener.
     */
    private void notifyMouseMoved(MouseEvent altE) {
        WorkspaceEvent e = new WorkspaceEvent();
        e.setLocation(altE.getPoint());
        for (WorkspaceListener l : listeners) {
            l.mouseMoved(e);
        }
    }

    /**
     * Notifies listeners of mouse dragged events on the workspace.
     * 
     * @param altE
     *            the MouseEvent received from the MouseListener.
     */
    private void notifyMouseDragged(MouseEvent altE) {
        WorkspaceEvent e = new WorkspaceEvent();
        e.setLocation(altE.getPoint());
        e.setButton(altE.getButton());
        e.setShiftDown(altE.isShiftDown());
        for (WorkspaceListener l : listeners) {
            l.mouseDragged(e);
        }
    }

    /**
     * Notifies listeners when the Window-Close Button was pushed.
     * 
     * @param altE
     *            the WindowEvent received from WindowAdapter.
     */
    private void notifyWindowClosing(WindowEvent altE) {
        for (WorkspaceListener l : listeners) {
            l.windowClosing();
        }
    }

    /**
     * Notifies Listeners when the MouseWheel was moved.
     * 
     * @param altE
     *            MouseWheelEvent from the MouseWheelListener.
     */
    private void notifyMouseWheelMoved(MouseWheelEvent altE) {
        WorkspaceEvent e = new WorkspaceEvent();
        e.setWheelRotation(altE.getWheelRotation());
        e.setShiftDown(altE.isShiftDown());
        for (WorkspaceListener l : listeners) {
            l.mouseWheelMoved(e);
        }
    }

    /*
     * Not needed at the Moment but maybe later useful.
     */
    /**
     * Checks if an Element is inside or intersects the ViewPort. Helps to decide whether or not to draw the Elements.
     * Elements out of sight must no be painted.
     * 
     * @param elem
     *            the Element to be checked
     * @return True or False. Whether the element is in sight or not.
     */
    private boolean isInView(DrawElement elem) {
        boolean isInView = false;
        if (elem instanceof Connection) {
            // Connections must be painted if one of the Modules it connects is in sight.
            if (viewPortRect.intersects(((Connection) elem).getInPort().getRectangle())) {
                isInView = true;
            } else if (viewPortRect.intersects(((Connection) elem).getOutPort().getRectangle())) {
                isInView = true;
            } else if (viewPortRect.contains(((Connection) elem).getInPort().getRectangle())) {
                isInView = true;
            } else if (viewPortRect.contains(((Connection) elem).getOutPort().getRectangle())) {
                isInView = true;
            }
        }
        if (elem instanceof Module) {
            // Modules must be painted if they are (partly) inside.
            if (viewPortRect.intersects(((Module) elem).getRectangle())) {
                isInView = true;
            } else if (viewPortRect.contains(((Module) elem).getRectangle())) {
                isInView = true;
            }
        }
        return isInView;
    }
}

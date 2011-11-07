package nandcat.view;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.JPanel;
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
     * Rectangle representing the Position and Boundaries of the ViewPort.
     */
    private Rectangle viewPortRect;

    /**
     * Constructs the workspace.
     */
    public Workspace() {
        setupWorkspace();
        mouseListener = new MouseAdapter() {

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
        this.addMouseListener(mouseListener);
    }

    /**
     * set up the Workspace.
     */
    private void setupWorkspace() {
        this.elementDrawer = new StandardElementDrawer();
        elementDrawer.setGraphics(this.getGraphics());
    }

    /**
     * Redraws the workspace with its elements. Checks if elements are in view (Means inside the ViewPort).
     * 
     * @param elements
     *            Set<Elements> with the elements to be redrawed.
     */
    public void redraw(Set<DrawElement> elements) {
        for (DrawElement elem : elements) {
            if (isInView(elem)) {
                if (elem instanceof Connection) {
                    elementDrawer.draw((Connection) elem);
                } else if (elem instanceof AndGate) {
                    elementDrawer.draw((AndGate) elem);
                } else if (elem instanceof Circuit) {
                    elementDrawer.draw((Circuit) elem);
                } else if (elem instanceof FlipFlop) {
                    elementDrawer.draw((FlipFlop) elem);
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
        elementDrawer.setGraphics(this.getGraphics());
    }

    /**
     * Sets the Rectangle representing the Boundaries and Position of the ViewPort.
     * 
     * @param rect
     *            Rectangle to be set.
     */
    public void setViewPortRect(Rectangle rect) {
        this.viewPortRect = rect;
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
        for (WorkspaceListener l : listeners) {
            l.mouseDragged(e);
        }
    }

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
            if (viewPortRect.intersects(((Connection) elem).getNextModule().getRectangle())) {
                isInView = true;
            } else if (viewPortRect.intersects(((Connection) elem).getPreviousModule().getRectangle())) {
                isInView = true;
            } else if (viewPortRect.contains(((Connection) elem).getPreviousModule().getRectangle())) {
                isInView = true;
            } else if (viewPortRect.contains(((Connection) elem).getNextModule().getRectangle())) {
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

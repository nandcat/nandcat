package nandcat.controller;

import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.ActiveEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.MenuComponent;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import java.util.WeakHashMap;
import javax.help.CSH;
import javax.help.DefaultHelpBroker;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.Map.ID;
import javax.help.Popup;
import javax.help.Presentation;
import javax.help.WindowPresentation;
import javax.swing.CellRendererPane;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.TreePath;

/**
 * This class extends the CSH class from the javax.help package. It makes sure that there is context-sensitive help in
 * the checkmanager.
 */
public class Help extends CSH {

    /**
     * Map containing parents of the object the user clicked on.
     */
    private static Map<Object, Object> parents;

    /**
     * Generic displayHelp for all CSH.Display* subclasses.
     * 
     * @param hb
     *            The HelpBroker to display in. Can be null but hs and and presentation must be supplies
     * @param hs
     *            The HelpSet to display in. Can be null if hb != null
     * @param presentation
     *            The Presentation class to display the content in
     * @param presentationName
     *            The named presentation to modify the Presentation class with. In some Presenations this is also a
     *            "named" Presentation.
     * @param obj
     *            The object for which the help is displayed for
     * @param source
     *            The Window for focusOwner purposes
     * @param event
     *            The event that caused this action.
     */
    private static void displayHelp(HelpBroker hb, HelpSet hs, String presentation, String presentationName,
            Object obj, Object source, AWTEvent event) {

        Presentation pres = null;

        if (hb != null) {
            // Start by setting the ownerWindow in the HelpBroker
            if (hb instanceof DefaultHelpBroker) {
                ((DefaultHelpBroker) hb).setActivationObject(source);
            }
        } else {
            // using a Presentation
            // Get a Presentation
            ClassLoader loader;
            Class<?> klass;
            Class<?>[] types = { HelpSet.class, String.class };
            Object[] args = { hs, presentationName };
            try {
                loader = hs.getLoader();
                if (loader == null) {
                    klass = Class.forName(presentation);
                } else {
                    klass = loader.loadClass(presentation);
                }
                Method m = klass.getMethod("getPresentation", types);
                pres = (Presentation) m.invoke(null, args);
            } catch (Exception ex) {
                throw new RuntimeException("error invoking presentation");
            }

            if (pres == null) {
                return;
            }

            if (pres instanceof WindowPresentation) {
                ((WindowPresentation) pres).setActivationObject(source);
            }
            if (pres instanceof Popup && obj instanceof Component) {
                ((Popup) pres).setInvoker((Component) obj);
            }
        }

        // OK now do the CSH stuff
        String helpID = null;
        HelpSet objHS = null;
        helpID = CSH.getHelpIDString(obj, event);
        objHS = CSH.getHelpSet(obj, event);
        if (objHS == null) {
            if (hb != null) {
                objHS = hb.getHelpSet();
            } else {
                objHS = hs;
            }
        }
        try {
            ID id = ID.create(helpID, objHS);
            if (id == null) {
                id = objHS.getHomeID();
            }
            if (hb != null) {
                hb.setCurrentID(id);
                hb.setDisplayed(true);
            } else {
                pres.setCurrentID(id);
                pres.setDisplayed(true);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /**
     * Context Sensitive Event Tracking
     * 
     * Creates a new EventDispatchThread from which to dispatch events. This method returns when stopModal is invoked.
     * 
     * @return MouseEvent The mouse event occurred. Null if cancelled on an undetermined object.
     */
    private static MouseEvent getMouseEvent() {
        // Should the cursor change to a quesiton mark here or
        // require the user to change the cursor externally to this method?
        // The problem is that each component can have it's own cursor.
        // For that reason it might be better to have the user change the
        // cusor rather than us.

        // To track context-sensitive events get the event queue and process
        // the events the same way EventDispatchThread does. Filter out
        // ContextSensitiveEvents SelectObject & Cancel (MouseDown & ???).
        // Note: This code only handles mouse events. Accessiblity might
        // require additional functionality or event trapping

        // If the eventQueue can't be retrieved, the thread gets interrupted,
        // or the thread isn't a instanceof EventDispatchThread then return
        // a null as we won't be able to trap events.
        try {
            if (EventQueue.isDispatchThread()) {
                EventQueue eq = null;

                // Find the eventQueue. If we can't get to it then just return
                // null since we won't be able to trap any events.

                try {
                    eq = Toolkit.getDefaultToolkit().getSystemEventQueue();
                } catch (Exception ee) {
                    debug(ee);
                }

                // Safe guard
                if (eq == null) {
                    return null;
                }

                int eventNumber = -1;

                // Process the events until an object has been selected or
                // the context-sensitive search has been canceled.
                while (true) {
                    // This is essentially the body of EventDispatchThread
                    // modified to trap context-senstive events and act
                    // appropriately
                    eventNumber++;
                    AWTEvent event = eq.getNextEvent();
                    Object src = event.getSource();
                    // can't call eq.dispatchEvent
                    // so I pasted it's body here

                    // debug(event);

                    // Not sure if I should suppress ActiveEvents or not
                    // Modal dialogs do. For now we will not suppress the
                    // ActiveEvent events

                    if (event instanceof ActiveEvent) {
                        ((ActiveEvent) event).dispatch();
                        continue;
                    }

                    if (src instanceof Component) {
                        // Trap the context-sensitive events here
                        if (event instanceof KeyEvent) {
                            KeyEvent e = (KeyEvent) event;
                            // if this is the cancel key then exit
                            // otherwise pass all other keys up
                            if (e.getKeyCode() == KeyEvent.VK_CANCEL || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                                e.consume();
                                return null;
                            } else {
                                e.consume();
                                // dispatchEvent(event);
                            }
                        } else if (event instanceof MouseEvent) {
                            MouseEvent e = (MouseEvent) event;
                            int eID = e.getID();
                            if ((eID == MouseEvent.MOUSE_CLICKED || eID == MouseEvent.MOUSE_PRESSED || eID == MouseEvent.MOUSE_RELEASED)
                                    && SwingUtilities.isLeftMouseButton(e)) {
                                if (eID == MouseEvent.MOUSE_CLICKED) {
                                    if (eventNumber == 0) {
                                        dispatchEvent(event);
                                        continue;
                                    }
                                }
                                e.consume();
                                return e;
                            } else {
                                e.consume();
                            }
                        } else {
                            dispatchEvent(event);
                        }
                    } else if (src instanceof MenuComponent) {
                        if (event instanceof InputEvent) {
                            ((InputEvent) event).consume();
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            debug(e);
        }
        debug("Fall Through code");
        return null;
    }

    /**
     * Dispatch a given event.
     * 
     * @param event
     *            The event to be dispatched.
     */
    private static void dispatchEvent(AWTEvent event) {
        Object src = event.getSource();
        if (event instanceof ActiveEvent) {
            // This could become the sole method of dispatching in time.
            ((ActiveEvent) event).dispatch();
        } else if (src instanceof Component) {
            ((Component) src).dispatchEvent(event);
        } else if (src instanceof MenuComponent) {
            ((MenuComponent) src).dispatchEvent(event);
        }
    }

    /**
     * Gets the highest visible component in a ancestor hierarchy at specific x,y coordinates.
     * 
     * @param parent
     *            Object representing parent of component.
     * @param x
     *            int representing x-coordinate.
     * @param y
     *            int representing y-coordinate.
     * @return Object representing deepest object at the given position.
     */
    private static Object getDeepestObjectAt(Object parent, int x, int y) {
        if (parent instanceof Container) {
            Container cont = (Container) parent;
            // use a copy of 1.3 Container.findComponentAt
            Component child = findComponentAt(cont, cont.getWidth(), cont.getHeight(), x, y);
            if (child != null && child != cont) {
                if (child instanceof JRootPane) {
                    JLayeredPane lp = ((JRootPane) child).getLayeredPane();
                    Rectangle b = lp.getBounds();
                    child = (Component) getDeepestObjectAt(lp, x - b.x, y - b.y);
                }
                if (child != null) {
                    return child;
                }
            }
        }
        // if the parent is not a Container then it might be a MenuItem.
        // But even if it isn't a MenuItem just return the parent because
        // that's a close as we can come.
        return parent;
    }

    /**
     * Find a component at a specific position.
     * 
     * @param cont
     *            Container of the component.
     * @param width
     *            int representing width of the component.
     * @param height
     *            int representing height of the component.
     * @param x
     *            int representing x-coordinate.
     * @param y
     *            int representing y-coordinate.
     * @return Component at the given position.
     */
    private static Component findComponentAt(Container cont, int width, int height, int x, int y) {
        synchronized (cont.getTreeLock()) {

            if (!((x >= 0) && (x < width) && (y >= 0) && (y < height) && cont.isVisible() && cont.isEnabled())) {
                return null;
            }

            Component[] component = cont.getComponents();
            int ncomponents = cont.getComponentCount();

            // Two passes: see comment in sun.awt.SunGraphicsCallback
            for (int i = 0; i < ncomponents; i++) {
                Component comp = component[i];
                Rectangle rect = null;

                if (comp instanceof CellRendererPane) {
                    Component c = getComponentAt((CellRendererPane) comp, x, y);
                    if (c != null) {
                        rect = getRectangleAt((CellRendererPane) comp, x, y);
                        comp = c;
                    }
                }

                if (comp != null && !comp.isLightweight()) {
                    if (rect == null || rect.width == 0 || rect.height == 0) {
                        rect = comp.getBounds();
                    }
                    if (comp instanceof Container) {
                        comp = findComponentAt((Container) comp, rect.width, rect.height, x - rect.x, y - rect.y);
                    } else {
                        comp = comp.getComponentAt(x - rect.x, y - rect.y);
                    }
                    if (comp != null && comp.isVisible() && comp.isEnabled()) {
                        return comp;
                    }
                }
            }

            for (int i = 0; i < ncomponents; i++) {
                Component comp = component[i];
                Rectangle rect = null;

                if (comp instanceof CellRendererPane) {
                    Component c = getComponentAt((CellRendererPane) comp, x, y);
                    if (c != null) {
                        rect = getRectangleAt((CellRendererPane) comp, x, y);
                        comp = c;
                    }
                }

                if (comp != null && comp.isLightweight()) {
                    if (rect == null || rect.width == 0 || rect.height == 0) {
                        rect = comp.getBounds();
                    }
                    if (comp instanceof Container) {
                        comp = findComponentAt((Container) comp, rect.width, rect.height, x - rect.x, y - rect.y);
                    } else {
                        comp = comp.getComponentAt(x - rect.x, y - rect.y);
                    }
                    if (comp != null && comp.isVisible() && comp.isEnabled()) {
                        return comp;
                    }
                }
            }
            return cont;
        }
    }

    /**
     * Returns the Rectangle enclosing component part that the component provided by renderer will be draw into.
     * 
     * @param cont
     *            CellRendererPain providing the component.
     * @param x
     *            int representing x-coordinate.
     * @param y
     *            int representing y-coordinate.
     * @return Rectangle at the given position.
     */
    private static Rectangle getRectangleAt(CellRendererPane cont, int x, int y) {
        Rectangle rect = null;
        Container c = cont.getParent();
        // I can process only this four components at present time
        if (c instanceof JTable) {
            rect = getRectangleAt((JTable) c, x, y);
        } else if (c instanceof JTableHeader) {
            rect = getRectangleAt((JTableHeader) c, x, y);
        } else if (c instanceof JTree) {
            rect = getRectangleAt((JTree) c, x, y);
        } else if (c instanceof JList) {
            rect = getRectangleAt((JList) c, x, y);
        }
        return rect;
    }

    /**
     * Number of parent objects.
     */
    private static final int PARENTS_SIZE = 4;

    /**
     * Returns the Component provided by Renderer at x, y coordinates.
     * 
     * @param cont
     *            CellRendererPane providing the component.
     * @param x
     *            int representing x-coordinate.
     * @param y
     *            int representing y-coordinate.
     * @return Component at the given position.
     */
    private static Component getComponentAt(CellRendererPane cont, int x, int y) {
        Component comp = null;
        Container c = cont.getParent();
        // I can process only this four components at present time
        if (c instanceof JTable) {
            comp = getComponentAt((JTable) c, x, y);
        } else if (c instanceof JTableHeader) {
            comp = getComponentAt((JTableHeader) c, x, y);
        } else if (c instanceof JTree) {
            comp = getComponentAt((JTree) c, x, y);
        } else if (c instanceof JList) {
            comp = getComponentAt((JList) c, x, y);
        }

        // store reference from comp to CellRendererPane
        // It is needed for backtrack searching of HelpSet and HelpID
        // in getHelpSet() and getHelpIDString().
        if (comp != null) {
            if (parents == null) {
                // WeakHashMap of WeakReferences
                parents = new WeakHashMap<Object, Object>(PARENTS_SIZE) {

                    public Object put(Object key, Object value) {
                        return super.put(key, new WeakReference<Object>(value));
                    }

                    public Object get(Object key) {
                        WeakReference<?> wr = (WeakReference<?>) super.get(key);
                        if (wr != null) {
                            return wr.get();
                        } else {
                            return null;
                        }
                    }
                };
            }
            parents.put(comp, cont);
        }
        return comp;
    }

    /**
     * Get the rectangle at a specific position.
     * 
     * @param header
     *            JTableHeader providing the rectangle.
     * @param x
     *            int representing x-coordinate.
     * @param y
     *            int representing y-coordinate.
     * @return Rectangle at the given position.
     */
    private static Rectangle getRectangleAt(JTableHeader header, int x, int y) {
        Rectangle rect = null;
        try {
            int column = header.columnAtPoint(new Point(x, y));
            rect = header.getHeaderRect(column);
        } catch (Exception e) {
            debug(e);
        }
        return rect;
    }

    /**
     * Get a component at a specific position.
     * 
     * @param header
     *            JTableHeader providing the component.
     * @param x
     *            int representing x-coordinate.
     * @param y
     *            int representing y-coordinate.
     * @return Component at the given position.
     */
    private static Component getComponentAt(JTableHeader header, int x, int y) {
        try {

            if (!(header.contains(x, y) && header.isVisible() && header.isEnabled())) {
                return null;
            }

            TableColumnModel columnModel = header.getColumnModel();
            int columnIndex = columnModel.getColumnIndexAtX(x);
            TableColumn column = columnModel.getColumn(columnIndex);

            TableCellRenderer renderer = column.getHeaderRenderer();
            if (renderer == null) {
                renderer = header.getDefaultRenderer();
            }

            return renderer.getTableCellRendererComponent(header.getTable(), column.getHeaderValue(), false, false, -1,
                    columnIndex);

        } catch (Exception e) {
            // NullPointerException in case of (header == null) or (columnModel == null)
            // ArrayIndexOutOfBoundsException from getColumn(columnIndex)
            debug(e);
        }
        return null;
    }

    /**
     * Get a rectangle at a specific position.
     * 
     * @param table
     *            JTable providing the rectangle.
     * @param x
     *            int representing x-coordinate.
     * @param y
     *            int representing y-coordinate.
     * @return Rectangle at the given position.
     */
    private static Rectangle getRectangleAt(JTable table, int x, int y) {
        Rectangle rect = null;
        try {
            Point point = new Point(x, y);
            int row = table.rowAtPoint(point);
            int column = table.columnAtPoint(point);
            rect = table.getCellRect(row, column, true);
        } catch (Exception e) {
            debug(e);
        }
        return rect;
    }

    /**
     * Get a component at a specific position.
     * 
     * @param table
     *            JTable providing the component.
     * @param x
     *            int representing x-coordinate.
     * @param y
     *            int representing y-coordinate.
     * @return Component at the given position.
     */
    private static Component getComponentAt(JTable table, int x, int y) {
        try {

            if (!(table.contains(x, y) && table.isVisible() && table.isEnabled())) {
                return null;
            }

            Point point = new Point(x, y);
            int row = table.rowAtPoint(point);
            int column = table.columnAtPoint(point);

            if (table.isEditing() && table.getEditingRow() == row && table.getEditingColumn() == column) {
                // Pointed component is provided by TableCellEditor. Editor
                // component is part of component hierarchy so it is checked
                // directly in loop in findComponentAt()
                // comp = table.getEditorComponent();
                return null;
            }

            TableCellRenderer renderer = table.getCellRenderer(row, column);
            return table.prepareRenderer(renderer, row, column);

        } catch (Exception e) {
            debug(e);
        }
        return null;
    }

    /**
     * Get a rectangle at a specific position.
     * 
     * @param tree
     *            JTree providing the rectangle.
     * @param x
     *            int representing x-coordinate.
     * @param y
     *            int representing y-coordinate.
     * @return Rectangle at the given position.
     */
    private static Rectangle getRectangleAt(JTree tree, int x, int y) {
        Rectangle rect = null;
        try {
            TreePath path = tree.getPathForLocation(x, y);
            rect = tree.getPathBounds(path);
        } catch (Exception e) {
            debug(e);
        }
        return rect;
    }

    /**
     * Get a component at a specific position.
     * 
     * @param tree
     *            JTree providing the component.
     * @param x
     *            int representing x-coordinate.
     * @param y
     *            int representing y-coordinate.
     * @return Component at the given position.
     */
    private static Component getComponentAt(JTree tree, int x, int y) {
        try {

            TreePath path = tree.getPathForLocation(x, y);

            if (tree.isEditing() && tree.getSelectionPath() == path) {
                return null;
            }

            int row = tree.getRowForPath(path);
            Object value = path.getLastPathComponent();
            boolean isSelected = tree.isRowSelected(row);
            boolean isExpanded = tree.isExpanded(path);
            boolean isLeaf = tree.getModel().isLeaf(value);
            boolean hasFocus = tree.hasFocus() && tree.getLeadSelectionRow() == row;

            return tree.getCellRenderer().getTreeCellRendererComponent(tree, value, isSelected, isExpanded, isLeaf,
                    row, hasFocus);

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get a rectangle at a specific position.
     * 
     * @param list
     *            JList providing the rectangle.
     * @param x
     *            int representing x-coordinate.
     * @param y
     *            int representing y-coordinate.
     * @return Rectangle at the given position.
     */
    private static Rectangle getRectangleAt(JList list, int x, int y) {
        Rectangle rect = null;
        try {
            int index = list.locationToIndex(new Point(x, y));
            rect = list.getCellBounds(index, index);
        } catch (Exception e) {
            debug(e);
        }
        return rect;
    }

    /**
     * Get a specific component at a point.
     * 
     * @param list
     *            JList
     * @param x
     *            Int representing x-coordinate.
     * @param y
     *            Int representing y-coordinate.
     * @return Component at specified position
     */
    private static Component getComponentAt(JList list, int x, int y) {
        try {

            int index = list.locationToIndex(new Point(x, y));
            Object value = list.getModel().getElementAt(index);
            boolean isSelected = list.isSelectedIndex(index);
            boolean hasFocus = list.hasFocus() && list.getLeadSelectionIndex() == index;

            return list.getCellRenderer().getListCellRendererComponent(list, value, index, isSelected, hasFocus);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * An ActionListener that displays help on a selected object after tracking context-sensitive events. It is normally
     * activated from a button. It uses CSH.trackingCSEvents to track context-sensitive events and when an object is
     * selected it gets the helpID for the object and displays the helpID in the help viewer.
     */
    public static class DisplayHelpAfterTracking implements ActionListener {

        /**
         * The HelpBroker.
         */
        private HelpBroker hb = null;

        /**
         * The HelpSet.
         */
        private HelpSet hs = null;

        /**
         * Type of presentation gives as String.
         */
        private String presentation = null;

        /**
         * Name of presentation type.
         */
        private String presentationName = null;

        /**
         * Constructor for DisplayHelpAfterTracking.
         * 
         * @param hb
         *            HelpBroker for displaying help.
         */
        public DisplayHelpAfterTracking(HelpBroker hb) {
            if (hb == null) {
                throw new NullPointerException("hb");
            }
            this.hb = hb;
        }

        /**
         * Create a DisplayHelpAfterTracking actionListener for a given HelpSet. Display the results in specific
         * Presentation of given PresentationName.
         * 
         * @param hs
         *            A valid HelpSet.
         * @param presentation
         *            A valid javax.help.Presentation class. Throws an IllegalArgumentException if the presentation
         *            class cannot instantiated.
         * @param presentationName
         *            The name of the presentation. This will retrieve the presentation details from the HelpSet hs if
         *            one exists. For some Presentation this name will also indicate the "named" Presentation to display
         *            the information in.
         */
        public DisplayHelpAfterTracking(HelpSet hs, String presentation, String presentationName) {
            if (hs == null) {
                throw new NullPointerException("hs");
            }

            ClassLoader loader;
            try {
                loader = hs.getLoader();
                if (loader == null) {
                    debug("Loader is null");
                } else {
                    debug("Could not get loader");
                }
            } catch (Exception ex) {
                throw new IllegalArgumentException(presentation + "presentation  invalid");
            }

            this.presentation = presentation;
            this.presentationName = presentationName;
            this.hs = hs;
        }

        /**
         * Method executed after action occurred on workspace.
         * 
         * @param e
         *            ActionEvent generated by action.
         */
        public void actionPerformed(ActionEvent e) {
            Cursor onItemCursor;

            // Make sure that LAF is installed.
            // It is necessery for UIManager.get("HelpOnItemCursor");
            // SwingHelpUtilities.installUIDefaults();

            // Get the onItemCursor
            onItemCursor = (Cursor) UIManager.get("HelpOnItemCursor");
            if (onItemCursor == null) {
                return;
            }

            // change all the cursors on all windows
            Vector<Component> topComponents = null;
            cursors = null;

            if (onItemCursor != null) {
                cursors = new Hashtable<Component, Cursor>();
                topComponents = getTopContainers(e.getSource());
                Enumeration<Component> enum1 = topComponents.elements();
                while (enum1.hasMoreElements()) {
                    setAndStoreCursors((Container) enum1.nextElement(), onItemCursor);
                }
            }

            MouseEvent event = Help.getMouseEvent();
            debug("CSH.getMouseEvent() >>> " + event);

            if (event != null) {

                Object obj = Help.getDeepestObjectAt(event.getSource(), event.getX(), event.getY());

                if (obj != null) {
                    if (obj instanceof JMenuItem) {
                        while (!(obj instanceof JPanel)) {
                            obj = ((Component) obj).getParent();
                        }
                    }
                    displayHelp(hb, hs, presentation, presentationName, obj, e.getSource(), event);
                }
            }

            // restore the old cursors
            if (topComponents != null) {
                Enumeration<Component> containers = topComponents.elements();
                while (containers.hasMoreElements()) {
                    resetAndRestoreCursors((Container) containers.nextElement());
                }
            }
            cursors = null;
        }

        /**
         * 
         */
        private Hashtable<Component, Cursor> cursors;

        /**
         * Get all top level containers to change it's cursors.
         * 
         * @param source
         *            Object of which containers are taken.
         * @return Vector of Components of object's top level containers.
         */
        private static Vector<Component> getTopContainers(Object source) {
            // This method is used to obtain all top level components of application
            // for which the changing of cursor to question mark is wanted.
            // Method Frame.getFrames() is used to get list of Frames and
            // Frame.getOwnedWindows() method on elements of the list
            // returns all Windows, Dialogs etc. It works correctly in application.
            // Problem is in applets. There is no way how to get reference to applets
            // from elsewhere than applet itself. So, if request for CSH (this means
            // pressing help button or select help menu item) does't come from component
            // in a Applet, cursor for applets is not changed to question mark. Only for
            // Frames, Windows and Dialogs is cursor changed properly.

            Vector<Component> containers = new Vector<Component>();
            Component topComponent = null;
            topComponent = getRoot(source);
            if (topComponent instanceof Applet) {
                try {
                    Enumeration<Applet> applets = ((Applet) topComponent).getAppletContext().getApplets();
                    while (applets.hasMoreElements()) {
                        containers.add(applets.nextElement());
                    }
                } catch (NullPointerException npe) {
                    containers.add(topComponent);
                }
            }
            Frame[] frames = Frame.getFrames();
            for (int i = 0; i < frames.length; i++) {
                Window[] windows = frames[i].getOwnedWindows();
                for (int j = 0; j < windows.length; j++) {
                    containers.add(windows[j]);
                }
                if (!containers.contains(frames[i])) {
                    containers.add(frames[i]);
                }
            }
            return containers;
        }

        /**
         * Get the root component an an object.
         * 
         * @param comp
         *            Object of which root is taken.
         * @return Component representing root of the object.
         */
        private static Component getRoot(Object comp) {
            Object parent = comp;
            while (parent != null) {
                comp = parent;
                if (comp instanceof MenuComponent) {
                    parent = ((MenuComponent) comp).getParent();
                } else if (comp instanceof Component) {
                    if (comp instanceof Window) {
                        break;
                    }
                    if (comp instanceof Applet) {
                        break;
                    }
                    parent = ((Component) comp).getParent();
                } else {
                    break;
                }
            }
            if (comp instanceof Component) {
                return ((Component) comp);
            }
            return null;
        }

        /**
         * Set the cursor for a component and its children. Store the old cursors for future resetting.
         * 
         * @param comp
         *            Component of which cursor is set.
         * @param cursor
         *            Cursor which is set.
         */
        private void setAndStoreCursors(Component comp, Cursor cursor) {
            if (comp == null) {
                return;
            }
            Cursor compCursor = comp.getCursor();
            if (compCursor != cursor) {
                cursors.put(comp, compCursor);
                debug("set cursor on " + comp);
                comp.setCursor(cursor);
            }
            if (comp instanceof Container) {
                Component[] component = ((Container) comp).getComponents();
                for (int i = 0; i < component.length; i++) {
                    setAndStoreCursors(component[i], cursor);
                }
            }
        }

        /**
         * Actually restore the cursor for a component and its children.
         * 
         * @param comp
         *            Components of which cursor is restored.
         */
        private void resetAndRestoreCursors(Component comp) {
            if (comp == null) {
                return;
            }
            Cursor oldCursor = (Cursor) cursors.get(comp);
            if (oldCursor != null) {
                debug("restored cursor " + oldCursor + " on " + comp);
                comp.setCursor(oldCursor);
            }
            if (comp instanceof Container) {
                Component[] component = ((Container) comp).getComponents();
                for (int i = 0; i < component.length; i++) {
                    resetAndRestoreCursors(component[i]);
                }
            }
        }
    }

    /**
     * Debugging code...
     */
    private static final boolean DEBUG = false;

    /**
     * Helper for printing debug messages.
     * 
     * @param msg
     *            Error message
     */
    private static void debug(Object msg) {
    }
}

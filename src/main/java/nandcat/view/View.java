package nandcat.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Set;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import nandcat.model.Model;
import nandcat.model.ModelEvent;
import nandcat.model.ModelListener;
import nandcat.model.ViewModule;

/**
 * View.
 * 
 * Displays all graphical components including the workspace.
 */
public class View extends JFrame {

    /**
     * frame of the Program.
     */
    private JFrame frame = new JFrame("NANDCat");

    /**
     * View over the Workspace.
     */
    private JScrollPane scroller;

    /**
     * ToolBar with Buttons for control.
     */
    private JToolBar toolBar;

    /**
     * Viewport, visible part of the Workspace.
     */
    private JViewport viewport;

    /**
     * Default serial version uid.
     */
    private static final long serialVersionUID = 1L;

    /**
     * CheckManager displays checks.
     */
    private CheckManager checkManager;

    /**
     * Workspace displays model elements.
     */
    private Workspace workspace;

    /**
     * Location of upper left corner of the frame on the screen.
     */
    private Point frameLocation = new Point(200, 150);

    /**
     * Location of the upper left corner of the viewport on the screen.
     */
    private Point viewportLocation = new Point(200, 250);

    /**
     * Dimension of the panel we work in.
     */
    private Dimension workspaceDimension = new Dimension(1000, 1000);

    /**
     * Menu of the application.
     */
    private JMenuBar menubar;

    /**
     * A Map with all the Tool functionalities and their Listeners.
     */
    private Map<String, ActionListener> toolFunctionalities;

    /**
     * A Set of Modules available for making circuits.
     */
    private Set<ViewModule> viewModules;

    /**
     * Constructs the view.
     * 
     * @param model
     *            The Model component of the application.
     */
    public View(Model model) {
        setupGui(model);
    }

    /**
     * Sets up GUI elements.
     * 
     * @param model
     *            Model we put the ModelListener on.
     */
    private void setupGui(Model model) {
        model.addListener(new ModelListener() {

            public void simulationChanged(ModelEvent e) {
                // TODO Auto-generated method stub
            }

            public void elementsChanged(ModelEvent e) {
                redraw(e);
            }

            public void checksChanged(ModelEvent e) {
            }

            public void checksStarted(ModelEvent e) {
                // TODO Auto-generated method stub
            }

            public void checksStopped(ModelEvent e) {
                // TODO Auto-generated method stub
            }

            public void simulationStarted(ModelEvent e) {
                // TODO Auto-generated method stub
            }

            public void simulationStopped(ModelEvent e) {
                // TODO Auto-generated method stub
            }

            public void importSucceeded(ModelEvent e) {
                // TODO Auto-generated method stub
            }

            public void importFailed(ModelEvent e) {
                // TODO Auto-generated method stub
            }

            public void exportSucceeded(ModelEvent e) {
                // TODO Auto-generated method stub
            }

            public void exportFailed(ModelEvent e) {
                // TODO Auto-generated method stub
            }
        });
        frame.setSize(600, 650);
        frame.setLocation(frameLocation);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        workspace = new Workspace();
        workspace.setPreferredSize(workspaceDimension);
        workspace.setBackground(Color.white);
        workspace.setLayout(null); // no layout is required for free move of the components
        scroller = new JScrollPane(workspace);
        viewport = scroller.getViewport();
        viewport.setViewPosition(viewportLocation);
        toolBar = new JToolBar();
        menubar = new JMenuBar();
        frame.getContentPane().add(scroller, "Center");
        frame.getContentPane().add(toolBar, "West");
        frame.getContentPane().add(menubar, "North");
        frame.setVisible(true);
    }

    /**
     * set up MenuBar of the Frame.
     * 
     * @param menubar
     *            the menuBar to be build
     */
    private void buildMenubar(JMenuBar menubar) {
        Set<String> functionalities = toolFunctionalities.keySet();
        JMenu file = new JMenu("Datei");
        JMenu edit = new JMenu("Bearbeiten");
        JMenu sim = new JMenu("Simulation");
        JMenu help = new JMenu("?");
        menubar.add(file);
        menubar.add(edit);
        menubar.add(sim);
        menubar.add(help);
        for (String func : functionalities) {
            if (func.charAt(0) == 'm') {
                if (func.charAt(1) == 'd') {
                    JMenuItem item = new JMenuItem(func);
                    item.addActionListener(toolFunctionalities.get(func));
                    item.setFocusable(false);
                    item.setName(func);
                    file.add(item);
                } else if (func.charAt(1) == 's') {
                    JMenuItem item = new JMenuItem(func);
                    item.addActionListener(toolFunctionalities.get(func));
                    item.setFocusable(false);
                    item.setName(func);
                    sim.add(item);
                } else if (func.charAt(1) == 'e') {
                    JMenuItem item = new JMenuItem(func);
                    item.addActionListener(toolFunctionalities.get(func));
                    item.setFocusable(false);
                    item.setName(func);
                    edit.add(item);
                } else if (func.charAt(1) == 'h') {
                    JMenuItem item = new JMenuItem(func);
                    item.addActionListener(toolFunctionalities.get(func));
                    item.setFocusable(false);
                    item.setName(func);
                    help.add(item);
                }
            }
        }
    }

    /**
     * set up the ToolBar of the Frame.
     * 
     * @param toolBar
     *            ToolBar to be build.
     */
    private void buildToolbar(JToolBar toolBar) {
        Set<String> functionalities = toolFunctionalities.keySet();
        for (String func : functionalities) {
            if (func.charAt(0) == 'b') {
                JButton button = new JButton(func);
                button.addActionListener(toolFunctionalities.get(func));
                button.setFocusable(false);
                button.setMaximumSize(new Dimension(60, 30));
                button.setName(func);
                toolBar.add(button);
            } else if (func.charAt(0) == 'c') {
                String[] modules = { "And", "Or", "Not" };
                // (String[]) viewModules.toArray();
                JComboBox box = new JComboBox(modules);
                box.addActionListener(toolFunctionalities.get(func));
                box.setName(func);
                box.setFocusable(false);
                box.setMaximumSize(new Dimension(100, 30));
                toolBar.add(box);
            }
        }
        toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.Y_AXIS));
    }

    /**
     * Redraws the workspace with its elements.
     * 
     * @param e
     *            ModelEvent with the elements to be redrawed.
     */
    public void redraw(ModelEvent e) {
        workspace.redraw(e);
    }

    /**
     * Enables all buttons.
     */
    public void enableButtons() {
        Component[] buttons = toolBar.getComponents();
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setEnabled(true);
        }
        Component[] menus = menubar.getComponents();
        for (int i = 0; i < menus.length; i++) {
            menus[i].setEnabled(true);
        }
    }

    /**
     * Disables all buttons except buttons for simulation.
     */
    public void disableButtons() {
        Component[] buttons = toolBar.getComponents();
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].getName() != "bstop" && buttons[i].getName() != "bplus" && buttons[i].getName() != "bminus") {
                buttons[i].setEnabled(false);
            }
        }
        Component[] menus = menubar.getComponents();
        for (int i = 0; i < menus.length; i++) {
            if (((JMenu) menus[i]).getActionCommand() != "Simulation") {
                menus[i].setEnabled(false);
            } else {
                Component[] items = ((JMenu) menus[i]).getComponents();
                for (int j = 0; j < items.length; j++) {
                    if (((JMenuItem) items[j]).getActionCommand() == "msstart") {
                        items[j].setEnabled(false);
                    }
                }
            }
        }
    }

    /**
     * Gets the current workspace instance.
     * 
     * @return Workspace Instance.
     */
    public Workspace getWorkspace() {
        return workspace;
    }

    /**
     * Set the ViewportPosition over the Workspace.
     * 
     * @param p
     *            Point where to be set.
     */
    public void setViewportPosition(Point p) {
        viewport.setViewPosition(p);
    }

    /**
     * With this method the Controller is able to give the View the Informations about the Tools needed.
     * 
     * @param map
     *            Map<String, ActionListener> a map with the Funktionalities of the Tools and their Listeners.
     */
    public void setFunctionalities(Map<String, ActionListener> map) {
        this.toolFunctionalities = map;
        buildMenubar(menubar);
        buildToolbar(toolBar);
    }

    /**
     * Changes the Height of the Workspace.
     * 
     * @param newHeight
     *            int to which the Height will be set.
     */
    public void setWorkspaceHeight(int newHeight) {
        workspace.setSize(workspace.getWidth(), newHeight);
    }

    /**
     * Changes the Width of the Workspace.
     * 
     * @param newWidth
     *            int to which the Width will be set.
     */
    public void setWorkspaceWidth(int newWidth) {
        workspace.setSize(newWidth, workspace.getHeight());
    }

    /**
     * With this method the Controller is able to give the View the Informations about the Elements available.
     * 
     * @param viewele
     *            Set<ViewModule> with the Elements available.
     */
    public void setViewModules(Set<ViewModule> viewele) {
        viewModules = viewele;
    }
}

package nandcat.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.event.ChangeListener;
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
     * View over the Workspace.
     */
    private JScrollPane scroller;

    /**
     * ToolBar with Buttons for control.
     */
    private JToolBar toolBar;

    /**
     * ViewPort, visible part of the Workspace.
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
     * A List of Modules available for making circuits.
     */
    private List<ViewModule> viewModules;

    /**
     * Set of JComponents we want to be disabled during simulation.
     */
    private Set<JComponent> disableElements = new HashSet<JComponent>();

    /**
     * Set of JComponents we want not to be disabled during simulation.
     */
    private Set<JComponent> noDisableElements = new HashSet<JComponent>();

    /**
     * Constructs the view.
     * 
     * @param model
     *            The Model component of the application.
     */
    public View(Model model) {
        super("NANDcat");
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

            public void elementsChanged(ModelEvent e) {
                redraw(e);
            }

            public void checksStarted(ModelEvent e) {
                // TODO Auto-generated method stub
            }

            public void checksStopped(ModelEvent e) {
                enableButtons();
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
        setSize(600, 650);
        setLocation(frameLocation);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        workspace = new Workspace();
        workspace.setPreferredSize(workspaceDimension);
        workspace.setBackground(Color.white);
        workspace.setLayout(null); // no layout is required for free move of the components
        scroller = new JScrollPane(workspace);
        viewport = scroller.getViewport();
        viewport.setViewPosition(viewportLocation);
        toolBar = new JToolBar();
        menubar = new JMenuBar();
        getContentPane().add(scroller, "Center");
        getContentPane().add(toolBar, "West");
        getContentPane().add(menubar, "North");
    }

    /**
     * set up MenuBar of the Frame. Creates Menu Elements and gives them Functionalities according to the set of
     * toolFunctionalities given from the Controller.
     * 
     * @param menubar
     *            the menuBar to be build
     */
    private void buildMenubar(JMenuBar menubar) {
        JMenu file = new JMenu("Datei");
        disableElements.add(file);
        JMenu edit = new JMenu("Bearbeiten");
        disableElements.add(edit);
        JMenu sim = new JMenu("Simulation");
        noDisableElements.add(sim);
        JMenu help = new JMenu("?");
        disableElements.add(help);
        JMenuItem mstart = new JMenuItem("Start");
        disableElements.add(mstart);
        JMenuItem mstop = new JMenuItem("Stop");
        noDisableElements.add(mstop);
        JMenuItem mslower = new JMenuItem("Langsamer");
        noDisableElements.add(mslower);
        JMenuItem mfaster = new JMenuItem("Schneller");
        noDisableElements.add(mfaster);
        JMenuItem mcreate = new JMenuItem("Erstellen");
        noDisableElements.add(mcreate);
        JMenuItem mmove = new JMenuItem("Bewegen");
        noDisableElements.add(mmove);
        JMenuItem mselect = new JMenuItem("Auswählen");
        noDisableElements.add(mselect);
        JMenuItem mstartcheck = new JMenuItem("Tests Ausführen");
        disableElements.add(mstartcheck);
        JMenuItem meditcheck = new JMenuItem("Tests Verwalten");
        disableElements.add(meditcheck);
        JMenuItem mnew = new JMenuItem("Neu");
        noDisableElements.add(mnew);
        JMenuItem mload = new JMenuItem("Schaltung laden");
        noDisableElements.add(mload);
        JMenuItem msave = new JMenuItem("Speichern");
        noDisableElements.add(msave);
        JMenuItem msave2 = new JMenuItem("Speichern unter..");
        noDisableElements.add(msave2);
        JMenuItem mloaddef = new JMenuItem("Schaltungsdefinitionen neu laden"); // Was genau is des?
        noDisableElements.add(mloaddef);
        JMenuItem mclose = new JMenuItem("Schließen");
        noDisableElements.add(mclose);
        JMenuItem mdelete = new JMenuItem("Löschen");
        noDisableElements.add(mdelete);
        JMenuItem mannotate = new JMenuItem("Benennen");
        disableElements.add(mannotate);
        JMenuItem mtoggle = new JMenuItem("Setze Schalter");
        disableElements.add(mtoggle);
        if (toolFunctionalities.containsKey("start")) {
            mstart.addActionListener(toolFunctionalities.get("start"));
            mstart.setActionCommand("start");
            mstart.setName("start");
        }
        if (toolFunctionalities.containsKey("toggle")) {
            mtoggle.addActionListener(toolFunctionalities.get("toggle"));
            mtoggle.setActionCommand("toggle");
            mtoggle.setName("toggle");
        }
        if (toolFunctionalities.containsKey("annotate")) {
            mannotate.addActionListener(toolFunctionalities.get("annotate"));
            mannotate.setActionCommand("annotate");
            mannotate.setName("annotate");
        }
        if (toolFunctionalities.containsKey("stop")) {
            mstop.addActionListener(toolFunctionalities.get("stop"));
            mstop.setActionCommand("stop");
            mstop.setName("stop");
        }
        if (toolFunctionalities.containsKey("faster")) {
            mfaster.addActionListener(toolFunctionalities.get("faster"));
            mfaster.setActionCommand("faster");
            mfaster.setName("faster");
        }
        if (toolFunctionalities.containsKey("slower")) {
            mslower.addActionListener(toolFunctionalities.get("slower"));
            mslower.setActionCommand("slower");
            mslower.setName("slower");
        }
        if (toolFunctionalities.containsKey("create")) {
            mcreate.addActionListener(toolFunctionalities.get("create"));
            mcreate.setActionCommand("create");
            mcreate.setName("create");
        }
        if (toolFunctionalities.containsKey("select")) {
            mselect.addActionListener(toolFunctionalities.get("select"));
            mselect.setActionCommand("select");
            mselect.setName("select");
        }
        if (toolFunctionalities.containsKey("move")) {
            mmove.addActionListener(toolFunctionalities.get("move"));
            mmove.setActionCommand("move");
            mmove.setName("move");
        }
        if (toolFunctionalities.containsKey("startcheck")) {
            mstartcheck.addActionListener(toolFunctionalities.get("startcheck"));
            mstartcheck.setActionCommand("startcheck");
            mstartcheck.setName("startcheck");
        }
        if (toolFunctionalities.containsKey("editcheck")) {
            meditcheck.addActionListener(toolFunctionalities.get("editcheck"));
            meditcheck.setActionCommand("editcheck");
            meditcheck.setName("editcheck");
        }
        if (toolFunctionalities.containsKey("new")) {
            mnew.addActionListener(toolFunctionalities.get("new"));
            mnew.setActionCommand("new");
            mnew.setName("new");
        }
        if (toolFunctionalities.containsKey("load")) {
            mload.addActionListener(toolFunctionalities.get("load"));
            mload.setActionCommand("load");
            mload.setName("load");
        }
        if (toolFunctionalities.containsKey("save")) {
            msave.addActionListener(toolFunctionalities.get("save"));
            msave.setActionCommand("save");
            msave.setName("save");
        }
        if (toolFunctionalities.containsKey("saveAs")) {
            msave2.addActionListener(toolFunctionalities.get("saveAs"));
            msave2.setActionCommand("saveAs");
            msave2.setName("saveAs");
        }
        if (toolFunctionalities.containsKey("loaddef")) {
            mloaddef.addActionListener(toolFunctionalities.get("loaddef"));
            mloaddef.setActionCommand("loaddef");
            mloaddef.setName("loaddef");
        }
        if (toolFunctionalities.containsKey("close")) {
            mclose.addActionListener(toolFunctionalities.get("close"));
            mclose.setActionCommand("close");
            mclose.setName("close");
        }
        if (toolFunctionalities.containsKey("delete")) {
            mdelete.addActionListener(toolFunctionalities.get("delete"));
            mdelete.setActionCommand("delete");
            mdelete.setName("delete");
        }
        menubar.add(file);
        menubar.add(edit);
        menubar.add(sim);
        menubar.add(help);
        sim.add(mstart);
        sim.add(mfaster);
        sim.add(mstop);
        sim.add(mslower);
        sim.add(mstartcheck);
        sim.add(meditcheck);
        edit.add(mcreate);
        edit.add(mselect);
        edit.add(mmove);
        edit.add(mdelete);
        edit.add(mannotate);
        edit.add(mtoggle);
        file.add(mnew);
        file.add(mload);
        file.add(msave);
        file.add(msave2);
        file.add(mloaddef);
        file.add(mclose);
    }

    /**
     * set up the ToolBar of the Frame. Creates Buttons and gives them Functionalities according to the set of
     * toolFunctionalities given from the Controller.
     * 
     * @param toolBar
     *            ToolBar to be build.
     */
    private void buildToolbar(JToolBar toolBar) {
        JButton start = new JButton("Start");
        disableElements.add(start);
        JButton move = new JButton("Verschieben");
        disableElements.add(move);
        JButton stop = new JButton("Stop");
        noDisableElements.add(stop);
        JButton faster = new JButton("Plus");
        noDisableElements.add(faster);
        JButton slower = new JButton("Minus");
        noDisableElements.add(slower);
        JButton create = new JButton("Erstellen");
        disableElements.add(create);
        JButton select = new JButton("Auswahl");
        disableElements.add(select);
        JButton toggle = new JButton("Setze Schalter");
        disableElements.add(toggle);
        JComboBox modules = null;
        if (toolFunctionalities.containsKey("start")) {
            start.addActionListener(toolFunctionalities.get("start"));
            start.setActionCommand("start");
            start.setName("start");
        }
        if (toolFunctionalities.containsKey("toggle")) {
            toggle.addActionListener(toolFunctionalities.get("toggle"));
            toggle.setActionCommand("toggle");
            toggle.setName("toggle");
        }
        if (toolFunctionalities.containsKey("stop")) {
            stop.addActionListener(toolFunctionalities.get("stop"));
            stop.setActionCommand("stop");
            stop.setName("stop");
        }
        if (toolFunctionalities.containsKey("faster")) {
            faster.addActionListener(toolFunctionalities.get("faster"));
            faster.setActionCommand("faster");
            faster.setName("faster");
        }
        if (toolFunctionalities.containsKey("slower")) {
            slower.addActionListener(toolFunctionalities.get("slower"));
            slower.setActionCommand("slower");
            slower.setName("slower");
        }
        if (toolFunctionalities.containsKey("createButton")) {
            create.addActionListener(toolFunctionalities.get("createButton"));
            create.setActionCommand("createButton");
            create.setName("createButton");
        }
        if (toolFunctionalities.containsKey("select")) {
            select.addActionListener(toolFunctionalities.get("select"));
            select.setActionCommand("select");
            select.setName("select");
        }
        if (toolFunctionalities.containsKey("move")) {
            move.addActionListener(toolFunctionalities.get("move"));
            move.setActionCommand("move");
            move.setName("move");
        }
        if (viewModules != null) {
            modules = new JComboBox(viewModules.toArray());
        }
        if (toolFunctionalities.containsKey("selectModule")) {
            modules.addActionListener(toolFunctionalities.get("selectModule"));
            modules.setActionCommand("selectModule");
            modules.setName("selectModule");
        }
        toolBar.add(start);
        toolBar.add(faster);
        toolBar.add(stop);
        toolBar.add(slower);
        toolBar.add(create);
        toolBar.add(select);
        toolBar.add(move);
        toolBar.add(modules);
        toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.Y_AXIS));
    }

    /**
     * Redraws the workspace with its elements.
     * 
     * @param e
     *            ModelEvent with the elements to be repainted.
     */
    public void redraw(ModelEvent e) {
        workspace.redraw(e.getElements());
    }

    /**
     * Enables all buttons and MenuItems.
     */
    public void enableButtons() {
        for (JComponent enable : disableElements) {
            enable.setEnabled(true);
        }
    }

    /**
     * Disables all buttons and MenuItems except those for controlling the Simulations.
     */
    public void disableButtons() {
        for (JComponent enable : disableElements) {
            enable.setEnabled(false);
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
     * Returns the current ElementDrawer of the Workspace.
     * 
     * @return ElementDrawer.
     */
    public ElementDrawer getDrawer() {
        return workspace.getDrawer();
    }

    /**
     * Sets the ElementDrawer of the Workspace.
     * 
     * @param drawer
     *            ElementDrawer to be set.
     */
    public void setDrawer(ElementDrawer drawer) {
        workspace.setDrawer(drawer);
    }

    /**
     * Change Viewport Position on the workspace.
     * 
     * @param x
     *            double value by which the x -coord is changed
     * @param y
     *            double value by which the x -coord is changed
     */
    public void setViewportPosition(int x, int y) {
        viewportLocation.x -= x;
        viewportLocation.y -= y;
        viewport.setViewPosition(viewportLocation);
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
     *            integer to which the Height will be set.
     */
    public void setWorkspaceHeight(int newHeight) {
        workspace.setSize(workspace.getWidth(), newHeight);
    }

    /**
     * Changes the Width of the Workspace.
     * 
     * @param newWidth
     *            integer to which the Width will be set.
     */
    public void setWorkspaceWidth(int newWidth) {
        workspace.setSize(newWidth, workspace.getHeight());
    }

    /**
     * With this method the Controller is able to give the View the Informations about the Elements available.
     * 
     * @param viewElem
     *            List<ViewModule> with the Elements available.
     */
    public void setViewModules(List<ViewModule> viewElem) {
        viewModules = viewElem;
    }

    /**
     * Adds a ChangeListener on the ViewPort.
     * 
     * @param changeListener
     *            ChangeListener to be set on the ViewPort.
     */
    public void addViewPortListener(ChangeListener changeListener) {
        viewport.addChangeListener(changeListener);
    }

    /**
     * Gives the ViewPort Rectangle to the Workspace.
     */
    public void giveViewPortRect() {
        workspace.setViewPortRect(viewport.getViewRect());
    }
}

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
     * Dimension of Buttons.
     */
    private Dimension buttonDim = new Dimension(60, 30);

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
                allModulesInSight();
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
        workspace = new Workspace(model, this);
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
            setupMenuItem(mstart, "start");
        }
        if (toolFunctionalities.containsKey("toggle")) {
            setupMenuItem(mtoggle, "toggle");
        }
        if (toolFunctionalities.containsKey("annotate")) {
            setupMenuItem(mannotate, "annotate");
        }
        if (toolFunctionalities.containsKey("stop")) {
            setupMenuItem(mstop, "stop");
        }
        if (toolFunctionalities.containsKey("faster")) {
            setupMenuItem(mfaster, "faster");
        }
        if (toolFunctionalities.containsKey("slower")) {
            setupMenuItem(mslower, "slower");
        }
        if (toolFunctionalities.containsKey("create")) {
            setupMenuItem(mcreate, "create");
        }
        if (toolFunctionalities.containsKey("select")) {
            setupMenuItem(mselect, "select");
        }
        if (toolFunctionalities.containsKey("move")) {
            setupMenuItem(mmove, "move");
        }
        if (toolFunctionalities.containsKey("startcheck")) {
            setupMenuItem(mstartcheck, "startcheck");
        }
        if (toolFunctionalities.containsKey("editcheck")) {
            setupMenuItem(meditcheck, "editcheck");
        }
        if (toolFunctionalities.containsKey("new")) {
            setupMenuItem(mnew, "new");
        }
        if (toolFunctionalities.containsKey("load")) {
            setupMenuItem(mload, "load");
        }
        if (toolFunctionalities.containsKey("save")) {
            setupMenuItem(msave, "save");
        }
        if (toolFunctionalities.containsKey("saveAs")) {
            setupMenuItem(msave2, "saveAs");
        }
        if (toolFunctionalities.containsKey("loaddef")) {
            setupMenuItem(mloaddef, "loaddef");
        }
        if (toolFunctionalities.containsKey("close")) {
            setupMenuItem(mclose, "close");
        }
        if (toolFunctionalities.containsKey("delete")) {
            setupMenuItem(mdelete, "delete");
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
     * Sets up the MenuItems. Gives them the Functionalities and ActionCommands.
     * 
     * @param item
     *            JMenuItem to be edited.
     * @param name
     *            String, Name of Functionality.
     */
    private void setupMenuItem(JMenuItem item, String name) {
        item.addActionListener(toolFunctionalities.get(name));
        item.setActionCommand(name);
        item.setName(name);
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
        start.setMaximumSize(buttonDim);
        start.setToolTipText("Startet die Simulation.");
        disableElements.add(start);
        JButton move = new JButton("Verschieben");
        move.setMaximumSize(buttonDim);
        move.setToolTipText("Aktiviert den Modus in welchem Mit der Maus gescrollt werden kann.");
        disableElements.add(move);
        JButton stop = new JButton("Stop");
        stop.setMaximumSize(buttonDim);
        stop.setToolTipText("Stoppt die Simulation.");
        noDisableElements.add(stop);
        JButton faster = new JButton("Plus");
        faster.setMaximumSize(buttonDim);
        faster.setToolTipText("Beschleunigt die Simulationsgeschwindigkeit.");
        noDisableElements.add(faster);
        JButton slower = new JButton("Minus");
        slower.setMaximumSize(buttonDim);
        slower.setToolTipText("Verlangsamt die Simulationsgeschwindigkeit.");
        noDisableElements.add(slower);
        JButton create = new JButton("Erstellen");
        create.setMaximumSize(buttonDim);
        create.setToolTipText("Versetzt das Programm in den Modus in welchem neue Bausteine und Leitungen gesetzt werden können.");
        disableElements.add(create);
        JButton select = new JButton("Auswahl");
        select.setMaximumSize(buttonDim);
        select.setToolTipText("Versetzt das Programm in den Modus in welchem Bausteine und Leitungen markiert werden können.");
        disableElements.add(select);
        JButton toggle = new JButton("Setze Schalter");
        toggle.setMaximumSize(buttonDim);
        toggle.setToolTipText("Versetzt das Programm in den Modus in welchem Schalter auf An und Aus gestellt werden können");
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
            modules.setMaximumSize(buttonDim);
            modules.setToolTipText("Liste mit verfügbaren Bausteinen");
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
        toolBar.add(toggle);
        toolBar.add(modules);
        toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.Y_AXIS));
    }

    /**
     * Checks after an Import if all Elements are on the workspace or if it has to be extended.
     */
    private void allModulesInSight() {
        // aus dem Event alle Elements dann überprüfen ob workspace groß genug.
    }

    /**
     * Redraws the workspace with its elements.
     * 
     * @param e
     *            ModelEvent with the elements to be repainted.
     */
    public void redraw(ModelEvent e) {
        workspace.redraw();
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
        workspace.setPreferredSize(new Dimension(workspace.getWidth(), newHeight));
    }

    /**
     * Changes the Width of the Workspace.
     * 
     * @param newWidth
     *            integer to which the Width will be set.
     */
    public void setWorkspaceWidth(int newWidth) {
        workspace.setPreferredSize(new Dimension(newWidth, workspace.getHeight()));
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

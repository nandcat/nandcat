package nandcat.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
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
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ChangeListener;
import nandcat.model.Model;
import nandcat.model.ModelEvent;
import nandcat.model.ModelListener;
import nandcat.model.ViewModule;
import nandcat.model.element.DrawElement;
import nandcat.model.element.Module;

/**
 * View.
 * 
 * Displays all graphical components including the workspace.
 */
public class View extends JFrame {

    /**
     * Frame title of the main frame.
     */
    private static final String FRAME_TITLE = "NANDcat";

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
    private Dimension buttonDim = new Dimension(48, 48);

    /**
     * Model instance.
     */
    private Model model;

    /**
     * Constructs the view.
     * 
     * @param model
     *            The Model component of the application.
     */
    public View(Model model) {
        this.model = model;
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // No catch needed cause if Nimbus is not installed the standard LookAndFeel will be used.
        }
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
            }

            public void checksStopped(ModelEvent e) {
                enableButtons();
            }

            public void simulationStarted(ModelEvent e) {
            }

            public void simulationStopped(ModelEvent e) {
                enableButtons();
            }

            public void importSucceeded(ModelEvent e) {
                allModulesInSight();
            }

            public void importFailed(ModelEvent e) {
            }

            public void exportSucceeded(ModelEvent e) {
            }

            public void exportFailed(ModelEvent e) {
            }
        });
        setTitle(FRAME_TITLE);
        setSize(600, 650);
        setLocation(frameLocation);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        workspace = new Workspace(model, this);
        workspace.setPreferredSize(workspaceDimension);
        workspace.setSize(workspaceDimension);
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
        // Create the Menus. Setting Shortcuts.
        JMenu file = new JMenu("Datei");
        file.setMnemonic(KeyEvent.VK_D);
        disableElements.add(file);
        JMenu edit = new JMenu("Bearbeiten");
        edit.setMnemonic(KeyEvent.VK_B);
        disableElements.add(edit);
        JMenu sim = new JMenu("Simulation");
        sim.setMnemonic(KeyEvent.VK_T);
        noDisableElements.add(sim);
        JMenu help = new JMenu("?");
        help.setMnemonic(KeyEvent.VK_H);
        disableElements.add(help);
        // Create MenuItems. Setting Shortcuts.
        JMenuItem mstart = new JMenuItem("Simulation starten", KeyEvent.VK_S);
        mstart.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0));
        disableElements.add(mstart);
        JMenuItem mstop = new JMenuItem("Simulation beenden", KeyEvent.VK_E);
        mstop.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0));
        noDisableElements.add(mstop);
        JMenuItem mslower = new JMenuItem("Langsamer", KeyEvent.VK_MINUS);
        mslower.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, ActionEvent.CTRL_MASK));
        noDisableElements.add(mslower);
        JMenuItem mfaster = new JMenuItem("Schneller", KeyEvent.VK_PLUS);
        mfaster.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, ActionEvent.CTRL_MASK));
        noDisableElements.add(mfaster);
        JMenuItem mcreate = new JMenuItem("Erstellen", KeyEvent.VK_E);
        mcreate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        noDisableElements.add(mcreate);
        JMenuItem mmove = new JMenuItem("Bewegen", KeyEvent.VK_B);
        mmove.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));
        noDisableElements.add(mmove);
        JMenuItem mselect = new JMenuItem("Auswählen", KeyEvent.VK_W);
        mselect.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
        noDisableElements.add(mselect);
        JMenuItem mstartcheck = new JMenuItem("Tests Ausführen", KeyEvent.VK_T);
        mstartcheck.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
        disableElements.add(mstartcheck);
        JMenuItem meditcheck = new JMenuItem("Tests Verwalten", KeyEvent.VK_V);
        meditcheck.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        disableElements.add(meditcheck);
        JMenuItem mnew = new JMenuItem("Neu", KeyEvent.VK_N);
        mnew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        noDisableElements.add(mnew);
        JMenuItem mload = new JMenuItem("Schaltung laden", KeyEvent.VK_L);
        mload.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        noDisableElements.add(mload);
        JMenuItem msave = new JMenuItem("Speichern", KeyEvent.VK_S);
        msave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        noDisableElements.add(msave);
        JMenuItem msave2 = new JMenuItem("Speichern unter..", KeyEvent.VK_A);
        msave2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        noDisableElements.add(msave2);
        JMenuItem mloaddef = new JMenuItem("Schaltungsdefinitionen neu laden", KeyEvent.VK_F5);
        mloaddef.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        noDisableElements.add(mloaddef);
        JMenuItem mclose = new JMenuItem("Schließen");
        mclose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
        noDisableElements.add(mclose);
        JMenuItem mdelete = new JMenuItem("Löschen", KeyEvent.VK_DELETE);
        mdelete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        noDisableElements.add(mdelete);
        JMenuItem mannotate = new JMenuItem("Benennen", KeyEvent.VK_N);
        mannotate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        disableElements.add(mannotate);
        JMenuItem mtoggle = new JMenuItem("Setze Schalter", KeyEvent.VK_T);
        mtoggle.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
        disableElements.add(mtoggle);
        /*
         * check if there are functionalities given for the MenuItems.
         */
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
        if (toolFunctionalities.containsKey("createButton")) {
            setupMenuItem(mcreate, "createButton");
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
        /*
         * Add MenuItems to the Menus they belong to.
         */
        menubar.add(file);
        menubar.add(edit);
        menubar.add(sim);
        menubar.add(help);
        sim.add(mstart);
        sim.add(mfaster);
        sim.add(mstop);
        sim.add(mslower);
        sim.addSeparator();
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
        // Create Buttons of the Application. Setting Icons and Descriptions and Size.
        ImageIcon startButtonIcon = new ImageIcon("src/resources/startmiddle.png");
        JButton start = new JButton("", startButtonIcon);
        start.setPreferredSize(buttonDim);
        start.setToolTipText("Startet die Simulation.");
        disableElements.add(start);
        ImageIcon moveButtonIcon = new ImageIcon("src/resources/movemiddle.png");
        JButton move = new JButton("", moveButtonIcon);
        move.setPreferredSize(buttonDim);
        move.setToolTipText("Aktiviert den Modus in welchem Mit der Maus gescrollt werden kann.");
        disableElements.add(move);
        ImageIcon stopButtonIcon = new ImageIcon("src/resources/stopmiddle.png");
        JButton stop = new JButton("", stopButtonIcon);
        stop.setPreferredSize(buttonDim);
        stop.setToolTipText("Stoppt die Simulation.");
        noDisableElements.add(stop);
        ImageIcon fasterButtonIcon = new ImageIcon("src/resources/plusmiddle.png");
        JButton faster = new JButton("", fasterButtonIcon);
        faster.setPreferredSize(buttonDim);
        faster.setToolTipText("Beschleunigt die Simulationsgeschwindigkeit.");
        noDisableElements.add(faster);
        ImageIcon slowerButtonIcon = new ImageIcon("src/resources/minusmiddle.png");
        JButton slower = new JButton("", slowerButtonIcon);
        slower.setPreferredSize(buttonDim);
        slower.setToolTipText("Verlangsamt die Simulationsgeschwindigkeit.");
        noDisableElements.add(slower);
        ImageIcon createButtonIcon = new ImageIcon("src/resources/createmiddle.png");
        JButton create = new JButton("", createButtonIcon);
        create.setPreferredSize(buttonDim);
        create.setToolTipText("Versetzt das Programm in den Modus in welchem neue Bausteine und Leitungen gesetzt werden können.");
        disableElements.add(create);
        ImageIcon selectButtonIcon = new ImageIcon("src/resources/selectmiddle.png");
        JButton select = new JButton("", selectButtonIcon);
        select.setPreferredSize(buttonDim);
        select.setToolTipText("Versetzt das Programm in den Modus in welchem Bausteine und Leitungen markiert werden können.");
        disableElements.add(select);
        ImageIcon toggleButtonIcon = new ImageIcon("src/resources/togglemiddle.png");
        JButton toggle = new JButton("", toggleButtonIcon);
        toggle.setPreferredSize(buttonDim);
        toggle.setToolTipText("Versetzt das Programm in den Modus in welchem Schalter auf An und Aus gestellt werden können");
        disableElements.add(toggle);
        JComboBox modules = null;
        // Check if there are Functionalities for the Buttons and if yes calling the setup.
        if (toolFunctionalities.containsKey("start")) {
            setupButton(start, "start");
        }
        if (toolFunctionalities.containsKey("toggle")) {
            setupButton(toggle, "toggle");
        }
        if (toolFunctionalities.containsKey("stop")) {
            setupButton(stop, "stop");
        }
        if (toolFunctionalities.containsKey("faster")) {
            setupButton(faster, "faster");
        }
        if (toolFunctionalities.containsKey("slower")) {
            setupButton(slower, "slower");
        }
        if (toolFunctionalities.containsKey("createButton")) {
            setupButton(create, "createButton");
        }
        if (toolFunctionalities.containsKey("select")) {
            setupButton(select, "select");
        }
        if (toolFunctionalities.containsKey("move")) {
            setupButton(move, "move");
        }
        if (viewModules != null) {
            modules = new JComboBox(viewModules.toArray());
            modules.setMaximumSize(new Dimension(80, 40));
            modules.setToolTipText("Liste mit verfügbaren Bausteinen");
        }
        if (toolFunctionalities.containsKey("selectModule")) {
            modules.addActionListener(toolFunctionalities.get("selectModule"));
            modules.setActionCommand("selectModule");
            modules.setName("selectModule");
        }
        // Adding Buttons to the ToolBar.
        toolBar.add(modules);
        toolBar.add(create);
        toolBar.add(select);
        toolBar.add(toggle);
        toolBar.add(move);
        toolBar.add(faster);
        toolBar.add(slower);
        toolBar.add(start);
        toolBar.add(stop);
        toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.Y_AXIS));
        // Buttons do not have to be Focusable.
        for (Component elem : toolBar.getComponents()) {
            elem.setFocusable(false);
        }
    }

    /**
     * Sets up the Buttons. Gives them the Functionalities and ActionCommands.
     * 
     * @param button
     *            JButton to be edited.
     * @param name
     *            String, Name of Functionality.
     */
    private void setupButton(JButton button, String name) {
        button.addActionListener(toolFunctionalities.get(name));
        button.setActionCommand(name);
        button.setName(name);
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
     * Checks after an Import if all Elements are on the workspace or if it has to be extended.
     */
    private void allModulesInSight() {
        for (DrawElement elem : model.getDrawElements()) {
            if (elem instanceof Module) {
                // If elem is a Module we must check if it is out of the workspace and if yes extend the workspace.
                if (((Module) elem).getRectangle().x >= workspace.getWidth() + 50) {
                    workspace.setSize(((Module) elem).getRectangle().x, workspace.getHeight());
                }
                if (((Module) elem).getRectangle().y >= workspace.getHeight() + 50) {
                    workspace.setSize(workspace.getWidth(), ((Module) elem).getRectangle().y);
                }
            }
        }
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
        // Enables all Elements.
        for (JComponent enable : disableElements) {
            enable.setEnabled(true);
        }
    }

    /**
     * Disables all buttons and MenuItems except those for controlling the Simulations.
     */
    public void disableButtons() {
        // Disables all Elements in the List of Elements to be disabled.
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
    public void setViewportPosition(Rectangle rect) {
        // viewportLocation.x -= x;
        // viewportLocation.y -= y;
        // viewport.setViewPosition(viewportLocation);
        viewport.scrollRectToVisible(rect);
    }

    /**
     * With this method the Controller is able to give the View the Informations about the Tools needed.
     * 
     * @param map
     *            Map<String, ActionListener> a map with the Functionalities of the Tools and their Listeners.
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

    /**
     * Getter for the ViewPort Rectangle.
     * 
     * @return Rectangle representing the visible part of the Workspace.
     */
    public Rectangle getViewRect() {
        return viewport.getViewRect();
    }
}

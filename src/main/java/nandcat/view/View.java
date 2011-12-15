package nandcat.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.SplashScreen;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeListener;
import nandcat.I18N;
import nandcat.I18N.I18NBundle;
import nandcat.Nandcat;
import nandcat.controller.Help.DisplayHelpAfterTracking;
import nandcat.model.Model;
import nandcat.model.ModelEvent;
import nandcat.model.ModelListenerAdapter;
import nandcat.model.ViewModule;
import nandcat.model.element.DrawElement;
import nandcat.model.element.Module;
import nandcat.model.element.factory.ModuleLayouter;

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
     * Icon of the Application.
     */
    private static final Image CAT = Toolkit.getDefaultToolkit().getImage(getResource("catsmal.png"));

    /**
     * Default serial version uid.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default Button Dimension.
     */
    private static final Dimension BUTTON_DIM = new Dimension(38, 38);

    /**
     * Default Workspace Dimension.
     */
    private static final Dimension WORKSPACE_SIZE = new Dimension(2000, 2000);

    /**
     * Default Viewport location on the workspace.
     */
    private static final Point VIEWPORT_LOCATION = new Point(200, 200);

    /**
     * Default Frame location on the Screen.
     */
    private static final Point START_LOCATION = new Point(200, 150);

    /**
     * Default Frame size.
     */
    private static final Dimension FRAME_SIZE = new Dimension(1024, 768);

    /**
     * How often the splash thread is sent to sleep.
     * 
     * this value multiplied with the SPLASH_THREAD_SLEEP gives the sleep time in ms.
     */
    private static final int SPLASH_SLEEPTIME = 20;

    /**
     * Sleep time of the thread representing the splash screen.
     */
    private static final long SPLASH_THREAD_SLEEP = 100;

    /**
     * Dimension of the Separator in the ToolBar.
     */
    private static final Dimension SEPERATOR_DIM = new Dimension(80, 40);

    /**
     * Dimension of the ComboBox in the ToolBar.
     */
    private static final Dimension COMBO_DIM = new Dimension(80, 40);

    /**
     * this value represents how much pixel should at least be around an Element.
     */
    private static final int SPACE_ARROUND_ELEM = 100;

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
     * Workspace displays model elements.
     */
    private Workspace workspace;

    /**
     * Location of upper left corner of the frame on the screen.
     */
    private Point frameLocation = new Point(START_LOCATION);

    /**
     * Location of the upper left corner of the viewport on the screen.
     */
    private Point viewportLocation = new Point(VIEWPORT_LOCATION);

    /**
     * Dimension of the panel we work in.
     */
    private Dimension workspaceDimension = new Dimension(WORKSPACE_SIZE);

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
     * Set of JComponents we want to be enabled during simulation and disabled else.
     */
    private Set<JComponent> reverseDisableElements = new HashSet<JComponent>();

    /**
     * Dimension of Buttons.
     */
    private Dimension buttonDim = new Dimension(BUTTON_DIM);

    /**
     * Model instance.
     */
    private Model model;

    /**
     * Translation unit.
     */
    private I18NBundle i18n = I18N.getBundle("view");

    /**
     * JLabel representing the Cycle Counter placed in the MenuBar.
     */
    private JLabel cycle = new JLabel();

    /**
     * JComboBox with the Available Modules.
     */
    private JComboBox modules;

    /**
     * Layouter used to layout modules.
     */
    private ModuleLayouter layouter = new StandardModuleLayouter();

    /**
     * JScrollBar, the Horizontal ScrollBar of the ScrollPane.
     */
    private JScrollBar horizontal;

    /**
     * JScrollBar, the Vertical ScrollBar of the ScrollPane.
     */
    private JScrollBar vertical;

    /**
     * The HelpBroker generated from the HelpSet for the context-sensitive help.
     */
    private HelpBroker hb;

    /**
     * HelpSet for context-sensitive help.
     */
    private HelpSet hs;

    /**
     * JButton, the Annotate Button.
     */
    private JButton annotate;

    /**
     * JButton, the Toggle Button.
     */
    private JButton toggle;

    /**
     * JButton, the SElect Button.
     */
    private JButton select;

    /**
     * JButton, the Create Button.
     */
    private JButton create;

    /**
     * JButton, the Move Button.
     */
    private JButton move;

    /**
     * JButton, the Button actual active.
     */
    private JButton selectedTool;

    /**
     * JButton, the Button for the Help Tool.
     */
    private JButton help;

    /**
     * Listener for the Help Tool.
     */
    private DisplayHelpAfterTracking helpListener;

    /**
     * JMenuItem msave2 the Save As MenuItem.
     */
    private JMenuItem msave2;

    /**
     * Constructs the view.
     * 
     * @param model
     *            The Model component of the application.
     */
    public View(Model model) {
        this.model = model;
        model.setLayouter(layouter);
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // No catch needed cause if Nimbus is not installed the standard
            // LookAndFeel will be used.
            System.out.println("No Nimbus Look and Feel found!");
        }
        setUpHelp();
        setupGui(model);
    }

    /**
     * Load HelpSet and defines HelpBroker for context-sensitive help.
     */
    private void setUpHelp() {
        try {
            ClassLoader cl = View.class.getClassLoader();
            URL url = HelpSet.findHelpSet(cl, "HelpSet");
            hs = new HelpSet(cl, url);
        } catch (Exception ee) {
            System.out.println("Help Set not found");
            return;
        } catch (ExceptionInInitializerError ex) {
            System.err.println("initialization error:");
            ex.getException().printStackTrace();
        }
        hb = hs.createHelpBroker();
    }

    /**
     * Sets up GUI elements.
     * 
     * @param model
     *            Model we put the ModelListener on.
     */
    private void setupGui(Model model) {
        model.addListener(new ModelListenerAdapter() {

            public void elementsChanged(ModelEvent e) {
                allModulesInSight(e.getElements());
                redraw(e);
            }

            public void importSucceeded(ModelEvent e) {
                Set<DrawElement> set = null;
                allModulesInSight(set);
            }
        });
        setTitle(FRAME_TITLE + " - " + i18n.getString("new"));
        setIconImage(CAT);
        setSize(FRAME_SIZE);
        setLocation(frameLocation);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        CSH.setHelpIDString(this, "app");
        workspace = new Workspace(model, this);
        workspace.setPreferredSize(workspaceDimension);
        workspace.setSize(workspaceDimension);
        workspace.setBackground(Color.white);
        workspace.setLayout(null); // no layout is required for free move of the components
        CSH.setHelpIDString(workspace, "workspace");
        scroller = new JScrollPane(workspace);
        scroller.setWheelScrollingEnabled(false);
        horizontal = scroller.getHorizontalScrollBar();
        vertical = scroller.getVerticalScrollBar();
        viewport = scroller.getViewport();
        viewport.setViewPosition(viewportLocation);
        toolBar = new JToolBar(JToolBar.VERTICAL);
        CSH.setHelpIDString(toolBar, "toolbar");
        menubar = new JMenuBar();
        CSH.setHelpIDString(menubar, "menubar");
        getContentPane().add(scroller, BorderLayout.CENTER);
        getContentPane().add(toolBar, BorderLayout.WEST);
        getContentPane().add(menubar, BorderLayout.NORTH);
        final SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash != null) {
            for (int i = 0; i < SPLASH_SLEEPTIME; i++) {
                try {
                    Thread.sleep(SPLASH_THREAD_SLEEP);
                } catch (InterruptedException e) {
                    System.out.println(e.getCause());
                }
            }
            splash.close();
            setVisible(true);
            toFront();
        }
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
        JMenu file = new JMenu(i18n.getString("menu.file"));
        file.setMnemonic(KeyEvent.VK_D);
        noDisableElements.add(file);
        JMenu edit = new JMenu(i18n.getString("menu.edit"));
        edit.setMnemonic(KeyEvent.VK_B);
        noDisableElements.add(edit);
        JMenu sim = new JMenu(i18n.getString("menu.simulation"));
        sim.setMnemonic(KeyEvent.VK_T);
        noDisableElements.add(sim);
        JMenu layout = new JMenu(i18n.getString("menu.layout"));
        layout.setMnemonic(KeyEvent.VK_Y);
        disableElements.add(layout);
        JMenu help = new JMenu(i18n.getString("menu.help"));
        help.setMnemonic(KeyEvent.VK_H);
        helpListener = new nandcat.controller.Help.DisplayHelpAfterTracking(hb.getHelpSet(), "javax.help.Popup", null);
        disableElements.add(help);
        // Create MenuItems. Setting Shortcuts.
        JMenuItem mstart = new JMenuItem(i18n.getString("menu.simulation.start"), KeyEvent.VK_S);
        mstart.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0));
        disableElements.add(mstart);
        JMenuItem mreset = new JMenuItem(i18n.getString("menu.simulation.reset"), KeyEvent.VK_R);
        mreset.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        disableElements.add(mreset);
        JMenuItem mresetSpeed = new JMenuItem(i18n.getString("menu.simulation.resetSpeed"), KeyEvent.VK_0);
        mresetSpeed.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, ActionEvent.CTRL_MASK));
        noDisableElements.add(mresetSpeed);
        JMenuItem mstop = new JMenuItem(i18n.getString("menu.simulation.stop"), KeyEvent.VK_E);
        mstop.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0));
        mstop.setEnabled(false);
        reverseDisableElements.add(mstop);
        JMenuItem mstep = new JMenuItem(i18n.getString("menu.simulation.step"), KeyEvent.VK_X);
        mstep.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, 0));
        noDisableElements.add(mstep);
        JMenuItem mslower = new JMenuItem(i18n.getString("menu.simulation.slower"), KeyEvent.VK_MINUS);
        mslower.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, ActionEvent.CTRL_MASK));
        noDisableElements.add(mslower);
        JMenuItem mfaster = new JMenuItem(i18n.getString("menu.simulation.faster"), KeyEvent.VK_PLUS);
        mfaster.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, ActionEvent.CTRL_MASK));
        noDisableElements.add(mfaster);
        JMenuItem mcreate = new JMenuItem(i18n.getString("menu.edit.create"), KeyEvent.VK_E);
        mcreate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        disableElements.add(mcreate);
        JMenuItem mmove = new JMenuItem(i18n.getString("menu.edit.move"), KeyEvent.VK_B);
        mmove.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));
        disableElements.add(mmove);
        JMenuItem mselect = new JMenuItem(i18n.getString("menu.edit.select"), KeyEvent.VK_W);
        mselect.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
        noDisableElements.add(mselect);
        JMenuItem mstartcheck = new JMenuItem(i18n.getString("menu.test.execute"), KeyEvent.VK_M);
        mstartcheck.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));
        disableElements.add(mstartcheck);
        JMenuItem meditcheck = new JMenuItem(i18n.getString("menu.test.manage"), KeyEvent.VK_V);
        meditcheck.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        disableElements.add(meditcheck);
        JMenuItem mnew = new JMenuItem(i18n.getString("menu.file.new"), KeyEvent.VK_N);
        mnew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        disableElements.add(mnew);
        JMenuItem mload = new JMenuItem(i18n.getString("menu.file.load"), KeyEvent.VK_L);
        mload.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        disableElements.add(mload);
        JMenuItem msave = new JMenuItem(i18n.getString("menu.file.save"), KeyEvent.VK_S);
        msave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        disableElements.add(msave);
        msave2 = new JMenuItem(i18n.getString("menu.file.saveas"), KeyEvent.VK_A);
        msave2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        // disableElements.add(msave2);
        JMenuItem msave3 = new JMenuItem(i18n.getString("menu.file.saveselectedas"), KeyEvent.VK_A);
        msave3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.SHIFT_MASK));
        disableElements.add(msave3);
        JMenuItem mloaddef = new JMenuItem(i18n.getString("menu.file.defload"), KeyEvent.VK_F5);
        mloaddef.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        disableElements.add(mloaddef);
        JMenuItem mclose = new JMenuItem(i18n.getString("menu.file.close"));
        mclose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
        noDisableElements.add(mclose);
        JMenuItem mdelete = new JMenuItem(i18n.getString("menu.edit.delete"), KeyEvent.VK_DELETE);
        mdelete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        disableElements.add(mdelete);
        JMenuItem mannotate = new JMenuItem(i18n.getString("menu.edit.annotate"), KeyEvent.VK_O);
        mannotate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        disableElements.add(mannotate);
        JMenuItem mtoggle = new JMenuItem(i18n.getString("menu.edit.toggle"), KeyEvent.VK_T);
        mtoggle.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
        disableElements.add(mtoggle);
        JMenuItem mgrid = new JMenuItem(i18n.getString("menu.edit.grid"), KeyEvent.VK_G);
        mgrid.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        disableElements.add(mgrid);
        JMenuItem mpause = new JMenuItem(i18n.getString("menu.simulation.pause"), KeyEvent.VK_P);
        mpause.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0));
        reverseDisableElements.add(mpause);
        mpause.setEnabled(false);
        JMenuItem mhelp = new JMenuItem(i18n.getString("menu.help.help"), KeyEvent.VK_I);
        mhelp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
        mhelp.addActionListener(helpListener);
        disableElements.add(mhelp);
        cycle.setText(i18n.getString("cycle.stand"));
        JMenuItem miec = new JMenuItem(i18n.getString("menu.layout.iec"), KeyEvent.VK_2);
        miec.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.CTRL_MASK));
        disableElements.add(miec);
        JMenuItem mstandard = new JMenuItem(i18n.getString("menu.layout.standard"), KeyEvent.VK_1);
        mstandard.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.CTRL_MASK));
        disableElements.add(mstandard);        
        JMenuItem mansi = new JMenuItem(i18n.getString("menu.layout.ansi"), KeyEvent.VK_3);
        mansi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.CTRL_MASK));
        disableElements.add(mansi);
        /*
         * check if there are functionalities given for the MenuItems.
         */
        if (toolFunctionalities.containsKey("help")) {
            setupMenuItem(mhelp, "help");
        }
        if (toolFunctionalities.containsKey("ansi")) {
            setupMenuItem(mansi, "ansi");
        }
        if (toolFunctionalities.containsKey("iec")) {
            setupMenuItem(miec, "iec");
        }
        if (toolFunctionalities.containsKey("standard")) {
            setupMenuItem(mstandard, "standard");
        }
        if (toolFunctionalities.containsKey("resetSpeed")) {
            setupMenuItem(mresetSpeed, "resetSpeed");
        }
        if (toolFunctionalities.containsKey("reset")) {
            setupMenuItem(mreset, "reset");
        }
        if (toolFunctionalities.containsKey("grid")) {
            setupMenuItem(mgrid, "grid");
        }
        if (toolFunctionalities.containsKey("pause")) {
            setupMenuItem(mpause, "pause");
        }
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
        if (toolFunctionalities.containsKey("saveSelectedAs")) {
            setupMenuItem(msave3, "saveSelectedAs");
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
        if (toolFunctionalities.containsKey("step")) {
            setupMenuItem(mstep, "step");
        }
        /*
         * Add MenuItems to the Menus they belong to.
         */
        menubar.add(file);
        menubar.add(edit);
        menubar.add(sim);
        menubar.add(help);
        layout.add(mstandard);
        layout.add(miec);
        layout.add(mansi);
        sim.add(mstart);
        sim.add(mstep);
        sim.add(mpause);
        sim.add(mstop);
        sim.add(mfaster);
        sim.add(mslower);
        sim.addSeparator();
        sim.add(mstartcheck);
        sim.add(meditcheck);
        edit.add(mselect);
        edit.add(mcreate);
        edit.add(mmove);
        edit.add(mdelete);
        edit.add(mannotate);
        edit.add(mtoggle);
        edit.add(mgrid);
        edit.addSeparator();
        edit.add(mreset);
        edit.add(mresetSpeed);
        edit.addSeparator();
        edit.add(layout);
        file.add(mnew);
        file.add(mload);
        file.add(msave);
        file.add(msave2);
        file.add(msave3);
        file.add(mloaddef);
        file.add(mclose);
        help.add(mhelp);
        menubar.add(cycle);
    }

    /**
     * Gets the resource URL depending on environment. Works with Jar.
     * 
     * @param file
     *            File to get URL for. Realpath: src/main/resources/main.png -> Parameter: main.png
     * @return URL to file.
     */
    private static URL getResource(String file) {
        return Nandcat.class.getClassLoader().getResource(file);
    }

    /**
     * set up the ToolBar of the Frame. Creates Buttons and gives them Functionalities according to the set of
     * toolFunctionalities given from the Controller.
     * 
     * @param toolBar
     *            ToolBar to be build.
     */
    private void buildToolbar(JToolBar toolBar) {
        toolBar.removeAll();
        // Create Buttons of the Application. Setting Icons and Descriptions and Size.
        ImageIcon startButtonIcon = new ImageIcon(getResource("startmiddle.png"));
        JButton start = new JButton("", startButtonIcon);
        start.setPreferredSize(buttonDim);
        start.setToolTipText(i18n.getString("tooltip.simulation.start"));
        CSH.setHelpIDString(start, "start");
        disableElements.add(start);
        ImageIcon moveButtonIcon = new ImageIcon(getResource("movemiddle.png"));
        move = new JButton("", moveButtonIcon);
        move.setPreferredSize(buttonDim);
        move.setToolTipText(i18n.getString("tooltip.view.move"));
        move.setBorder(new LineBorder(Color.lightGray, 1, true));
        move.setBorderPainted(false);
        CSH.setHelpIDString(move, "move");
        disableElements.add(move);
        ImageIcon stopButtonIcon = new ImageIcon(getResource("stopmiddle.png"));
        JButton stop = new JButton("", stopButtonIcon);
        stop.setPreferredSize(buttonDim);
        stop.setToolTipText(i18n.getString("tooltip.simulation.stop"));
        stop.setEnabled(false);
        reverseDisableElements.add(stop);
        CSH.setHelpIDString(stop, "stop");
        ImageIcon stepButtonIcon = new ImageIcon(getResource("stepmiddle.png"));
        JButton step = new JButton("", stepButtonIcon);
        step.setPreferredSize(buttonDim);
        step.setToolTipText(i18n.getString("tooltip.simulation.step"));
        CSH.setHelpIDString(step, "step");
        noDisableElements.add(step);
        ImageIcon fasterButtonIcon = new ImageIcon(getResource("plusmiddle.png"));
        JButton faster = new JButton("", fasterButtonIcon);
        CSH.setHelpIDString(faster, "faster");
        faster.setPreferredSize(buttonDim);
        faster.setToolTipText(i18n.getString("tooltip.simulation.faster"));
        noDisableElements.add(faster);
        ImageIcon slowerButtonIcon = new ImageIcon(getResource("minusmiddle.png"));
        JButton slower = new JButton("", slowerButtonIcon);
        slower.setPreferredSize(buttonDim);
        slower.setToolTipText(i18n.getString("tooltip.simulation.slower"));
        CSH.setHelpIDString(slower, "slower");
        noDisableElements.add(slower);
        ImageIcon createButtonIcon = new ImageIcon(getResource("createmiddle.png"));
        create = new JButton("", createButtonIcon);
        create.setPreferredSize(buttonDim);
        create.setToolTipText(i18n.getString("tooltip.create"));
        create.setBorder(new LineBorder(Color.lightGray, 1, true));
        create.setBorderPainted(false);
        CSH.setHelpIDString(create, "create");
        disableElements.add(create);
        ImageIcon selectButtonIcon = new ImageIcon(getResource("selectmiddle.png"));
        select = new JButton("", selectButtonIcon);
        select.setPreferredSize(buttonDim);
        select.setToolTipText(i18n.getString("tooltip.select"));
        select.setBorder(new LineBorder(Color.lightGray, 1, true));
        select.setBorderPainted(false);
        CSH.setHelpIDString(select, "select");
        noDisableElements.add(select);
        ImageIcon toggleButtonIcon = new ImageIcon(getResource("togglemiddle.png"));
        toggle = new JButton("", toggleButtonIcon);
        toggle.setPreferredSize(buttonDim);
        toggle.setToolTipText(i18n.getString("tooltip.state.toggle"));
        toggle.setBorder(new LineBorder(Color.lightGray, 1, true));
        toggle.setBorderPainted(false);
        CSH.setHelpIDString(toggle, "toggle");
        disableElements.add(toggle);
        ImageIcon annotateButtonIcon = new ImageIcon(getResource("annotatemiddle.png"));
        annotate = new JButton("", annotateButtonIcon);
        annotate.setPreferredSize(buttonDim);
        annotate.setToolTipText(i18n.getString("tooltip.annotate"));
        annotate.setBorder(new LineBorder(Color.lightGray, 1, true));
        annotate.setBorderPainted(false);
        CSH.setHelpIDString(annotate, "annotate");
        disableElements.add(annotate);
        ImageIcon pauseButtonIcon = new ImageIcon(getResource("pausemiddle.png"));
        JButton pause = new JButton("", pauseButtonIcon);
        pause.setPreferredSize(buttonDim);
        pause.setToolTipText(i18n.getString("tooltip.simulation.pause"));
        pause.setEnabled(false);
        reverseDisableElements.add(pause);
        CSH.setHelpIDString(pause, "pause");
        ImageIcon helpButtonIcon = new ImageIcon(getResource("Questionmark.png"));
        help = new JButton("", helpButtonIcon);
        help.setPreferredSize(buttonDim);
        help.setToolTipText(i18n.getString("tooltip.help"));
        help.addActionListener(helpListener);
        // Check if there are Functionalities for the Buttons and if yes calling the setup.
        if (toolFunctionalities.containsKey("help")) {
            setupButton(help, "help");
        }
        if (toolFunctionalities.containsKey("step")) {
            setupButton(step, "step");
        }
        if (toolFunctionalities.containsKey("annotate")) {
            setupButton(annotate, "annotate");
        }
        if (toolFunctionalities.containsKey("pause")) {
            setupButton(pause, "pause");
        }
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
        if (toolFunctionalities.containsKey("help")) {
            setupButton(move, "help");
        }
        if (viewModules != null) {
            modules = new WideComboBox(viewModules.toArray());
            modules.setPreferredSize(COMBO_DIM);
            modules.setToolTipText(i18n.getString("tooltip.modules"));
        }
        if (toolFunctionalities.containsKey("selectModule")) {
            modules.addActionListener(toolFunctionalities.get("selectModule"));
            modules.setActionCommand("selectModule");
            modules.setName("selectModule");
        }
        CSH.setHelpIDString(modules, "create");
        // Adding Buttons to the ToolBar.
        toolBar.add(modules);
        toolBar.addSeparator(SEPERATOR_DIM);
        toolBar.add(select);
        toolBar.add(create);
        toolBar.add(toggle);
        toolBar.add(move);
        toolBar.add(annotate);
        toolBar.add(help);
        toolBar.addSeparator(SEPERATOR_DIM);
        toolBar.add(faster);
        toolBar.add(slower);
        toolBar.add(start);
        toolBar.add(step);
        toolBar.add(pause);
        toolBar.add(stop);
        toolBar.addSeparator(SEPERATOR_DIM);
        for (Component comp : toolBar.getComponents()) {
            comp.setFocusable(false);
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
     * 
     * @param set
     *            Set<DrawElement> the Set with the Elements to Check.
     */
    private void allModulesInSight(Set<DrawElement> set) {
        List<DrawElement> elementsToCheck = new LinkedList<DrawElement>();
        if (set == null) {
            elementsToCheck = model.getDrawElements();
        } else {
            elementsToCheck.addAll(set);
        }
        for (DrawElement elem : elementsToCheck) {
            if (elem instanceof Module) {
                // If elem is a Module we must check if it is out of the workspace and if yes extend the workspace.
                if (((Module) elem).getRectangle().x >= workspace.getWidth()) {
                    workspace.setSize(((Module) elem).getRectangle().x, workspace.getHeight());
                    workspace.setPreferredSize(new Dimension(((Module) elem).getRectangle().x + SPACE_ARROUND_ELEM,
                            workspace.getHeight()));
                }
                if (((Module) elem).getRectangle().y >= workspace.getHeight()) {
                    workspace.setSize(workspace.getWidth(), ((Module) elem).getRectangle().y);
                    workspace.setPreferredSize(new Dimension(workspace.getWidth(), ((Module) elem).getRectangle().y
                            + SPACE_ARROUND_ELEM));
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
            msave2.setEnabled(true);
        }
        for (JComponent enable : reverseDisableElements) {
            enable.setEnabled(false);
        }
        modules.setEnabled(true);
    }

    /**
     * Disables all buttons and MenuItems except those for controlling the Simulations.
     */
    public void disableButtons() {
        // Disables all Elements in the List of Elements to be disabled.
        for (JComponent enable : disableElements) {
            enable.setEnabled(false);
            msave2.setEnabled(false);
        }
        for (JComponent enable : reverseDisableElements) {
            enable.setEnabled(true);
        }
        modules.setEnabled(false);
    }

    /**
     * Set the msave2 (Save As Button) en/disabled.
     * 
     * @param state
     *            boolean state to be set.
     */
    public void setEnableSaveAs(boolean state) {
        msave2.setEnabled(state);
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
     * Change ViewPort Position on the workspace by manipulating the ScrollBars.
     * 
     * @param dx
     *            the x-value by which the ScrollBsar should be moved.
     * @param dy
     *            the y-value by which the ScrollBar should be moved.
     */
    public void setViewportPosition(int dx, int dy) {
        horizontal.setValue(horizontal.getValue() + dx);
        vertical.setValue(vertical.getValue() + dy);
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
     * Sets the Text of the JLable cycle, the counter of the Program.
     * 
     * @param text
     *            String new text of the cycle.s
     */
    public void setCycleCount(String text) {
        cycle.setText(text);
    }

    /**
     * If new Modules have been loaded the list of Modules has to be refreshed.
     */
    public void refreshBox() {
        // remove ComboBox from ToolBar
        toolBar.remove(modules);
        // Build it new and adds it again at pos 0
        modules = new WideComboBox(viewModules.toArray());
        modules.setPreferredSize(COMBO_DIM);
        modules.setToolTipText(i18n.getString("tooltip.modules"));
        if (toolFunctionalities.containsKey("selectModule")) {
            modules.addActionListener(toolFunctionalities.get("selectModule"));
            modules.setActionCommand("selectModule");
            modules.setName("selectModule");
        }
        toolBar.add(modules, 0);
    }

    /**
     * Makes the Border around the active Tool Visible.
     * 
     * @param name
     *            String representing the name of the active Tool.
     */
    public void focuseButton(String name) {
        if (selectedTool != null) {
            selectedTool.setBorderPainted(false);
        }
        if (name.equals("move")) {
            move.setBorderPainted(true);
            selectedTool = move;
        } else if (name.equals("select")) {
            select.setBorderPainted(true);
            selectedTool = select;
        } else if (name.equals("toggle")) {
            toggle.setBorderPainted(true);
            selectedTool = toggle;
        } else if (name.equals("annotate")) {
            annotate.setBorderPainted(true);
            selectedTool = annotate;
        } else if (name.equals("create")) {
            create.setBorderPainted(true);
            selectedTool = create;
        } else if (name.equals("help")) {
            help.setBorderPainted(true);
            selectedTool = help;
        }
    }

    /**
     * set a new Title to the Frame.
     * 
     * @param title
     *            the new Title add.
     */
    public void setNewTitle(String title) {
        if (title != null) {
            this.setTitle(FRAME_TITLE + " - " + title);
        } else {
            this.setTitle(FRAME_TITLE + " - " + i18n.getString("unsaved"));
        }
    }

    /**
     * Extension of JComboBox to ensure the PopupMenu of the ComoBox is wide enough to Display the full names of the
     * Elements.
     */
    @SuppressWarnings("serial")
    public class WideComboBox extends JComboBox {

        /**
         * Width of the Combo Popup Menu.
         */
        private static final int COMBO_WIDTH = 160;

        /**
         * Default Constructor.
         */
        public WideComboBox() {
            super();
        }

        /**
         * Constructor for the WideComboBox with an array of Objects.
         * 
         * @param items
         *            array of Objects to be placed in the ComboBox.
         */
        public WideComboBox(final Object[] items) {
            super(items);
        }

        /**
         * Boolean representing if it was layed out.
         */
        private boolean layingOut = false;

        /**
         * Does the Layout.
         */
        public void doLayout() {
            try {
                layingOut = true;
                super.doLayout();
            } finally {
                layingOut = false;
            }
        }

        /**
         * Getter for the Dimension of the Popupmenu.
         * 
         * @return Dimension, representing the Dimension of the Popup.
         */
        public Dimension getSize() {
            Dimension dim = super.getSize();
            if (!layingOut) {
                dim.width = Math.max(COMBO_WIDTH, getPreferredSize().width);
            }
            return dim;
        }
    }
}

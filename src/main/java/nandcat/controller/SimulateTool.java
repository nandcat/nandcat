package nandcat.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.plaf.basic.BasicComboBoxUI.ItemHandler;
import nandcat.I18N;
import nandcat.I18N.I18NBundle;
import nandcat.model.Clock;
import nandcat.model.Model;
import nandcat.model.ModelEvent;
import nandcat.model.ModelListener;
import nandcat.model.ModelListenerAdapter;
import nandcat.view.CheckManager;
import nandcat.view.View;

/**
 * The SimulateTool is responsible for handling the Simulation and Checks.
 */
public class SimulateTool implements Tool {

    /**
     * Current Model instance.
     */
    private Model model;

    /**
     * Current Controller instance.
     */
    private Controller controller;

    /**
     * Current CheckManager instance.
     */
    private CheckManager checkManager;

    /**
     * Icon representation of the Tool.
     */
    private ImageIcon icon;

    /**
     * Translation unit.
     */
    private I18NBundle i18n = I18N.getBundle("toolsimulate");

    /**
     * String representation of the Tool.
     */
    @SuppressWarnings("serial")
    private List<String> represent = new LinkedList<String>() {

        {
            add("start");
            add("stop");
            add("faster");
            add("slower");
            add("startcheck");
            add("editcheck");
        }
    };

    /**
     * ActionListener of the Tool on the Buttons.
     */
    private ActionListener buttonListener;

    /**
     * ModelListener of the Tool on the Model.
     */
    private ModelListener modelListener;

    /**
     * ItemHanlder of the Tool the the ComboBox in the CheckManager.
     */
    private ItemHandler comboboxListener;

    /**
     * Reference on this Tool.
     */
    private Tool simulateTool;

    /**
     * View instance.
     */
    private View view;

    /**
     * Represents if the user wants to start a simulation.
     */
    private boolean simToStart = false;

    /**
     * Integer by which the simulation speed is raised or reduced.
     */
    private static final int SPEED_STEPS = 100;

    /**
     * Constructs the SimulateTool.
     * 
     * @param controller
     *            Controller component of the application.
     */
    public SimulateTool(Controller controller) {
        this.controller = controller;
        model = controller.getModel();
        view = controller.getView();
        simulateTool = this;
    }

    /**
     * {@inheritDoc}
     */
    public void setActive(boolean active) {
        if (active) {
            if (modelListener == null) {
                modelListener = new ModelListenerAdapter() {

                    private boolean simulating = false;

                    public void elementsChanged(ModelEvent e) {
                        if (simulating) {
                            // set the cycle count on the current cycle.
                            view.setCycleCount(i18n.getString("cycle.count") + model.getCycle());
                        }
                    }

                    public void simulationStarted(ModelEvent e) {
                        simulating = true;
                        view.disableButtons();
                    }

                    public void simulationStopped(ModelEvent e) {
                        // Stopping the simulation needs to enable the buttons and set the "Counter".
                        simulating = false;
                        view.enableButtons();
                        view.setCycleCount(i18n.getString("cycle.stand"));
                    }

                    public void checksStopped(ModelEvent e) {
                        // All checks are passed if everyone was successful we can start the simulation.
                        if (e.allChecksPassed() && simToStart) {
                            checkManager.setVisible(false);
                            model.startSimulation();
                        }
                    }
                };
            }
            model.addListener(modelListener);
        } else {
            model.removeListener(modelListener);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, ActionListener> getFunctionalities() {
        buttonListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("start")) {
                    // User wants to start simulation. First we have to perform Checks.
                    controller.requestActivation(simulateTool);
                    simToStart = true;
                    if (checkManager == null) {
                        checkManager = new CheckManager(model.getChecks(), comboboxListener);
                    }
                    checkManager.setVisible(true);
                    model.startChecks();
                } else if (e.getActionCommand().equals("stop")) {
                    model.stopSimulation();
                    simToStart = false;
                } else if (e.getActionCommand().equals("faster")) {
                    // reduce clock sleep time -> faster simulation.
                    Clock clock = model.getClock();
                    clock.setSleepTime(clock.getSleepTime() - SPEED_STEPS);
                } else if (e.getActionCommand().equals("slower")) {
                    // raise clock sleep time -> slower simulation.
                    Clock clock = model.getClock();
                    clock.setSleepTime(clock.getSleepTime() + SPEED_STEPS);
                } else if (e.getActionCommand().equals("startcheck")) {
                    if (checkManager == null) {
                        checkManager = new CheckManager(model.getChecks(), comboboxListener);
                    }
                    checkManager.setVisible(true);
                    model.startChecks();
                } else if (e.getActionCommand().equals("editcheck")) {
                    if (checkManager == null) {
                        checkManager = new CheckManager(model.getChecks(), comboboxListener);
                    }
                    checkManager.setVisible(true);
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
     * {@inheritDoc}
     */
    public ImageIcon getIcon() {
        return icon;
    }
}

package nandcat.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
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
     * ActionListenr for the CheckManager.
     */
    private ActionListener checkManagerListener;

    /**
     * String representation of the Tool.
     */
    @SuppressWarnings("serial")
    private List<String> represent = new LinkedList<String>() {

        {
            add("start");
            add("stop");
            add("pause");
            add("faster");
            add("slower");
            add("startcheck");
            add("editcheck");
            add("step");
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
     * Reference on this Tool.
     */
    private Tool simulateTool;

    /**
     * View instance.
     */
    private View view;

    /**
     * Boolean representing if the simulation has been paused or not.
     */
    private boolean paused = false;

    /**
     * Represents if the user wants to start a simulation.
     */
    private boolean simToStart = false;

    private boolean stepSim = false;

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
                checkManager.setButton(false);
            }

            public void simulationStopped(ModelEvent e) {
                // Stopping the simulation needs to enable the buttons and set the "Counter".
                System.out.println("stopped");
                simulating = false;
                view.enableButtons();
                view.setCycleCount(i18n.getString("cycle.stand"));
                checkManager.setButton(true);
            }

            public void checksStopped(ModelEvent e) {
                // All checks are passed if everyone was successful we can start the simulation.
                if (e.allChecksPassed() && simToStart) {
                    // checkManager.setVisible(false);
                    model.startSimulation();
                    if (stepSim) {
                        model.pause();
                    }
                }
            }
        };
        model.addListener(modelListener);
    }

    /**
     * {@inheritDoc}
     */
    public void setActive(boolean active) {
        // if (active) {
        // if (modelListener == null) {
        // modelListener = new ModelListenerAdapter() {
        //
        // private boolean simulating = false;
        //
        // public void elementsChanged(ModelEvent e) {
        // if (simulating) {
        // // set the cycle count on the current cycle.
        // view.setCycleCount(i18n.getString("cycle.count") + model.getCycle());
        // }
        // }
        //
        // public void simulationStarted(ModelEvent e) {
        // simulating = true;
        // view.disableButtons();
        // checkManager.setButton(false);
        // }
        //
        // public void simulationStopped(ModelEvent e) {
        // // Stopping the simulation needs to enable the buttons and set the "Counter".
        // simulating = false;
        // view.enableButtons();
        // view.setCycleCount(i18n.getString("cycle.stand"));
        // checkManager.setButton(true);
        // }
        //
        // public void checksStopped(ModelEvent e) {
        // // All checks are passed if everyone was successful we can start the simulation.
        // if (e.allChecksPassed() && simToStart) {
        // // checkManager.setVisible(false);
        // model.startSimulation();
        // }
        // }
        // };
        // }
        // model.addListener(modelListener);
        // } else {
        // model.removeListener(modelListener);
        // }
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
                    startCheckManager();
                    model.startChecks();
                } else if (e.getActionCommand().equals("stop")) {
                    model.stopSimulation();
                    simToStart = false;
                    stepSim = false;
                    paused = false;
                } else if (e.getActionCommand().equals("faster")) {
                    // reduce clock sleep time -> faster simulation.
                    Clock clock = model.getClock();
                    clock.setSleepTime(clock.getSleepTime() - SPEED_STEPS);
                } else if (e.getActionCommand().equals("slower")) {
                    // raise clock sleep time -> slower simulation.
                    Clock clock = model.getClock();
                    clock.setSleepTime(clock.getSleepTime() + SPEED_STEPS);
                } else if (e.getActionCommand().equals("startcheck")) {
                    startCheckManager();
                    model.startChecks();
                } else if (e.getActionCommand().equals("editcheck")) {
                    startCheckManager();
                } else if (e.getActionCommand().equals("pause")) {
                    if (paused) {
                        model.unpause();
                        paused = false;
                    } else {
                        model.pause();
                        paused = true;
                    }
                } else if (e.getActionCommand().equals("step")) {
                    if (simToStart) {
                        if (paused) {
                            model.unpause();
                            model.pause();
                        } else {
                            model.pause();
                            paused = true;
                        }
                    } else {
                        controller.requestActivation(simulateTool);
                        simToStart = true;
                        stepSim = true;
                        paused = true;
                        startCheckManager();
                        model.startChecks();
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

    private void startCheckManager() {
        if (checkManager == null) {
            if (checkManagerListener == null) {
                checkManager = new CheckManager(model.getChecks(), checkManagerListener = new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        if (e.getActionCommand().equals("okay")) {
                            checkManager.setVisible(false);
                        } else if (e.getActionCommand().equals("check")) {
                            if (model.isDirty()) {
                                checkManager.resetList();
                            }
                            model.startChecks();
                        }
                    }
                });
            } else {
                checkManager = new CheckManager(model.getChecks(), checkManagerListener);
            }
        }
        checkManager.setVisible(true);
    }

    /**
     * {@inheritDoc}
     */
    public ImageIcon getIcon() {
        return icon;
    }
}

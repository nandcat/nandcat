package nandcat.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.plaf.basic.BasicComboBoxUI.ItemHandler;
import nandcat.model.Model;
import nandcat.model.ModelEvent;
import nandcat.model.ModelListener;
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
    private ImageIcon icon; // TODO icon setzen

    /**
     * String representation of the Tool.
     */
    private List<String> represent = new LinkedList<String>() {

        {
            add("start");
            add("stop");
            add("faster");
            add("slower");
            add("startcheck");
            add("editcheck");
        }
    }; // TODO beschreibung schreiben

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
    private ItemHandler comboboxListener;// TODO nachschaun wie des funzt!!

    /**
     * Reference on this Tool.
     */
    protected Tool simulateTool;

    /**
     * View instance.
     */
    private View view;

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
                modelListener = new ModelListener() {

                    public void elementsChanged(ModelEvent e) {
                    }

                    public void checksStarted(ModelEvent e) {
                        if (checkManager == null) {
                            checkManager = new CheckManager(e.getChecks(), comboboxListener);
                        }
                        checkManager.setVisible(true);
                    }

                    public void checksStopped(ModelEvent e) {
                        // TODO Auto-generated method stub
                    }

                    public void simulationStarted(ModelEvent e) {
                        // TODO Auto-generated method stub
                    }

                    public void simulationStopped(ModelEvent e) {
                        view.enableButtons();
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
                if (e.getActionCommand() == "start") {
                    controller.requestActivation(simulateTool);
                    model.startSimulation();
                    view.disableButtons();
                } else if (e.getActionCommand() == "stop") {
                    model.stopSimulation();
                    view.enableButtons();
                } else if (e.getActionCommand() == "faster") {
                    // sim geschw. erhöhen
                } else if (e.getActionCommand() == "slower") {
                    // sim geschw. verringern
                } else if (e.getActionCommand() == "startcheck") {
                    if (checkManager == null) {
                        checkManager = new CheckManager(model.getChecks(), comboboxListener);
                    }
                    checkManager.setVisible(true);
                } else if (e.getActionCommand() == "editcheck") {
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

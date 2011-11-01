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
            add("bstart");
            add("bstop");
            add("bplus");
            add("bminus");
            add("msstart");
            add("msstop");
            add("msplus");
            add("msminus");
            add("cbausteine");
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
    private ItemHandler comboboxListener;

    protected Tool simulateTool;

    private nandcat.view.View view;

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

                    public void checksChanged(ModelEvent e) {
                        // TODO Auto-generated method stub
                    }

                    public void simulationChanged(ModelEvent e) {
                        // TODO Auto-generated method stub
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
                if (e.getActionCommand() == "bstart" || e.getActionCommand() == "msstart") {
                    controller.requestActivation(simulateTool);
                    model.startSimulation();
                    view.disableButtons();
                } else if (e.getActionCommand() == "bstop" || e.getActionCommand() == "msstop") {
                    model.stopSimulation();
                    view.enableButtons();
                } else if (e.getActionCommand() == "bplus" || e.getActionCommand() == "msplus") {
                    // sim geschw. erhöhen
                } else if (e.getActionCommand() == "bminus" || e.getActionCommand() == "msminus") {
                    // sim geschw. verringern
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

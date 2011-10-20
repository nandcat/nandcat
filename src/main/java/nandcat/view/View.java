package nandcat.view;

import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Set;
import javax.swing.JFrame;
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
     * A Map with all the Tool functionalities and their Listeners.
     */
    private Map<String, ActionListener> toolFunctionalities;

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
    }

    /**
     * Redraws the workspace with its elements.
     * 
     * @param e
     *            ModelEvent with the elements to be redrawed.
     */
    public void redraw(ModelEvent e) {
    }

    /**
     * Enables all buttons.
     */
    public void enableButtons() {
        // TODO Wirklich enable Buttons, weil simulation buttons ja noch
        // laufen?...
        // TODO Naja ich glaub der FAll is ja eh nur in der Simulation relevant
        // -> Alle anderen Buttons Disabeln
    }

    /**
     * Disables all buttons except buttons for simulation.
     */
    public void disableButtons() {
        // TODO Wirklich disable Buttons?
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
    }

    /**
     * With this method the Controller is able to give the View the Informations about the Tools needed.
     * 
     * @param map
     *            Map<String, ActionListener> a map with the Funktionalities of the Tools and their Listeners.
     */
    public void setFunctionalities(Map<String, ActionListener> map) {
        this.toolFunctionalities = map;
    }

    /**
     * Changes the Height of the Workspace.
     * 
     * @param newHeight
     *            int to which the Height will be set.
     */
    public void setWorkspaceHeight(int newHeight) {
    }

    /**
     * Changes the Width of the Workspace.
     * 
     * @param newWidth
     *            int to which the Width will be set.
     */
    public void setWorkspaceWidth(int newWidth) {
    }

    /**
     * With this method the Controller is able to give the View the Informations about the Elements available.
     * 
     * @param viewele
     *            Set<ViewModule> with the Elements available.
     */
    public void setViewModules(Set<ViewModule> viewele) {
    }
}

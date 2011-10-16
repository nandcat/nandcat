package de.unipassau.sep.nandcat.view;

import javax.swing.JFrame;
import de.unipassau.sep.nandcat.model.Model;

/**
 * View.
 * 
 * Displays all graphical components including the workspace.
 * 
 * @version 0.1
 */
public class View extends JFrame {

    // TODO brauchen methode um den Fenster-Ausschnitt über dem Workespace zu setzen.
    // TODO Implements modellistener
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
     * Constructs the view.
     * 
     * @param model
     *            The Model component of the application.
     */
    public View(Model model) {
        setupGui();
    }

    /**
     * Sets up GUI elements.
     */
    private void setupGui() {
    }

    /**
     * Redraws the workspace with its elements.
     */
    public void redraw() {
        // TODO Missing parameter
    }

    /**
     * Enables all buttons.
     */
    public void enableButtons() {
        // TODO Wirklich enable Buttons, weil simulation buttons ja noch laufen?...
        // TODO Naja ich glaub der FAll is ja eh nur in der Simulation relevant -> Alle anderen Buttons Disabeln
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
}

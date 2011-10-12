package de.unipassau.sep.nandcat;

import javax.swing.SwingUtilities;
import de.unipassau.sep.nandcat.controller.Controller;
import de.unipassau.sep.nandcat.model.Model;
import de.unipassau.sep.nandcat.view.View;

/**
 * NandCat Main Class.
 * 
 * Main class with main method.
 * 
 * @version 0.1
 */
public final class Nandcat {

    /**
     * Private constructor.
     * 
     * Prevents instantiation.
     */
    private Nandcat() {
        throw new UnsupportedOperationException();
    }

    /**
     * Main method, executed while application start.
     * 
     * Starts Swingthread and application.
     * 
     * @param args
     *            Command-line arguments.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                Nandcat.initApp();
            }
        });
    }

    /**
     * Initializes application components.
     * 
     * Creates Model, View, Controller instances and shows the gui.
     */
    private static void initApp() {
        Model model = new Model();
        View view = new View(model);
        new Controller(view, model);
        view.setVisible(true);
    }
}

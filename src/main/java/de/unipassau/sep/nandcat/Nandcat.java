package de.unipassau.sep.nandcat;

import javax.swing.SwingUtilities;
import de.unipassau.sep.nandcat.model.Model;

/**
 * NandCat Main Class.
 * 
 * @version 0.1
 * 
 */
public final class Nandcat {

    // private View view;
    // private Model model;
    // private Controller controller;
    // TODO Creates model, view, controller but only holds instances
    // temporarily, no fields.
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
     * Init application.
     */
    private static void initApp() {
        Model model = new Model();
        // TODO View und Controller entsprechend initialisieren und
        // Konstruktoren schreiben.
    }
}

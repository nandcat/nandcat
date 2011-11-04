package nandcat;

import javax.swing.SwingUtilities;
import nandcat.controller.Controller;
import nandcat.model.Model;
import nandcat.view.View;

/**
 * NandCat Main Class.
 * 
 * Initializes Model, View, Controller and starts the application.
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
     * Starts Swingthread and application within.
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
        Controller controller = new Controller(view, model);
        view.setVisible(true);
        controller.giveFunctionalities();
    }
}

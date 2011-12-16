package nandcat;

import java.util.Locale;
import javax.swing.SwingUtilities;
import nandcat.controller.Controller;
import nandcat.model.Model;
import nandcat.view.View;
import org.apache.log4j.Logger;

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
     * Class logger instance.
     */
    private static final Logger LOG = Logger.getLogger(Nandcat.class);

    /**
     * Main method, executed while application start.
     * 
     * Starts Swingthread and application within.
     * 
     * @param args
     *            Command-line arguments.
     */
    public static void main(final String[] args) {

        // Handle all exceptions with log4j
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            public void uncaughtException(Thread t, Throwable e) {
                LOG.warn("Exception caught in Thread: " + t.toString(), e);
            }
        });
        setLocales(args);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                initApp();
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
        controller.giveFunctionalities();
        view.setVisible(true);
    }

    /**
     * Sets the locale depending on first argument. If no first argument exists, the system language is used.
     * 
     * @param args
     *            Command-line arguments.
     */
    private static void setLocales(String[] args) {
        if (args.length >= 1) {
            LOG.debug("Locale got from commandline arguments");
            I18N.setLocale(args[0]);
        } else {
            LOG.debug("Locale got from system property");

            // java -Duser.language=fr/de/en
            I18N.setLocale(Locale.getDefault());
        }
    }
}

package nandcat.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import nandcat.model.Model;
import org.apache.log4j.Logger;

/**
 * Tool for importing files. Includes loading.
 */
public class ImportTool implements Tool {

    /**
     * Current Model instance.
     */
    private Model model;

    /**
     * Current Controller instance.
     */
    private Controller controller;

    /**
     * Icon representation of the Tool.
     */
    private ImageIcon icon; // TODO icon setzen

    /**
     * String representation of the Tool.
     */
    private List<String> represent = new LinkedList<String>() {

        {
            add("new");
            add("load");
        }
    }; // TODO beschreibung schreiben

    /**
     * ActionListerner of the Tool on the Buttons.
     */
    private ActionListener buttonListener;

    /**
     * Class logger instance.
     */
    private static final Logger LOG = Logger.getLogger(ImportTool.class);

    /**
     * Constructs the ImportTool.
     * 
     * @param controller
     *            Controller component of the application.
     */
    public ImportTool(Controller controller) {
        this.controller = controller;
        this.model = controller.getModel();
    }

    /**
     * {@inheritDoc}
     */
    public void setActive(boolean active) {
        // Always active
    }

    /**
     * Request activation of functionality.
     * 
     * @param command
     *            String representing functionality to active.
     */
    private void request(String command) {
        LOG.debug("Request command: " + command);
        if (command.equals("load")) {
            actionLoad();
        } else if (command.equals("new")) {
            actionNew();
        }
    }

    /**
     * Requests to create a new Circuit and replace the old one.
     */
    private void actionNew() {
        model.newCircuit();
    }

    /**
     * Shows a file dialog to load a file as a circuit.
     */
    private void actionLoad() {
        JFileChooser fc = new JFileChooser();
        ImportExportUtils.addFileFilterToChooser(fc, model.getImportFormats());
        fc.setAcceptAllFileFilterUsed(false);
        int returnVal = fc.showOpenDialog(controller.getView());

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            if (file != null) {
                LOG.debug("Importing: " + file.getName());
                model.importFromFile(file);
            } else {
                LOG.debug("File is null");
            }
        } else {
            LOG.debug("Open command cancelled by user.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, ActionListener> getFunctionalities() {
        buttonListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                request(e.getActionCommand());
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

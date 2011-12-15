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
import javax.swing.JOptionPane;
import nandcat.I18N;
import nandcat.I18N.I18NBundle;
import nandcat.model.Model;
import nandcat.model.ModelEvent;
import nandcat.model.ModelListener;
import nandcat.model.ModelListenerAdapter;
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
    private ImageIcon icon;

    /**
     * String representation of the Tool.
     */
    @SuppressWarnings("serial")
    private List<String> represent = new LinkedList<String>() {

        {
            add("new");
            add("load");
        }
    };

    /**
     * ActionListerner of the Tool on the Buttons.
     */
    private ActionListener buttonListener;

    /**
     * Last loaded file if exists.
     */
    private File lastLoadedFile = null;

    /**
     * Class logger instance.
     */
    private static final Logger LOG = Logger.getLogger(ImportTool.class);

    /**
     * Translation unit.
     */
    private I18NBundle i18n = I18N.getBundle("toolimport");

    /**
     * Constructs the ImportTool.
     * 
     * @param controller
     *            Controller component of the application.
     */
    public ImportTool(Controller controller) {
        this.controller = controller;
        this.model = controller.getModel();
        this.model.addListener(getModelListener());
    }

    /**
     * Gets the model listener to register on model events.
     * 
     * @return ModelListener.
     */
    private ModelListener getModelListener() {
        return new ModelListenerAdapter() {

            @Override
            public void importFailed(ModelEvent e) {
                JOptionPane.showMessageDialog(controller.getView(), i18n.getString("dialog.importfail.text") + " \n "
                        + e.getMessage(), i18n.getString("dialog.importfail.title"), JOptionPane.WARNING_MESSAGE);
            }

            @Override
            public void importCustomCircuitFailed(ModelEvent e) {
                JOptionPane.showMessageDialog(controller.getView(), i18n.getString("dialog.importcustomfail.text")
                        + " \n " + e.getMessage(), i18n.getString("dialog.importcustomfail.title"),
                        JOptionPane.WARNING_MESSAGE);
            }

        };
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

        // Deactivate other tools to stay consistent.
        controller.requestActivation(this);
        controller.getView().focuseButton("nothing");
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
        if (lastLoadedFile != null) {
            fc.setSelectedFile(lastLoadedFile);
        }
        ImportExportUtils.addFileFilterToChooser(fc, model.getImportFormats());
        if (fc.getFileFilter() == null && fc.getChoosableFileFilters().length > 0) {
            fc.setFileFilter(fc.getChoosableFileFilters()[0]);
        }
        fc.setAcceptAllFileFilterUsed(false);
        int returnVal = fc.showOpenDialog(controller.getView());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            if (file != null) {
                LOG.debug("Importing: " + file.getName());
                this.lastLoadedFile = file;
                model.importRootFromFile(file);
                if (controller.getView().getWorkspace().getGridEnable()) {
                    model.adaptAllToGrid(controller.getView().getWorkspace().getGridSize());
                }
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

package nandcat.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import nandcat.model.Model;
import nandcat.model.ModelEvent;
import nandcat.model.ModelListener;
import nandcat.model.ModelListenerAdapter;
import org.apache.log4j.Logger;

/**
 * Tool for exporting files. Includes saving.
 */
public class ExportTool implements Tool {

    /**
     * Option 'YES' of the dialog shown if circuit is changed.
     */
    private static final int CIRCUIT_CHANGE_CONFIRM_OPTION_YES = 2;

    /**
     * Option 'NO' of the dialog shown if circuit is changed.
     */
    private static final int CIRCUIT_CHANGE_CONFIRM_OPTION_NO = 0;

    /**
     * Option 'CANCEL' of the dialog shown if circuit is changed.
     */
    private static final int CIRCUIT_CHANGE_CONFIRM_OPTION_CANCEL = 1;

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
     * Holds uuid of last saved circuit.
     */
    private String saveLastUUID = "";

    /**
     * Holds file handle to last saved circuit file.
     */
    private File saveLastFile = null;

    /**
     * String representation of the Tool.
     */
    private List<String> represent = new LinkedList<String>() {

        /**
         * Default serial verion uid.
         */
        private static final long serialVersionUID = 1L;

        {
            add("save");
            add("saveAs");
        }
    }; // TODO beschreibung schreiben

    /**
     * ActionListerner of the Tool on the Buttons.
     */
    private ActionListener buttonListener;

    /**
     * Class logger instance.
     */
    private static final Logger LOG = Logger.getLogger(ExportTool.class);

    /**
     * Constructs the ExportTool.
     * 
     * @param controller
     *            Controller component of the application.
     */
    public ExportTool(Controller controller) {
        this.controller = controller;
        this.model = controller.getModel();
        this.model.addListener(getModelListener());
        this.controller.getView().addWindowListener(getWindowListener());
    }

    /**
     * Gets the model listener used to interrupt if circuit is changed.
     * 
     * @return The ModelListener.
     */
    private ModelListener getModelListener() {
        return new ModelListenerAdapter() {

            @Override
            public boolean changeCircuitRequested(ModelEvent e) {
                return actionSaveBeforeLost();
            }
        };
    }

    /**
     * Gets the window listener used to interrupt if window is closing.
     * 
     * @return The Windowlistener.
     */
    private WindowListener getWindowListener() {
        return new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                actionSaveBeforeLost();
            }
        };
    }

    /**
     * Action executed to save last progress before the changes would be lost.
     * 
     * @return True to interrupt the process if possible.
     */
    private boolean actionSaveBeforeLost() {
        if (model.getCircuit() != null && model.isDirty()) {
            int n = showCircuitChangeConfirmDialog();
            switch (n) {
                case CIRCUIT_CHANGE_CONFIRM_OPTION_YES:
                    actionSave();
                    break;
                case CIRCUIT_CHANGE_CONFIRM_OPTION_CANCEL:
                    return true;
                case CIRCUIT_CHANGE_CONFIRM_OPTION_NO:
                    return false;
                default:
                    return false;
            }
        }
        return false;
    }

    /**
     * Shows the dialog to check next step after circuit will change and circuit is modified.
     * 
     * @return Option No, Cancel, Yes as Integer.
     */
    private int showCircuitChangeConfirmDialog() {
        Object[] options = { "No", "Cancel", "Yes" };
        return JOptionPane.showOptionDialog(controller.getView(), "Circuit has been modified. Save changes?",
                "Save Circuit", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
                options[2]);
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
     *            String representing functionality to activate.
     */
    private void request(String command) {
        if (command.equals("saveAs")) {
            actionSaveAs();
        } else if (command.equals("save")) {
            actionSave();
        } else {
            LOG.debug("Command '" + command + "' not supported.");
        }
    }

    /**
     * Checks if the action 'quick save' is available.
     * 
     * @return True if 'quick save' is available.
     */
    private boolean isQuickSaveAvailable() {
        return (model.getCircuit().getUuid().equals(saveLastUUID) && saveLastFile != null);
    }

    /**
     * Performs a 'quicksave' to the last used file if available, otherwise a 'save as'.
     */
    private void actionSave() {
        if (isQuickSaveAvailable()) {
            model.exportToFile(saveLastFile);
        } else {
            LOG.debug("Last save not available - no quicksave");
            actionSaveAs();
        }
    }

    /**
     * Shows save dialogs to export the circuit.
     */
    private void actionSaveAs() {
        JFileChooser fc = new JFileChooser();
        ImportExportUtils.addFileFilterToChooser(fc, model.getExportFormats());
        fc.setAcceptAllFileFilterUsed(false);
        int returnVal = fc.showSaveDialog(controller.getView());

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            if (file != null) {
                String extension = ImportExportUtils.getExtension(file);
                if (extension == null) {
                    ExtensionFileFilter eFileFilter = (ExtensionFileFilter) fc.getFileFilter();
                    String ext = eFileFilter.getExtension();
                    file = new File(file.getAbsolutePath() + "." + ext);
                }

                // Overwrite Dialog
                if (file.exists()) {
                    int response = JOptionPane.showConfirmDialog(null, "Overwrite existing file?", "Confirm Overwrite",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.CANCEL_OPTION) {
                        LOG.debug("Save command cancelled by user.");
                        return;
                    }
                }

                // Add Image to circuit
                Object[] options = { "Yes", "No", "Delete existing Image" };
                int n = JOptionPane.showOptionDialog(controller.getView(),
                        "Would you like to add a image to the current circuit?", "Circuit Image",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                if (n == 0) {
                    showImageLoadFileChooser();
                } else if (n == 2) {
                    model.getCircuit().setSymbol(null);
                }

                LOG.debug("Exporting: " + file.getName());
                saveLastFile = file;
                saveLastUUID = model.getCircuit().getUuid();
                model.exportToFile(file);
            } else {
                LOG.debug("File is null");
            }
        } else {
            LOG.debug("Save command cancelled by user.");
        }
    }

    /**
     * Shows the image chooser dialog for changing circuits symbol.
     */
    private void showImageLoadFileChooser() {
        JFileChooser fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        int returnVal = fc.showOpenDialog(controller.getView());

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            if (file != null) {
                boolean imgSuccess = false;
                try {
                    LOG.debug("Importing image: " + file.getName());
                    BufferedImage im = ImageIO.read(file);
                    if (im != null) {
                        model.getCircuit().setSymbol(im);
                        LOG.debug("Imported image successful");
                        imgSuccess = true;
                    }
                } catch (IOException e) {
                    // TODO Check exception
                    e.printStackTrace();
                }
                if (!imgSuccess) {
                    JOptionPane.showMessageDialog(controller.getView(), "Image can not be attached to circuit!",
                            "Export error", JOptionPane.ERROR_MESSAGE);
                    showImageLoadFileChooser();
                }

            } else {
                LOG.debug("File is null");
            }
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

package nandcat.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import nandcat.I18N;
import nandcat.I18N.I18NBundle;
import nandcat.model.Model;
import nandcat.model.ModelEvent;
import nandcat.model.ModelListener;
import nandcat.model.ModelListenerAdapter;
import nandcat.model.element.Circuit;
import nandcat.model.element.Connection;
import nandcat.model.element.DrawElement;
import nandcat.view.WorkspaceListenerAdapter;
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
    private ImageIcon icon;

    /**
     * Holds uuid of last saved circuit.
     */
    private String saveLastUUID = "";

    /**
     * Holds file handle to last saved circuit file using "save selected".
     */
    private File saveSelectedLastFile = null;

    /**
     * Holds file handle to last saved circuit file.
     */
    private File saveLastFile = null;

    /**
     * Translation unit.
     */
    private I18NBundle i18n = I18N.getBundle("toolexport");

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
            add("saveSelectedAs");
            add("close");
        }
    };

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
        this.controller.getView().getWorkspace().addListener(getWindowListener());
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

            @Override
            public void exportFailed(ModelEvent e) {
                JOptionPane.showMessageDialog(controller.getView(), i18n.getString("dialog.exportfail.text") + " \n "
                        + e.getMessage(), i18n.getString("dialog.exportfail.title"), JOptionPane.WARNING_MESSAGE);
            }

            @Override
            public void exportSucceeded(ModelEvent e) {
                ImportExportUtils.setViewTitle(controller.getView(), e.getFile());
            }

            @Override
            public void importSucceeded(ModelEvent e) {
                saveLastFile = e.getFile();
                saveLastUUID = e.getCircuitUuid();
            }
        };
    }

    /**
     * Gets the Workspace listener used to interrupt if window is closing.
     * 
     * @return The WorkspaceListenerAdapter with the windowClosing Event.
     */
    private WorkspaceListenerAdapter getWindowListener() {
        return new WorkspaceListenerAdapter() {

            public void windowClosing() {
                if (!actionSaveBeforeLost()) {
                    System.exit(0);
                }
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
        Object[] options = { i18n.getString("dialog.options.no"), i18n.getString("dialog.options.cancle"),
                i18n.getString("dialog.options.yes") };
        return JOptionPane.showOptionDialog(controller.getView(), i18n.getString("dialog.options.text"),
                i18n.getString("dialog.options.title"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[2]);
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
        } else if (command.equals("close")) {
            getWindowListener().windowClosing();
        } else if (command.equals("saveSelectedAs")) {
            actionSaveSelectedAs();
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
     * Shows an error dialog informing about an empty circuit.
     */
    private void showDialogCircuitEmpty() {
        JOptionPane.showMessageDialog(controller.getView(), i18n.getString("dialog.savecircuit.empty.text"),
                i18n.getString("dialog.savecircuit.empty.title"), JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Performs a 'quicksave' to the last used file if available, otherwise a 'save as'.
     */
    private void actionSave() {
        if (model.getDrawElements().size() == 0) {
            showDialogCircuitEmpty();
            return;
        }

        if (isQuickSaveAvailable()) {
            model.exportToFile(model.getCircuit(), saveLastFile, null);
        } else {
            LOG.debug("Last save not available - no quicksave");
            actionSaveAs();
        }
    }

    /**
     * Shows save dialogs to export the circuit.
     */
    private void actionSaveAs() {
        if (model.getDrawElements().size() == 0) {
            showDialogCircuitEmpty();
            return;
        }

        JFileChooser fc = buildExportFileChooser();
        if (saveLastFile != null) {
            if (saveLastUUID.equals(model.getCircuit().getUuid())) {
                fc.setSelectedFile(saveLastFile);
            } else {
                File dir = new File(ImportExportUtils.getFilePath(saveLastFile));
                if (dir.isDirectory()) {
                    fc.setCurrentDirectory(dir);
                } else {
                    fc.setSelectedFile(saveLastFile);
                }
            }

        }
        int returnVal = fc.showSaveDialog(controller.getView());

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (fc.getFileFilter() == null) {
                actionSaveAs();
                return;
            }
            File file = getFileToSaveTo(fc);

            // Restart process if file is null
            if (file == null) {
                actionSaveAs();
                return;
            }

            // Overwrite Dialog
            if (file.exists()) {
                int response = showFileOverwriteConfirmation();
                if (response == JOptionPane.CANCEL_OPTION) {
                    LOG.debug("Save command cancelled by user.");
                    return;
                } else if (response == JOptionPane.NO_OPTION) {
                    actionSaveAs();
                    return;
                }
            }

            // Add Image to circuit
            int n = showImageOptionDialog();

            // Replace current image with new one.
            if (n == 0) {
                BufferedImage im = showImageLoadFileChooser();

                // Only set image if image available.
                if (im != null) {
                    model.getCircuit().setSymbol(new ImageIcon(im));
                }
            } else if (n == 2) {

                // Clear current image.
                model.getCircuit().setSymbol(null);
            }
            LOG.debug("Exporting: " + file.getName());
            saveLastFile = file;
            saveLastUUID = model.getCircuit().getUuid();
            model.exportToFile(model.getCircuit(), file, controller.getView().getDrawer());

        } else {
            LOG.debug("Save command cancelled by user.");
        }
    }

    /**
     * Checks if selected elements are available.
     * 
     * @return True if selected elements are available.
     */
    private boolean selectedElementsAvailable() {
        List<DrawElement> elements = model.getDrawElements();
        for (DrawElement drawElement : elements) {
            if (drawElement.isSelected() && !(drawElement instanceof Connection)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Shows save dialogs to export the selected elements as circuit.
     */
    private void actionSaveSelectedAs() {
        if (!selectedElementsAvailable()) {
            JOptionPane.showMessageDialog(controller.getView(), i18n.getString("dialog.selected.fail.text"),
                    i18n.getString("dialog.selected.fail.title"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        JFileChooser fc = buildExportFileChooser();

        // If no last save is available use last save path.
        if (saveSelectedLastFile == null && saveLastFile != null) {
            saveSelectedLastFile = saveLastFile;
        }
        if (saveSelectedLastFile != null) {
            File dir = new File(ImportExportUtils.getFilePath(saveSelectedLastFile));
            if (dir.isDirectory()) {
                fc.setCurrentDirectory(dir);
            } else {
                fc.setSelectedFile(saveSelectedLastFile);
            }
        }
        int returnVal = fc.showSaveDialog(controller.getView());

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (fc.getFileFilter() == null) {
                actionSaveSelectedAs();
                return;
            }
            File file = getFileToSaveTo(fc);

            // Restart process if file is null
            if (file == null) {
                actionSaveAs();
                return;
            }

            // Overwrite Dialog
            if (file.exists()) {
                int response = showFileOverwriteConfirmation();
                if (response == JOptionPane.CANCEL_OPTION) {
                    LOG.debug("Save command cancelled by user.");
                    return;
                } else if (response == JOptionPane.NO_OPTION) {
                    actionSaveSelectedAs();
                    return;
                }
            }

            Circuit c = model.getCircuitFromSelected();

            // Add Image to circuit
            int n = showImageOptionDialogSaveSelected();

            // Replace current image with new one.
            if (n == 0) {
                BufferedImage im = showImageLoadFileChooser();

                // Only set image if image available.
                if (im != null) {
                    c.setSymbol(new ImageIcon(im));
                }
            }

            LOG.debug("Exporting: " + file.getName());
            saveSelectedLastFile = file;
            model.exportToFile(c, file, controller.getView().getDrawer());

        } else {
            LOG.debug("Save command cancelled by user.");
        }
    }

    /**
     * Shows a dialog to confirm if file would be overwritten.
     * 
     * @return Response code (JOptionPane).
     */
    private int showFileOverwriteConfirmation() {
        return JOptionPane
                .showConfirmDialog(null, i18n.getString("dialog.override.text"),
                        i18n.getString("dialog.override.title"), JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Get file to save to from file chooser.
     * 
     * @param fc
     *            FileChooser to get file from.
     * @return File to save to, null otherwise.
     */
    private File getFileToSaveTo(JFileChooser fc) {
        File file = fc.getSelectedFile();
        if (file != null) {
            String extension = ImportExportUtils.getExtension(file);
            ExtensionFileFilter eFileFilter = (ExtensionFileFilter) fc.getFileFilter();
            String ext = eFileFilter.getExtension();
            if (extension == null || !extension.equalsIgnoreCase(ext)) {
                file = new File(file.getAbsolutePath() + "." + ext);
            }
        }
        return file;
    }

    /**
     * Builds the export file chooser.
     * 
     * @return Export file chooser.
     */
    private JFileChooser buildExportFileChooser() {
        JFileChooser fc = new JFileChooser();
        ImportExportUtils.addFileFilterToChooser(fc, model.getExportFormats());
        if (fc.getFileFilter() == null && fc.getChoosableFileFilters().length > 0) {
            fc.setFileFilter(fc.getChoosableFileFilters()[0]);
        }
        fc.setAcceptAllFileFilterUsed(false);
        return fc;
    }

    /**
     * Shows dialog with image options.
     * 
     * @param options
     *            Available options.
     * @param selected
     *            Selected option.
     * @return Selected option. 0 == Yes, 1 == No, 2 == Delete existing image.
     */
    private int showImageOptionDialog(Object[] options, int selected) {
        return JOptionPane.showOptionDialog(controller.getView(), i18n.getString("dialog.image.text"),
                i18n.getString("dialog.image.title"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[selected]);
    }

    /**
     * Shows dialog with image options.
     * 
     * @return Selected option. 0 == Yes, 1 == No, 2 == Delete existing image.
     */
    private int showImageOptionDialog() {
        Object[] options = { i18n.getString("dialog.options.yes"), i18n.getString("dialog.options.no"),
                i18n.getString("dialog.options.delete") };
        return showImageOptionDialog(options, 1);
    }

    /**
     * Shows dialog with image options used for 'save selected as'.
     * 
     * @return Selected option. 0 == Yes, 1 == No
     */
    private int showImageOptionDialogSaveSelected() {
        Object[] options = { i18n.getString("dialog.options.yes"), i18n.getString("dialog.options.no") };
        return showImageOptionDialog(options, 1);
    }

    /**
     * Shows the image chooser dialog for changing circuits symbol.
     * 
     * @return BufferedImage read from selected file, null otherwise.
     */
    private BufferedImage showImageLoadFileChooser() {
        JFileChooser fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        BufferedImage im = null;
        int returnVal = fc.showOpenDialog(controller.getView());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            if (file != null) {
                boolean imgSuccess = false;
                try {
                    LOG.debug("Importing image: " + file.getName());
                    im = ImageIO.read(file);
                    if (im != null) {
                        LOG.debug("Imported image successful");
                        imgSuccess = true;
                    }
                } catch (IOException e) {
                    // If happend, imgSuccess is false and dialog is shown again.
                    LOG.debug("Can't read file");
                }
                if (!imgSuccess) {
                    JOptionPane.showMessageDialog(controller.getView(), i18n.getString("dialog.image.fail.text"),
                            i18n.getString("dialog.image.fail.title"), JOptionPane.ERROR_MESSAGE);
                    return showImageLoadFileChooser();
                }
            } else {
                LOG.debug("File is null");
            }
        }
        return im;
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

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
import nandcat.model.Model;
import nandcat.view.WorkspaceListener;
import org.apache.log4j.Logger;

/**
 * Tool for exporting files. Includes saving.
 */
public class ExportTool implements Tool {

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
            add("save");
            add("saveAs");
        }
    }; // TODO beschreibung schreiben

    /**
     * ActionListerner of the Tool on the Buttons.
     */
    private ActionListener buttonListener;

    /**
     * WorkspaceListener of the Tool.
     */
    private WorkspaceListener workspaceListener;

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
        if (command.equals("save") || command.equals("saveAs")) {
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

                    if (file.exists()) {
                        int response = JOptionPane.showConfirmDialog(null, "Overwrite existing file?",
                                "Confirm Overwrite", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (response == JOptionPane.CANCEL_OPTION) {
                            LOG.debug("Save command cancelled by user.");
                            return;
                        }
                    }
                    LOG.debug("Exporting: " + file.getName());
                    // Ergänze extension
                    model.exportToFile(file);
                } else {
                    LOG.debug("File is null");
                }
            } else {
                LOG.debug("Save command cancelled by user.");
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

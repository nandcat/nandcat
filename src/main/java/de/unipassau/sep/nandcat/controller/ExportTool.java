package de.unipassau.sep.nandcat.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import de.unipassau.sep.nandcat.model.Model;
import de.unipassau.sep.nandcat.view.WorkspaceListener;

/**
 * Tool for exporting files. Includes saving.
 * 
 * @version 0.1
 * 
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
    private String represent; // TODO beschreibung schreiben

    /**
     * ActionListerner of the Tool on the Buttons.
     */
    private ActionListener buttonListener;

    /**
     * WorkspaceListener of the Tool.
     */
    private WorkspaceListener workspaceListener;

    /**
     * Constructs the SelectTool.
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
     * {@inheritDoc}
     */
    public ActionListener getListener() {
        if (buttonListener != null) {
            return buttonListener;
        } else {
            buttonListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    // TODO Auto-generated method stub
                }
            };
        }
        return buttonListener;
    }

    /**
     * {@inheritDoc}
     */
    public String getText() {
        return represent;
    }

    /**
     * {@inheritDoc}
     */
    public ImageIcon getIcon() {
        return icon;
    }
}

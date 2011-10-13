package de.unipassau.sep.nandcat.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import de.unipassau.sep.nandcat.model.Model;

/**
 * The SelectTool is responsible for selecting and moving Elements on the Workspace.
 * 
 * @version 0.1
 * 
 */
public class SelectTool implements Tool {

    /**
     * Current Model instance.
     */
    private Model model = null;
    /**
     * Current Controller instance.
     */
    private Controller controller;
    /**
     * Icon representation of the Tool.
     */
    private ImageIcon icon;// TODO icon setzen
    /**
     * String representation of the Tool.
     */
    private String represent; // TODO beschreibung schreiben
    /**
     * ActionListerner of the Tool.
     */
    private ActionListener selectListener;

    public SelectTool(Model model, Controller controller) {
        this.model = model;
        this.controller = controller;
    }

    /**
     * {@inheritDoc}
     */
    public void setActive(boolean active) {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     */
    public ActionListener getListener() {
        if (selectListener != null) {
            return selectListener;
        } else {
            selectListener = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    // TODO Auto-generated method stub
                }
            };
        }
        return selectListener;
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

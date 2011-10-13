package de.unipassau.sep.nandcat.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import de.unipassau.sep.nandcat.model.Model;
import de.unipassau.sep.nandcat.view.View;

/**
 * The ViewTool is responsible for moving the display range over the Workspace.
 * 
 * @version 0.1
 * 
 */
public class ViewTool implements Tool {

    /**
     * Current View instance.
     */
    private View view = null;
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

    public ViewTool(View view, Controller controller) {
        this.view = view;
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

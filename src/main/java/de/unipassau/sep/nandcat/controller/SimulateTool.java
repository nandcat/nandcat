package de.unipassau.sep.nandcat.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import de.unipassau.sep.nandcat.model.Model;

/**
 * The SimulateTool is responsible for handling the Simulation and Checks.
 * 
 * @version 0.1
 * 
 */
public class SimulateTool implements Tool {

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
    private ActionListener simulateListener;

    public SimulateTool(Model model, Controller controller) {
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
        if (simulateListener != null) {
            return simulateListener;
        } else {
            simulateListener = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    // TODO Auto-generated method stub
                }
            };
        }
        return simulateListener;
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
    
    // TODO Implements modellistener anonym.
}

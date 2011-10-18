package de.unipassau.sep.nandcat.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import de.unipassau.sep.nandcat.model.Model;
import de.unipassau.sep.nandcat.view.View;

/**
 * A tool for deleting Elements from the circuit.
 * 
 * @version 0.1
 * 
 */
public class DeleteTool implements Tool {

    /**
     * Current Model instance.
     */
    private Model model;

    /**
     * Current Controller instance.
     */
    private Controller controller;

    /**
     * Current View instance.
     */
    private View view;

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
     * Constructs the DeleteTool.
     * 
     * @param controller
     *            Controller component of the application.
     */
    public DeleteTool(Controller controller) {
        this.controller = controller;
        this.model = controller.getModel();
        this.view = controller.getView();
        // TODO Funktioniert das mit dem Keylistener so? Ich würde den Keymanager nehmen - Ben
        this.view.addKeyListener(new KeyListener() {

            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
            }

            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
            }

            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
            }
        });
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

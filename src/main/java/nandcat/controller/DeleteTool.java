package nandcat.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import nandcat.model.Model;
import nandcat.model.element.Element;
import nandcat.view.View;

/**
 * A tool for deleting Elements from the circuit.
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
    private List<String> represent = new LinkedList<String>() {

        {
            add("delete");
        }
    };// TODO beschreibung schreiben

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
        represent = new LinkedList<String>();
        represent.add("delete");
        // TODO Funktioniert das mit dem Keylistener so? Ich würde den
        // Keymanager nehmen - Ben
        setActive(true);
    }


    /**
     * {@inheritDoc}
     */
    public void setActive(boolean active) {
        setListeners();
    }

    public void setListeners() {
        this.view.addKeyListener(new KeyListener() {

            public void keyTyped(KeyEvent e) {
                Element element = (Element) e.getSource();
                model.removeElement(element);
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
    public Map<String, ActionListener> getFunctionalities() {
        buttonListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
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

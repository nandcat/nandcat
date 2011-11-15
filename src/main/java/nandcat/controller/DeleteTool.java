package nandcat.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import nandcat.model.Model;
import nandcat.model.element.DrawElement;
import nandcat.model.element.Element;

/**
 * A tool for deleting Elements from the circuit.
 */
public class DeleteTool implements Tool {

    /**
     * Current Model instance.
     */
    private Model model;

    /**
     * Icon representation of the Tool.
     */
    private ImageIcon icon;

    /**
     * String representation of the Tool.
     */
    private List<String> represent = new LinkedList<String>() {

        /**
         * Default UID.
         */
        private static final long serialVersionUID = 1L;

        {
            add("delete");
        }
    };

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
        this.model = controller.getModel();
        represent = new LinkedList<String>();
        represent.add("delete");
    }

    /**
     * {@inheritDoc}
     * Not used because the tool is always active.
     */
    public void setActive(boolean active) {
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, ActionListener> getFunctionalities() {
        buttonListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for (DrawElement d : model.getDrawElements()) {
                    if (d.isSelected()) {
                        model.removeElement((Element) d);
                    }
                }
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

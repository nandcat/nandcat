package nandcat.controller;

import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.ImageIcon;

/**
 * Tool is an Interface implemented by the Tools. The user can interact with the Tools. If someone wants to add new
 * Tools he has to implement this Interface.
 */
public interface Tool {

    /**
     * Activates or deactivates tool.
     * 
     * @param active
     *            true to activate tool.
     */
    void setActive(boolean active);

    /**
     * Returns a Map with the functionalities as Strings (key) and their Listeners as ActionListeners (value).
     * 
     * This map is used to map between functionalities served by the tool and the view, which creates buttons and other
     * elements to activate these functionalities. The view creates the GUI and sets the specific action listeners
     * needed for providing the tool functionality.
     * 
     * @return Map<String, ActionListener> Map representing the Functionalities,
     */
    Map<String, ActionListener> getFunctionalities();

    /**
     * Returns a Icon representation of the Tool.
     * 
     * @return ImageIcon which represents the Tool
     */
    ImageIcon getIcon();
}

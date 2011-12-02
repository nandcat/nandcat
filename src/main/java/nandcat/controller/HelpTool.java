package nandcat.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import nandcat.view.View;

/**
 * The Help Tool is responsible for the Help Dialogs given to the User while using the Program.
 */
public class HelpTool implements Tool {

    /**
     * Current Controller instance.
     */
    private Controller controller;

    /**
     * View instance.
     */
    private View view;

    /**
     * Icon representation of the Tool.
     */
    private ImageIcon icon;

    /**
     * String representation of the Tool.
     */
    @SuppressWarnings("serial")
    private List<String> represent = new LinkedList<String>() {

        {
            add("help");
        }
    };

    /**
     * ActionListener of the Tool on the Buttons.
     */
    private ActionListener buttonListener;

    /**
     * Constructs the HelpTool.
     * 
     * @param controller
     *            Controller component of the application.
     */
    public HelpTool(Controller controller) {
        this.controller = controller;
        this.view = controller.getView();
    }

    /**
     * {@inheritDoc}
     */
    public void setActive(boolean active) {
        // It is only activated for deactivating the old tool.
        view.focuseButton("nothing");
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, ActionListener> getFunctionalities() {
        buttonListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                activate();
            }
        };
        Map<String, ActionListener> map = new HashMap<String, ActionListener>();
        for (String functionality : represent) {
            map.put(functionality, buttonListener);
        }
        return map;
    }

    /**
     * Requests activation by the controller.
     */
    private void activate() {
        controller.requestActivation(this);
    }

    /**
     * {@inheritDoc}
     */
    public ImageIcon getIcon() {
        return icon;
    }
}

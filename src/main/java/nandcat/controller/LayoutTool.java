package nandcat.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import nandcat.view.ANSIStandardElementDrawer;
import nandcat.view.IECStandardElementDrawer;
import nandcat.view.StandardElementDrawer;
import nandcat.view.View;

/**
 * The Help Tool is responsible for the Help Dialogs given to the User while using the Program.
 */
public class LayoutTool implements Tool {

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
            add("iec");
            add("standard");
            add("ansi");
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
    public LayoutTool(Controller controller) {
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
                if (e.getActionCommand().equals("iec")) {
                    view.getWorkspace().setDrawer(new IECStandardElementDrawer());
                    controller.setLastToolActive();
                } else if (e.getActionCommand().equals("standard")) {
                    view.getWorkspace().setDrawer(new StandardElementDrawer());
                    controller.setLastToolActive();
                } else if (e.getActionCommand().equals("ansi")) {
                    view.getWorkspace().setDrawer(new ANSIStandardElementDrawer());
                    controller.setLastToolActive();
                }
                view.getWorkspace().redraw();
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

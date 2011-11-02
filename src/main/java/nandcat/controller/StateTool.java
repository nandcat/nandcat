package nandcat.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.ImageIcon;
import nandcat.model.Model;
import nandcat.model.element.Element;
import nandcat.model.element.Module;
import nandcat.model.element.ImpulseGenerator;
import nandcat.view.View;
import nandcat.view.WorkspaceEvent;
import nandcat.view.WorkspaceListener;
import nandcat.view.WorkspaceListenerAdapter;

/**
 * The StateTool is responsible for setting the states (true or false) at the ImpulseGenerator.
 */
public class StateTool implements Tool {

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
    private List<String> represent; // TODO beschreibung schreiben

    /**
     * ActionListerner of the Tool for the Buttons and the Menu.
     */
    private ActionListener buttonListener;

    /**
     * WorkspaceListener of the Tool on the Workspace.
     */
    private WorkspaceListener workspaceListener;

    /**
     * Constructs the StateTool.
     * 
     * @param controller
     *            Controller component of the application.
     */
    public StateTool(Controller controller) {
        this.controller = controller;
        view = controller.getView();
        model = controller.getModel();
    }

    /**
     * {@inheritDoc}
     */
    public void setActive(boolean active) {
        if (active) {
            setListeners();
        } else {
            removeListeners();
        }
    }

    private void setListeners() {
        if (workspaceListener == null) {
            workspaceListener = new WorkspaceListenerAdapter() {
                @Override
                public void mouseClicked(WorkspaceEvent e) {
                    changeState(e.getLocation());
                }
            };
        }
        view.getWorkspace().addListener(workspaceListener);
    }

    private void removeListeners() {
        view.getWorkspace().removeListener(workspaceListener);
    }

    private void changeState(Point point) {
        assert point != null;
        Set<Element> elementsAt = model.getElementsAt(new Rectangle(point));
        Module toChangeState = null;
        for (Element element : elementsAt) {
            if (element instanceof Module) {
                toChangeState = (Module) element;
            }
        }
        if (toChangeState != null) {
            model.toggleModule(toChangeState);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, ActionListener> getFunctionalities() {
        buttonListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                activateTool();
            }
        };
        Map<String, ActionListener> map = new HashMap<String, ActionListener>();
        for (String functionality : represent) {
            map.put(functionality, buttonListener);
        }
        return map;
    }

    private void activateTool() {
        controller.requestActivation(this);
    }
    /**
     * {@inheritDoc}
     */
    public ImageIcon getIcon() {
        return icon;
    }
}

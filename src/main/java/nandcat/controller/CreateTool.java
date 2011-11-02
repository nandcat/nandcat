package nandcat.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import nandcat.model.Model;
import nandcat.model.element.Element;
import nandcat.model.element.Port;
import nandcat.model.element.Module;
import nandcat.view.View;
import nandcat.view.WorkspaceEvent;
import nandcat.view.WorkspaceListener;
import nandcat.view.WorkspaceListenerAdapter;

/**
 * The CreateTool is responsible for the creation of new Modules and Connections.
 */
public class CreateTool implements Tool {

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
     * ActionListerner of the Tool on the Buttons.
     */
    private ActionListener buttonListener;

    /**
     * WorkspaceListener of the Tool.
     */
    private WorkspaceListener workspaceListener;

    private Module selectedModule;

    private Port sourcePort;
    /**
     * Constructs the SelectTool.
     * 
     * @param controller
     *            Controller component of the application.
     */
    public CreateTool(Controller controller) {
        this.controller = controller;
        this.view = controller.getView();
        this.model = controller.getModel();
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
                    createElementAtPoint(e.getLocation());
                }
            };
        }
        view.getWorkspace().addListener(workspaceListener);
    }

    private void createElementAtPoint(Point point) {
        Set<Element> elementsAt = model.getElementsAt(point);
        if (elementsAt.isEmpty()) {
            if (selectedModule != null) {
                model.addModule(selectedModule, point);
            }
        } else {
            Module toConnect = null;
            for (Element element : elementsAt) {
                if (element instanceof Module) {
                    toConnect = (Module) element;
                }
            }
            if (sourcePort == null) {
                sourcePort = new Port(toConnect);
            } else {
                Port targetPort = new Port(toConnect);
                model.addConnection(sourcePort, targetPort);
                sourcePort = null;
            }
        }
    }

    private void removeListeners() {
        view.getWorkspace().removeListener(workspaceListener);
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, ActionListener> getFunctionalities() {
        buttonListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                activateTool();
                JComboBox cb = (JComboBox) e.getSource();
                selectedModule = (Module) cb.getSelectedItem();
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

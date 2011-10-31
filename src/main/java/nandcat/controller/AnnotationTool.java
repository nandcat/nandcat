package nandcat.controller;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import nandcat.model.Model;
import nandcat.model.element.Element;
import nandcat.model.element.Module;
import nandcat.view.View;
import nandcat.view.WorkspaceEvent;
import nandcat.view.WorkspaceListener;
import nandcat.view.WorkspaceListenerAdapter;

/**
 * The AnnotationTool is responsible for the naming of modules.
 */
public class AnnotationTool implements Tool {

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
    private ActionListener actionListener;

    /**
     * WorkspaceListener of the Tool.
     */
    private WorkspaceListener workspaceListener;

    /**
     * Constructs the SelectTool.
     * 
     * @param controller
     *            Controller component of the application.
     */
    public AnnotationTool(Controller controller) {
        this.controller = controller;
        this.view = controller.getView();
        this.model = controller.getModel();
    }

    /**
     * {@inheritDoc}
     */
    public void setActive(boolean active) {
        if (active) {
            setWorkspaceListener();
        } else {
            unsetWorkspaceListeners();
        }
    }

    /**
     * Gets the WorkspaceListener if existing, otherwise creates it.
     * @return WorkspaceListener used to get notified.
     */
    private WorkspaceListener getWorkspaceListener() {
        if (workspaceListener == null) {
            workspaceListener = new WorkspaceListenerAdapter() {
                @Override
                public void mouseClicked(WorkspaceEvent e) {
                    mouseClickedOnWorkspace(e.getLocation());
                }
            };
        }
        return workspaceListener;
    }

    /**
     * Adds the WorkspaceListener to the view's workspace. 
     */
    private void setWorkspaceListener() {
        view.getWorkspace().addListener(getWorkspaceListener());
    }

    /**
     * Action if the mouse clicked on workspace event occurred.
     * Gets elements at click point and opens a dialog to edit the annotation.
     * @param p Point of mouse click.
     */
    private void mouseClickedOnWorkspace(Point p) {
        Set<Element> elements = model.getElementsAt(p);
        Module toAnnotate = null;
        for (Element element : elements) {
            // annotations on modules only
            if (element instanceof Module) {
                toAnnotate = (Module) element;
            }
        }
        if (toAnnotate != null) {
            String newAnnotation = askForAnnotation(toAnnotate.getName());
            toAnnotate.setName(newAnnotation);
            view.repaint();
        }
    }

    private String askForAnnotation(String oldAnnotation) {
        return (String) JOptionPane.showInputDialog(view, "Baustein mit Text versehen:\n",
                "Customized Dialog", JOptionPane.PLAIN_MESSAGE, icon, null, oldAnnotation);
    }

    /**
     * Removes the workspace listener from the workspace.
     */
    private void unsetWorkspaceListeners() {
        view.getWorkspace().removeListener(getWorkspaceListener());
    }

    /**
     * {@inheritDoc}
     */
    public ImageIcon getIcon() {
        return icon;
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, ActionListener> getFunctionalities() {
        actionListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
            }
        };
        Map<String, ActionListener> map = new HashMap<String, ActionListener>();
        for (String functionality : represent) {
            map.put(functionality, actionListener);
        }
        return map;
    }
}

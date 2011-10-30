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
    private ActionListener buttonListener;

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
            setListeners();
        } else {
            unsetListeners();
        }
    }

    private void setListeners() {
        if (workspaceListener == null) {
            workspaceListener = new WorkspaceListener() {

                public void mouseReleased(WorkspaceEvent e) {
                    // TODO Auto-generated method stub
                }

                public void mousePressed(WorkspaceEvent e) {
                    // TODO Auto-generated method stub
                }

                public void mouseMoved(WorkspaceEvent e) {
                    // TODO Auto-generated method stub
                }

                public void mouseDragged(WorkspaceEvent e) {
                    // TODO Auto-generated method stub
                }

                public void mouseClicked(WorkspaceEvent e) {
                    mouseClickedOnWorkspace(e.getLocation());
                }
            };
        }
        view.getWorkspace().addListener(workspaceListener);
    }

    private void mouseClickedOnWorkspace(Point p) {
        assert p.getLocation() != null;
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
        return (String) JOptionPane.showInputDialog(view, "Complete the sentence:\n" + "\"Green eggs and...\"",
                "Customized Dialog", JOptionPane.PLAIN_MESSAGE, icon, null, oldAnnotation);
    }

    private void unsetListeners() {
        view.getWorkspace().removeListener(workspaceListener);
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
}

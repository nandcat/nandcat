package nandcat.controller;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import nandcat.I18N;
import nandcat.I18N.I18NBundle;
import nandcat.model.Model;
import nandcat.model.element.DrawElement;
import nandcat.model.element.Module;
import nandcat.view.View;
import nandcat.view.WorkspaceEvent;
import nandcat.view.WorkspaceListener;
import nandcat.view.WorkspaceListenerAdapter;

/**
 * The AnnotationTool is responsible for the naming of modules.
 * 
 * Using this tool gives the user the option to click on existing elements on the workspace and change their annotation
 * text which is a description for better usability. The annotation will be saved persistently in the standard export
 * format.
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
    private ImageIcon icon;

    /**
     * Translation unit.
     */
    private I18NBundle i18n = I18N.getBundle("toolannotation");

    /**
     * String representation of the Tool.
     */
    @SuppressWarnings("serial")
    private List<String> represent = new LinkedList<String>() {

        {
            add("annotate");
        }
    };

    /**
     * ActionListerner of the Tool on the Buttons.
     */
    private ActionListener actionListener;

    /**
     * WorkspaceListener of the Tool.
     */
    private WorkspaceListener workspaceListener;

    /**
     * Tolerance used if mouse clicked.
     */
    private static final Dimension MOUSE_TOLERANCE = new Dimension(2, 2);

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
            view.focuseButton("annotate");
            setWorkspaceListener();
        } else {
            unsetWorkspaceListeners();
        }
    }

    /**
     * Gets the WorkspaceListener if existing, otherwise creates it.
     * 
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
     * Action if the mouse clicked on workspace event occurred. Gets elements at click point and opens a dialog to edit
     * the annotation.
     * 
     * @param p
     *            Point of mouse click.
     */
    private void mouseClickedOnWorkspace(Point p) {
        Set<DrawElement> elements = model.getDrawElementsAt(new Rectangle(p, MOUSE_TOLERANCE));
        Module toAnnotate = null;
        for (DrawElement element : elements) {
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

    /**
     * Shows a dialog box to edit the existing annotation text of an element.
     * 
     * @param oldAnnotation
     *            Annotation before editing.
     * @return String entered as new annotation.
     */
    private String askForAnnotation(String oldAnnotation) {
        return (String) JOptionPane.showInputDialog(view, i18n.getString("dialog.annotate.text"),
                i18n.getString("dialog.annotate.title"), JOptionPane.PLAIN_MESSAGE, null, null, oldAnnotation);
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
     * Request activation of functionality.
     * 
     * @param command
     *            String representing functionality to active.
     */
    private void request(String command) {
        if (command.equals("annotate")) {
            controller.requestActivation(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, ActionListener> getFunctionalities() {
        actionListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                request(e.getActionCommand());
            }
        };
        Map<String, ActionListener> map = new HashMap<String, ActionListener>();
        for (String functionality : represent) {
            map.put(functionality, actionListener);
        }
        return map;
    }
}

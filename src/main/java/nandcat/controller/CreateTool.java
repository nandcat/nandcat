package nandcat.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.ImageIcon;
import nandcat.model.Model;
import nandcat.model.element.Element;
import nandcat.view.View;
import nandcat.view.WorkspaceEvent;
import nandcat.view.WorkspaceListener;

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
            if (workspaceListener == null) {
                workspaceListener = new WorkspaceListener() {

                    public void mouseReleased(WorkspaceEvent e) {
                        // TODO Auto-generated method stub
                    }

                    public void mousePressed(WorkspaceEvent e) {
                        // TODO Auto-generated method stub
                        Set<Element> elementsAt = model.getElementsAt(e.getLocation());
                        if (elementsAt.isEmpty()) {
                            //Ben: Module ist ein Interface!
                            //Module module = new Module();
                            //TODO find out how to know which module it is.
                            //model.addModule(module, e.getLocation());
                            //Ben: Schau das nächste mal ins irc wenn du nen build failst!
                            //Ben: mvn compile && mvn test Vor commit!
                        } else {
                        // create new connection
                        }
                    }

                    public void mouseMoved(WorkspaceEvent e) {
                        // TODO Auto-generated method stub
                    }

                    public void mouseDragged(WorkspaceEvent e) {
                        // TODO Auto-generated method stub
                    }

                    public void mouseClicked(WorkspaceEvent e) {
                        // TODO Auto-generated method stub
                    }
                };
            }
            view.getWorkspace().addListener(workspaceListener);
        } else {
            view.getWorkspace().removeListener(workspaceListener);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, ActionListener> getFunctionalities() {
        buttonListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                //Ben: So laeuft das nicht, kapseln ueber private Methode.
                //controller.requestActivation(this);
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

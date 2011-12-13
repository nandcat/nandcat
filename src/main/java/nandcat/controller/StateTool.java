package nandcat.controller;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
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
import nandcat.model.element.ImpulseGenerator;
import nandcat.model.element.Module;
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
    private ImageIcon icon;

    /**
     * Translation unit.
     */
    private I18NBundle i18n = I18N.getBundle("toolstate");

    /**
     * String representation of the Tool.
     */
    @SuppressWarnings("serial")
    private List<String> represent = new LinkedList<String>() {

        {
            add("toggle");
        }
    };

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
            view.focuseButton("toggle");
            setListeners();
        } else {
            removeListeners();
        }
    }

    /**
     * Set the Listeners on the Workspace and if Listener is Null creates new one.
     */
    private void setListeners() {
        if (workspaceListener == null) {
            workspaceListener = new WorkspaceListenerAdapter() {

                @Override
                public void mouseClicked(WorkspaceEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        changeState(e.getLocation());
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        popTextField(e.getLocation());
                    }
                }
            };
        }
        view.getWorkspace().addListener(workspaceListener);
    }

    /**
     * Removes Listener on the Workspace.
     */
    private void removeListeners() {
        view.getWorkspace().removeListener(workspaceListener);
    }

    /**
     * Changes the state of the Element under the Point if it is a ImpulseGenerator.
     * 
     * @param point
     *            the Point where it was clicked.
     */
    private void changeState(Point point) {
        assert point != null;
        Set<DrawElement> elementsAt = model.getDrawElementsAt(new Rectangle(point, new Dimension(1, 1)));
        Module toChangeState = null;
        for (DrawElement element : elementsAt) {
            if (element instanceof Module) {
                toChangeState = (Module) element;
            }
        }
        if (toChangeState != null) {
            model.toggleModule(toChangeState);
        }
    }

    /**
     * If clicked on a ImpulseGenerator this pops up the TextField, waiting for the new Frequenz set.
     * 
     * @param point
     *            Point where it was clicked, under this the IG must be.
     */
    private void popTextField(Point point) {
        assert point != null;
        Set<DrawElement> elementsAt = model.getDrawElementsAt(new Rectangle(point, new Dimension(1, 1)));
        for (DrawElement element : elementsAt) {
            if (element instanceof ImpulseGenerator) {
                ImpulseGenerator ig = (ImpulseGenerator) element;
                String frequenzy = askForFrequenz(ig.getFrequency());
                if (frequenzy != null) {
                    boolean worked = false;
                    int freq = -1;
                    try {
                        freq = Integer.parseInt(frequenzy);
                        if (freq >= 0) {
                            worked = true;
                        } else {
                            worked = false;
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    worked = model.setFrequency(ig, freq);
                    if (!worked) {
                        JOptionPane.showMessageDialog(view, i18n.getString("dialog.state.freqerrormsg"),
                                i18n.getString("dialog.state.freqerrortitle"), JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    /**
     * This Method returns the TextField which ask for the new Frequency.
     * 
     * @param frequency
     *            old Frequency shown in the TextField.
     * @return String with new Frequency.
     */
    private String askForFrequenz(int frequency) {
        return (String) JOptionPane.showInputDialog(view, i18n.getString("dialog.state.text"),
                i18n.getString("dialog.state.title"), JOptionPane.PLAIN_MESSAGE, null, null, frequency);
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, ActionListener> getFunctionalities() {
        buttonListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                activateTool();
            }
        };
        Map<String, ActionListener> map = new HashMap<String, ActionListener>();
        for (String functionality : represent) {
            map.put(functionality, buttonListener);
        }
        return map;
    }

    /**
     * Requests activation for this tool by the Controller.
     */
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

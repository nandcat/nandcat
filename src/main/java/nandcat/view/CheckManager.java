package nandcat.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Set;
import javax.help.CSH;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import nandcat.I18N;
import nandcat.I18N.I18NBundle;
import nandcat.model.check.CheckEvent;
import nandcat.model.check.CheckEvent.State;
import nandcat.model.check.CheckListener;
import nandcat.model.check.CircuitCheck;
import nandcat.model.check.CountCheck;
import nandcat.model.check.FeedbackCheck;
import nandcat.model.check.IllegalConnectionCheck;
import nandcat.model.check.MultipleConnectionsCheck;
import nandcat.model.check.OrphanCheck;
import nandcat.model.check.SinkCheck;
import nandcat.model.check.SourceCheck;

/**
 * CheckManager is responsible for selecting and deselecting Checks and showing their states.
 */
public class CheckManager extends JDialog {

    /**
     * Default Location on Screen of the Frame.
     */
    private static final Point FRAME_LOCATION = new Point(800, 10);

    /**
     * Default Size of the Buttons.
     */
    private static final Dimension BUTTON_SIZE = new Dimension(200, 30);

    /**
     * Default serial version uid.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Dimension representing the Size of the Frame.
     */
    private static final Dimension FRAME_SIZE = new Dimension(680, 350);

    /**
     * Icon representing a check has not started yet.
     */
    private ImageIcon checkPending = new ImageIcon("src/resources/help.jpg");;

    /**
     * Icon representing a check has started but did not finish yet.
     */
    private ImageIcon checkStarted = new ImageIcon("src/resources/exclamation_mark.png");

    /**
     * Icon representing a check passed successful.
     */
    private ImageIcon checkSuccessful = new ImageIcon("src/resources/check-icon.gif");;

    /**
     * Icon representing a check failed.
     */
    private ImageIcon checkFailed = new ImageIcon("src/resources/cross_icon1.gif");;

    /**
     * Location of upper left corner of the frame on the screen.
     */
    private Point frameLocation = FRAME_LOCATION;

    /**
     * JPanel on which the CheckBoxes with its CheckBoxMenuItem are placed.
     */
    private JPanel panel;

    /**
     * Translation unit.
     */
    private I18NBundle i18n = I18N.getBundle("model");

    /**
     * Listener on the Buttons.
     */
    private ActionListener buttonListener;

    /**
     * Dimension of the Buttons.
     */
    private Dimension buttonDim = BUTTON_SIZE;

    /**
     * Button to start the checks.
     */
    private JButton calc;

    /**
     * Button to start the checks and then the Simulation.
     */
    private JButton calcStart;

    /**
     * Constructs the CheckManager.
     * 
     * @param set
     *            List with all checks to be performed.
     * @param buttonListener
     *            ActionListener from the ControllerTool for the Buttons.
     */
    public CheckManager(Set<CircuitCheck> set, ActionListener buttonListener) {
        CSH.setHelpIDString(this, "checkmanager");
        this.buttonListener = buttonListener;
        setupCheckmanager(set);
        CheckListener checkListener = new CheckListener() {

            public void checkStarted() {
                // changeSymbol(checkStarted);
            }

            public void checkChanged(CheckEvent e) {
                String checkName = getNameForCheck(e.getSource());
                if (e.getState().equals(State.RUNNING)) {
                    changeSymbol(checkStarted, checkName);
                } else if (e.getState().equals(State.SUCCEEDED)) {
                    changeSymbol(checkSuccessful, checkName);
                } else if (e.getState().equals(State.FAILED)) {
                    changeSymbol(checkFailed, checkName);
                }
            }
        };
        for (CircuitCheck c : set) {
            c.addListener(checkListener);
        }
    }

    /**
     * Sets the String representation for a check.
     * 
     * @param check
     *            CircuitCheck getting a String.
     * @return String representation for the check, indicating its functionality.
     */
    private String getNameForCheck(CircuitCheck check) {
        String name = null;
        if (check instanceof CountCheck) {
            name = i18n.getString("check.count.description");
        } else if (check instanceof IllegalConnectionCheck) {
            name = i18n.getString("check.illegalconnection.description");
        } else if (check instanceof MultipleConnectionsCheck) {
            name = i18n.getString("check.multipleconnections.description");
        } else if (check instanceof OrphanCheck) {
            name = i18n.getString("check.orphan.description");
        } else if (check instanceof SinkCheck) {
            name = i18n.getString("check.sink.description");
        } else if (check instanceof SourceCheck) {
            name = i18n.getString("check.source.description");
        } else if (check instanceof FeedbackCheck) {
            name = i18n.getString("check.feedback.description");
        }
        return name;
    }

    /**
     * Set the Frame visible or not.
     * 
     * @param visible
     *            boolean represents if visible or not.
     */
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        panel.setVisible(visible);
        // When the CheckManager is re-opened all states are set to pending.
    }

    /**
     * Sets up CheckManager elements.
     * 
     * @param checks
     *            List with Checks to be listed in the Frame.
     */
    private void setupCheckmanager(final Set<CircuitCheck> checks) {
        setSize(FRAME_SIZE);
        setLocation(frameLocation);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new BorderLayout());
        setFocusable(false);
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.white);
        panel.setFocusable(false);
        JScrollPane scroller = new JScrollPane(panel);
        // The listener waits for changes on the checkbox.
        ItemListener itemListener = new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                JCheckBoxMenuItem box = (JCheckBoxMenuItem) e.getSource();
                for (CircuitCheck check : checks) {
                    setCheckActive(check, box.getActionCommand());
                }
            }
        };
        JCheckBox checkbox = null;
        JCheckBoxMenuItem checkboxItem = null;
        for (CircuitCheck check : checks) {
            checkbox = new JCheckBox();
            checkbox.setFocusable(false);
            // By default all checks will be executed.
            checkboxItem = setMenuItem(check, checkboxItem);
            checkboxItem.setFocusable(false);
            checkboxItem.addItemListener(itemListener);
            checkbox.add(checkboxItem);
            panel.add(checkbox);
        }
        CSH.setHelpIDString(panel, "checkmanager");
        CSH.setHelpIDString(checkboxItem, "checkmanager");
        CSH.setHelpIDString(checkbox, "checkmanager");
        JButton okayButton = new JButton(i18n.getString("check.dialog.ok"));
        okayButton.setActionCommand(i18n.getString("check.button.okay"));
        okayButton.setPreferredSize(buttonDim);
        okayButton.setFocusable(false);
        calc = new JButton(i18n.getString("check.dialog.refresh"));
        calc.setActionCommand(i18n.getString("check.button.refresh"));
        calc.setPreferredSize(buttonDim);
        calc.setFocusable(false);
        calcStart = new JButton(i18n.getString("check.dialog.checkStart"));
        calcStart.setActionCommand(i18n.getString("check.button.checkStart"));
        calcStart.setPreferredSize(buttonDim);
        calcStart.setFocusable(false);
        okayButton.addActionListener(buttonListener);
        calc.addActionListener(buttonListener);
        calcStart.addActionListener(buttonListener);
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new FlowLayout());
        toolbar.setBackground(Color.white);
        toolbar.add(okayButton);
        toolbar.add(calc);
        toolbar.add(calcStart);
        this.add(scroller, BorderLayout.CENTER);
        this.add(toolbar, BorderLayout.PAGE_END);
    }

    /**
     * Sets the check active if the String representation of the check equals the input String.
     * 
     * @param check
     *            CircuitCheck to be set to active.
     * @param match
     *            String matched against the String representation of the check.
     */
    private void setCheckActive(CircuitCheck check, String match) {
        if ((check instanceof CountCheck) && match.equals(i18n.getString("check.count.description"))) {
            check.setActive(!check.isActive());
            changeSymbol(checkPending, i18n.getString("check.count.description"));
        } else if ((check instanceof IllegalConnectionCheck)
                && match.equals(i18n.getString("check.illegalconnection.description"))) {
            check.setActive(!check.isActive());
            changeSymbol(checkPending, i18n.getString("check.illegalconnection.description"));
        } else if ((check instanceof MultipleConnectionsCheck)
                && match.equals(i18n.getString("check.multipleconnections.description"))) {
            check.setActive(!check.isActive());
            changeSymbol(checkPending, i18n.getString("check.multipleconnections.description"));
        } else if ((check instanceof OrphanCheck) && match.equals(i18n.getString("check.orphan.description"))) {
            check.setActive(!check.isActive());
            changeSymbol(checkPending, i18n.getString("check.orphan.description"));
        } else if ((check instanceof SinkCheck) && match.equals(i18n.getString("check.sink.description"))) {
            check.setActive(!check.isActive());
            changeSymbol(checkPending, i18n.getString("check.sink.description"));
        } else if ((check instanceof SourceCheck) && match.equals(i18n.getString("check.source.description"))) {
            check.setActive(!check.isActive());
            changeSymbol(checkPending, i18n.getString("check.source.description"));
        } else if ((check instanceof FeedbackCheck) && match.equals(i18n.getString("check.feedback.description"))) {
            check.setActive(!check.isActive());
            changeSymbol(checkPending, i18n.getString("check.feedback.description"));
        }
    }

    /**
     * Set the MenuItem with a String representation of the check.
     * 
     * @param check
     *            CircuitCheck which will be represented in the checkbox menu
     * @param checkboxItem
     *            JCheckBoxMenuItem to be set.
     * @return JCheckMenuItem containing the String representation and an icon displaying the state of the check.
     */
    private JCheckBoxMenuItem setMenuItem(CircuitCheck check, JCheckBoxMenuItem checkboxItem) {
        if (check instanceof CountCheck) {
            checkboxItem = new JCheckBoxMenuItem(i18n.getString("check.count.description"), checkPending, true);
        } else if (check instanceof IllegalConnectionCheck) {
            checkboxItem = new JCheckBoxMenuItem(i18n.getString("check.illegalconnection.description"), checkPending,
                    true);
        } else if (check instanceof MultipleConnectionsCheck) {
            checkboxItem = new JCheckBoxMenuItem(i18n.getString("check.multipleconnections.description"), checkPending,
                    true);
        } else if (check instanceof OrphanCheck) {
            checkboxItem = new JCheckBoxMenuItem(i18n.getString("check.orphan.description"), checkPending, true);
        } else if (check instanceof SinkCheck) {
            checkboxItem = new JCheckBoxMenuItem(i18n.getString("check.sink.description"), checkPending, true);
        } else if (check instanceof SourceCheck) {
            checkboxItem = new JCheckBoxMenuItem(i18n.getString("check.source.description"), checkPending, true);
        } else if (check instanceof FeedbackCheck) {
            checkboxItem = new JCheckBoxMenuItem(i18n.getString("check.feedback.description"), checkPending, true);
        }
        return checkboxItem;
    }

    /**
     * Changes the symbol representing the state of the Check.
     * 
     * @param icon
     *            ImageIcon the new icon.
     * @param check
     *            String Name of the Check which icon will be changed.
     */
    private void changeSymbol(ImageIcon icon, String check) {
        // First get the CheckBox from the panel.
        for (Component checkbox : panel.getComponents()) {
            if (checkbox instanceof JCheckBox) {
                // Then get the MenuItem and change its icon.
                for (Component menuitem : ((JCheckBox) checkbox).getComponents()) {
                    if (menuitem instanceof JCheckBoxMenuItem) {
                        if (((JCheckBoxMenuItem) menuitem).getActionCommand().equals(check)) {
                            ((JCheckBoxMenuItem) menuitem).setIcon(icon);
                            super.repaint();
                        }
                    }
                }
            }
        }
    }

    /**
     * Resets the Icons in the CheckManger sets all Icons on Pending.
     */
    public void resetList() {
        for (Component checkbox : panel.getComponents()) {
            if (checkbox instanceof JCheckBox) {
                // Then get the MenuItem and change its icon.
                for (Component menuitem : ((JCheckBox) checkbox).getComponents()) {
                    if (menuitem instanceof JCheckBoxMenuItem) {
                        ((JCheckBoxMenuItem) menuitem).setIcon(checkPending);
                    }
                }
            }
        }
    }

    /**
     * Enables or Disables the calc Button.
     * 
     * @param state
     *            boolean the value to be set.
     */
    public void setButton(boolean state) {
        calc.setEnabled(state);
        calcStart.setEnabled(state);
    }
}

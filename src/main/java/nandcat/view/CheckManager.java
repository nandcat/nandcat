package nandcat.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Set;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JPanel;
import nandcat.model.check.CheckEvent;
import nandcat.model.check.CheckEvent.State;
import nandcat.model.check.CheckListener;
import nandcat.model.check.CircuitCheck;

/**
 * CheckManager is responsible for selecting and deselecting Checks and showing their states.
 */
public class CheckManager extends JFrame {

    /**
     * Default serial version uid.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Icon representing a check has not started yet.
     */
    private ImageIcon checkPending = new ImageIcon("src/resources/Questionmark.png");;

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
    private Point frameLocation = new Point(300, 250);

    /**
     * JPanel on which the CheckBoxes with its CheckBoxMenuItem are placed.
     */
    private JPanel panel;

    /**
     * Constructs the CheckManager.
     * 
     * @param set
     *            List with all checks to be performed.
     */
    public CheckManager(Set<CircuitCheck> set) {
        setupCheckmanager(set);
        CheckListener checkListener = new CheckListener() {

            public void checkStarted() {
                // changeSymbol(checkStarted);
            }

            public void checkChanged(CheckEvent e) {
                if (e.getState().equals(State.RUNNING)) {
                    changeSymbol(checkStarted, e.getSource());
                } else if (e.getState().equals(State.SUCCEEDED)) {
                    changeSymbol(checkSuccessful, e.getSource());
                } else if (e.getState().equals(State.FAILED)) {
                    changeSymbol(checkFailed, e.getSource());
                }
            }
        };
        for (CircuitCheck c : set) {
            c.addListener(checkListener);
        }
    }

    /**
     * Set the Frame visible or not.
     * 
     * @param visible
     *            boolean represents if visible or not.
     */
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            super.setExtendedState(JFrame.NORMAL);
        }
        // When the CheckManager is re-opened all states are set to pending.
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
     * Sets up CheckManager elements.
     * 
     * @param checks
     *            List with Checks to be listed in the Frame.
     */
    private void setupCheckmanager(final Set<CircuitCheck> checks) {
        setSize(620, 300);
        setLocation(frameLocation);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.white);
        // The listener waits for changes on the checkbox.
        ItemListener itemListener = new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                JCheckBoxMenuItem box = (JCheckBoxMenuItem) e.getSource();
                for (CircuitCheck check : checks) {
                    if (box.getActionCommand().equals(check.toString())) {
                        check.setActive(!check.isActive());
                    }
                }
            }
        };
        JCheckBox checkbox = null;
        JCheckBoxMenuItem checkboxItem = null;
        for (CircuitCheck check : checks) {
            checkbox = new JCheckBox();
            // By default all checks will be executed.
            checkboxItem = new JCheckBoxMenuItem(check.toString(), checkPending, true);
            checkboxItem.addItemListener(itemListener);
            checkbox.add(checkboxItem);
            panel.add(checkbox);
        }
        this.add(panel);
    }

    /**
     * Changes the symbol representing the state of the Check.
     * 
     * @param icon
     *            ImageIcon the new icon.
     */
    private void changeSymbol(ImageIcon icon, CircuitCheck check) {
        // First get the CheckBox from the panel.
        for (Component checkbox : panel.getComponents()) {
            if (checkbox instanceof JCheckBox) {
                // Then get the MenuItem and change its icon.
                for (Component menuitem : ((JCheckBox) checkbox).getComponents()) {
                    if (menuitem instanceof JCheckBoxMenuItem) {
                        if (((JCheckBoxMenuItem) menuitem).getActionCommand().equals(check.toString())) {
                            ((JCheckBoxMenuItem) menuitem).setIcon(icon);
                            super.repaint();
                        }
                    }
                }
            }
        }
    }
}

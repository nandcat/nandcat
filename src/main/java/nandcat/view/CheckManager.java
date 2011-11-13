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
import javax.swing.plaf.basic.BasicComboBoxUI.ItemHandler;
import nandcat.model.check.CheckEvent;
import nandcat.model.check.CheckEvent.State;
import nandcat.model.check.CheckListener;
import nandcat.model.check.CircuitCheck;

/**
 * CheckManager is responsible for selecting and deselecting Checks and showing their states.
 */
public class CheckManager extends JFrame {

    /**
     * frame of the CheckManager.
     */
    // private JFrame frame = new JFrame("CheckManager");

    /**
     * Default serial version uid.
     */
    private static final long serialVersionUID = 1L;

    /**
     * CheckListener of the CheckManager, listening on the Checks.
     */
    // private CheckListener checkListener;

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
     * Reference on this CheckManager.
     */
    // private CheckManager checkManager;

    /**
     * Location of upper left corner of the frame on the screen.
     */
    private Point frameLocation = new Point(300, 250);

    private ImageIcon icon;

    private JPanel panel;

    /**
     * Constructs the CheckManager.
     * 
     * @param set
     *            List with all checks to be performed.
     * @param boxListener
     *            ItemHandler, Listener for a CheckBox.
     */
    public CheckManager(Set<CircuitCheck> set, ItemHandler boxListener) {
        setupCheckmanager(set, boxListener);
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
        // checkManager = this;
    }

    /**
     * Set the Frame visible or not.
     * 
     * @param visible
     *            boolean represents if visible or not.
     */
    public void setVisible(boolean visible) {
        // checkManager.setVisible(visible);
        super.setVisible(visible);
    }

    /**
     * Sets up CheckManager elements.
     * 
     * @param checks
     *            List with Checks to be listed in the Frame.
     * @param boxListener
     *            Listener for the CheckBoxes
     */
    private void setupCheckmanager(final Set<CircuitCheck> checks, ItemHandler boxListener) {
        // frame.setSize(600, 400);
        // frame.setLocation(frameLocation);
        // frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
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
        icon = checkPending;
        for (CircuitCheck check : checks) {
            // JCheckBoxMenuItem item = new JCheckBoxMenuItem(check.toString(), icon);
            // item.addItemListener(boxListener);
            // checkbox.add(item);
            checkbox = new JCheckBox();

            // By default all checks will be executed.
            checkboxItem = new JCheckBoxMenuItem(check.toString(), icon, true);

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
        for (Component comps : panel.getComponents()) {
            if (comps instanceof JCheckBox) {

                // Then get the MenuItem and change its icon.
                for (Component comp : ((JCheckBox) comps).getComponents()) {
                    if (comp instanceof JCheckBoxMenuItem) {
                        if (((JCheckBoxMenuItem) comp).getActionCommand().equals(check.toString())) {
                            ((JCheckBoxMenuItem) comp).setIcon(icon);
                            super.repaint();
                        }
                    }
                }
            }
        }
    }
}

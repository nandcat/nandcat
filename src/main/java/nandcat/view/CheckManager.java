package nandcat.view;

import java.awt.Point;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.plaf.basic.BasicComboBoxUI.ItemHandler;
import nandcat.model.check.CheckEvent;
import nandcat.model.check.CheckListener;
import nandcat.model.check.CircuitCheck;

/**
 * CheckManager is responsible for selecting and deselecting Checks and showing their states.
 */
public class CheckManager extends JFrame {

    /**
     * frame of the CheckManager.
     */
    private JFrame frame = new JFrame("CheckManager");

    /**
     * Default serial version uid.
     */
    private static final long serialVersionUID = 1L;

    /**
     * CheckListener of the CheckManager, listening on the Checks.
     */
    private CheckListener checkListener;

    /**
     * Icon representing a check has not started yet.
     */
    private ImageIcon checkPending;

    /**
     * Icon representing a check has started but did not finish yet.
     */
    private ImageIcon checkStarted;

    /**
     * Icon representing a check passed successful.
     */
    private ImageIcon checkSuccessful;

    /**
     * Icon representing a check failed.
     */
    private ImageIcon checkFailed;

    /**
     * Reference on this CheckManager.
     */
    private CheckManager checkManager;

    /**
     * Location of upper left corner of the frame on the screen.
     */
    private Point frameLocation = new Point(300, 250);

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
        checkListener = new CheckListener() {

            public void checkStarted() {
                changeSymbol(checkStarted);
            }

            public void checkChanged(CheckEvent e) {
                // TODO Auto-generated method stub
            }
        };
        for (CircuitCheck c : set) {
            c.addListener(checkListener);
        }
        checkManager = this;
    }

    /**
     * Set the Frame visible or not.
     * 
     * @param visible
     *            boolean represents if visible or not.
     */
    public void setVisible(boolean visible) {
        checkManager.setVisible(visible);
    }

    /**
     * Sets up CheckManager elements.
     * 
     * @param set
     *            List with Checks to be listed in the Frame.
     * @param boxListener
     *            Listener for the CheckBoxes
     */
    private void setupCheckmanager(Set<CircuitCheck> checks, ItemHandler boxListener) {
        frame.setSize(600, 400);
        frame.setLocation(frameLocation);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        JCheckBox checkbox = new JCheckBox();
        for(CircuitCheck check : checks) {
            JCheckBoxMenuItem item = new JCheckBoxMenuItem(check.toString(), checkPending);
            item.addItemListener(boxListener);
            checkbox.add(item);
        }
    }

    /**
     * Changes the symbol representing the state of the Check.
     * 
     * @param icon
     *            ImageIcon the new icon.
     */
    private void changeSymbol(ImageIcon icon) {
    }
}

package nandcat.view;

import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.plaf.basic.BasicComboBoxUI.ItemHandler;

import nandcat.model.check.CheckEvent;
import nandcat.model.check.CheckListener;
import nandcat.model.check.CircuitCheck;

/**
 * CheckManager is responsible for selecting and deselecting Checks and showing their stats.
 * 
 * @version 0.1
 * 
 */
public class CheckManager extends JFrame {

    /**
     * Default serial version uid.
     */
    private static final long serialVersionUID = 1L;

    /**
     * CheckListener of the Checkmanager, listening on the Checks.
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
     * Constructs the CheckManager.
     * 
     * @param set
     *            List with all checks to be performed.
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
    }

    /**
     * Set the Frame visible or not.
     */
    public void setVisible(boolean visible) {
        // TODO implement
    }

    /**
     * Sets up CheckManager elements.
     * 
     * @param set
     *            List with Checks to be listed in the Frame.
     * @param boxListener 
     */
    private void setupCheckmanager(Set<CircuitCheck> set, ItemHandler boxListener) {
        // TODO Auto-generated method stub
    }

    private void changeSymbol(ImageIcon icon) {
    }
}
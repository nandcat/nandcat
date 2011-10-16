package de.unipassau.sep.nandcat.view;

import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import de.unipassau.sep.nandcat.model.check.CheckEvent;
import de.unipassau.sep.nandcat.model.check.CheckListener;
import de.unipassau.sep.nandcat.model.check.CircuitCheck;

/**
 * Checkmanager.
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
     * Icon representing a check passed succsessful.
     */
    private ImageIcon checkSuccsessful;

    /**
     * Icon representing a check failed.
     */
    private ImageIcon checkFailed;

    /**
     * Constructs the CheckManager.
     * 
     * @param checksToDo
     *            List with all checks to be performed.
     */
    public CheckManager(List<CircuitCheck> checksToDo) {
        setupCheckmanager(checksToDo);
        checkListener = new CheckListener() {

            @Override
            public void checkStarted() {
                changeSymbol(checkStarted);
            }

            @Override
            public void checkChanged(CheckEvent e) {
                // TODO Auto-generated method stub
            }
        };
        for (CircuitCheck c : checksToDo) {
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
     * @param checksToDo
     *            List with Checks to be listed in the Frame.
     */
    private void setupCheckmanager(List<CircuitCheck> checksToDo) {
        // TODO Auto-generated method stub
    }

    private void changeSymbol(ImageIcon icon) {
    }
}

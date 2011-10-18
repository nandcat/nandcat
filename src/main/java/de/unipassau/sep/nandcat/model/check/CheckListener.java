package de.unipassau.sep.nandcat.model.check;

/**
 * Checklistener Interface, used for notification.
 * 
 * It notifies implementing classes about state changes of the check registered on.
 * @version 0.4
 */
public interface CheckListener {

    /**
     * Called if check started.
     */
    // TODO Wirklich benoetigt, ich würde noch checkChanged und einen State Started benutzen - Ben
    void checkStarted();

    /**
     * Called if check changed its state.
     * 
     * @param e
     *            CheckEvent used to get checks state and additional information about the check.
     */
    void checkChanged(CheckEvent e);
}

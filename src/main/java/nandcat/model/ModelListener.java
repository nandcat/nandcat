package nandcat.model;

/**
 * Modellistener.
 * 
 * @version 0.1
 * 
 */
public interface ModelListener {

    /**
     * Invoked when elements changes.
     * 
     * @param e
     *            ModelEvent which holds changes.
     */
    void elementsChanged(ModelEvent e);

    /**
     * Invoked when checks changes.
     * 
     * @param e
     *            ModelEvent which holds changes.
     */
    void checksChanged(ModelEvent e);

    /**
     * Invoked when checks changes.
     * 
     * @param e
     *            ModelEvent which holds changes.
     */
    void simulationChanged(ModelEvent e);
}

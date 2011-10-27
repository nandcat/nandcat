package nandcat.model;

/**
 * Classes that implement this interface can be notified if the model changed in certain ways (Checks, Elements,
 * Simulation).
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
     * Invoked when checks were started.
     * 
     * @param e
     *            ModelEvent which holds changes.
     */
    void checksStarted(ModelEvent e);

    /**
     * Invoked when checks were stopped.
     * 
     * @param e
     *            ModelEvent which holds changes.
     */
    void checksStopped(ModelEvent e);

    /**
     * Invoked when simulation was started.
     * 
     * @param e
     *            ModelEvent which holds changes.
     */
    void simulationStarted(ModelEvent e);

    /**
     * Invoked when simulation was stopped.
     * 
     * @param e
     *            ModelEvent which holds changes.
     */
    void simulationStopped(ModelEvent e);

    /**
     * Invoked when import succeeded.
     * 
     * @param e
     *            ModelEvent which holds changes.
     */
    void importSucceeded(ModelEvent e);

    /**
     * Invoked when import failed.
     * 
     * @param e
     *            ModelEvent which holds changes.
     */
    void importFailed(ModelEvent e);

    /**
     * Invoked when export succeeded.
     * 
     * @param e
     *            ModelEvent which holds changes.
     */
    void exportSucceeded(ModelEvent e);

    /**
     * Invoked when export failed.
     * 
     * @param e
     *            ModelEvent which holds changes.
     */
    void exportFailed(ModelEvent e);
}

package nandcat.model;

/**
 * Adapter class for ModelListener. Implements all methods with empty bodys.
 * 
 * @see ModelListener
 */
public class ModelListenerAdapter implements ModelListener {

    /**
     * {@inheritDoc}
     */
    public void elementsChanged(ModelEvent e) {
    }

    /**
     * {@inheritDoc}
     */
    public void checksStarted(ModelEvent e) {
    }

    /**
     * {@inheritDoc}
     */
    public void checksStopped(ModelEvent e) {
    }

    /**
     * {@inheritDoc}
     */
    public void simulationStarted(ModelEvent e) {
    }

    /**
     * {@inheritDoc}
     */
    public void simulationStopped(ModelEvent e) {
    }

    /**
     * {@inheritDoc}
     */
    public void importSucceeded(ModelEvent e) {
    }

    /**
     * {@inheritDoc}
     */
    public boolean changeCircuitRequested(ModelEvent e) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public void importFailed(ModelEvent e) {
    }

    /**
     * {@inheritDoc}
     */
    public void exportSucceeded(ModelEvent e) {
    }

    /**
     * {@inheritDoc}
     */
    public void exportFailed(ModelEvent e) {
    }
}

package nandcat.model.element;

/**
 * read-only Connection interface. Representing a connection between two Modules. Propagates the signal from the first
 * Module to the second. Is responsible for registering the next Module on the clock.
 */
public interface DrawConnection extends DrawElement {

    /**
     * Return connection's name.
     * 
     * @return String representing connection's name
     */
    public String getName();

    /**
     * Return connection's state.
     * 
     * @return the state
     */
    public boolean getState();

    /**
     * Return the next module (the Connection is attached to). Next: going from one element's outPort to the other
     * element's inPort
     * 
     * @return Module whose inPort is attached to this connection
     */
    public Module getNextModule();

    /**
     * Return the previous module (the Connection is attached to). Previous: going from one element's inPort to the
     * other element's outPort
     * 
     * @return Module whose outPort is attached to this connection
     */
    public Module getPreviousModule();

    /**
     * Getter for inPort.
     * 
     * @return Port inPort
     */
    public Port getInPort();

    /**
     * Getter for outPort.
     * 
     * @return Port outPort
     */
    public Port getOutPort();

    /**
     * {@inheritDoc}
     */
    public boolean isSelected();
}

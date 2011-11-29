package nandcat.model.element;

import nandcat.model.Clock;

/**
 * Identity gate implementation. The output equals the input.
 */
public class IdentityGate extends Gate {

    /**
     * Default serial version uid.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Advanced constructor. Creates new Identity gate with inPorts incoming and outPorts outgoing Ports.
     * 
     * @param outPorts
     *            int number of outPorts to append
     */
    protected IdentityGate(int outPorts) {
        super(1, outPorts);
    }

    /**
     * {@inheritDoc}
     */
    protected void calculate(Clock clock) {
        Port port = getInPorts().iterator().next();
        for (Port p : getOutPorts()) {
            p.setState(port.getState(), clock);
        }
    }

    /**
     * {@inheritDoc}
     */
    protected boolean isValidOutBoundary(int outPorts) {
        return (outPorts >= 1);
    }

    /**
     * {@inheritDoc}
     */
    protected boolean isValidInBoundary(int inPorts) {
        return (inPorts == 1);
    }
}

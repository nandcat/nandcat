package nandcat.model.element;

import nandcat.model.Clock;

/**
 * Identity gate implementation.
 * 
 * @version 0.5
 * 
 */
public class IdentityGate extends Gate {

    /**
     * Default constructor. Create new identity gate with 1 incoming and 2 outcoming ports.
     */
    public IdentityGate() {
        super(1, 2);
    }

    /**
     * Advanced constructor. Creates new Identity gate with inPorts incoming and outPorts outgoing Ports.
     * 
     * @param inPorts
     *            int number of inPorts to append
     * @param outPorts
     *            int number of outPorts to append
     */
    public IdentityGate(int inPorts, int outPorts) {
        super(inPorts, outPorts);
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
    protected boolean outBoundaries(int outPorts) {
        return (outPorts == 1);
    }

    /**
     * {@inheritDoc}
     */
    protected boolean inBoundaries(int inPorts) {
        return (inPorts >= 1);
    }
}

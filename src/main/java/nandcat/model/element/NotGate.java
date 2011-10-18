package nandcat.model.element;

import nandcat.model.Clock;

/**
 * NOT gate implementation. It represents the boolean NOT-operator.
 * 
 * @version 0.5
 * 
 */
public class NotGate extends Gate {

    /**
     * Default constructor. Create new not gate with 1 incoming and 1 outgoing ports.
     */
    public NotGate() {
        super(1, 1);
    }

    /**
     * Advanced constructor. Creates new Not with 1 incoming and outPorts outgoing Ports. outPorts has to be a positive
     * integer
     * 
     * @param outPorts
     *            int number of outPorts to append
     */
    public NotGate(int outPorts) {
        super(1, outPorts);
    }

    /**
     * {@inheritDoc}
     */
    protected void calculate(Clock clock) {
        Port inPort = getInPorts().iterator().next();
        for (Port p : getOutPorts()) {
            p.setState(!inPort.getState(), clock);
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
        return (inPorts == 1);
    }
}

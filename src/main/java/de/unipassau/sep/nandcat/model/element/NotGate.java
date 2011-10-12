package de.unipassau.sep.nandcat.model.element;

import de.unipassau.sep.nandcat.model.Clock;

/**
 * Not Gate.
 * 
 * @version 0.5
 * 
 */
public class NotGate extends Gate {

    /**
     * Default constructor. Create new identity gate with 1 incoming and 2 outcoming ports.
     */
    public NotGate() {
        super(1, 1);
    }

    /**
     * Advanced constructor. Creates new Not with inPorts incoming and outPorts outgoing Ports.
     * 
     * @param inPorts
     *            int number of inPorts to append
     * @param outPorts
     *            int number of outPorts to append
     */
    public NotGate(int inPorts, int outPorts) {
        // if() not allowed as super has to be the first statement
        super(inPorts != 1 ? -1 : 1, outPorts != 1 ? -1 : 1);
    }

    /**
     * {@inheritDoc}
     */
    protected void calculate(Clock clock) {
        if (getInPorts().size() != 1) {
            throw new IllegalStateException("Illegal number of inPorts");
        }
        // TODO double check
        Port port = getInPorts().iterator().next();
        for (Port p : getOutPorts()) {
            p.setState(!port.getState(), clock);
        }
    }
}

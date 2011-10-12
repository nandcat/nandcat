package de.unipassau.sep.nandcat.model.element;

import de.unipassau.sep.nandcat.model.Clock;

/**
 * Identity Gate.
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
     * Advanced constructor. Creates new Identity with inPorts incoming and outPorts outgoing Ports.
     * 
     * @param inPorts
     *            int number of inPorts to append
     * @param outPorts
     *            int number of outPorts to append
     */
    public IdentityGate(int inPorts, int outPorts) {
        // if() not allowed as super has to be the first statement
        super(inPorts != 1 ? -1 : 1, outPorts);
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
            p.setState(port.getState(), clock);
        }
    }
}

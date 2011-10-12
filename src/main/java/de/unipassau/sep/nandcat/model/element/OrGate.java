package de.unipassau.sep.nandcat.model.element;

import de.unipassau.sep.nandcat.model.Clock;

/**
 * Or Gate.
 * 
 * @version 0.5
 * 
 */
public class OrGate extends Gate {

    /**
     * Default constructor. Create new identity gate with 1 incoming and 2 outcoming ports.
     */
    public OrGate() {
        super(2, 1);
    }

    /**
     * Advanced constructor. Creates new Or with inPorts incoming and outPorts outgoing Ports.
     * 
     * @param inPorts
     *            int number of inPorts to append
     * @param outPorts
     *            int number of outPorts to append
     */
    public OrGate(int inPorts, int outPorts) {
        // if() not allowed as super has to be the first statement
        super(inPorts, outPorts);
    }

    /**
     * {@inheritDoc}
     */
    protected void calculate(Clock clock) {
        boolean result = false;
        for (Port p : getInPorts()) {
            if (p.getState()) {
                result = true;
                break;
            }
        }
        for (Port p : getOutPorts()) {
            p.setState(result, clock);
        }
    }
}

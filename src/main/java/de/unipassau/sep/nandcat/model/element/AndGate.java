package de.unipassau.sep.nandcat.model.element;

import java.util.LinkedHashSet;
import de.unipassau.sep.nandcat.model.Clock;

/**
 * And Gate.
 * 
 * @version 0.5
 * 
 */
public class AndGate extends Gate {

    /**
     * Default constructor. Creates new AND Gate with 2 incoming and 1 outgoing Ports.
     */
    public AndGate() {
        super(2, 1);
    }

    /**
     * Advanced constructor. Creates new AND with inPorts incoming and outPorts outgoing Ports.
     * 
     * @param inPorts
     *            int number of inPorts to append
     * @param outPorts
     *            int number of outPorts to append
     */
    public AndGate(int inPorts, int outPorts) {
        super(inPorts, outPorts);
    }

    /**
     * {@inheritDoc}
     */
    protected void calculate(Clock clock) {
        boolean result = true;
        for (Port p : getInPorts()) {
            if (!p.getState()) {
                result = false;
                break;
            }
        }
        for (Port p : getOutPorts()) {
            p.setState(result, clock);
        }
    }
}

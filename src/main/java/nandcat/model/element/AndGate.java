package nandcat.model.element;

import nandcat.model.Clock;

/**
 * AND gate implementation.
 * 
 * It represents the boolean AND-operator.
 */
public class AndGate extends Gate {

    /**
     * Default serial version uid.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Advanced constructor. Creates new AND with inPorts incoming and outPorts outgoing Ports.
     * 
     * @param inPorts
     *            int number of inPorts to append
     * @param outPorts
     *            int number of outPorts to append
     * 
     */
    protected AndGate(int inPorts, int outPorts) {
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

    /**
     * {@inheritDoc}
     */
    protected boolean isValidOutBoundary(int outPorts) {
        // allowed for FlipFlop
        return (outPorts >= 1);
    }

    /**
     * {@inheritDoc}
     */
    protected boolean isValidInBoundary(int inPorts) {
        return (inPorts >= 1);
    }
}

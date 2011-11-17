package nandcat.model.element;

import nandcat.model.Clock;

/**
 * OR gate implementation. It represents the boolean OR-operator.
 */
public class OrGate extends Gate {

    /**
     * Default serial version uid.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor. Create new OR gate with 1 incoming and 2 outcoming ports.
     */
    protected OrGate() {
        super(2, 1);
    }

    /**
     * Advanced constructor. Creates new OR with inPorts incoming and outPorts outgoing Ports.
     * 
     * @param inPorts
     *            int number of inPorts to append
     * @param outPorts
     *            int number of outPorts to append
     */
    protected OrGate(int inPorts, int outPorts) {
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
        return (inPorts >= 1);
    }
}

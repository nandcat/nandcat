package nandcat.model.element;

import java.awt.Point;
import nandcat.model.Clock;

/**
 * FlipFlop implementation.
 * 
 * @version 0.1
 * 
 */
public class FlipFlop extends Circuit {

    /**
     * Default constructor, Creates new RS-FlipFlop.
     */
    public FlipFlop() {
        AndGate r = new AndGate(2, 2);
        AndGate s = new AndGate(2, 2);
        NotGate rNot = new NotGate();
        NotGate sNot = new NotGate();
        addModule(r, new Point());
        addModule(s, new Point());
        addModule(rNot, new Point());
        addModule(sNot, new Point());
        addConnection(r.getOutPorts().get(0), rNot.getInPorts().get(0));
        addConnection(s.getOutPorts().get(0), sNot.getInPorts().get(0));
        addConnection(rNot.getOutPorts().get(0), s.getInPorts().get(0));
        addConnection(sNot.getOutPorts().get(0), r.getInPorts().get(0));
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

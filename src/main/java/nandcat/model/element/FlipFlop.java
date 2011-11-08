package nandcat.model.element;

import java.awt.Point;

/**
 * FlipFlop implementation. A FlipFlop is used to "hold" a boolean state for a certain amount of time.
 */
public class FlipFlop extends Circuit {

    /**
     * Create new RS-FlipFlop at p.
     * 
     * @param p
     *            Point specifying FlipFlop's location.
     */
    public FlipFlop(Point p) {
        super(p);
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
     * Default constructor.
     */
    public FlipFlop() {
        this(new Point(0, 0));
    }
}

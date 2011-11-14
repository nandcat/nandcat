package nandcat.model.element;

import java.awt.Point;

/**
 * FlipFlop implementation. A FlipFlop is used to "hold" a boolean state for a certain amount of time.
 */
public class FlipFlop extends Circuit {

    /**
     * Default serial version uid.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create new RS-FlipFlop.
     */
    public FlipFlop() {
        super("FlipFlop");
        AndGate r = new AndGate(2, 2);
        AndGate s = new AndGate(2, 2);
        NotGate rNot = new NotGate();
        NotGate sNot = new NotGate();
        addModule(r, new Point());
        addModule(s, new Point(1, 0));
        addModule(rNot, new Point(0, 1));
        addModule(sNot, new Point(1, 1));
        addConnection(r.getOutPorts().get(0), rNot.getInPorts().get(0));
        addConnection(s.getOutPorts().get(0), sNot.getInPorts().get(0));
        addConnection(rNot.getOutPorts().get(0), s.getInPorts().get(0));
        addConnection(sNot.getOutPorts().get(0), r.getInPorts().get(0));
        // FIXME try to fix location of ports.
    }

}

package nandcat.model.element;


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
    protected FlipFlop() {
        super("FlipFlop");
        AndGate r = new AndGate(2, 2);
        AndGate s = new AndGate(2, 2);
        NotGate rNot = new NotGate(1);
        NotGate sNot = new NotGate(1);
        r.getRectangle().setLocation(0, 0);
        s.getRectangle().setLocation(1, 0);
        rNot.getRectangle().setLocation(0, 1);
        sNot.getRectangle().setLocation(1, 1);
        addModule(r);
        addModule(s);
        addModule(rNot);
        addModule(sNot);
        addConnection(r.getOutPorts().get(0), rNot.getInPorts().get(0));
        addConnection(s.getOutPorts().get(0), sNot.getInPorts().get(0));
        addConnection(rNot.getOutPorts().get(0), s.getInPorts().get(0));
        addConnection(sNot.getOutPorts().get(0), r.getInPorts().get(0));
    }

}

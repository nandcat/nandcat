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
    public FlipFlop() {
        super("FlipFlop");
        AndGate r = new AndGate();
        AndGate s = new AndGate();
        NotGate rNot = new NotGate();
        NotGate sNot = new NotGate();
        IdentityGate idR = new IdentityGate();
        IdentityGate idS = new IdentityGate();

        addModule(r);
        addModule(s);
        addModule(rNot);
        addModule(sNot);
        addModule(idR);
        addModule(idS);

        // and -> not = nand
        addConnection(r.getOutPorts().get(0), rNot.getInPorts().get(0));
        addConnection(s.getOutPorts().get(0), sNot.getInPorts().get(0));

        // nand -> ID
        addConnection(rNot.getOutPorts().get(0), idR.getInPorts().get(0));
        addConnection(sNot.getOutPorts().get(0), idS.getInPorts().get(0));

        // ID -> significant other
        addConnection(idR.getOutPorts().get(0), s.getInPorts().get(0));
        addConnection(idS.getOutPorts().get(0), r.getInPorts().get(0));
    }

}

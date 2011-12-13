package nandcat.model.element;

import java.util.LinkedList;
import java.util.List;

/**
 * FlipFlop implementation. A FlipFlop is used to "hold" a boolean state for a certain amount of time.
 */
public class FlipFlop extends Circuit {

    /**
     * special inPort list for the Flipflop.
     */
    private List<Port> inProts = new LinkedList<Port>();

    /**
     * special outPort list for the Flipflop.
     */
    private List<Port> outProts = new LinkedList<Port>();

    /**
     * Default serial version uid.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create new RS-FlipFlop.
     */
    protected FlipFlop() {
        super("FlipFlop");
        AndGate r = new AndGate(2, 1);
        AndGate s = new AndGate(2, 1);
        NotGate rNot = new NotGate(1);
        NotGate sNot = new NotGate(1);
        IdentityGate idR = new IdentityGate(2);
        IdentityGate idS = new IdentityGate(2);
        ImpulseGenerator impy = new ImpulseGenerator(0);
        ImpulseGenerator impz = new ImpulseGenerator(0);
        Lamp licht = new Lamp();
        Lamp scheffel = new Lamp();

        // ImpulseGenerator rimp = new ImpulseGenerator(0);
        // ImpulseGenerator simp = new ImpulseGenerator(0);
        // Lamp rlamp = new Lamp();
        // Lamp slamp = new Lamp();

        addModule(r);
        addModule(s);
        addModule(rNot);
        addModule(sNot);
        addModule(idR);
        addModule(idS);
        addModule(impy);
        addModule(impz);
        addModule(licht);
        addModule(scheffel);

        // addModule(rimp);
        // addModule(simp);
        // addModule(slamp);
        // addModule(rlamp);

        // and -> not = nand
        addConnection(r.getOutPorts().get(0), rNot.getInPorts().get(0));
        addConnection(s.getOutPorts().get(0), sNot.getInPorts().get(0));

        // nand -> ID
        addConnection(rNot.getOutPorts().get(0), idR.getInPorts().get(0));
        addConnection(sNot.getOutPorts().get(0), idS.getInPorts().get(0));

        // ID -> significant other
        addConnection(idR.getOutPorts().get(0), r.getInPorts().get(0));
        addConnection(idS.getOutPorts().get(0), s.getInPorts().get(0));

        // // imps -> and
        // addConnection(rimp.getOutPorts().get(0), s.getInPorts().get(1));
        // addConnection(simp.getOutPorts().get(0), r.getInPorts().get(1));
        //
        // // id -> lamp
        // addConnection(idR.getOutPorts().get(1), rlamp.getInPorts().get(0));
        // addConnection(idS.getOutPorts().get(1), slamp.getInPorts().get(0));

        inProts.add(s.getInPorts().get(1));
        inProts.add(r.getInPorts().get(1));

        outProts.add(idR.getOutPorts().get(1));
        outProts.add(idS.getOutPorts().get(1));

        // this.overrideInPorts(inProts);
        // this.overrideOutPorts(outProts);

    }

    /**
     * Get inports.
     * 
     * @return List of inports
     */
    public List<Port> getInPorts() {
        return inProts;

    }

    /**
     * Get outports.
     * 
     * @return List of outports
     */
    public List<Port> getOutPorts() {
        return outProts;

    }
}

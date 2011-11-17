package nandcat.view;

import java.awt.Dimension;
import nandcat.model.element.AndGate;
import nandcat.model.element.Circuit;
import nandcat.model.element.FlipFlop;
import nandcat.model.element.IdentityGate;
import nandcat.model.element.ImpulseGenerator;
import nandcat.model.element.Lamp;
import nandcat.model.element.NotGate;
import nandcat.model.element.OrGate;
import nandcat.model.element.factory.ModuleLayouter;

public class StandardModuleLayouter extends ModuleLayouter {

    /**
     * Default gate dimension.
     */
    private static final Dimension GATE_DIMENSION = new Dimension(60, 40);

    /**
     * Default lamp dimension.
     */
    private static final Dimension LAMP_DIMENSION = new Dimension(40, 40);

    @Override
    public void layout(AndGate m) {
        m.getRectangle().setSize(GATE_DIMENSION);
    }

    @Override
    public void layout(Circuit m) {
        m.getRectangle().setSize(GATE_DIMENSION);
    }

    @Override
    public void layout(FlipFlop m) {
        m.getRectangle().setSize(GATE_DIMENSION);
    }

    @Override
    public void layout(IdentityGate m) {
        m.getRectangle().setSize(GATE_DIMENSION);
    }

    @Override
    public void layout(ImpulseGenerator m) {
        m.getRectangle().setSize(GATE_DIMENSION);
    }

    @Override
    public void layout(Lamp m) {
        m.getRectangle().setSize(LAMP_DIMENSION);
    }

    @Override
    public void layout(NotGate m) {
        m.getRectangle().setSize(GATE_DIMENSION);
    }

    @Override
    public void layout(OrGate m) {
        m.getRectangle().setSize(GATE_DIMENSION);
    }

}

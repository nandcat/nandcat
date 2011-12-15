package nandcat.model.check;

import static org.junit.Assert.assertFalse;
import java.awt.Rectangle;
import nandcat.model.ModelElementDefaults;
import nandcat.model.element.AndGate;
import nandcat.model.element.Circuit;
import nandcat.model.element.IdentityGate;
import nandcat.model.element.ImpulseGenerator;
import nandcat.model.element.Lamp;
import nandcat.model.element.factory.ModuleBuilderFactory;
import nandcat.view.StandardModuleLayouter;
import org.junit.Before;
import org.junit.Test;

public class FeedbackCheckTest {

    private ModuleBuilderFactory factory;

    @Before
    public void setUp() {
        factory = new ModuleBuilderFactory();
        factory.setDefaults(new ModelElementDefaults());
        factory.setLayouter(new StandardModuleLayouter());
    }

    /**
     * Flipflop is TOPLEVELCIRCUIT.
     */
    @Test
    public void testFlipFlop() {
        Circuit c = (Circuit) factory.getFlipFlopBuilder().build();
        CircuitCheck check = new FeedbackCheck();

        // expected: false - Flipflop haz feedback and cheezeburgerz
        assertFalse(check.test(c));
    }

    /**
     * FlipFlop will be 2ndLevel.
     */
    @Test
    public void test2ndLevelFlop() {
        Circuit top = (Circuit) factory.getCircuitBuilder().build();
        ImpulseGenerator impyOne = (ImpulseGenerator) factory.getClockBuilder().setFrequency(1).build();
        ImpulseGenerator impyTwo = (ImpulseGenerator) factory.getClockBuilder().setFrequency(1).build();
        ImpulseGenerator impyThree = (ImpulseGenerator) factory.getClockBuilder().setFrequency(1).build();
        AndGate twoThree = (AndGate) factory.getAndGateBuilder().build();

        Circuit flipper = (Circuit) factory.getFlipFlopBuilder().build();

        Lamp scheffel = (Lamp) factory.getLampBuilder().build();
        Lamp licht = (Lamp) factory.getLampBuilder().build();

        top.addModule(impyOne);
        top.addModule(impyTwo);
        top.addModule(impyThree);
        top.addModule(flipper);
        top.addModule(scheffel);
        top.addModule(licht);

        top.addConnection(impyOne.getOutPorts().get(0), flipper.getInPorts().get(0));

        top.addConnection(impyTwo.getOutPorts().get(0), twoThree.getInPorts().get(0));
        top.addConnection(impyThree.getOutPorts().get(0), twoThree.getInPorts().get(1));

        top.addConnection(twoThree.getOutPorts().get(0), flipper.getInPorts().get(1));

        top.addConnection(flipper.getOutPorts().get(0), scheffel.getInPorts().get(0));
        top.addConnection(flipper.getOutPorts().get(1), licht.getInPorts().get(0));

        // zwecks entkäferer.
        twoThree.setSelected(true);
        twoThree.setRectangle(new Rectangle(50, 50, 50, 50));

        CircuitCheck check = new FeedbackCheck();
        // expected flase - ff hat fb
        assertFalse(check.test(top));
    }

    /**
     * This test must return false if there is a feedback from an identity gate to another one and back.
     */
    @Test
    public void testIdGateFeedback() {
        Circuit circuit = (Circuit) factory.getCircuitBuilder().build();
        AndGate andGate = (AndGate) factory.getAndGateBuilder().build();
        IdentityGate id = (IdentityGate) factory.getIdentityGateBuilder().build();

        circuit.addModule(andGate);
        circuit.addModule(id);

        circuit.addConnection(andGate.getOutPorts().get(0), id.getInPorts().get(0));
        circuit.addConnection(id.getOutPorts().get(0), andGate.getInPorts().get(0));

        CircuitCheck check = new FeedbackCheck();
        assertFalse(check.test(circuit));
    }
}

package nandcat.model.check;

import junit.framework.TestCase;
import nandcat.model.element.AndGate;
import nandcat.model.element.Circuit;
import nandcat.model.element.FlipFlop;
import nandcat.model.element.ImpulseGenerator;
import nandcat.model.element.Lamp;

public class FeedbackCheckTest extends TestCase {

    /**
     * Flipflop is TOPLEVELCIRCUIT
     */
    public void testFlipFlop() {
        Circuit c = new FlipFlop();
        CircuitCheck check = new FeedbackCheck();

        // expected: false - Flipflop haz feedback and cheezeburgerz
        assertTrue(check.test(c));
    }

    /**
     * FlipFlop will be 2ndLevel
     */
    public void test2ndLevelFlop() {
        Circuit top = new Circuit();
        ImpulseGenerator impyOne = new ImpulseGenerator();
        ImpulseGenerator impyTwo = new ImpulseGenerator();
        ImpulseGenerator impyThree = new ImpulseGenerator();
        AndGate twoThree = new AndGate();

        Circuit flipper = new FlipFlop();

        Lamp scheffel = new Lamp();
        Lamp licht = new Lamp();

        top.addConnection(impyOne.getOutPorts().get(0), flipper.getInPorts().get(0));

        top.addConnection(impyTwo.getOutPorts().get(0), twoThree.getInPorts().get(0));
        top.addConnection(impyThree.getOutPorts().get(0), twoThree.getInPorts().get(1));

        top.addConnection(twoThree.getOutPorts().get(0), flipper.getInPorts().get(1));

        top.addConnection(flipper.getOutPorts().get(0), scheffel.getInPorts().get(0));
        top.addConnection(flipper.getOutPorts().get(1), licht.getInPorts().get(0));

        // Magic ?
        CircuitCheck check = new FeedbackCheck();
        assertFalse(check.test(top));
    }

}

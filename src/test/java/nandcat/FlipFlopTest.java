package nandcat;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.awt.Point;
import java.util.List;
import nandcat.model.Clock;
import nandcat.model.ClockListener;
import nandcat.model.Model;
import nandcat.model.ModelElementDefaults;
import nandcat.model.element.AndGate;
import nandcat.model.element.ImpulseGenerator;
import nandcat.model.element.Lamp;
import nandcat.model.element.NotGate;
import nandcat.model.element.Port;
import nandcat.model.element.factory.ModuleBuilderFactory;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests flipflop functionality.
 * 
 * @author ben
 * 
 */
public class FlipFlopTest {

    /**
     * Model used for testing.
     */
    private Model model;

    /**
     * FlipFlop's set button.
     */
    private ImpulseGenerator setButton;

    /**
     * FlipFlop's reset button.
     */
    private ImpulseGenerator resetButton;

    /**
     * FlipFlop's lamp.
     */
    private Lamp lampQ1;

    private ModuleBuilderFactory factory;

    /**
     * Sets the model and components up.
     * 
     * @throws Exception
     *             Any Exception should fail the test.
     */
    // @Ignore
    @Before
    public void setUp() throws Exception {
        factory = new ModuleBuilderFactory();
        factory.setDefaults(new ModelElementDefaults());
        model = new Model();
        AndGate andGate1 = (AndGate) factory.getAndGateBuilder().build();
        AndGate andGate2 = (AndGate) factory.getAndGateBuilder().build();
        NotGate notGate1 = (NotGate) factory.getNotGateBuilder().setOutPorts(2).build();
        NotGate notGate2 = (NotGate) factory.getNotGateBuilder().setOutPorts(2).build();
        setButton = (ImpulseGenerator) factory.getSwitchBuilder().build();
        resetButton = (ImpulseGenerator) factory.getSwitchBuilder().build();
        lampQ1 = (Lamp) factory.getLampBuilder().build();
        Lamp lampQ2 = (Lamp) factory.getLampBuilder().build();
        // IdentityGate splitterNot1 = new IdentityGate();
        // IdentityGate splitterNot2 = new IdentityGate();
        List<Port> andGate1InPorts = andGate1.getInPorts();
        List<Port> andGate1OutPorts = andGate1.getOutPorts();
        List<Port> andGate2InPorts = andGate2.getInPorts();
        List<Port> andGate2OutPorts = andGate2.getOutPorts();
        List<Port> notGate1InPorts = notGate1.getInPorts();
        List<Port> notGate1OutPorts = notGate1.getOutPorts();
        List<Port> notGate2InPorts = notGate2.getInPorts();
        List<Port> notGate2OutPorts = notGate2.getOutPorts();
        List<Port> setButtonOutPorts = setButton.getOutPorts();
        List<Port> resetButtonOutPorts = resetButton.getOutPorts();
        List<Port> lampQ1InPorts = lampQ1.getInPorts();
        List<Port> lampQ2InPorts = lampQ2.getInPorts();
        // List<Port> splitterNot1InPorts = splitterNot1.getInPorts();
        // List<Port> splitterNot2InPorts = splitterNot2.getInPorts();
        // List<Port> splitterNot1OutPorts = splitterNot1.getOutPorts();
        // List<Port> splitterNot2OutPorts = splitterNot2.getOutPorts();
        model.addConnection(setButtonOutPorts.get(0), andGate1InPorts.get(0));
        model.addConnection(resetButtonOutPorts.get(0), andGate2InPorts.get(1));
        model.addConnection(andGate1OutPorts.get(0), notGate1InPorts.get(0));
        model.addConnection(andGate2OutPorts.get(0), notGate2InPorts.get(0));
        model.addConnection(notGate1OutPorts.get(0), lampQ1InPorts.get(0));
        model.addConnection(notGate2OutPorts.get(0), lampQ2InPorts.get(0));
        model.addConnection(notGate1OutPorts.get(1), andGate2InPorts.get(0));
        model.addConnection(notGate2OutPorts.get(1), andGate1InPorts.get(1));
        model.addModule(andGate1, new Point(1, 1));
        model.addModule(andGate2, new Point(2, 2));
        model.addModule(notGate1, new Point(3, 3));
        model.addModule(notGate2, new Point(4, 4));
        model.addModule(setButton, new Point(5, 5));
        model.addModule(resetButton, new Point(6, 6));
        model.addModule(lampQ1, new Point(7, 7));
        model.addModule(lampQ2, new Point(8, 8));
    }

    /**
     * Tests the flipflop functionality at specific cycles.
     */
    // @Ignore
    @Test
    public void test() {
        // Test if both buttons are off
        assertFalse(setButton.getState());
        assertFalse(resetButton.getState());
        model.getClock().addListener(new ClockListener() {

            /**
             * Counts the cycle.
             */
            private int counter = 1;

            public void clockTicked(Clock clock) {
                if (counter <= 10) {
                    testFirst10Cycles();
                }
                if (counter == 10) {
                    activateSetAtCycle10();
                }
                if (counter == 20) {
                    testCycle20();
                    resetFlipFlop();
                }
                if (counter == 30) {
                    testCycle30();
                    model.stopSimulation();
                }
                counter++;
            }
        });
    }

    /**
     * After 10 cycles and Reset = 1, Set = 0.
     */
    private void testCycle30() {
        assertFalse(lampQ1.getState());
    }

    /**
     * Reset FlipFlop, set Reset = 1, Set = 0.
     */
    private void resetFlipFlop() {
        model.stopSimulation();
        setButton.toggleState();
        assertFalse(setButton.getState());
        resetButton.toggleState();
        assertTrue(resetButton.getState());
        model.startSimulation();
    }

    /**
     * Test first 10 cycles with Reset = 0, Set = 0.
     */
    private void testFirst10Cycles() {
        assertFalse(lampQ1.getState());
    }

    /**
     * At Cycle 10, sets Set-button = 1.
     */
    private void activateSetAtCycle10() {
        model.stopSimulation();
        setButton.toggleState();
        assertTrue(setButton.getState());
        model.startSimulation();
    }

    /**
     * At cycle 20, lamp should be on.
     */
    private void testCycle20() {
        assertTrue(lampQ1.getState());
    }
}

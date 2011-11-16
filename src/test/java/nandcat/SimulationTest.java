package nandcat;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.awt.Point;
import java.util.List;
import nandcat.model.Clock;
import nandcat.model.ClockListener;
import nandcat.model.Model;
import nandcat.model.ModelElementDefaults;
import nandcat.model.element.ImpulseGenerator;
import nandcat.model.element.Lamp;
import nandcat.model.element.OrGate;
import nandcat.model.element.Port;
import nandcat.model.element.factory.ModuleBuilderFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Simulation Test 1.
 * 
 * Tests simulation of a circuit of one Or gate connected to a lamp and 2 buttons (off, on).
 */
public class SimulationTest {

    /**
     * Clock cycle to test.
     */
    private static final int CORRECT_AFTER_CYCLE = 2;

    /**
     * Model used for testing.
     */
    private Model model;

    /**
     * Outgoing port of button1.
     */
    private Port outPortButton1;

    /**
     * Outgoing port of button2.
     */
    private Port outPortButton2;

    /**
     * Incoming first port of or gate.
     */
    private Port inPort1Or;

    /**
     * Incoming second port of or gate.
     */
    private Port inPort2Or;

    /**
     * Outgoing port of or gate.
     */
    private Port outPortOr;

    /**
     * Lamp.
     */
    private Lamp lamp;

    /**
     * Button.
     */
    private ImpulseGenerator button1;

    private ModuleBuilderFactory factory;

    /**
     * Sets the model and components up.
     * 
     * @throws Exception
     *             Any Exception should fail the test.
     */
    @Ignore
    @Before
    public void setUp() throws Exception {
        factory = new ModuleBuilderFactory();
        factory.setDefaults(new ModelElementDefaults());

        model = new Model();
        OrGate orGate = (OrGate) factory.getOrGateBuilder().build();
        model.addModule(orGate, new Point(1, 1));
        lamp = (Lamp) factory.getLampBuilder().build();
        model.addModule(lamp, new Point(1, 2));
        button1 = (ImpulseGenerator) factory.getSwitchBuilder().build();
        ImpulseGenerator button2 = (ImpulseGenerator) factory.getSwitchBuilder().build();
        List<Port> outPortsButton1 = button1.getOutPorts();
        List<Port> outPortsButton2 = button2.getOutPorts();
        List<Port> inPortsOr = orGate.getInPorts();
        List<Port> outPortsOr = orGate.getOutPorts();
        List<Port> inPortsLamp = lamp.getInPorts();
        outPortButton1 = outPortsButton1.get(0);
        outPortButton2 = outPortsButton2.get(0);
        inPort1Or = inPortsOr.get(0);
        inPort2Or = inPortsOr.get(1);
        outPortOr = outPortsOr.get(0);
        Port inPortLamp = inPortsLamp.get(0);
        model.addConnection(outPortButton1, inPort1Or);
        model.addConnection(outPortButton2, inPort2Or);
        model.addConnection(outPortOr, inPortLamp);
    }

    /**
     * Tests the simulation.
     */
    @Ignore
    @Test
    public void test() {
        model.getClock().addListener(new ClockListener() {

            /**
             * Counts the cycle.
             */
            private int counter = 0;

            public void clockTicked(Clock clock) {
                if (counter == CORRECT_AFTER_CYCLE || counter == CORRECT_AFTER_CYCLE + 1) {
                    testCorrectValuesAfter3Cycles();
                }
                if (counter == CORRECT_AFTER_CYCLE + 2) {
                    model.stopSimulation();
                }
                counter++;
            }
        });
        button1.toggleState();
        assertTrue(button1.getState());
        model.startSimulation();
    }

    /**
     * Tests correct values after 3 cycles.
     */
    private void testCorrectValuesAfter3Cycles() {
        assertTrue(button1.getState());
        assertTrue(outPortButton1.getState());
        assertFalse(outPortButton2.getState());
        assertTrue(inPort1Or.getState());
        assertFalse(inPort2Or.getState());
        assertTrue(outPortOr.getState());
        assertTrue(lamp.getState());
    }
}

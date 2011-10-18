package de.unipassau.sep.nandcat;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.awt.Point;
import java.util.List;
import org.junit.Ignore;
import de.unipassau.sep.nandcat.model.Clock;
import de.unipassau.sep.nandcat.model.ClockListener;
import de.unipassau.sep.nandcat.model.Model;
import de.unipassau.sep.nandcat.model.element.ImpulseGenerator;
import de.unipassau.sep.nandcat.model.element.Lamp;
import de.unipassau.sep.nandcat.model.element.OrGate;
import de.unipassau.sep.nandcat.model.element.Port;

/**
 * Simulation Test 1.
 * 
 * Tests simulation of a circuit of one Or gate connected to a lamp and 2 buttons (off, on).
 * 
 * @version 0.1
 */
public class SimulationTest {

    /**
     * TODO Missing doc comment.
     */
    private static final int CORRECT_AFTER_CYCLE = 3;

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

    /**
     * Sets the model and components up.
     * 
     * @throws Exception
     *             Any Exception should fail the test.
     */
    @Ignore
    // @Before
    public void setUp() throws Exception {
        model = new Model();
        OrGate orGate = new OrGate();
        model.addModule(orGate, new Point(1, 1));
        lamp = new Lamp();
        model.addModule(lamp, new Point(1, 2));
        button1 = new ImpulseGenerator(0);
        ImpulseGenerator button2 = new ImpulseGenerator(0);
        // Lists of ports
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
        // Connection conButton1ToOr = new Connection(outPortButton1, inPort1Or);
        // Connection conButton2ToOr = new Connection(outPortButton2, inPort2Or);
        // Connection conOrToLamp = new Connection(outPortOr, inPortLamp);
        model.addConnection(outPortButton1, inPort1Or);
        model.addConnection(outPortButton2, inPort2Or);
        model.addConnection(outPortOr, inPortLamp);
    }

    /**
     * Tests the simulation.
     */
    @Ignore
    // @Test
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

package de.unipassau.sep.nandcat;

import static org.junit.Assert.*;
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

    private static final int CORRECT_AFTER_CYCLE = 3;

    private Model model;

    private ClockListener clocklistener;

    private Port outPortButton1;

    private Port outPortButton2;

    private Port inPort1Or;

    private Port inPort2Or;

    private Port outPortOr;

    private Port inPortLamp;

    @Ignore
    // @Before
    public void setUp() throws Exception {
        model = new Model();
        OrGate orGate = new OrGate();
        model.addModule(orGate, new Point(1, 1));
        Lamp lamp = new Lamp();
        model.addModule(lamp, new Point(1, 2));
        ImpulseGenerator button1 = new ImpulseGenerator(0);
        // TODO Impulse Generator how to activate?
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
        inPortLamp = inPortsLamp.get(0);
        // Connection conButton1ToOr = new Connection(outPortButton1, inPort1Or);
        // Connection conButton2ToOr = new Connection(outPortButton2, inPort2Or);
        // Connection conOrToLamp = new Connection(outPortOr, inPortLamp);
        // TODO Model sollte connection auch zu den Ports hinzufügen
        model.addConnection(outPortButton1, inPort1Or);
        model.addConnection(outPortButton2, inPort2Or);
        model.addConnection(outPortOr, inPortLamp);
        clocklistener = new ClockListener() {

            private int counter = 0;

            public void clockTicked(Clock clock) {
                // TODO Test all states
                if (counter == CORRECT_AFTER_CYCLE || counter == CORRECT_AFTER_CYCLE + 1) {
                    test();
                }
                counter++;
                if (counter == CORRECT_AFTER_CYCLE + 2) {
                    model.stopSimulation();
                }
            }
        };
        model.getClock().addListener(clocklistener);
        model.startSimulation();
        // TODO Set Button1 on!
    }

    @Ignore
    // @After
    public void tearDown() {
        model.getClock().removeListener(clocklistener);
    }

    @Ignore
    public void test() {
        assertTrue(outPortButton1.getState());
        assertFalse(outPortButton2.getState());
        assertTrue(inPort1Or.getState());
        assertFalse(inPort2Or.getState());
        assertTrue(outPortOr.getState());
        assertTrue(inPortLamp.getState());
    }
}

package de.unipassau.sep.nandcat;

import static org.junit.Assert.fail;
import java.awt.Point;
import java.util.List;
import org.junit.Before;
import org.junit.Ignore;
import de.unipassau.sep.nandcat.model.Model;
import de.unipassau.sep.nandcat.model.element.ImpulseGenerator;
import de.unipassau.sep.nandcat.model.element.Lamp;
import de.unipassau.sep.nandcat.model.element.OrGate;
import de.unipassau.sep.nandcat.model.element.Port;

public class SimulationTest {

    private Model model;

    @Ignore //@Before
    public void setUp() throws Exception {
        model = new Model();
        OrGate orGate = new OrGate();
        model.addModule(orGate, new Point(1, 1));
        Lamp lamp = new Lamp();
        model.addModule(lamp, new Point(1, 2));
        ImpulseGenerator button1 = new ImpulseGenerator(0);
        ImpulseGenerator button2 = new ImpulseGenerator(0);
        // Lists of ports
        List<Port> outPortsButton1 = button1.getOutPorts();
        List<Port> outPortsButton2 = button2.getOutPorts();
        List<Port> inPortsOr = orGate.getInPorts();
        List<Port> outPortsOr = orGate.getOutPorts();
        List<Port> inPortsLamp = lamp.getInPorts();
        // Ports
        Port outPortButton1 = outPortsButton1.get(0);
        Port outPortButton2 = outPortsButton2.get(0);
        Port inPort1Or = inPortsOr.get(0);
        Port inPort2Or = inPortsOr.get(1);
        Port outPortOr = outPortsOr.get(0);
        Port inPortLamp = inPortsLamp.get(0);

        //Connection conButton1ToOr = new Connection(outPortButton1, inPort1Or);
        //Connection conButton2ToOr = new Connection(outPortButton2, inPort2Or);
        //Connection conOrToLamp = new Connection(outPortOr, inPortLamp);
        // TODO Model sollte connection auch zu den Ports hinzufügen
        model.addConnection(outPortButton1, inPort1Or);
        model.addConnection(outPortButton2, inPort2Or);
        model.addConnection(outPortOr, inPortLamp);
        
        // TODO Weiter machen
    }

    @Ignore
    public void test() {
        fail("Not yet implemented");
    }
}

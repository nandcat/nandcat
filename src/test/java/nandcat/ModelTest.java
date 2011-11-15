package nandcat;

import java.awt.Point;
import junit.framework.TestCase;
import nandcat.model.Model;
import nandcat.model.element.AndGate;
import nandcat.model.element.ImpulseGenerator;
import nandcat.model.element.Lamp;

public class ModelTest extends TestCase {

    public void testCycle() {
        Model model = new Model();
        Point p = new Point(0, 0);
        ImpulseGenerator one = new ImpulseGenerator(1);
        ImpulseGenerator two = new ImpulseGenerator(0);
        model.addModule(one, p);
        model.addModule(two, p);
        AndGate and = new AndGate();
        model.addModule(and, p);
        model.addConnection(one.getOutPorts().get(0), and.getInPorts().get(0));
        model.addConnection(two.getOutPorts().get(0), and.getInPorts().get(1));
        Lamp lamp = new Lamp();
        model.addModule(lamp, p);
        model.addConnection(and.getOutPorts().get(0), lamp.getInPorts().get(0));

        /*
         * erwartetes Verhalten: lampe leuchtet immer einen zyklus nachdem and ein true weitergibt
         */
        two.toggleState();
        model.startSimulation();

        // cycle one
        model.getClock().cycle();
        assertFalse(one.getOutPorts().get(0).getState());
        assertTrue(two.getOutPorts().get(0).getState());
        assertFalse(and.getInPorts().get(0).getState());
        assertTrue(and.getInPorts().get(1).getState());

        assertFalse(and.getOutPorts().get(0).getState());
        assertFalse(lamp.getState());

        // cycle two
        model.getClock().cycle();
        assertTrue(one.getOutPorts().get(0).getState());
        assertTrue(two.getOutPorts().get(0).getState());
        assertTrue(and.getInPorts().get(0).getState());
        assertTrue(and.getInPorts().get(1).getState());

        assertFalse(and.getOutPorts().get(0).getState());
        assertFalse(lamp.getState());

        // cycle three
        model.getClock().cycle();
        assertFalse(one.getOutPorts().get(0).getState());
        assertTrue(two.getOutPorts().get(0).getState());
        assertFalse(and.getInPorts().get(0).getState());
        assertTrue(and.getInPorts().get(1).getState());

        assertTrue(and.getOutPorts().get(0).getState());
        assertTrue(lamp.getState());

        // cycle four
        model.getClock().cycle();
        assertTrue(one.getOutPorts().get(0).getState());
        assertTrue(two.getOutPorts().get(0).getState());

        assertFalse(and.getOutPorts().get(0).getState());
        assertFalse(lamp.getState());
    }
}

package nandcat.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;
import nandcat.model.element.AndGate;
import nandcat.model.element.Circuit;
import nandcat.model.element.Connection;
import nandcat.model.element.Element;
import nandcat.model.element.ImpulseGenerator;
import nandcat.model.element.factory.ModuleBuilderFactory;
import org.junit.Before;
import org.junit.Test;

public class ModelTest {

    private ModuleBuilderFactory factory;

    @Before
    public void setUp() {
        factory = new ModuleBuilderFactory();
        factory.setDefaults(new ModelElementDefaults());
    }

    @Test
    public void testGetFrequency() {
        Model model = new Model();
        model.clearCircuit();
        ImpulseGenerator impy = (ImpulseGenerator) factory.getClockBuilder().setFrequency(500).build();
        model.addModule(impy, new Point(0, 0));
        model.setFrequency(impy, 100);
        assertEquals(100, impy.getFrequency());
        model.startChecks();
    }

    @Test
    public void testGetCircuitFromSelected() {
        Model m = new Model();
        Circuit c = m.getCircuit();
        Set<Element> selectit = new HashSet<Element>();

        AndGate drinSelektiert = (AndGate) factory.getAndGateBuilder().build();
        c.addModule(drinSelektiert);
        drinSelektiert.setRectangle(new Rectangle(new Point(1, 1)));
        AndGate drinSelektiert2 = (AndGate) factory.getAndGateBuilder().build();
        c.addModule(drinSelektiert2);
        drinSelektiert2.setRectangle(new Rectangle(new Point(2, 2)));
        AndGate unselektiert = (AndGate) factory.getAndGateBuilder().build();
        c.addModule(unselektiert);
        unselektiert.setRectangle(new Rectangle(new Point(3, 3)));

        Connection c1 = c.addConnection(drinSelektiert.getOutPorts().get(0), drinSelektiert2.getInPorts().get(0));
        Connection c2 = c.addConnection(drinSelektiert2.getOutPorts().get(0), unselektiert.getInPorts().get(0));

        drinSelektiert.setSelected(true);
        selectit.add(drinSelektiert);
        drinSelektiert2.setSelected(true);
        selectit.add(drinSelektiert2);
        c1.setSelected(true);
        selectit.add(c1);
        c2.setSelected(true);
        selectit.add(c2);

        Circuit neu = m.getCircuitFromSelected();

        System.out.println("selectit contains drinSelektiert : " + selectit.contains(drinSelektiert));
        System.out.println("circuit contains drinSelektiert : " + neu.getElements().contains(drinSelektiert) + "\n");

        System.out.println("selectit contains drinSelektiert2 : " + selectit.contains(drinSelektiert2));
        System.out.println("circuit contains drinSelektiert2 : " + neu.getElements().contains(drinSelektiert2) + "\n");

        System.out.println("selectit contains unselektiert : " + selectit.contains(unselektiert));
        System.out.println("circuit contains unselektiert : " + neu.getElements().contains(unselektiert) + "\n");

        assertFalse(neu.getElements().contains(unselektiert));
        assertFalse(neu.getElements().contains(c2));

        Connection con = neu.getConnections().iterator().next();
        // connection is included
        assertEquals(con.getInPort(), c1.getInPort());
        assertEquals(con.getOutPort(), c1.getOutPort());
        assertTrue(neu.getElements().contains(drinSelektiert));
        assertTrue(neu.getElements().contains(drinSelektiert2));

        assertEquals(1, neu.getConnections().size());
        assertEquals(3, neu.getElements().size());
    }
}

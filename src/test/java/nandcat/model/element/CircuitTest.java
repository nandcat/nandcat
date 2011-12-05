package nandcat.model.element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
import nandcat.model.ModelElementDefaults;
import nandcat.model.element.factory.ModuleBuilderFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class CircuitTest {

    /**
     * Default location for elements.
     */
    private Point p;

    private ModuleBuilderFactory factory;

    @Before
    public void setUp() {
        factory = new ModuleBuilderFactory();
        factory.setDefaults(new ModelElementDefaults());
        p = new Point(0, 0);
    }

    /**
     * AND, ImpulseGen.
     */
    @Test
    public void testAddModule() {
        AndGate and0 = new AndGate(2, 1);
        and0.getRectangle().setLocation(p);
        ImpulseGenerator impy = new ImpulseGenerator(0);
        impy.getRectangle().setLocation(p);
        Circuit c = (Circuit) factory.getCircuitBuilder().build();
        c.addModule(and0);
        c.addModule(impy);
        assertTrue(c.getElements().contains(and0));
        assertTrue(c.getElements().contains(impy));
    }

    /**
     * Test: Switch, AND0-->AND1, Switch-->AND2, AND1-->AND2.
     */
    @Test
    public void testStartingModules() {
        AndGate and0 = new AndGate(2, 1);
        and0.getRectangle().setLocation(p);
        AndGate and1 = new AndGate(2, 1);
        and1.getRectangle().setLocation(p);
        AndGate and2 = new AndGate(2, 1);
        and2.getRectangle().setLocation(p);
        ImpulseGenerator impy = new ImpulseGenerator(0);
        impy.getRectangle().setLocation(p);
        Circuit c = (Circuit) factory.getCircuitBuilder().build();
        // NOTE will fail ???
        // Importer impo = new SEPAFImporter();
        // impo.setFile(new File("/tmp/fuck.xml"));
        // impo.importCircuit();
        // Circuit c = impo.getCircuit();
        c.addModule(and0);
        c.addModule(and1);
        c.addModule(and2);
        c.addModule(impy);
        c.addConnection(and0.getOutPorts().get(0), and1.getInPorts().get(1));
        c.addConnection(impy.getOutPorts().get(0), and2.getInPorts().get(0));
        c.addConnection(and1.getOutPorts().get(0), and2.getInPorts().get(1));
        assertTrue(c.getStartingModules().contains(impy));
        assertTrue(c.getStartingModules().contains(and0));
        assertTrue(c.getStartingModules().contains(and1));
        assertFalse(c.getStartingModules().contains(and2));
        // Exporter expo = new SEPAFExporter();
        // expo.setCircuit(c);
        // expo.setFile(new File("/tmp/fuck1.xml"));
        // expo.exportCircuit();
    }

    /**
     * Remove element (AND1) AND0 --> AND1.
     */
    @Test
    public void testRemoveElement() {
        AndGate and0 = new AndGate(2, 1);
        and0.getRectangle().setLocation(p);
        AndGate and1 = new AndGate(2, 1);
        and1.getRectangle().setLocation(p);
        Circuit c = (Circuit) factory.getCircuitBuilder().build();
        c.addModule(and0);
        c.addModule(and1);
        Connection conn = c.addConnection(and0.getOutPorts().get(0), and1.getInPorts().get(0));
        // removal of module
        c.removeElement(and1);
        assertFalse(c.getElements().contains(and1));
        assertFalse(c.getElements().contains(conn));
        assertNull(and0.getOutPorts().get(0).getConnection());
        assertNull(and1.getInPorts().get(0).getConnection());
        // removal of connection
        c.addModule(and1);
        conn = c.addConnection(and0.getOutPorts().get(0), and1.getInPorts().get(0));
        c.removeElement(conn);
        assertFalse(c.getElements().contains(conn));
        assertNull(and0.getOutPorts().get(0).getConnection());
        assertNull(and1.getInPorts().get(0).getConnection());
    }

    /**
     * Test selecting a single module. (AND)
     */
    @Test
    public void testSetModuleActive() {
        Circuit c = (Circuit) factory.getCircuitBuilder().build();
        AndGate and0 = new AndGate(2, 1);
        and0.getRectangle().setLocation(p);
        c.addModule(and0);
        assertFalse(and0.isSelected());
        c.setModuleSelected(and0, true);
        assertTrue(and0.isSelected());
        c.setModuleSelected(and0, false);
        assertFalse(and0.isSelected());
    }

    /**
     * Test connecting to Modules (ImpulseGen --> Lamp).
     */
    @Test
    public void testAddConnection() {
        Circuit c = (Circuit) factory.getCircuitBuilder().build();
        ImpulseGenerator impy = new ImpulseGenerator(0);
        impy.getRectangle().setLocation(p);
        Lamp lamp = new Lamp();
        lamp.getRectangle().setLocation(p);
        c.addModule(impy);
        impy.toggleState();
        c.addModule(lamp);
        Connection conn = c.addConnection(impy.getOutPorts().get(0), lamp.getInPorts().get(0));
        assertTrue(c.getElements().contains(conn));
        assertTrue(impy.getOutPorts().get(0).getConnection().getNextModule() == lamp);
        assertTrue(lamp.getInPorts().get(0).getConnection().getPreviousModule() == impy);
    }

    /**
     * Test circuit containing a circuit (dawg).
     */
    @Test
    public void testDawg() {
        Circuit c = (Circuit) factory.getCircuitBuilder().build();
        Circuit innerCircuit = new FlipFlop();
        innerCircuit.getRectangle().setLocation(p);
        c.addModule(innerCircuit);
    }

    @Ignore
    @Test
    public void testDeReconstruct() {
        Circuit c = (Circuit) factory.getCircuitBuilder().build();
        ImpulseGenerator clock = (ImpulseGenerator) factory.getClockBuilder().build();
        AndGate a = (AndGate) factory.getAndGateBuilder().build();
        Lamp lamp = (Lamp) factory.getLampBuilder().build();
        c.addModule(clock);
        c.addModule(a);
        c.addModule(lamp);
        c.addConnection(clock.getOutPorts().get(0), a.getInPorts().get(0));
        c.addConnection(a.getOutPorts().get(0), lamp.getInPorts().get(0));
        assertEquals(5, c.getElements().size());
        Circuit enclosure = (Circuit) factory.getCircuitBuilder().build();
        enclosure.addModule(c);
        assertEquals(1, enclosure.getElements().size());
        assertTrue(enclosure.getElements().get(0) instanceof Circuit);
        Circuit deconstructed = (Circuit) enclosure.getElements().get(0);
        assertEquals(1, deconstructed.getElements().size());
        assertTrue(deconstructed.getElements().get(0) instanceof AndGate);
        assertEquals(1, deconstructed.getInPorts().size());
        assertEquals(1, deconstructed.getOutPorts().size());

        Circuit reconstructed = deconstructed.reconstruct();
        assertEquals(5, reconstructed.getElements().size());
        assertEquals(0, reconstructed.getInPorts().size());
        assertEquals(0, reconstructed.getOutPorts().size());

        AndGate reA = null;
        ImpulseGenerator reC = null;
        Lamp reL = null;
        Set<Connection> cache = new HashSet<Connection>();
        Connection reC2A = null;
        Connection reA2L = null;
        for (Element e : reconstructed.getElements()) {
            if (e instanceof Connection) {
                cache.add((Connection) e);
            } else if (e instanceof AndGate) {
                reA = (AndGate) e;
            } else if (e instanceof ImpulseGenerator) {
                reC = (ImpulseGenerator) e;
            } else if (e instanceof Lamp) {
                reL = (Lamp) e;
            }
        }
        assertTrue(reA != null);
        assertTrue(reC != null);
        assertTrue(reL != null);

        for (Connection connection : cache) {
            if (connection.getInPort() == reC.getOutPorts().get(0)
                    && connection.getOutPort() == reA.getInPorts().get(0)) {
                reC2A = connection;
            } else if (connection.getInPort() == reA.getOutPorts().get(0)
                    && connection.getOutPort() == reL.getInPorts().get(0)) {
                reA2L = connection;
            }
        }
        assertTrue(reC2A != null);
        assertTrue(reA2L != null);

    }
}

package nandcat.model.element;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.awt.Point;
import nandcat.model.ModelElementDefaults;
import nandcat.model.element.factory.ModuleBuilderFactory;
import org.junit.Before;
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
        ImpulseGenerator impy = new ImpulseGenerator(0);
        Circuit c = (Circuit) factory.getCircuitBuilder().build();
        c.addModule(and0, p);
        c.addModule(impy, p);
        assertTrue(c.getElements().contains(and0));
        assertTrue(c.getElements().contains(impy));
    }

    /**
     * Test: Switch, AND0-->AND1, Switch-->AND2, AND1-->AND2.
     */
    @Test
    public void testStartingModules() {
        AndGate and0 = new AndGate(2, 1);
        AndGate and1 = new AndGate(2, 1);
        AndGate and2 = new AndGate(2, 1);
        ImpulseGenerator impy = new ImpulseGenerator(0);
        Circuit c = (Circuit) factory.getCircuitBuilder().build();
        // NOTE will fail ???
        // Importer impo = new SEPAFImporter();
        // impo.setFile(new File("/tmp/fuck.xml"));
        // impo.importCircuit();
        // Circuit c = impo.getCircuit();
        c.addModule(and0, p);
        c.addModule(and1, p);
        c.addModule(and2, p);
        c.addModule(impy, p);
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
        AndGate and1 = new AndGate(2, 1);
        Circuit c = (Circuit) factory.getCircuitBuilder().build();
        c.addModule(and0, p);
        c.addModule(and1, p);
        Connection conn = c.addConnection(and0.getOutPorts().get(0), and1.getInPorts().get(0));
        // removal of module
        c.removeElement(and1);
        assertFalse(c.getElements().contains(and1));
        assertFalse(c.getElements().contains(conn));
        assertNull(and0.getOutPorts().get(0).getConnection());
        assertNull(and1.getInPorts().get(0).getConnection());
        // removal of connection
        c.addModule(and1, p);
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
        c.addModule(and0, p);
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
        Lamp lamp = new Lamp();
        c.addModule(impy, p);
        impy.toggleState();
        c.addModule(lamp, p);
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
        c.addModule(innerCircuit, p);
    }
}

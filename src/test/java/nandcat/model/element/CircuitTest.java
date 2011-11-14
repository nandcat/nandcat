package nandcat.model.element;

import java.awt.Point;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class CircuitTest extends TestCase {

    /**
     * Default location for elements.
     */
    private Point p;

    /**
     * Create the test case.
     * 
     * @param testName
     *            name of the test case
     */
    public CircuitTest(String testName) {
        super(testName);
        p = new Point(0, 0);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(CircuitTest.class);
    }

    /**
     * AND, ImpulseGen.
     */
    public void testAddModule() {
        AndGate and0 = new AndGate(2, 1);
        ImpulseGenerator impy = new ImpulseGenerator(0);
        Circuit c = new Circuit();
        c.addModule(and0, p);
        c.addModule(impy, p);
        assertTrue(c.getElements().contains(and0));
        assertTrue(c.getElements().contains(impy));
    }

    /**
     * Test: Switch, AND0-->AND1, Switch-->AND2, AND1-->AND2.
     */
    public void testStartingModules() {
        AndGate and0 = new AndGate(2, 1);
        AndGate and1 = new AndGate(2, 1);
        AndGate and2 = new AndGate(2, 1);
        ImpulseGenerator impy = new ImpulseGenerator(0);
        Circuit c = new Circuit();
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
    public void testRemoveElement() {
        AndGate and0 = new AndGate(2, 1);
        AndGate and1 = new AndGate(2, 1);
        Circuit c = new Circuit();
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
    public void testSetModuleActive() {
        Circuit c = new Circuit();
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
    public void testAddConnection() {
        Circuit c = new Circuit();
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
    public void testDawg() {
        Circuit c = new Circuit();
        Circuit innerCircuit = new FlipFlop();
        c.addModule(innerCircuit, p);
    }
}

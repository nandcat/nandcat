package de.unipassau.sep.nandcat.model.element;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AndGateTest extends TestCase {

    /**
     * Create the test case.
     * 
     * @param testName
     *            name of the test case
     */
    public AndGateTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AndGateTest.class);
    }

    /**
     * Rigourous Test :-).
     */
    public void testApp() {
        AndGate and = new AndGate(2, 1);
        Lamp lamp = new Lamp();
        assertNotNull(and);
        assertNotNull(lamp);
        for (Port p : and.getInPorts()) {
            p.setState(true, null);
        }
        Connection conn = new Connection(and.getOutPorts().get(0), lamp.getInPorts().get(0));
        assertNotNull(conn);
        // before the first tick, every outport has to be false
        for (Port p : and.getOutPorts()) {
            assertFalse(p.getState());
        }
        // simulate ticking clock
        and.clockTicked(null);
        for (Port p : and.getOutPorts()) {
            System.out.println("AND: " + p.getState());
            assertTrue(p.getState());
        }
        // before 1st calculation
        assertFalse(lamp.getState());
        lamp.clockTicked(null);
        // after 1st tick
        assertTrue(lamp.getState());
    }
}

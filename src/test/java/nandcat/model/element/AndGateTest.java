package nandcat.model.element;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for ANDGate.
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
     * Test: TRUE,TRUE->AND->LAMP.
     */
    public void testApp() {
        AndGate and = new AndGate(2, 1);
        Lamp lamp = new Lamp();
        for (Port p : and.getInPorts()) {
            p.setState(true, null);
        }
        Connection conn = new Connection(and.getOutPorts().get(0), lamp.getInPorts().get(0));
        assertNotNull(conn);
        // before the first tick, every outport has to be false
        for (Port p : and.getOutPorts()) {
            assertFalse(p.getState());
        }
        assertFalse(lamp.getState());
        assertFalse(conn.getState());
        // true, true liegt an, false geht raus
        // nächster tick setzt ausgang und leitung auf true
        // eingang lampe true, lampe selber derpt false
        and.clockTicked(null);
        conn.clockTicked(null);
        assertTrue(and.getOutPorts().get(0).getState());
        assertTrue(conn.getState());
        assertTrue(lamp.getInPorts().get(0).getState());
        assertFalse(lamp.getState());
        // nächster tick setzt lampe auf true, sonst ändert si nix
        lamp.clockTicked(null);
        assertTrue(and.getOutPorts().get(0).getState());
        assertTrue(conn.getState());
        assertTrue(lamp.getInPorts().get(0).getState());
        assertTrue(lamp.getState());

        // FALSE,TRUE->AND
        and.getInPorts().get(0).setState(false, null);
        and.clockTicked(null);
        assertFalse(and.getOutPorts().get(0).getState());

        // check in/outBoundaries
        assertFalse(and.isValidInBoundary(0));
        assertFalse(and.isValidOutBoundary(0));
        assertTrue(and.isValidInBoundary(3));
        assertTrue(and.isValidOutBoundary(3));

    }
}

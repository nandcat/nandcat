package nandcat.model.element;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for OrGate.
 */
public class OrGateTest extends TestCase {

    /**
     * Create the test case.
     * 
     * @param testName
     *            name of the test case
     */
    public OrGateTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(OrGateTest.class);
    }

    /**
     * Test: FALSE,TRUE->OR->LAMP.
     */
    public void testOr() {
        OrGate or = new OrGate(2, 1);
        // check in/outBoundaries
        assertFalse(or.isValidInBoundary(-1));
        assertFalse(or.isValidOutBoundary(-1));

        or = new OrGate();
        Lamp lamp = new Lamp();
        or.getInPorts().get(1).setState(true, null);
        Connection conn = new Connection(or.getOutPorts().get(0), lamp.getInPorts().get(0));
        assertNotNull(conn);
        // before the first tick, every outport has to be false
        for (Port p : or.getOutPorts()) {
            assertFalse(p.getState());
        }
        assertFalse(lamp.getState());
        assertFalse(conn.getState());
        // true, false liegt an, false geht raus
        // nächster tick setzt ausgang und leitung auf true
        // eingang lampe true, lampe selber derpt jetzt wieder true
        or.clockTicked(null);
        conn.clockTicked(null);
        assertTrue(or.getOutPorts().get(0).getState());
        assertTrue(conn.getState());
        assertTrue(lamp.getInPorts().get(0).getState());
        assertTrue(lamp.getState());
        // nächster tick setzt lampe auf true, sonst ändert si nix
        lamp.clockTicked(null);
        assertTrue(or.getOutPorts().get(0).getState());
        assertTrue(conn.getState());
        assertTrue(lamp.getInPorts().get(0).getState());
        assertTrue(lamp.getState());
        // set OR-input back to false,false
        or.getInPorts().get(0).setState(false, null);
        or.getInPorts().get(1).setState(false, null);
        or.clockTicked(null);
        conn.clockTicked(null);
        assertFalse(or.getOutPorts().get(0).getState());
        assertFalse(conn.getState());
        assertFalse(lamp.getInPorts().get(0).getState());
        assertFalse(lamp.getState());
        lamp.clockTicked(null);
        // der Letzte macht das Licht aus
        assertFalse(lamp.getState());
    }
}

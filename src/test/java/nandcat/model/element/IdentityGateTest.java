package nandcat.model.element;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class IdentityGateTest {

    /**
     * Test default constructor.
     */
    @Test
    public void testDefaultGate() {
        IdentityGate iGate = new IdentityGate(2);
        assertFalse(iGate.isValidInBoundary(0));
        assertTrue(iGate.isValidInBoundary(1));
        assertFalse(iGate.isValidOutBoundary(0));
        assertTrue(iGate.isValidOutBoundary(2 * 2));
        checkConnectedIgate(iGate);
    }

    /**
     * Test advanced constructor.
     */
    @Test
    public void testAdvancedGate() {
        IdentityGate iGate = new IdentityGate(2);
        checkConnectedIgate(iGate);
    }

    /**
     * Test: IMPY -> ID -> LAMP1, LAMP2.
     * 
     * @param iGate
     *            gate to test
     */
    public void checkConnectedIgate(IdentityGate iGate) {
        Lamp lamp1 = new Lamp();
        Lamp lamp2 = new Lamp();
        Connection conn1 = new Connection(iGate.getOutPorts().get(0), lamp1.getInPorts().get(0));
        Connection conn2 = new Connection(iGate.getOutPorts().get(1), lamp2.getInPorts().get(0));
        for (Port in : iGate.getInPorts()) {
            in.setState(true, null);
        }
        // initially, every inport/outport/connection has to be false
        for (Port out : iGate.getOutPorts()) {
            assertFalse(out.getState());
        }
        assertFalse(conn1.getState());
        assertFalse(conn2.getState());
        // iGate-input setzen: TRUE
        iGate.getInPorts().get(0).setState(true, null);
        // TRUE propagiert sich ueber iGate, conn{1,2} bis zu den 1. inPorts von lamp{1,2}
        iGate.clockTicked(null);
        conn1.clockTicked(null);
        conn2.clockTicked(null);
        assertTrue(iGate.getOutPorts().get(0).getState());
        assertTrue(conn1.getState());
        assertTrue(conn2.getState());
        assertTrue(lamp1.getInPorts().get(0).getState());
        assertTrue(lamp2.getInPorts().get(0).getState());
        // Lampen noch aus, noch nicht berechnet
        assertTrue(lamp1.getState());
        assertTrue(lamp2.getState());
        // Lamp{1,2} aufwecken
        lamp1.clockTicked(null);
        lamp2.clockTicked(null);
        // Lampen an
        assertTrue(lamp1.getState());
        assertTrue(lamp2.getState());
    }
}

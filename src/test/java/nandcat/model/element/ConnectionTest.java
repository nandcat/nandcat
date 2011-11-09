package nandcat.model.element;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for Connection.
 */
public class ConnectionTest extends TestCase {

    /**
     * Create the test case.
     * 
     * @param testName
     *            name of the test case
     */
    public ConnectionTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(ConnectionTest.class);
    }

    /**
     * Test: Impy->Lamp.
     */
    @SuppressWarnings("unused")
    public void testConnection() {
        ImpulseGenerator impy = new ImpulseGenerator(0);
        Lamp lamp = new Lamp();
        Connection c = new Connection(impy.getOutPorts().get(0), lamp.getInPorts().get(0));
        assertNotNull(c);

        // test selection
        assertFalse(c.isSelected());
        c.setSelected(true);
        assertTrue(c.isSelected());
        c.setSelected(false);
        assertFalse(c.isSelected());

        // test connectivity-functionality
        assertEquals(impy, c.getPreviousModule());
        assertEquals(lamp, c.getNextModule());

        // default is: false
        assertEquals(false, c.getState());

        // test (failing) NULL initialisation
        boolean failed = false;
        try {
            Connection fail = new Connection(impy.getOutPorts().get(0), null);
        } catch (Exception e) {
            failed = true;
        }
        assertTrue(failed);
        failed = false;
        try {
            Connection fail = new Connection(null, impy.getOutPorts().get(0));
        } catch (Exception e) {
            failed = true;
        }
        assertTrue(failed);

        // Test signal propagation
        impy.toggleState();
        // just to be sure FIXME OUTPORT!
        assertTrue(impy.getState());

        impy.clockTicked(null);
        c.clockTicked(null);
        lamp.clockTicked(null);

        impy.clockTicked(null);
        // just to be sure
        // FIXME run assertion
        // assertFalse(impy.getState());

        c.clockTicked(null);
        lamp.clockTicked(null);
        assertFalse(lamp.getState());
    }
}

package nandcat.model.element;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for Gate.
 */
public class GateTest extends TestCase {

    /**
     * Create the test case.
     * 
     * @param testName
     *            name of the test case
     */
    public GateTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(GateTest.class);
    }

    /**
     * Test: abstract Gate and Gate -> Gate.
     */
    public void testGate() {
        String name = "AND1";
        Gate g1 = (Gate) new AndGate(2, 1);
        assertNull(g1.getName());
        g1.setName(name);
        assertEquals(name, g1.getName());
        assertNotNull(g1.getNextElements());
        assertEquals(0, g1.getNextElements().size());
        Gate g2 = (Gate) new AndGate(2, 1);
        @SuppressWarnings("unused")
        Connection conn = new Connection(g1.getOutPorts().get(0), g2.getInPorts().get(0));
        assertEquals(1, g1.getNextElements().size());
        assertTrue(g1.getNextElements().contains(g2));
        try {
            @SuppressWarnings("unused")
            Gate bogusAnd = new AndGate(2, -1);
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
    }
}

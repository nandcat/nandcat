package nandcat.model.element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Unit test for Gate.
 */
public class GateTest {

    /**
     * Test: abstract Gate and Gate -> Gate.
     */
    @Test
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

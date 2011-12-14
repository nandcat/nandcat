package nandcat.model.element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Unit test for Connection.
 */
public class ConnectionTest {

    /**
     * Test: Impy->Lamp.
     */
    @Test
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
            new Connection(impy.getOutPorts().get(0), null);
        } catch (Exception e) {
            failed = true;
        }
        assertTrue(failed);
        failed = false;
        try {
            new Connection(null, impy.getOutPorts().get(0));
        } catch (Exception e) {
            failed = true;
        }
        assertTrue(failed);

        // Test signal propagation
        impy.toggleState();
        assertTrue(impy.getState());

        impy.clockTicked(null);
        c.clockTicked(null);
        lamp.clockTicked(null);

        impy.clockTicked(null);

        c.clockTicked(null);
        lamp.clockTicked(null);
        assertTrue(lamp.getState());
    }
}

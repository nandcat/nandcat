package nandcat.model;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Unit test for ANDGate.
 */
public class ClockTest {

    /**
     * Test checks for frequency and parent model.
     */
    @Test
    public void testApp() {
        Clock clk;
        boolean negativeArg = false;
        try {
            clk = new Clock(-1, new Model());
        } catch (IllegalArgumentException e) {
            negativeArg = true;
        }
        boolean nullArg = false;
        try {
            clk = new Clock(1, null);
        } catch (IllegalArgumentException e) {
            nullArg = true;
        }
        assertTrue(nullArg);
        assertTrue(negativeArg);
        clk = new Clock(1, new Model());
        clk.addListener(null);
    }
}

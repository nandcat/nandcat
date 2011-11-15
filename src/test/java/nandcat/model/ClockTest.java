package nandcat.model;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for ANDGate.
 */
public class ClockTest extends TestCase {

    /**
     * Create the test case.
     * 
     * @param testName
     *            name of the test case
     */
    public ClockTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(ClockTest.class);
    }

    /**
     * Test checks for frequency and parent model.
     */
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

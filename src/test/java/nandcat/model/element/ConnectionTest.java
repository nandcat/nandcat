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
     * Test: ->.
     */
    public void testConnection() {
        Connection conn = new Connection();
        assertNotNull(conn);
        assertNull(conn.getInPort());
        assertNull(conn.getOutPort());
    }
}

package nandcat.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.io.File;
import org.junit.Before;
import org.junit.Test;

public class ExtensionFileFilterTest {

    private File xmlTestFile;

    private File otherTestFile;

    private File directoryFile;

    @Before
    public void setUp() throws Exception {
        xmlTestFile = File.createTempFile("test", ".xml");
        otherTestFile = File.createTempFile("test", ".other");
        directoryFile = File.createTempFile("test", "xml");
        directoryFile.delete();
        directoryFile.mkdir();
    }

    @Test
    public void test() {
        ExtensionFileFilter filter = new ExtensionFileFilter("xml", "description");
        assertTrue(filter.accept(xmlTestFile));
        assertFalse(filter.accept(otherTestFile));
        assertTrue(filter.accept(directoryFile));
        assertEquals("xml", filter.getExtension());
        assertEquals("description", filter.getDescription());
    }

}

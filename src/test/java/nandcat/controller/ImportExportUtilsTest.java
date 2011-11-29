package nandcat.controller;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class ImportExportUtilsTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void test() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("xml", "description");
        JFileChooser chooser = Mockito.mock(JFileChooser.class);
        ImportExportUtils.addFileFilterToChooser(chooser, map);
        ArgumentCaptor<ExtensionFileFilter> capture1 = ArgumentCaptor.forClass(ExtensionFileFilter.class);

        Mockito.verify(chooser, Mockito.times(1)).addChoosableFileFilter(capture1.capture());
        assertEquals("description", capture1.getValue().getDescription());
        assertEquals("xml", capture1.getValue().getExtension());

        Mockito.verifyNoMoreInteractions(chooser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNull() {
        ImportExportUtils.addFileFilterToChooser(null, new LinkedHashMap<String, String>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullMap() {
        JFileChooser chooser = Mockito.mock(JFileChooser.class);
        ImportExportUtils.addFileFilterToChooser(chooser, null);
    }

    @Test
    public void testExtension() throws IOException {
        File file = File.createTempFile("pre", ".xml");
        file.deleteOnExit();
        assertEquals("xml", ImportExportUtils.getExtension(file));
    }
}

package nandcat.model.importexport.sepaf;

import java.io.File;
import java.io.IOException;
import nandcat.model.ModelElementDefaults;
import nandcat.model.element.Circuit;
import nandcat.model.element.factory.ModuleBuilderFactory;
import nandcat.model.importexport.Exporter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SEPAFExporterBasicTest {

    Exporter exporter;

    File file;

    private Circuit c;

    private ModuleBuilderFactory factory;

    @Before
    public void setup() throws IOException {
        file = File.createTempFile("export", ".xml");
        factory = new ModuleBuilderFactory();
        factory.setDefaults(new ModelElementDefaults());
        c = (Circuit) factory.getCircuitBuilder().build();
        exporter = new SEPAFExporter();
    }

    @After
    public void tearDown() throws Exception {
        file.delete();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExportWithoutFile() {
        exporter.setCircuit((Circuit) factory.getCircuitBuilder().build());
        exporter.exportCircuit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCircuit() {
        exporter.setCircuit(null);
        exporter.exportCircuit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExportWithoutCircuit() {
        exporter.setFile(file);
        exporter.exportCircuit();
    }
}

package nandcat.model.importexport.sepaf;

import static org.junit.Assert.assertTrue;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import nandcat.model.element.AndGate;
import nandcat.model.element.Circuit;
import nandcat.model.element.OrGate;
import nandcat.model.importexport.Exporter;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

public class SEPAFExporterConnectionTest {

    Exporter exporter;

    File file;

    private Circuit c;

    @Before
    public void setup() throws IOException {
        exporter = new SEPAFExporter();
        file = File.createTempFile("export", ".xml");
        exporter.setFile(file);
        c = new Circuit();
    }

    @Test
    public void testOneConnection() throws Exception {
        AndGate gate1 = new AndGate();
        OrGate gate2 = new OrGate();
        gate1.setRectangle(new Rectangle(10, 20, 30, 40));
        gate2.setRectangle(new Rectangle(50, 60, 70, 80));
        c.addModule(gate1);
        c.addModule(gate2);
        c.addConnection(gate1.getOutPorts().get(0), gate2.getInPorts().get(0));
        exporter.setCircuit(c);
        assertTrue(exporter.exportCircuit());
        String content = ImportExportUtil.getFileContent(file);
        System.out.println(content.replace(">", ">\n"));
        assertTrue(content.contains("targetPort=\"a\""));
        assertTrue(content.contains("sourcePort=\"o\""));
        assertTrue(content.contains("source=\"" + SEPAFFormat.getObjectAsUniqueString(gate1) + "\""));
        assertTrue(content.contains("target=\"" + SEPAFFormat.getObjectAsUniqueString(gate2) + "\""));
    }

    /**
     * Trust the model not to allow a reverse connection!
     * 
     * @throws Exception
     */
    @Ignore
    @Test
    public void testOneReverseConnection() throws Exception {
        AndGate gate1 = new AndGate();
        OrGate gate2 = new OrGate();
        gate1.setRectangle(new Rectangle(10, 20, 30, 40));
        gate2.setRectangle(new Rectangle(50, 60, 70, 80));
        c.addModule(gate1);
        c.addModule(gate2);
        c.addConnection(gate2.getInPorts().get(0), gate1.getOutPorts().get(0));
        exporter.setCircuit(c);
        assertTrue(exporter.exportCircuit());
        String content = ImportExportUtil.getFileContent(file);
        System.out.println(content.replace(">", ">\n"));
        assertTrue(content.contains("targetPort=\"a\""));
        assertTrue(content.contains("sourcePort=\"o\""));
        assertTrue(content.contains("source=\"" + SEPAFFormat.getObjectAsUniqueString(gate1) + "\""));
        assertTrue(content.contains("target=\"" + SEPAFFormat.getObjectAsUniqueString(gate2) + "\""));
    }

    @After
    public void validateOutput() throws FileNotFoundException, SAXException, IOException {
        ImportExportUtil.testValidOutput(file);
        file.delete();
    }
}

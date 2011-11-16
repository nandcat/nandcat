package nandcat.model.importexport.sepaf;

import static org.junit.Assert.assertTrue;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import nandcat.model.ModelElementDefaults;
import nandcat.model.element.AndGate;
import nandcat.model.element.Circuit;
import nandcat.model.element.FlipFlop;
import nandcat.model.element.OrGate;
import nandcat.model.element.factory.ModuleBuilderFactory;
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

    private ModuleBuilderFactory factory;

    @Before
    public void setup() throws IOException {
        exporter = new SEPAFExporter();
        file = File.createTempFile("export", ".xml");
        exporter.setFile(file);
        factory = new ModuleBuilderFactory();
        factory.setDefaults(new ModelElementDefaults());
        c = (Circuit) factory.getCircuitBuilder().build();
    }

    @Test
    public void testOneConnection() throws Exception {
        AndGate gate1 = (AndGate) factory.getAndGateBuilder().build();
        OrGate gate2 = (OrGate) factory.getOrGateBuilder().build();
        gate1.setRectangle(new Rectangle(10, 20, 30, 40));
        gate2.setRectangle(new Rectangle(50, 60, 70, 80));
        c.addModule(gate1);
        c.addModule(gate2);
        c.addConnection(gate1.getOutPorts().get(0), gate2.getInPorts().get(0));
        exporter.setCircuit(c);
        assertTrue(exporter.exportCircuit());
        String content = ImportExportUtil.getFileContent(file);
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
        AndGate gate1 = (AndGate) factory.getAndGateBuilder().build();
        OrGate gate2 = (OrGate) factory.getOrGateBuilder().build();
        gate1.setRectangle(new Rectangle(10, 20, 30, 40));
        gate2.setRectangle(new Rectangle(50, 60, 70, 80));
        c.addModule(gate1);
        c.addModule(gate2);
        c.addConnection(gate2.getInPorts().get(0), gate1.getOutPorts().get(0));
        exporter.setCircuit(c);
        assertTrue(exporter.exportCircuit());
        String content = ImportExportUtil.getFileContent(file);
        assertTrue(content.contains("targetPort=\"a\""));
        assertTrue(content.contains("sourcePort=\"o\""));
        assertTrue(content.contains("source=\"" + SEPAFFormat.getObjectAsUniqueString(gate1) + "\""));
        assertTrue(content.contains("target=\"" + SEPAFFormat.getObjectAsUniqueString(gate2) + "\""));
    }

    /**
     * Connection to circuits (e.g. flipflops) point to gates inside the circuit not outside.
     * 
     * @throws IOException
     */
    @Test
    public void testDeepConnectionProblem() throws IOException {
        AndGate gate1 = (AndGate) factory.getAndGateBuilder().setLocation(new Point(10, 20)).build();
        FlipFlop ff = (FlipFlop) factory.getFlipFlopBuilder().setLocation(new Point(30, 40)).build();
        c.addModule(gate1);
        c.addModule(ff);
        c.addConnection(gate1.getOutPorts().get(0), ff.getInPorts().get(0));
        exporter.setCircuit(c);
        assertTrue(exporter.exportCircuit());
        String content = ImportExportUtil.getFileContent(file);
        System.out.println(content.replace(">", ">\n"));
        assertTrue(content.contains("targetPort=\"a\""));
        assertTrue(content.contains("sourcePort=\"o\""));
        assertTrue(content.contains("source=\"" + SEPAFFormat.getObjectAsUniqueString(gate1) + "\""));
        assertTrue(content.contains("target=\"" + SEPAFFormat.getObjectAsUniqueString(ff) + "\""));
    }

    @After
    public void validateOutput() throws FileNotFoundException, SAXException, IOException {
        ImportExportUtil.testValidOutput(file);
        file.delete();
    }
}

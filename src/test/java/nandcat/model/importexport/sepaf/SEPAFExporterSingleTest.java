package nandcat.model.importexport.sepaf;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import nandcat.model.element.AndGate;
import nandcat.model.element.Circuit;
import nandcat.model.element.FlipFlop;
import nandcat.model.element.IdentityGate;
import nandcat.model.element.ImpulseGenerator;
import nandcat.model.element.Lamp;
import nandcat.model.element.NotGate;
import nandcat.model.element.OrGate;
import nandcat.model.importexport.Exporter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class SEPAFExporterSingleTest {

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
    public void testDefaultAndGate() throws Exception {
        AndGate gate = new AndGate();
        gate.setName("Annotation");
        gate.setRectangle(new Rectangle(5, 10, 80, 90));
        c.addModule(gate);
        exporter.setCircuit(c);
        assertTrue(exporter.exportCircuit());
        String content = ImportExportUtil.getFileContent(file);
        assertTrue(content.contains("nandcat:annotation=\"Annotation\""));
        assertTrue(content.contains("posx=\"5\""));
        assertTrue(content.contains("posy=\"10\""));
        assertTrue(content.contains("name=\""));
        assertTrue(content.contains("type=\"and\""));
        assertFalse(content.contains("nandcat:ports_in"));
        assertFalse(content.contains("nandcat:ports_out"));
    }

    @Test
    public void testMultiAndGate() throws Exception {
        AndGate gate = new AndGate(3, 5);
        gate.setName("Annotation");
        gate.setRectangle(new Rectangle(5, 10, 80, 90));
        c.addModule(gate);
        exporter.setCircuit(c);
        assertTrue(exporter.exportCircuit());
        String content = ImportExportUtil.getFileContent(file);
        assertTrue(content.contains("nandcat:annotation=\"Annotation\""));
        assertTrue(content.contains("posx=\"5\""));
        assertTrue(content.contains("posy=\"10\""));
        assertTrue(content.contains("name=\""));
        assertTrue(content.contains("type=\"and\""));
        assertTrue(content.contains("nandcat:ports_in=\"3\""));
        assertTrue(content.contains("nandcat:ports_out=\"5\""));
    }

    @Test
    public void testDefaultOrGate() throws Exception {
        OrGate gate = new OrGate();
        gate.setName("Annotation");
        gate.setRectangle(new Rectangle(5, 10, 80, 90));
        c.addModule(gate);
        exporter.setCircuit(c);
        assertTrue(exporter.exportCircuit());
        String content = ImportExportUtil.getFileContent(file);
        assertTrue(content.contains("nandcat:annotation=\"Annotation\""));
        assertTrue(content.contains("posx=\"5\""));
        assertTrue(content.contains("posy=\"10\""));
        assertTrue(content.contains("name=\""));
        assertTrue(content.contains("type=\"or\""));
        assertFalse(content.contains("nandcat:ports_in"));
        assertFalse(content.contains("nandcat:ports_out"));
    }

    @Test
    public void testMultiOrGate() throws Exception {
        OrGate gate = new OrGate(3, 5);
        gate.setName("Annotation");
        gate.setRectangle(new Rectangle(5, 10, 80, 90));
        c.addModule(gate);
        exporter.setCircuit(c);
        assertTrue(exporter.exportCircuit());
        String content = ImportExportUtil.getFileContent(file);
        assertTrue(content.contains("nandcat:annotation=\"Annotation\""));
        assertTrue(content.contains("posx=\"5\""));
        assertTrue(content.contains("posy=\"10\""));
        assertTrue(content.contains("name=\""));
        assertTrue(content.contains("type=\"or\""));
        assertTrue(content.contains("nandcat:ports_in=\"3\""));
        assertTrue(content.contains("nandcat:ports_out=\"5\""));
    }

    @Test
    public void testDefaultNotGate() throws Exception {
        NotGate gate = new NotGate();
        gate.setName("Annotation");
        gate.setRectangle(new Rectangle(5, 10, 80, 90));
        c.addModule(gate);
        exporter.setCircuit(c);
        assertTrue(exporter.exportCircuit());
        String content = ImportExportUtil.getFileContent(file);
        assertTrue(content.contains("nandcat:annotation=\"Annotation\""));
        assertTrue(content.contains("posx=\"5\""));
        assertTrue(content.contains("posy=\"10\""));
        assertTrue(content.contains("name=\""));
        assertTrue(content.contains("type=\"not\""));
        assertFalse(content.contains("nandcat:ports_in"));
        assertFalse(content.contains("nandcat:ports_out"));
    }

    @Test
    public void testMultiNotGate() throws Exception {
        NotGate gate = new NotGate(5);
        gate.setName("Annotation");
        gate.setRectangle(new Rectangle(5, 10, 80, 90));
        c.addModule(gate);
        exporter.setCircuit(c);
        assertTrue(exporter.exportCircuit());
        String content = ImportExportUtil.getFileContent(file);
        assertTrue(content.contains("nandcat:annotation=\"Annotation\""));
        assertTrue(content.contains("posx=\"5\""));
        assertTrue(content.contains("posy=\"10\""));
        assertTrue(content.contains("name=\""));
        assertTrue(content.contains("type=\"not\""));
        assertFalse(content.contains("nandcat:ports_in"));
        assertTrue(content.contains("nandcat:ports_out=\"5\""));
    }

    @Test
    public void testDefaultIdentityGate() throws Exception {
        IdentityGate gate = new IdentityGate();
        gate.setName("Annotation");
        gate.setRectangle(new Rectangle(5, 10, 80, 90));
        c.addModule(gate);
        exporter.setCircuit(c);
        assertTrue(exporter.exportCircuit());
        String content = ImportExportUtil.getFileContent(file);
        assertTrue(content.contains("nandcat:annotation=\"Annotation\""));
        assertTrue(content.contains("posx=\"5\""));
        assertTrue(content.contains("posy=\"10\""));
        assertTrue(content.contains("name=\""));
        assertTrue(content.contains("type=\"id\""));
        assertFalse(content.contains("nandcat:ports_in"));
        assertFalse(content.contains("nandcat:ports_out"));
    }

    @Test
    public void testMultiIdentityGate() throws Exception {
        IdentityGate gate = new IdentityGate(1, 5);
        gate.setName("Annotation");
        gate.setRectangle(new Rectangle(5, 10, 80, 90));
        c.addModule(gate);
        exporter.setCircuit(c);
        assertTrue(exporter.exportCircuit());
        String content = ImportExportUtil.getFileContent(file);
        assertTrue(content.contains("nandcat:annotation=\"Annotation\""));
        assertTrue(content.contains("posx=\"5\""));
        assertTrue(content.contains("posy=\"10\""));
        assertTrue(content.contains("name=\""));
        assertTrue(content.contains("type=\"id\""));
        assertFalse(content.contains("nandcat:ports_in"));
        assertTrue(content.contains("nandcat:ports_out=\"5\""));
    }

    @Test
    public void testDefaultLamp() throws Exception {
        Lamp gate = new Lamp();
        gate.setName("Annotation");
        gate.setRectangle(new Rectangle(5, 10, 80, 90));
        c.addModule(gate);
        exporter.setCircuit(c);
        assertTrue(exporter.exportCircuit());
        String content = ImportExportUtil.getFileContent(file);
        assertTrue(content.contains("nandcat:annotation=\"Annotation\""));
        assertTrue(content.contains("posx=\"5\""));
        assertTrue(content.contains("posy=\"10\""));
        assertTrue(content.contains("name=\""));
        assertTrue(content.contains("type=\"out\""));
        assertFalse(content.contains("nandcat:ports_in"));
        assertFalse(content.contains("nandcat:ports_out"));
        assertFalse(content.contains("nandcat:in_state"));
    }

    @Test
    public void testDefaultFlipFlop() throws Exception {
        FlipFlop gate = new FlipFlop();
        gate.setName("Annotation");
        gate.setRectangle(new Rectangle(5, 10, 80, 90));
        c.addModule(gate);
        exporter.setCircuit(c);
        assertTrue(exporter.exportCircuit());
        String content = ImportExportUtil.getFileContent(file);
        assertTrue(content.contains("nandcat:annotation=\"Annotation\""));
        assertTrue(content.contains("posx=\"5\""));
        assertTrue(content.contains("posy=\"10\""));
        assertTrue(content.contains("name=\""));
        assertTrue(content.contains("type=\"flipflop\""));
        assertFalse(content.contains("nandcat:ports_in"));
        assertFalse(content.contains("nandcat:ports_out"));
        assertFalse(content.contains("nandcat:in_state"));
    }

    @Test
    public void testDefaultClock() throws Exception {
        ImpulseGenerator gate = new ImpulseGenerator(10);
        gate.setName("Annotation");
        gate.setRectangle(new Rectangle(5, 10, 80, 90));
        c.addModule(gate);
        exporter.setCircuit(c);
        assertTrue(exporter.exportCircuit());
        String content = ImportExportUtil.getFileContent(file);
        System.out.println(content.replace(">", ">\n"));
        assertTrue(content.contains("nandcat:annotation=\"Annotation\""));
        assertTrue(content.contains("posx=\"5\""));
        assertTrue(content.contains("posy=\"10\""));
        assertTrue(content.contains("name=\""));
        assertTrue(content.contains("type=\"clock\""));
        assertTrue(content.contains("nandcat:in_timing=\"10\""));
        assertFalse(content.contains("nandcat:ports_in"));
        assertFalse(content.contains("nandcat:ports_out"));
        assertFalse(content.contains("nandcat:in_state"));
    }

    @Test
    public void testDefaultSwitch() throws Exception {
        ImpulseGenerator gate = new ImpulseGenerator(0);
        gate.setName("Annotation");
        gate.setRectangle(new Rectangle(5, 10, 80, 90));
        c.addModule(gate);
        exporter.setCircuit(c);
        assertTrue(exporter.exportCircuit());
        String content = ImportExportUtil.getFileContent(file);
        assertTrue(content.contains("nandcat:annotation=\"Annotation\""));
        assertTrue(content.contains("posx=\"5\""));
        assertTrue(content.contains("posy=\"10\""));
        assertTrue(content.contains("name=\""));
        assertTrue(content.contains("type=\"in\""));
        assertFalse(content.contains("nandcat:in_timing"));
        assertFalse(content.contains("nandcat:ports_in"));
        assertFalse(content.contains("nandcat:ports_out"));
        assertFalse(content.contains("nandcat:in_state"));
    }

    @Test
    public void testDefaultCircuit() throws Exception {
        Circuit circuit = new Circuit();
        AndGate gate = new AndGate();
        gate.setRectangle(new Rectangle(10, 20, 30, 40));
        circuit.addModule(gate);
        circuit.setName("Annotation");
        circuit.setRectangle(new Rectangle(5, 10, 80, 90));
        c.addModule(circuit);
        exporter.setCircuit(c);
        assertTrue(exporter.exportCircuit());
        String content = ImportExportUtil.getFileContent(file);
        System.out.println(content.replace(">", ">\n"));
        assertTrue(content.contains("nandcat:annotation=\"Annotation\""));
        assertTrue(content.contains("posx=\"5\""));
        assertTrue(content.contains("posy=\"10\""));
        assertTrue(content.contains("name=\""));
        assertTrue(content.contains("type=\"circuit\""));
        assertFalse(content.contains("nandcat:in_timing"));
        assertFalse(content.contains("nandcat:ports_in"));
        assertFalse(content.contains("nandcat:ports_out"));
        assertFalse(content.contains("nandcat:in_state"));

        // subcircuit
        assertTrue(content.contains("circuit name=\"" + circuit.getUuid() + "\""));
        assertTrue(content.contains("posx=\"10\""));
        assertTrue(content.contains("posy=\"20\""));
        assertTrue(content.contains("type=\"and\""));
    }

    @Test
    public void testMissingCircuit() throws Exception {
        Circuit circuit = new Circuit();
        AndGate gate = new AndGate();
        gate.setRectangle(new Rectangle(10, 20, 30, 40));
        circuit.addModule(gate);
        circuit.setName("Annotation");
        circuit.setRectangle(new Rectangle(5, 10, 80, 90));
        c.addModule(circuit);
        exporter.setCircuit(c);
        Map<String, String> externalCircuits = new HashMap<String, String>();
        externalCircuits.put(circuit.getUuid(), "myMissingCircuit");
        exporter.setExternalCircuits(externalCircuits);
        assertTrue(exporter.exportCircuit());
        String content = ImportExportUtil.getFileContent(file);
        System.out.println(content.replace(">", ">\n"));
        assertTrue(content.contains("nandcat:annotation=\"Annotation\""));
        assertTrue(content.contains("posx=\"5\""));
        assertTrue(content.contains("posy=\"10\""));
        assertTrue(content.contains("name=\""));
        assertTrue(content.contains("type=\"missing-circuit\""));
        assertTrue(content.contains("type2=\"myMissingCircuit\""));
        assertFalse(content.contains("nandcat:in_timing"));
        assertFalse(content.contains("nandcat:ports_in"));
        assertFalse(content.contains("nandcat:ports_out"));
        assertFalse(content.contains("nandcat:in_state"));

        // subcircuit
        assertFalse(content.contains("circuit name=\"" + circuit.getUuid() + "\""));
        assertFalse(content.contains("circuit name=\"myMissingCircuit\""));
        assertFalse(content.contains("posx=\"10\""));
        assertFalse(content.contains("posy=\"20\""));
        assertFalse(content.contains("type=\"and\""));
    }

    @After
    public void validateOutput() throws FileNotFoundException, SAXException, IOException {
        ImportExportUtil.testValidOutput(file);
        file.delete();
    }

}

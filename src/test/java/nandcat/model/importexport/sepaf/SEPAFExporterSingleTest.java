package nandcat.model.importexport.sepaf;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import nandcat.model.ModelElementDefaults;
import nandcat.model.element.Circuit;
import nandcat.model.element.factory.ModuleBuilder;
import nandcat.model.element.factory.ModuleBuilderFactory;
import nandcat.model.importexport.Exporter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class SEPAFExporterSingleTest {

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
    public void testDefaultAndGate() throws Exception {
        ModuleBuilder b = factory.getAndGateBuilder();
        b.setAnnotation("Annotation");
        b.setLocation(new Point(5, 10));
        c.addModule(b.build());
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
        ModuleBuilder b = factory.getAndGateBuilder();
        b.setInPorts(3);
        b.setOutPorts(5);
        b.setAnnotation("Annotation");
        b.setLocation(new Point(5, 10));
        c.addModule(b.build());
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
    public void testMultiAndGateDefaultInPorts() throws Exception {
        ModuleBuilder b = factory.getAndGateBuilder();
        b.setInPorts(SEPAFFormat.GATEDEFAULTS.DEFAULT_INPORTS_AND);
        b.setOutPorts(SEPAFFormat.GATEDEFAULTS.DEFAULT_OUTPORTS_AND + 2);
        b.setAnnotation("Annotation");
        b.setLocation(new Point(5, 10));
        c.addModule(b.build());
        exporter.setCircuit(c);
        assertTrue(exporter.exportCircuit());
        String content = ImportExportUtil.getFileContent(file);
        assertTrue(content.contains("nandcat:annotation=\"Annotation\""));
        assertTrue(content.contains("posx=\"5\""));
        assertTrue(content.contains("posy=\"10\""));
        assertTrue(content.contains("name=\""));
        assertTrue(content.contains("type=\"and\""));
        assertFalse(content.contains("nandcat:ports_in"));
        assertTrue(content
                .contains("nandcat:ports_out=\"" + (SEPAFFormat.GATEDEFAULTS.DEFAULT_OUTPORTS_AND + 2) + "\""));
    }

    @Test
    public void testMultiAndGateDefaultOutPorts() throws Exception {
        ModuleBuilder b = factory.getAndGateBuilder();
        b.setInPorts(SEPAFFormat.GATEDEFAULTS.DEFAULT_INPORTS_AND + 2);
        b.setOutPorts(SEPAFFormat.GATEDEFAULTS.DEFAULT_OUTPORTS_AND);
        b.setAnnotation("Annotation");
        b.setLocation(new Point(5, 10));
        c.addModule(b.build());
        exporter.setCircuit(c);
        assertTrue(exporter.exportCircuit());
        String content = ImportExportUtil.getFileContent(file);
        assertTrue(content.contains("nandcat:annotation=\"Annotation\""));
        assertTrue(content.contains("posx=\"5\""));
        assertTrue(content.contains("posy=\"10\""));
        assertTrue(content.contains("name=\""));
        assertTrue(content.contains("type=\"and\""));
        assertTrue(content.contains("nandcat:ports_in=\"" + (SEPAFFormat.GATEDEFAULTS.DEFAULT_INPORTS_AND + 2) + "\""));
        assertFalse(content.contains("nandcat:ports_out"));
    }

    @Test
    public void testDefaultOrGate() throws Exception {
        ModuleBuilder b = factory.getOrGateBuilder();
        b.setAnnotation("Annotation");
        b.setLocation(new Point(5, 10));
        c.addModule(b.build());
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
        ModuleBuilder b = factory.getOrGateBuilder();
        b.setInPorts(3);
        b.setOutPorts(5);
        b.setAnnotation("Annotation");
        b.setLocation(new Point(5, 10));
        c.addModule(b.build());
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
    public void testMultiOrGateDefaultOutPorts() throws Exception {
        ModuleBuilder b = factory.getOrGateBuilder();
        b.setInPorts(SEPAFFormat.GATEDEFAULTS.DEFAULT_INPORTS_OR + 2);
        b.setOutPorts(SEPAFFormat.GATEDEFAULTS.DEFAULT_OUTPORTS_OR);
        b.setAnnotation("Annotation");
        b.setLocation(new Point(5, 10));
        c.addModule(b.build());
        exporter.setCircuit(c);
        assertTrue(exporter.exportCircuit());
        String content = ImportExportUtil.getFileContent(file);
        assertTrue(content.contains("nandcat:annotation=\"Annotation\""));
        assertTrue(content.contains("posx=\"5\""));
        assertTrue(content.contains("posy=\"10\""));
        assertTrue(content.contains("name=\""));
        assertTrue(content.contains("type=\"or\""));
        assertTrue(content.contains("nandcat:ports_in=\"" + (SEPAFFormat.GATEDEFAULTS.DEFAULT_INPORTS_OR + 2) + "\""));
        assertFalse(content.contains("nandcat:ports_out"));
    }

    @Test
    public void testMultiOrGateDefaultInPorts() throws Exception {
        ModuleBuilder b = factory.getOrGateBuilder();
        b.setInPorts(SEPAFFormat.GATEDEFAULTS.DEFAULT_INPORTS_OR);
        b.setOutPorts(SEPAFFormat.GATEDEFAULTS.DEFAULT_OUTPORTS_OR + 2);
        b.setAnnotation("Annotation");
        b.setLocation(new Point(5, 10));
        c.addModule(b.build());
        exporter.setCircuit(c);
        assertTrue(exporter.exportCircuit());
        String content = ImportExportUtil.getFileContent(file);
        assertTrue(content.contains("nandcat:annotation=\"Annotation\""));
        assertTrue(content.contains("posx=\"5\""));
        assertTrue(content.contains("posy=\"10\""));
        assertTrue(content.contains("name=\""));
        assertTrue(content.contains("type=\"or\""));
        assertFalse(content.contains("nandcat:ports_in"));
        assertTrue(content.contains("nandcat:ports_out=\"" + (SEPAFFormat.GATEDEFAULTS.DEFAULT_OUTPORTS_OR + 2) + "\""));
    }

    @Test
    public void testDefaultNotGate() throws Exception {
        ModuleBuilder b = factory.getNotGateBuilder();
        b.setAnnotation("Annotation");
        b.setLocation(new Point(5, 10));
        c.addModule(b.build());
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
        ModuleBuilder b = factory.getNotGateBuilder();
        b.setOutPorts(5);
        b.setAnnotation("Annotation");
        b.setLocation(new Point(5, 10));
        c.addModule(b.build());
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
    public void testMultiNotGateDefaultOutPorts() throws Exception {
        ModuleBuilder b = factory.getNotGateBuilder();
        b.setOutPorts(SEPAFFormat.GATEDEFAULTS.DEFAULT_OUTPORTS_NOT);
        b.setAnnotation("Annotation");
        b.setLocation(new Point(5, 10));
        c.addModule(b.build());
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
    public void testDefaultIdentityGate() throws Exception {
        ModuleBuilder b = factory.getIdentityGateBuilder();
        b.setAnnotation("Annotation");
        b.setLocation(new Point(5, 10));
        c.addModule(b.build());
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
        ModuleBuilder b = factory.getIdentityGateBuilder();
        b.setOutPorts(5);
        b.setAnnotation("Annotation");
        b.setLocation(new Point(5, 10));
        c.addModule(b.build());
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
    public void testMultiIdentityGateDefaultOutPorts() throws Exception {
        ModuleBuilder b = factory.getIdentityGateBuilder();
        b.setOutPorts(SEPAFFormat.GATEDEFAULTS.DEFAULT_OUTPORTS_IDENTITY);
        b.setAnnotation("Annotation");
        b.setLocation(new Point(5, 10));
        c.addModule(b.build());
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
    public void testDefaultLamp() throws Exception {
        ModuleBuilder b = factory.getLampBuilder();
        b.setAnnotation("Annotation");
        b.setLocation(new Point(5, 10));
        c.addModule(b.build());
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
        ModuleBuilder b = factory.getFlipFlopBuilder();
        b.setAnnotation("Annotation");
        b.setLocation(new Point(5, 10));
        c.addModule(b.build());
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
        ModuleBuilder b = factory.getClockBuilder();
        b.setFrequency(10);
        b.setAnnotation("Annotation");
        b.setLocation(new Point(5, 10));
        c.addModule(b.build());
        exporter.setCircuit(c);
        assertTrue(exporter.exportCircuit());
        String content = ImportExportUtil.getFileContent(file);
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
        ModuleBuilder b = factory.getSwitchBuilder();
        b.setAnnotation("Annotation");
        b.setLocation(new Point(5, 10));
        c.addModule(b.build());
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
        ModuleBuilder CircuitB = factory.getCircuitBuilder();
        CircuitB.setAnnotation("Annotation");
        CircuitB.setLocation(new Point(5, 10));

        ModuleBuilder AndB = factory.getAndGateBuilder();
        AndB.setAnnotation("Annotation");
        AndB.setLocation(new Point(10, 20));

        Circuit circuit = (Circuit) CircuitB.build();
        circuit.addModule(AndB.build());
        c.addModule(circuit);
        exporter.setCircuit(c);
        assertTrue(exporter.exportCircuit());
        String content = ImportExportUtil.getFileContent(file);
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
        ModuleBuilder CircuitB = factory.getCircuitBuilder();
        CircuitB.setAnnotation("Annotation");
        CircuitB.setLocation(new Point(5, 10));

        ModuleBuilder AndB = factory.getAndGateBuilder();
        AndB.setAnnotation("Annotation");
        AndB.setLocation(new Point(10, 20));

        Circuit circuit = (Circuit) CircuitB.build();
        circuit.addModule(AndB.build());
        c.addModule(circuit);
        exporter.setCircuit(c);
        Map<String, String> externalCircuits = new HashMap<String, String>();
        externalCircuits.put(circuit.getUuid(), "myMissingCircuit");
        exporter.setExternalCircuits(externalCircuits);
        assertTrue(exporter.exportCircuit());
        String content = ImportExportUtil.getFileContent(file);
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

package nandcat.model.importexport.sepaf;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import nandcat.model.ModelElementDefaults;
import nandcat.model.element.AndGate;
import nandcat.model.element.Circuit;
import nandcat.model.element.NotGate;
import nandcat.model.element.OrGate;
import nandcat.model.element.factory.ModuleBuilder;
import nandcat.model.element.factory.ModuleBuilderFactory;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class SEPAFExporterTest {

    private ModuleBuilderFactory factory;

    @Before
    public void setUp() {
        factory = new ModuleBuilderFactory();
        factory.setDefaults(new ModelElementDefaults());
    }

    @Test
    public void testStandardGatter() throws IOException, SAXException {
        SEPAFExporter export = new SEPAFExporter();
        File file = File.createTempFile("export", ".xml");
        export.setFile(file);
        Circuit c = (Circuit) factory.getCircuitBuilder().build();

        ModuleBuilder andB = factory.getAndGateBuilder();
        andB.setLocation(new Point(1, 1));
        andB.setAnnotation("AndGate");
        AndGate andGate = (AndGate) andB.build();
        c.addModule(andGate);

        ModuleBuilder orB = factory.getOrGateBuilder();
        orB.setLocation(new Point(2, 2));
        orB.setAnnotation("OrGate");
        OrGate orGate = (OrGate) orB.build();
        c.addModule(orGate);

        ModuleBuilder notB = factory.getNotGateBuilder();
        notB.setLocation(new Point(3, 3));
        notB.setAnnotation("NotGate");
        NotGate notGate = (NotGate) notB.build();
        c.addModule(notGate);

        c.addConnection(andGate.getOutPorts().get(0), orGate.getInPorts().get(0));

        export.setCircuit(c);
        assertTrue(export.exportCircuit());
        String content = ImportExportUtil.getFileContent(file);
        assertTrue(content.contains("type=\"and\""));
        assertTrue(content.contains("type=\"or\""));
        assertTrue(content.contains("type=\"not\""));
        assertTrue(content.contains("nandcat:annotation=\"NotGate\""));
        assertTrue(content.contains("nandcat:annotation=\"OrGate\""));
        assertTrue(content.contains("nandcat:annotation=\"AndGate\""));
        assertTrue(content.contains("<connection"));
        ImportExportUtil.testValidOutput(file);
    }

    private Circuit buildSimpleCircuit(String uuid, String prefix, Point p) {
        Circuit c = null;
        if (uuid == null) {
            c = (Circuit) factory.getCircuitBuilder().build();
        } else {
            c = (Circuit) factory.getCircuitBuilder().setUUID(uuid).build();
        }
        c.getRectangle().setLocation(p);

        // And gate
        ModuleBuilder andB = factory.getAndGateBuilder();
        andB.setLocation(new Point(0, 0));
        andB.setAnnotation(prefix + ":AndGate");
        c.addModule(andB.build());

        return c;
    }

    @Test
    public void testCircuits() throws Exception {
        SEPAFExporter export = new SEPAFExporter();
        File file = File.createTempFile("export", ".xml");
        export.setFile(file);
        Circuit c = (Circuit) factory.getCircuitBuilder().build();

        Circuit c1 = buildSimpleCircuit(null, "c1", new Point(1, 1));
        Circuit c2 = buildSimpleCircuit(null, "c2", new Point(1, 1));
        Circuit c3 = buildSimpleCircuit(null, "c3", new Point(1, 1));

        Circuit c4 = buildSimpleCircuit("un-iq-ue", "c4", new Point(2, 2));
        Circuit c5 = buildSimpleCircuit("un-iq-ue", "not-parsed!", new Point(3, 3));
        c2.addModule(c3);
        c1.addModule(c2);
        c.addModule(c1);
        c.addModule(c4);
        c.addModule(c5);
        export.setCircuit(c);
        assertTrue(export.exportCircuit());
        String content = ImportExportUtil.getFileContent(file);
        assertTrue(content.contains("nandcat:annotation=\"c1:AndGate\""));
        assertTrue(content.contains("nandcat:annotation=\"c2:AndGate\""));
        assertTrue(content.contains("nandcat:annotation=\"c3:AndGate\""));
        assertTrue(content.contains("nandcat:annotation=\"c4:AndGate\""));
        assertTrue(content.contains("type2=\"un-iq-ue\""));
        assertTrue(content.contains("name=\"un-iq-ue\""));

        // should not be written to xml because the circuit un-iq-ue is already defined by c4
        assertFalse(content.contains("no-parsed!"));
        ImportExportUtil.testValidOutput(file);
    }

}

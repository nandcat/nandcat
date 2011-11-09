package nandcat.model.importexport;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import nandcat.Nandcat;
import nandcat.model.element.AndGate;
import nandcat.model.element.Circuit;
import nandcat.model.element.NotGate;
import nandcat.model.element.OrGate;
import nandcat.model.importexport.sepaf.SEPAFExporter;
import org.junit.Test;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class SEPAFExporterTest {

    @Test
    public void testStandardGatter() throws IOException, SAXException {
        SEPAFExporter export = new SEPAFExporter();
        File file = File.createTempFile("export", ".xml");
        export.setFile(file);
        Circuit c = new Circuit();
        AndGate andGate = new AndGate();
        andGate.getRectangle().setLocation(new Point(1, 1));
        andGate.setName("AndGate");
        c.addModule(andGate);

        OrGate orGate = new OrGate();
        orGate.getRectangle().setLocation(new Point(2, 2));
        orGate.setName("OrGate");
        c.addModule(orGate);

        NotGate notGate = new NotGate();
        notGate.getRectangle().setLocation(new Point(3, 3));
        notGate.setName("NotGate");
        c.addModule(notGate);

        c.addConnection(andGate.getOutPorts().get(0), orGate.getInPorts().get(0));

        export.setCircuit(c);
        assertTrue(export.exportCircuit());
        String content = getFileContent(file);
        assertTrue(content.contains("type=\"and\""));
        assertTrue(content.contains("type=\"or\""));
        assertTrue(content.contains("type=\"not\""));
        assertTrue(content.contains("nandcat:annotation=\"NotGate\""));
        assertTrue(content.contains("nandcat:annotation=\"OrGate\""));
        assertTrue(content.contains("nandcat:annotation=\"AndGate\""));
        assertTrue(content.contains("<connection"));
        testValidOutput(file);
    }

    private Circuit buildSimpleCircuit(String uuid, String prefix, Point p) {
        Circuit c = null;
        if (uuid == null) {
            c = new Circuit();
        } else {
            c = new Circuit(uuid);
        }
        c.getRectangle().setLocation(p);

        // And gate
        AndGate andGate = new AndGate();
        andGate.getRectangle().setLocation(new Point(0, 0));
        andGate.setName(prefix + ":AndGate");
        c.addModule(andGate);

        return c;
    }

    @Test
    public void testCircuits() throws Exception {
        SEPAFExporter export = new SEPAFExporter();
        File file = File.createTempFile("export", ".xml");
        export.setFile(file);
        Circuit c = new Circuit();

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
        String content = getFileContent(file);
        assertTrue(content.contains("nandcat:annotation=\"c1:AndGate\""));
        assertTrue(content.contains("nandcat:annotation=\"c2:AndGate\""));
        assertTrue(content.contains("nandcat:annotation=\"c3:AndGate\""));
        assertTrue(content.contains("nandcat:annotation=\"c4:AndGate\""));
        assertTrue(content.contains("type2=\"un-iq-ue\""));
        assertTrue(content.contains("name=\"un-iq-ue\""));

        // should not be written to xml because the circuit un-iq-ue is already defined by c4
        assertFalse(content.contains("no-parsed!"));
        testValidOutput(file);
    }

    private String getFileContent(File file) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String strLine;
        StringBuilder content = new StringBuilder();
        while ((strLine = in.readLine()) != null) {
            content.append(strLine);
            content.append("\n");
        }
        // Close the input stream
        in.close();

        return content.toString();
    }

    private void testValidOutput(File file) throws FileNotFoundException, SAXException, IOException {
        Source[] xsdSources = new Source[] {
                new StreamSource(Nandcat.class.getResourceAsStream("../sepaf-extension.xsd")),
                new StreamSource(Nandcat.class.getResourceAsStream("../circuits-1.0.xsd")) };
        ErrorHandler throwingErrorHandler = new ErrorHandler() {

            public void warning(SAXParseException exception) throws SAXException {
                throw exception;
            }

            public void fatalError(SAXParseException exception) throws SAXException {
                throw exception;
            }

            public void error(SAXParseException exception) throws SAXException {
                throw exception;
            }
        };
        XsdValidation.validate(new StreamSource(new FileInputStream(file)), xsdSources, throwingErrorHandler);
    }
}

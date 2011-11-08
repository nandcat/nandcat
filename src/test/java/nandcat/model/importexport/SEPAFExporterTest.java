package nandcat.model.importexport;

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
        Circuit c = new Circuit(null);
        AndGate andGate = new AndGate();
        andGate.setLocation(new Point(1, 1));
        andGate.setName("AndGate");
        c.addModule(andGate);

        OrGate orGate = new OrGate();
        orGate.setLocation(new Point(2, 2));
        orGate.setName("OrGate");
        c.addModule(orGate);

        NotGate notGate = new NotGate();
        notGate.setLocation(new Point(3, 3));
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

    private Circuit buildSimpleCircuit(String prefix) {
        Circuit c = new Circuit(new Point(0, 0));

        // And gate
        AndGate andGate = new AndGate();
        andGate.setLocation(new Point(1, 1));
        andGate.setName(prefix + ":AndGate");
        c.addModule(andGate);

        return c;
    }

    @Test
    public void testCircuits() throws Exception {
        SEPAFExporter export = new SEPAFExporter();
        File file = File.createTempFile("export", ".xml");
        export.setFile(file);
        Circuit c = new Circuit(null);

        Circuit c1 = buildSimpleCircuit("c1");
        Circuit c2 = buildSimpleCircuit("c2");
        Circuit c3 = buildSimpleCircuit("c3");
        c2.addModule(c3);
        c1.addModule(c2);
        c.addModule(c1);

        export.setCircuit(c);
        assertTrue(export.exportCircuit());
        String content = getFileContent(file);
        System.out.println(file.getAbsolutePath());
        System.out.println(content);
        assertTrue(content.contains("nandcat:annotation=\"c1:AndGate\""));
        assertTrue(content.contains("nandcat:annotation=\"c2:AndGate\""));
        assertTrue(content.contains("nandcat:annotation=\"c3:AndGate\""));
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

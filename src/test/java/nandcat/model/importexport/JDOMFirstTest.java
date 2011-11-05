package nandcat.model.importexport;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import nandcat.Nandcat;
import nandcat.NandcatTest;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;
import org.junit.Test;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class JDOMFirstTest {

    @Test
    public void testSimpleInputOutput() throws JDOMException, IOException, SAXException {
        SAXBuilder builder = new SAXBuilder();
        builder.setValidation(false);
        builder.setIgnoringElementContentWhitespace(true);
        Document doc = builder.build(NandcatTest.class.getResourceAsStream("../formattest/sepaf-example-valid.xml"));
        XMLOutputter outputter = new XMLOutputter();
        Element root = doc.getRootElement();
        for (Object e : root.getChildren()) {
            if (e instanceof Element) {
                // System.out.println("Name: " + ((Element) e).getName());
                // System.out.println("Qualified name: " + ((Element) e).getQualifiedName());
                for (Object sube : ((Element) e).getChildren()) {
                    if (sube instanceof Element) {
                        // System.out.println("--Qualified name: " + ((Element) sube).getQualifiedName());
                        // System.out.println("Name: " + ((Element) sube).getName());
                        // System.out.println("Type: " + ((Element) sube).getAttributeValue("type"));
                        String name = ((Element) sube).getAttributeValue("name");
                        if (name != null && name.equals("inner0")) {
                            // System.out.println("name gesetzt");
                            ((Element) sube).setAttribute("name", "inner1");
                        }
                    }
                }
            }
        }
        File file = File.createTempFile("xmloutput", ".xml");
        // file.deleteOnExit();
        outputter.output(doc, new FileOutputStream(file));
        // System.out.println(file.getAbsolutePath());
        testValidOutput(file);
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

    @Test
    public void testXPath() throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder();
        builder.setValidation(false);
        builder.setIgnoringElementContentWhitespace(true);
        Document doc = builder.build(NandcatTest.class.getResourceAsStream("../formattest/sepaf-example-valid.xml"));
        XPath xpath = XPath.newInstance("//c:circuit");
        xpath.addNamespace("c", "http://www.sosy-lab.org/Teaching/2011-WS-SEP/xmlns/circuits-1.0");

        List result = xpath.selectNodes(doc);
        // for (Object res : result) {
        // System.out.println("Element gefunden: " + ((Element) res).getName());
        // }
        assertEquals(2, result.size());

    }
}

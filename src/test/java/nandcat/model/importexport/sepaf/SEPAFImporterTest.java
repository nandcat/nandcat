package nandcat.model.importexport.sepaf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import nandcat.Nandcat;
import nandcat.NandcatTest;
import nandcat.model.ModelElementDefaults;
import nandcat.model.element.AndGate;
import nandcat.model.element.Circuit;
import nandcat.model.element.Element;
import nandcat.model.element.FlipFlop;
import nandcat.model.element.IdentityGate;
import nandcat.model.element.ImpulseGenerator;
import nandcat.model.element.Lamp;
import nandcat.model.element.NotGate;
import nandcat.model.element.OrGate;
import nandcat.model.element.Port;
import nandcat.model.element.factory.ModuleBuilderFactory;
import nandcat.model.importexport.FormatErrorHandler;
import nandcat.model.importexport.FormatException;
import nandcat.model.importexport.Importer;
import nandcat.model.importexport.XsdValidation;
import nandcat.view.StandardModuleLayouter;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class SEPAFImporterTest {

    private Importer importer;

    /**
     * Class logger instance.
     */
    private static final Logger LOG = Logger.getLogger(SEPAFImporterTest.class);

    @Before
    public void setUp() {
        importer = new SEPAFImporter();
        ModuleBuilderFactory factory = new ModuleBuilderFactory();
        factory.setDefaults(new ModelElementDefaults());
        factory.setLayouter(new StandardModuleLayouter());
        importer.setFactory(factory);
        importer.setErrorHandler(new FormatErrorHandler() {

            public void warning(FormatException exception) throws FormatException {
                LOG.debug("Warning: ");
                LOG.debug(exception.getMessage());
            }

            public void fatal(FormatException exception) throws FormatException {
                LOG.debug("Fatal Error: ");
                LOG.debug(exception.getMessage());
                throw exception;
            }

            public void error(FormatException exception) throws FormatException {
                LOG.debug("Error: ");
                LOG.debug(exception.getMessage());
                throw exception;
            }
        });
    }

    @Test
    public void testLocation() {
        File file = getFile("../formattest/sepaf-example-valid-fewcomponents.xml");
        importer.setFile(file);

        ModuleBuilderFactory factory = new ModuleBuilderFactory();
        factory.setDefaults(new ModelElementDefaults());
        factory.setLayouter(new StandardModuleLayouter());
        importer.setFactory(factory);
        assertTrue(importer.importCircuit());

        Circuit circuit = importer.getCircuit();
        assertTrue(circuit != null);
        List<Element> elements = circuit.getElements();
        NotGate notGate = null;
        AndGate andGate = null;
        IdentityGate idGate = null;
        OrGate orGate = null;
        Lamp lamp = null;
        FlipFlop flipFlop = null;
        ImpulseGenerator in = null;
        ImpulseGenerator ig = null;
        for (Element element : elements) {
            if (element instanceof NotGate)
                notGate = (NotGate) element;
            if (element instanceof AndGate)
                andGate = (AndGate) element;
            if (element instanceof IdentityGate)
                idGate = (IdentityGate) element;
            if (element instanceof OrGate)
                orGate = (OrGate) element;
            if (element instanceof Lamp)
                lamp = (Lamp) element;
            if (element instanceof FlipFlop)
                flipFlop = (FlipFlop) element;
            if (element instanceof ImpulseGenerator) {
                ImpulseGenerator igtmp = (ImpulseGenerator) element;
                if (igtmp.getFrequency() == 0)
                    in = igtmp;
                else
                    ig = igtmp;
            }
        }

        // Check if all element were parsed correctly.
        assertEquals(new Point(1, 1), notGate.getRectangle().getLocation());
        assertEquals("not", notGate.getName());

        assertEquals(new Point(2, 2), andGate.getRectangle().getLocation());
        assertEquals("and", andGate.getName());

        assertEquals(new Point(3, 3), idGate.getRectangle().getLocation());
        assertEquals("id", idGate.getName());

        assertEquals(new Point(4, 4), orGate.getRectangle().getLocation());
        assertEquals("or", orGate.getName());

        assertEquals(new Point(5, 5), lamp.getRectangle().getLocation());
        assertEquals("out", lamp.getName());

        assertEquals(new Point(6, 6), flipFlop.getRectangle().getLocation());
        assertEquals("flipflop", flipFlop.getName());

        assertEquals(new Point(7, 7), in.getRectangle().getLocation());
        assertEquals(false, in.getState());
        assertEquals("in", in.getName());

        assertEquals(new Point(8, 8), ig.getRectangle().getLocation());
        assertEquals(20, ig.getFrequency());
        assertEquals("clock", ig.getName());

        // Check if no element exists twice.
        assertEquals(8, elements.size());
    }

    @Test
    public void testConnections() {
        File file = getFile("../formattest/sepaf-example-valid-connectedcomponents.xml");
        importer.setFile(file);
        assertTrue(importer.importCircuit());
        Circuit circuit = importer.getCircuit();
        assertTrue(circuit != null);
        List<Element> elements = circuit.getElements();
        NotGate notGate = null;
        AndGate andGate = null;
        IdentityGate idGate = null;
        OrGate orGate = null;
        Lamp lamp = null;
        FlipFlop flipFlop = null;
        ImpulseGenerator in = null;
        ImpulseGenerator ig = null;
        for (Element element : elements) {
            if (element instanceof NotGate)
                notGate = (NotGate) element;
            if (element instanceof AndGate)
                andGate = (AndGate) element;
            if (element instanceof IdentityGate)
                idGate = (IdentityGate) element;
            if (element instanceof OrGate)
                orGate = (OrGate) element;
            if (element instanceof Lamp)
                lamp = (Lamp) element;
            if (element instanceof FlipFlop)
                flipFlop = (FlipFlop) element;
            if (element instanceof ImpulseGenerator) {
                ImpulseGenerator igtmp = (ImpulseGenerator) element;
                if (igtmp.getFrequency() == 0)
                    in = igtmp;
                else
                    ig = igtmp;
            }
        }
        // Test connection between and and orgate
        Port source1 = andGate.getOutPorts().get(0);
        Port target1 = orGate.getInPorts().get(0);
        assertTrue(source1.getConnection() != null);
        assertEquals(source1, source1.getConnection().getInPort());
        assertEquals(target1, source1.getConnection().getOutPort());

        // Test connection between or and andgate
        Port source2 = orGate.getOutPorts().get(0);
        Port target2 = andGate.getInPorts().get(1);
        assertTrue(source2.getConnection() != null);
        assertEquals(source2, source2.getConnection().getInPort());
        assertEquals(target2, source2.getConnection().getOutPort());
    }

    @Test
    public void testRecursiveCircuits() {
        File file = getFile("../formattest/sepaf-example-valid-recursivecircuits.xml");
        importer.setFile(file);
        assertTrue(importer.importCircuit());
        Circuit circuit = importer.getCircuit();
        assertTrue(circuit != null);
        List<Element> elements = circuit.getElements();
        assertEquals(4, countRecursiveCircuits(elements));
    }

    @Test
    public void testExportedCircuit() throws FileNotFoundException, SAXException, IOException {
        File file = getFile("../formattest/sepaf-example-valid-fewcomponentsexported.xml");
        testValidOutput(file);
        importer.setFile(file);
        assertTrue(importer.importCircuit());
        Circuit circuit = importer.getCircuit();
        assertTrue(circuit != null);
        List<Element> elements = circuit.getElements();
        assertEquals(8, elements.size());
    }

    @Test
    public void testDoubleRefCircuits() {
        File file = getFile("../formattest/sepaf-example-valid-doublerefcircuits.xml");
        importer.setFile(file);
        assertTrue(importer.importCircuit());
        Circuit circuit = importer.getCircuit();
        assertTrue(circuit != null);
        List<Element> elements = circuit.getElements();
        for (Element element : elements) {
            if (element instanceof Circuit) {
                assertEquals("un-iq-ue", ((Circuit) element).getUuid());
                for (Element sub : ((Circuit) element).getElements()) {
                    if (sub instanceof AndGate) {
                        assertEquals("c4:AndGate", sub.getName());
                    } else {
                        fail("There should be only a Andgate");
                    }
                }
            } else {
                fail("There should be two circuits nothing more");
            }
        }
        assertEquals(2, countRecursiveCircuits(elements));
    }

    // private void drawHierarchy(Element e, int indent) {
    // for (int i = 0; i < indent; i++) {
    // System.out.print("-");
    // }
    // System.out.print(e.getClass().getName() + " : " + Integer.toHexString(System.identityHashCode(e)));
    // if (e instanceof Module) {
    // System.out.println(": Name=" + ((Module) e).getName());
    // }
    // System.out.println();
    // if (e instanceof Circuit) {
    // for (Element el : ((Circuit) e).getElements()) {
    // drawHierarchy(el, indent + 1);
    // }
    // }
    //
    // }

    private int countRecursiveCircuits(List<Element> elements) {
        for (Element inelement : elements) {
            if (inelement instanceof Circuit) {
                return 1 + countRecursiveCircuits(((Circuit) inelement).getElements());
            }
        }
        return 1;
    }

    private File getFile(String path) {
        try {
            return new File(NandcatTest.class.getResource(path).toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
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

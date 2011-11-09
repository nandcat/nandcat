package nandcat.model.importexport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.awt.Point;
import java.io.File;
import java.net.URISyntaxException;
import java.util.List;
import nandcat.NandcatTest;
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
import nandcat.model.importexport.sepaf.SEPAFImporter;
import org.junit.Test;

public class SEPAFImporterTest {

    @Test
    public void testLocation() {
        File file = getFile("../formattest/sepaf-example-valid-fewcomponents.xml");
        Importer importer = new SEPAFImporter();
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
        assertEquals(true, in.getState());
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
        Importer importer = new SEPAFImporter();
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
        Importer importer = new SEPAFImporter();
        importer.setFile(file);
        assertTrue(importer.importCircuit());
        Circuit circuit = importer.getCircuit();
        assertTrue(circuit != null);
        List<Element> elements = circuit.getElements();
        assertEquals(4, countRecursiveCircuits(elements));
    }

    @Test
    public void testDoubleRefCircuits() {
        File file = getFile("../formattest/sepaf-example-valid-doublerefcircuits.xml");
        Importer importer = new SEPAFImporter();
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

}

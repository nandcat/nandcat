package nandcat.model.importexport;

import static org.junit.Assert.assertEquals;
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
import nandcat.model.importexport.sepaf.SEPAFImporter;
import org.junit.Test;

public class SEPAFImporterTest {

    @Test
    public void testLocation() {
        File file = getFile("../formattest/sepaf-example-valid-fewcomponents.xml");
        Importer importer = new SEPAFImporter();
        importer.setFile(file);
        importer.importCircuit();
        Circuit circuit = importer.getCircuit();
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
        assertEquals(new Point(1, 1), notGate.getLocation());
        assertEquals(new Point(2, 2), andGate.getLocation());
        assertEquals(new Point(3, 3), idGate.getLocation());
        assertEquals(new Point(4, 4), orGate.getLocation());
        assertEquals(new Point(5, 5), lamp.getLocation());
        assertEquals(new Point(6, 6), flipFlop.getLocation());
        assertEquals(new Point(7, 7), in.getLocation());
        assertEquals(true, in.getState());
        assertEquals(new Point(8, 8), ig.getLocation());
        assertEquals(20, ig.getFrequency());

        // Check if no element exists twice.
        assertEquals(8, elements.size());

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

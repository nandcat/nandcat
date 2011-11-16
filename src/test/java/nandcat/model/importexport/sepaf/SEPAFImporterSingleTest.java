package nandcat.model.importexport.sepaf;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import nandcat.model.element.AndGate;
import nandcat.model.element.Circuit;
import nandcat.model.element.Connection;
import nandcat.model.element.Element;
import nandcat.model.element.FlipFlop;
import nandcat.model.element.IdentityGate;
import nandcat.model.element.ImpulseGenerator;
import nandcat.model.element.Lamp;
import nandcat.model.element.Module;
import nandcat.model.element.NotGate;
import nandcat.model.element.OrGate;
import nandcat.model.importexport.Exporter;
import nandcat.model.importexport.Importer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * WARNING! Should run AFTER Exporter Test.
 */
public class SEPAFImporterSingleTest {

    Exporter exporter;

    Importer importer;

    File file;

    private Circuit c;

    private Circuit exportC;

    @Before
    public void setup() throws IOException {
        exporter = new SEPAFExporter();
        file = File.createTempFile("export", ".xml");
        exporter.setFile(file);
        exportC = new Circuit();

        importer = new SEPAFImporter();
        importer.setFile(file);
    }

    @Test
    public void testDefaultAndGate() {
        AndGate gate = new AndGate();
        gate.setRectangle(new Rectangle(10, 20, 30, 40));
        exportC.addModule(gate);
        exporter.setCircuit(exportC);
        exporter.exportCircuit();
        assertTrue(importer.importCircuit());
        assertTrue(importer.getCircuit() != null);
        c = importer.getCircuit();
        deepCompareCircuits(exportC, c);
    }

    @Test
    public void testMultiAndGate() {
        AndGate gate = new AndGate(3, 4);
        gate.setRectangle(new Rectangle(10, 20, 30, 40));
        exportC.addModule(gate);
        exporter.setCircuit(exportC);
        exporter.exportCircuit();
        assertTrue(importer.importCircuit());
        assertTrue(importer.getCircuit() != null);
        c = importer.getCircuit();
        deepCompareCircuits(exportC, c);
    }

    @Test
    public void testDefaultOrGate() {
        OrGate gate = new OrGate();
        gate.setRectangle(new Rectangle(10, 20, 30, 40));
        exportC.addModule(gate);
        exporter.setCircuit(exportC);
        exporter.exportCircuit();
        assertTrue(importer.importCircuit());
        assertTrue(importer.getCircuit() != null);
        c = importer.getCircuit();
        deepCompareCircuits(exportC, c);
    }

    @Test
    public void testMultiOrGate() {
        OrGate gate = new OrGate(4, 3);
        gate.setRectangle(new Rectangle(10, 20, 30, 40));
        exportC.addModule(gate);
        exporter.setCircuit(exportC);
        exporter.exportCircuit();
        assertTrue(importer.importCircuit());
        assertTrue(importer.getCircuit() != null);
        c = importer.getCircuit();
        deepCompareCircuits(exportC, c);
    }

    @Test
    public void testDefaultNotGate() {
        NotGate gate = new NotGate();
        gate.setRectangle(new Rectangle(10, 20, 30, 40));
        exportC.addModule(gate);
        exporter.setCircuit(exportC);
        exporter.exportCircuit();
        assertTrue(importer.importCircuit());
        assertTrue(importer.getCircuit() != null);
        c = importer.getCircuit();
        deepCompareCircuits(exportC, c);
    }

    @Test
    public void testMultiNotGate() {
        NotGate gate = new NotGate(10);
        gate.setRectangle(new Rectangle(10, 20, 30, 40));
        exportC.addModule(gate);
        exporter.setCircuit(exportC);
        exporter.exportCircuit();
        assertTrue(importer.importCircuit());
        assertTrue(importer.getCircuit() != null);
        c = importer.getCircuit();
        deepCompareCircuits(exportC, c);
    }

    @Test
    public void testDefaultIdentityGate() {
        IdentityGate gate = new IdentityGate();
        gate.setRectangle(new Rectangle(10, 20, 30, 40));
        exportC.addModule(gate);
        exporter.setCircuit(exportC);
        exporter.exportCircuit();
        assertTrue(importer.importCircuit());
        assertTrue(importer.getCircuit() != null);
        c = importer.getCircuit();
        deepCompareCircuits(exportC, c);
    }

    @Test
    public void testMultiIdentityGate() {
        IdentityGate gate = new IdentityGate(1, 10);
        gate.setRectangle(new Rectangle(10, 20, 30, 40));
        exportC.addModule(gate);
        exporter.setCircuit(exportC);
        exporter.exportCircuit();
        assertTrue(importer.importCircuit());
        assertTrue(importer.getCircuit() != null);
        c = importer.getCircuit();
        deepCompareCircuits(exportC, c);
    }

    @Test
    public void testDefaultLamp() {
        Lamp gate = new Lamp();
        gate.setRectangle(new Rectangle(10, 20, 30, 40));
        exportC.addModule(gate);
        exporter.setCircuit(exportC);
        exporter.exportCircuit();
        assertTrue(importer.importCircuit());
        assertTrue(importer.getCircuit() != null);
        c = importer.getCircuit();
        deepCompareCircuits(exportC, c);
    }

    @Test
    public void testDefaultFlipFlop() {
        FlipFlop gate = new FlipFlop();
        gate.setRectangle(new Rectangle(10, 20, 30, 40));
        exportC.addModule(gate);
        exporter.setCircuit(exportC);
        exporter.exportCircuit();
        assertTrue(importer.importCircuit());
        assertTrue(importer.getCircuit() != null);
        c = importer.getCircuit();
        deepCompareCircuits(exportC, c);
    }

    @Test
    public void testDefaultClock() throws IOException {
        ImpulseGenerator gate = new ImpulseGenerator(10);
        gate.setRectangle(new Rectangle(10, 20, 30, 40));
        exportC.addModule(gate);
        exporter.setCircuit(exportC);
        exporter.exportCircuit();
        assertTrue(importer.importCircuit());
        assertTrue(importer.getCircuit() != null);
        c = importer.getCircuit();
        deepCompareCircuits(exportC, c);
    }

    @Test
    public void testDefaultSwitch() throws IOException {
        ImpulseGenerator gate = new ImpulseGenerator(0);
        gate.setRectangle(new Rectangle(10, 20, 30, 40));
        exportC.addModule(gate);
        exporter.setCircuit(exportC);
        exporter.exportCircuit();
        assertTrue(importer.importCircuit());
        assertTrue(importer.getCircuit() != null);
        c = importer.getCircuit();
        deepCompareCircuits(exportC, c);
        // String content = ImportExportUtil.getFileContent(file);
        // System.out.println(content.replace(">", ">\n"));
    }

    @After
    public void validateOutput() throws Exception {
        ImportExportUtil.testValidOutput(file);
        file.delete();
    }

    private void deepCompareCircuits(Circuit expected, Circuit actual) {
        if (!expected.getUuid().equals(actual.getUuid())) {
            fail("Uuid differs: " + expected.getUuid() + " actual: " + actual.getUuid());
        } else {
            if (expected.getElements().size() != actual.getElements().size()) {
                fail("Circuits differ in amount of elements");
            }
            for (Element actualElement : actual.getElements()) {
                boolean found = false;
                for (Element expectedElement : expected.getElements()) {
                    if (actualElement.getClass().getName().equals(expectedElement.getClass().getName())) {
                        if (actualElement instanceof Module) {
                            if (((Module) actualElement).getRectangle().x == ((Module) expectedElement).getRectangle().x
                                    && ((Module) actualElement).getRectangle().y == ((Module) expectedElement)
                                            .getRectangle().y) {
                                // Found element at same position.
                                if (deepCompareModules((Module) expectedElement, (Module) actualElement)) {
                                    found = true;
                                } else {
                                    System.out.println("Similar element found: " + (Module) actualElement);
                                }
                            } else {
                                System.out.println("coords not right");
                            }
                        } else if (actualElement instanceof Connection) {
                            if (deepCompareConnections((Connection) expectedElement, (Connection) actualElement)) {
                                found = true;
                            }
                        }
                    }
                }
                if (!found) {
                    fail("Element " + actualElement + " not found in expectations");
                }
            }
        }
    }

    private boolean deepCompareConnections(Connection expected, Connection actual) {
        if (!deepCompareModules(expected.getNextModule(), actual.getNextModule())) {
            System.out.println("Next Module not similar");
            return false;
        }

        if (!deepCompareModules(expected.getPreviousModule(), actual.getPreviousModule())) {
            System.out.println("Previous Module not similar");
            return false;
        }
        return true;

    }

    private boolean deepCompareModules(Module expected, Module actual) {
        if (!(((Module) actual).getRectangle().x == ((Module) expected).getRectangle().x && ((Module) actual)
                .getRectangle().y == ((Module) expected).getRectangle().y)) {
            System.out.println("Coordinates of modules differ");
            return false;
        }

        if (expected.getInPorts().size() != actual.getInPorts().size()) {
            System.out.println("InPorts differ");
            return false;
        }

        if (expected.getOutPorts().size() != actual.getOutPorts().size()) {
            System.out.println("OutPorts differ");
            System.out.println("Expected: " + expected.getOutPorts().size());
            System.out.println("Actual: " + actual.getOutPorts().size());
            return false;
        }
        if (expected.getName() == null ^ actual.getName() == null) {
            System.out.println("One name is null");
            return false;
        }
        if (expected.getName() != null && !expected.getName().equals(actual.getName())) {
            System.out.println("Name differs");
            return false;
        }
        if (expected instanceof ImpulseGenerator) {
            if (((ImpulseGenerator) expected).getFrequency() != ((ImpulseGenerator) actual).getFrequency()) {
                System.out.println("Frequency differs");
                return false;
            }
        }
        return true;
    }
}

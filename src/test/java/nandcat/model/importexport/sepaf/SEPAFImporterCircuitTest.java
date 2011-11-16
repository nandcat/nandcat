package nandcat.model.importexport.sepaf;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import nandcat.model.element.AndGate;
import nandcat.model.element.Circuit;
import nandcat.model.element.Connection;
import nandcat.model.element.Element;
import nandcat.model.element.FlipFlop;
import nandcat.model.element.ImpulseGenerator;
import nandcat.model.element.Module;
import nandcat.model.element.OrGate;
import nandcat.model.importexport.Exporter;
import nandcat.model.importexport.ExternalCircuitSource;
import nandcat.model.importexport.Importer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * WARNING! Should run AFTER Exporter Test.
 */
public class SEPAFImporterCircuitTest {

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
    public void testExternalCircuit() throws IOException {
        AndGate gate = new AndGate();
        gate.setRectangle(new Rectangle(10, 20, 30, 40));
        exportC.addModule(gate);

        // Inner circuit
        final Circuit innercircuit = new Circuit("innercircuit-uuid");
        OrGate innergate = new OrGate();
        innergate.setRectangle(new Rectangle(50, 60, 70, 80));
        innercircuit.addModule(innergate);

        exportC.addModule(innercircuit);

        exporter.setCircuit(exportC);
        Map<String, String> externalCircuits = new HashMap<String, String>();
        externalCircuits.put("innercircuit-uuid", "external-name-of-circuit");
        exporter.setExternalCircuits(externalCircuits);
        exporter.exportCircuit();
        importer.setExternalCircuitSource(new ExternalCircuitSource() {

            public Circuit getExternalCircuit(String identifier) {
                if (identifier.equals("external-name-of-circuit")) {
                    return innercircuit;
                } else {
                    return null;
                }
            }
        });
        String content = ImportExportUtil.getFileContent(file);
        System.out.println(content.replace(">", ">\n"));
        assertTrue(importer.importCircuit());
        assertTrue(importer.getCircuit() != null);
        c = importer.getCircuit();
        deepCompareCircuits(exportC, c);
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

        if (expected instanceof Circuit && !(expected instanceof FlipFlop)) {
            deepCompareCircuits((Circuit) expected, (Circuit) actual);
        }
        return true;
    }
}

package nandcat.model.importexport.sepaf;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import nandcat.model.ModelElementDefaults;
import nandcat.model.element.Circuit;
import nandcat.model.element.Connection;
import nandcat.model.element.Element;
import nandcat.model.element.ImpulseGenerator;
import nandcat.model.element.Module;
import nandcat.model.element.factory.ModuleBuilder;
import nandcat.model.element.factory.ModuleBuilderFactory;
import nandcat.model.importexport.Exporter;
import nandcat.model.importexport.Importer;
import nandcat.view.StandardModuleLayouter;
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

    private ModuleBuilderFactory factory;

    @Before
    public void setup() throws IOException {
        factory = new ModuleBuilderFactory();
        factory.setDefaults(new ModelElementDefaults());
        factory.setLayouter(new StandardModuleLayouter());
        exporter = new SEPAFExporter();
        file = File.createTempFile("export", ".xml");
        exporter.setFile(file);
        exportC = (Circuit) factory.getCircuitBuilder().build();

        importer = new SEPAFImporter();

        importer.setFactory(factory);
        importer.setFile(file);
    }

    @Test
    public void testDefaultAndGate() {
        ModuleBuilder andB = factory.getAndGateBuilder();
        andB.setLocation(new Point(10, 20));
        exportC.addModule(andB.build());
        exporter.setCircuit(exportC);
        exporter.exportCircuit();
        assertTrue(importer.importCircuit());
        assertTrue(importer.getCircuit() != null);
        c = importer.getCircuit();
        deepCompareCircuits(exportC, c);
    }

    @Test
    public void testMultiAndGate() {
        ModuleBuilder andB = factory.getAndGateBuilder();
        andB.setInPorts(3);
        andB.setOutPorts(4);
        andB.setLocation(new Point(10, 20));
        exportC.addModule(andB.build());

        exporter.setCircuit(exportC);
        exporter.exportCircuit();
        assertTrue(importer.importCircuit());
        assertTrue(importer.getCircuit() != null);
        c = importer.getCircuit();
        deepCompareCircuits(exportC, c);
    }

    @Test
    public void testDefaultOrGate() {
        ModuleBuilder orB = factory.getOrGateBuilder();
        orB.setLocation(new Point(10, 20));
        exportC.addModule(orB.build());
        exporter.setCircuit(exportC);
        exporter.exportCircuit();
        assertTrue(importer.importCircuit());
        assertTrue(importer.getCircuit() != null);
        c = importer.getCircuit();
        deepCompareCircuits(exportC, c);
    }

    @Test
    public void testMultiOrGate() {
        ModuleBuilder orB = factory.getOrGateBuilder();
        orB.setInPorts(4);
        orB.setOutPorts(3);
        orB.setLocation(new Point(10, 20));
        exportC.addModule(orB.build());
        exporter.setCircuit(exportC);
        exporter.exportCircuit();
        assertTrue(importer.importCircuit());
        assertTrue(importer.getCircuit() != null);
        c = importer.getCircuit();
        deepCompareCircuits(exportC, c);
    }

    @Test
    public void testDefaultNotGate() {
        ModuleBuilder notB = factory.getNotGateBuilder();
        notB.setLocation(new Point(10, 20));
        exportC.addModule(notB.build());
        exporter.setCircuit(exportC);
        exporter.exportCircuit();
        assertTrue(importer.importCircuit());
        assertTrue(importer.getCircuit() != null);
        c = importer.getCircuit();
        deepCompareCircuits(exportC, c);
    }

    @Test
    public void testMultiNotGate() {
        ModuleBuilder notB = factory.getNotGateBuilder();
        notB.setLocation(new Point(10, 20));
        notB.setOutPorts(10);
        exportC.addModule(notB.build());
        exporter.setCircuit(exportC);
        exporter.exportCircuit();
        assertTrue(importer.importCircuit());
        assertTrue(importer.getCircuit() != null);
        c = importer.getCircuit();
        deepCompareCircuits(exportC, c);
    }

    @Test
    public void testDefaultIdentityGate() {
        ModuleBuilder idB = factory.getIdentityGateBuilder();
        idB.setLocation(new Point(10, 20));
        exportC.addModule(idB.build());
        exporter.setCircuit(exportC);
        exporter.exportCircuit();
        assertTrue(importer.importCircuit());
        assertTrue(importer.getCircuit() != null);
        c = importer.getCircuit();
        deepCompareCircuits(exportC, c);
    }

    @Test
    public void testMultiIdentityGate() {
        ModuleBuilder idB = factory.getIdentityGateBuilder();
        idB.setLocation(new Point(10, 20));
        idB.setOutPorts(10);
        exportC.addModule(idB.build());
        exporter.setCircuit(exportC);
        exporter.exportCircuit();
        assertTrue(importer.importCircuit());
        assertTrue(importer.getCircuit() != null);
        c = importer.getCircuit();
        deepCompareCircuits(exportC, c);
    }

    @Test
    public void testDefaultLamp() {
        ModuleBuilder builder = factory.getLampBuilder();
        builder.setLocation(new Point(10, 20));
        exportC.addModule(builder.build());
        exporter.setCircuit(exportC);
        exporter.exportCircuit();
        assertTrue(importer.importCircuit());
        assertTrue(importer.getCircuit() != null);
        c = importer.getCircuit();
        deepCompareCircuits(exportC, c);
    }

    @Test
    public void testDefaultFlipFlop() {
        ModuleBuilder builder = factory.getFlipFlopBuilder();
        builder.setLocation(new Point(10, 20));
        exportC.addModule(builder.build());
        exporter.setCircuit(exportC);
        exporter.exportCircuit();
        assertTrue(importer.importCircuit());
        assertTrue(importer.getCircuit() != null);
        c = importer.getCircuit();
        deepCompareCircuits(exportC, c);
    }

    @Test
    public void testDefaultClock() throws IOException {
        ModuleBuilder builder = factory.getClockBuilder();
        builder.setLocation(new Point(10, 20));
        exportC.addModule(builder.build());
        exporter.setCircuit(exportC);
        exporter.exportCircuit();
        assertTrue(importer.importCircuit());
        assertTrue(importer.getCircuit() != null);
        c = importer.getCircuit();
        deepCompareCircuits(exportC, c);
    }

    @Test
    public void testDefaultSwitch() throws IOException {
        ModuleBuilder builder = factory.getSwitchBuilder();
        builder.setLocation(new Point(10, 20));
        exportC.addModule(builder.build());
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

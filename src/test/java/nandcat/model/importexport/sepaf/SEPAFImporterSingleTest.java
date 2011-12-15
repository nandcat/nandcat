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
import nandcat.model.element.NotGate;
import nandcat.model.element.OrGate;
import nandcat.model.element.factory.ModuleBuilder;
import nandcat.model.element.factory.ModuleBuilderFactory;
import nandcat.model.importexport.Exporter;
import nandcat.model.importexport.FormatErrorHandler;
import nandcat.model.importexport.FormatException;
import nandcat.model.importexport.Importer;
import nandcat.model.importexport.RecursionException;
import nandcat.view.StandardModuleLayouter;
import org.apache.log4j.Logger;
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

    /**
     * Class logger instance.
     */
    private static final Logger LOG = Logger.getLogger(SEPAFImporterSingleTest.class);

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

        importer.setFactory(factory);
        importer.setFile(file);
    }

    @Test
    public void testDefaultAndGate() throws RecursionException {
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
    public void testMultiAndGate() throws RecursionException {
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
    public void testDefaultOrGate() throws RecursionException {
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
    public void testMultiOrGate() throws RecursionException {
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
    public void testDefaultNotGate() throws RecursionException {
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
    public void testMultiNotGate() throws RecursionException {
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
    public void testDefaultIdentityGate() throws RecursionException {
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
    public void testMultiIdentityGate() throws RecursionException {
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
    public void testDefaultLamp() throws RecursionException {
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
    public void testDefaultFlipFlop() throws RecursionException {
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
    public void testDefaultClock() throws IOException, RecursionException {
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
    public void testDefaultSwitch() throws IOException, RecursionException {
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
        // LOG.debug(content.replace(">", ">\n"));
    }

    @Test
    public void testCircuit() throws RecursionException {
        ModuleBuilder builder = factory.getCircuitBuilder();
        builder.setLocation(new Point(10, 20));
        Circuit innerC = (Circuit) builder.build();
        ModuleBuilder andGateB = factory.getAndGateBuilder();
        andGateB.setLocation(new Point(20, 30));
        innerC.addModule(andGateB.build());
        exportC.addModule(innerC);
        exporter.setCircuit(exportC);
        exporter.exportCircuit();
        assertTrue(importer.importCircuit());
        assertTrue(importer.getCircuit() != null);
        c = importer.getCircuit();
        deepCompareCircuits(exportC, c);
    }

    @Test
    public void testMultipleGates() throws RecursionException {
        ModuleBuilder notB = factory.getNotGateBuilder();
        notB.setLocation(new Point(10, 20));
        notB.setOutPorts(10);
        ModuleBuilder orB = factory.getOrGateBuilder();
        orB.setInPorts(4);
        orB.setOutPorts(3);
        orB.setLocation(new Point(20, 30));
        NotGate not = (NotGate) notB.build();
        OrGate or = (OrGate) orB.build();
        exportC.addModule(or);
        exportC.addModule(not);
        exporter.setCircuit(exportC);
        exporter.exportCircuit();
        assertTrue(importer.importCircuit());
        assertTrue(importer.getCircuit() != null);
        c = importer.getCircuit();
        deepCompareCircuits(exportC, c);
    }

    @Test
    public void testConnection() throws RecursionException {
        ModuleBuilder notB = factory.getNotGateBuilder();
        notB.setLocation(new Point(10, 20));
        notB.setOutPorts(10);
        ModuleBuilder orB = factory.getOrGateBuilder();
        orB.setInPorts(4);
        orB.setOutPorts(3);
        orB.setLocation(new Point(20, 30));
        NotGate not = (NotGate) notB.build();
        OrGate or = (OrGate) orB.build();
        exportC.addConnection(not.getOutPorts().get(0), or.getInPorts().get(0));
        exportC.addModule(or);
        exportC.addModule(not);
        exporter.setCircuit(exportC);
        exporter.exportCircuit();
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
                                LOG.debug("coords not right");
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
        LOG.debug("Check connection");
        if (!deepCompareModules(expected.getNextModule(), actual.getNextModule())) {
            LOG.debug("Next Module not similar");
            return false;
        }

        if (!deepCompareModules(expected.getPreviousModule(), actual.getPreviousModule())) {
            LOG.debug("Previous Module not similar");
            return false;
        }
        return true;

    }

    private boolean deepCompareModules(Module expected, Module actual) {
        if (!(((Module) actual).getRectangle().x == ((Module) expected).getRectangle().x && ((Module) actual)
                .getRectangle().y == ((Module) expected).getRectangle().y)) {
            LOG.debug("Coordinates of modules differ");
            return false;
        }

        if (expected.getInPorts().size() != actual.getInPorts().size()) {
            LOG.debug("InPorts differ");
            return false;
        }

        if (expected.getOutPorts().size() != actual.getOutPorts().size()) {
            LOG.debug("OutPorts differ");
            LOG.debug("Expected: " + expected.getOutPorts().size());
            LOG.debug("Actual: " + actual.getOutPorts().size());
            return false;
        }
        if (expected.getName() == null ^ actual.getName() == null) {
            LOG.debug("One name is null");
            return false;
        }
        if (expected.getName() != null && !expected.getName().equals(actual.getName())) {
            LOG.debug("Name differs");
            return false;
        }
        if (expected instanceof ImpulseGenerator) {
            if (((ImpulseGenerator) expected).getFrequency() != ((ImpulseGenerator) actual).getFrequency()) {
                LOG.debug("Frequency differs");
                return false;
            }
        }
        return true;
    }
}

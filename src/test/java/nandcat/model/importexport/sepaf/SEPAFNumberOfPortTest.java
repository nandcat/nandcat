package nandcat.model.importexport.sepaf;

import static org.junit.Assert.assertTrue;
import java.io.File;
import java.net.URISyntaxException;
import nandcat.NandcatTest;
import nandcat.model.ModelElementDefaults;
import nandcat.model.element.Circuit;
import nandcat.model.element.factory.ModuleBuilderFactory;
import nandcat.model.importexport.FormatErrorHandler;
import nandcat.model.importexport.FormatException;
import nandcat.model.importexport.Importer;
import nandcat.model.importexport.RecursionException;
import nandcat.view.StandardModuleLayouter;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class SEPAFNumberOfPortTest {

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
    public void testLocation() throws RecursionException {
        File file = getFile("../formattest/derpina.xml");
        importer.setFile(file);

        ModuleBuilderFactory factory = new ModuleBuilderFactory();
        factory.setDefaults(new ModelElementDefaults());
        factory.setLayouter(new StandardModuleLayouter());
        importer.setFactory(factory);
        assertTrue(importer.importCircuit());

        Circuit circuit = importer.getCircuit();
        assertTrue(circuit != null);

        Circuit derp = (Circuit) factory.getCircuitBuilder().build();
        derp.addModule(circuit);

        System.out.println(circuit.getInPorts().size());
        assertTrue(circuit.getInPorts().size() == 4);
        assertTrue(circuit.getOutPorts().size() == 3);

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

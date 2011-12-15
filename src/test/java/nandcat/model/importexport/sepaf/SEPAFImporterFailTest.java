package nandcat.model.importexport.sepaf;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import nandcat.NandcatTest;
import nandcat.model.ModelElementDefaults;
import nandcat.model.element.factory.ModuleBuilderFactory;
import nandcat.model.importexport.FormatErrorHandler;
import nandcat.model.importexport.FormatException;
import nandcat.model.importexport.RecursionException;
import nandcat.view.StandardModuleLayouter;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class SEPAFImporterFailTest {

    /**
     * Class logger instance.
     */
    private static final Logger LOG = Logger.getLogger(SEPAFImporterFailTest.class);

    private ModuleBuilderFactory factory;

    private SEPAFExporter exporter;

    private SEPAFImporter importer;

    int countWarn = 0;

    int countErr = 0;

    int countFatal = 0;

    @Before
    public void setup() throws IOException {
        factory = new ModuleBuilderFactory();
        factory.setDefaults(new ModelElementDefaults());
        factory.setLayouter(new StandardModuleLayouter());

        importer = new SEPAFImporter();

        importer.setErrorHandler(new FormatErrorHandler() {

            public void warning(FormatException exception) throws FormatException {
                LOG.debug("Warning: ");
                LOG.debug(exception.getMessage());
                countWarn++;
                throw exception;
            }

            public void fatal(FormatException exception) throws FormatException {
                LOG.debug("Fatal Error: ");
                LOG.debug(exception.getMessage());
                countFatal++;
                throw exception;
            }

            public void error(FormatException exception) throws FormatException {
                LOG.debug("Error: ");
                LOG.debug(exception.getMessage());
                countErr++;
                throw exception;
            }
        });
        importer.setFactory(factory);
    }

    private File getFile(String path) {
        try {
            return new File(NandcatTest.class.getResource(path).toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void testFailFlipFlopWrongPort() throws RecursionException {
        importer.setFile(getFile("../formattest/sepaf-example-invalid-flipflopwrongsourceport.xml"));
        assertFalse(importer.importCircuit());
        assertTrue(countFatal > 0 || countErr > 0);
    }
}

package nandcat.model.importexport.benchmark;

import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;
import nandcat.model.ModelElementDefaults;
import nandcat.model.element.factory.ModuleBuilderFactory;
import nandcat.model.importexport.FormatErrorHandler;
import nandcat.model.importexport.FormatException;
import nandcat.model.importexport.sepaf.SEPAFImporter;
import nandcat.view.StandardModuleLayouter;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class ImportBenchmark extends AbstractBenchmark {

    private ModuleBuilderFactory factory;

    private SEPAFImporter importer;

    @Rule
    public MethodRule benchmarkRun = new BenchmarkRule();

    /**
     * Class logger instance.
     */
    private static final Logger LOG = Logger.getLogger(ImportBenchmark.class);

    @Before
    public void setup() {
        factory = new ModuleBuilderFactory();
        factory.setDefaults(new ModelElementDefaults());
        factory.setLayouter(new StandardModuleLayouter());
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
    }

    @BenchmarkOptions(benchmarkRounds = 20, warmupRounds = 0)
    @Test
    public void test() throws IOException {
        File f = XMLGenerator.generateBenchmarkFile(50, 10);
        importer.setFile(f);
        assertTrue(importer.importCircuit());
    }

    @BenchmarkOptions(benchmarkRounds = 20, warmupRounds = 0)
    @Test
    public void test2500() throws IOException {
        File f = XMLGenerator.generateBenchmarkFile(50, 50);
        importer.setFile(f);
        assertTrue(importer.importCircuit());
    }

    @Test
    public void test5000() throws IOException {
        File f = XMLGenerator.generateBenchmarkFile(50, 100);
        importer.setFile(f);
        assertTrue(importer.importCircuit());
    }

    @Test
    public void test8000() throws IOException {
        File f = XMLGenerator.generateBenchmarkFile(80, 100);
        importer.setFile(f);
        assertTrue(importer.importCircuit());
    }

    @Test
    public void test10000() throws IOException {
        File f = XMLGenerator.generateBenchmarkFile(100, 100);
        importer.setFile(f);
        assertTrue(importer.importCircuit());
    }

}

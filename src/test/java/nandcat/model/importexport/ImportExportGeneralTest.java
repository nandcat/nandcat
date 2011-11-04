package nandcat.model.importexport;

import java.io.IOException;
import nandcat.Nandcat;
import org.junit.Test;

public class ImportExportGeneralTest {

    /**
     * Tests if used xsd files are available. If failing and xsd exist in src/resources then mvn clean, mvn package
     * should help.
     * 
     * @throws IOException
     *             Fail on exception.
     */
    @Test
    public void testXSDexist() throws IOException {
        Nandcat.class.getResourceAsStream("../sepaf-extension.xsd").available();
        Nandcat.class.getResourceAsStream("../circuits-1.0.xsd").available();
    }
}

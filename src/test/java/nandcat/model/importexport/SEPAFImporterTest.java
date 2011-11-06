package nandcat.model.importexport;

import java.io.File;
import java.net.URISyntaxException;
import nandcat.NandcatTest;
import nandcat.model.importexport.sepaf.SEPAFImporter;
import org.junit.Ignore;
import org.junit.Test;

public class SEPAFImporterTest {

    @Ignore
    @Test
    public void test() {
        File file = getFile("../formattest/sepaf-example-valid-fewcomponents.xml");
        Importer importer = new SEPAFImporter();
        importer.setFile(file);
        importer.importCircuit();

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

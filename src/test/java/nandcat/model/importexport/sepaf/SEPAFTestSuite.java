package nandcat.model.importexport.sepaf;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({ SEPAFCheckReferenceTest.class, SEPAFExporterBasicTest.class, SEPAFExporterConnectionTest.class,
        SEPAFExporterSingleTest.class, SEPAFExporterTest.class, SEPAFImporterTest.class })
public class SEPAFTestSuite {

}

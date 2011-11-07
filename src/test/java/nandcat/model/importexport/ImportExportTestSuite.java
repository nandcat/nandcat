package nandcat.model.importexport;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ImportExportGeneralTest.class, XsdValidationTest.class, JDOMFirstTest.class,
        SEPAFCheckReferenceTest.class, SEPAFImporterTest.class, SEPAFExporterTest.class })
public class ImportExportTestSuite {
}

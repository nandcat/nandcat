package nandcat.model.importexport;

import nandcat.model.importexport.sepaf.SEPAFTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ImportExportGeneralTest.class, DrawExporterTest.class, XsdValidationTest.class, JDOMFirstTest.class,
        FastDeepCopyTest.class, XsdValidationTest.class, SEPAFTestSuite.class })
public class ImportExportTestSuite {
}

package nandcat.model.importexport;

import nandcat.model.importexport.sepaf.SEPAFCheckReferenceTest;
import nandcat.model.importexport.sepaf.SEPAFExporterTest;
import nandcat.model.importexport.sepaf.SEPAFImporterTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ImportExportGeneralTest.class, XsdValidationTest.class, JDOMFirstTest.class,
        SEPAFCheckReferenceTest.class, SEPAFImporterTest.class, SEPAFExporterTest.class, FastDeepCopyTest.class })
public class ImportExportTestSuite {
}

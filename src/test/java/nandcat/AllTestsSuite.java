package nandcat;

import nandcat.controller.ControllerTestSuite;
import nandcat.model.ModelTestSuite;
import nandcat.view.ViewTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ NANDcatTestSuite.class, ControllerTestSuite.class, ModelTestSuite.class, ViewTestSuite.class })
public class AllTestsSuite {

}

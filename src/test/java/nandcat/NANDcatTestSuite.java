package nandcat;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ControllerTest.class, FeedbackCheckTest.class, NandcatTest.class, ResourcesTest.class,
        SimulationTest.class })
public class NANDcatTestSuite {

}

package nandcat.controller;

import nandcat.controller.tool.ToolTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ExtensionFileFilterTest.class, ImportExportUtilsTest.class, ToolTestSuite.class })
public class ControllerTestSuite {

}

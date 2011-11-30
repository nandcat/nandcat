package nandcat.view;

import nandcat.view.elementdrawer.ElementDrawerTestSuite;
import nandcat.view.modulelayouter.StandardModuleLayouterTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ElementDrawerTestSuite.class, StandardModuleLayouterTestSuite.class })
public class ViewTestSuite {

}

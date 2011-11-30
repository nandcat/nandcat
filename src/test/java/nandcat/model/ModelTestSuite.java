package nandcat.model;

import nandcat.model.check.CheckTestSuite;
import nandcat.model.element.ElementTestSuite;
import nandcat.model.factory.FactoryTestSuite;
import nandcat.model.importexport.ImportExportTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ClockTest.class, ModelTest.class, CheckTestSuite.class, ElementTestSuite.class,
        ImportExportTestSuite.class, FactoryTestSuite.class })
public class ModelTestSuite {

}

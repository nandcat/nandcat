package nandcat.model.element;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({ AndGateTest.class, CircuitTest.class, ConnectionTest.class, GateTest.class, IdentityGateTest.class,
        OrGateTest.class })
public class ElementTestSuite {

}

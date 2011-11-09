package nandcat.view.elementdrawer;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ElementDrawerConnectionTest.class, ElementDrawerPortTest.class, ElementDrawerAndGateTest.class,
        ElementDrawerOrGateTest.class, ElementDrawerNotGateTest.class, ElementDrawerCircuitTest.class,
        ElementDrawerLampTest.class, ElementDrawerImpulseGeneratorTest.class, ElementDrawerIdentityGateTest.class,
        ElementDrawerFlipFlopTest.class, ElementDrawerRectangleTest.class })
public class ElementDrawerTestSuite {
}

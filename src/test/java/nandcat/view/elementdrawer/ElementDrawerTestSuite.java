package nandcat.view.elementdrawer;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ElementDrawerAndGateTest.class, ElementDrawerOrGateTest.class, ElementDrawerNotGateTest.class,
        ElementDrawerCircuitTest.class, ElementDrawerLampTest.class, ElementDrawerImpulseGeneratorTest.class,
        ElementDrawerIdentityGateTest.class, ElementDrawerFlipFlopTest.class, ElementDrawerRectangleTest.class })
public class ElementDrawerTestSuite {
}

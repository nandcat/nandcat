package nandcat.model.check;

import static org.junit.Assert.assertFalse;
import nandcat.model.ModelElementDefaults;
import nandcat.model.element.AndGate;
import nandcat.model.element.Circuit;
import nandcat.model.element.OrGate;
import nandcat.model.element.factory.ModuleBuilderFactory;
import nandcat.view.StandardModuleLayouter;
import org.junit.Before;
import org.junit.Test;

public class OrphanCheckTest {

    private ModuleBuilderFactory factory;

    @Before
    public void setUp() {
        factory = new ModuleBuilderFactory();
        factory.setDefaults(new ModelElementDefaults());
        factory.setLayouter(new StandardModuleLayouter());
    }

    /**
     * Test simple circuit
     */
    @Test
    public void test() {
        Circuit c = (Circuit) factory.getCircuitBuilder().build();
        CircuitCheck check = new OrphanCheck();
        AndGate andGate1 = (AndGate) factory.getAndGateBuilder().build();
        AndGate andGate2 = (AndGate) factory.getAndGateBuilder().build();
        AndGate andGate3 = (AndGate) factory.getAndGateBuilder().build();
        OrGate orGate1 = (OrGate) factory.getOrGateBuilder().build();

        c.addModule(andGate3);
        c.addModule(orGate1);
        c.addModule(andGate2);
        c.addModule(andGate1);

        c.addConnection(andGate2.getOutPorts().get(0), andGate1.getInPorts().get(0));
        c.addConnection(orGate1.getOutPorts().get(0), andGate3.getInPorts().get(0));
        // expected: false
        assertFalse(check.test(c));
    }
}

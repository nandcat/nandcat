package nandcat.model.check;

import static org.junit.Assert.assertTrue;
import nandcat.model.ModelElementDefaults;
import nandcat.model.element.Circuit;
import nandcat.model.element.factory.ModuleBuilderFactory;
import nandcat.view.StandardModuleLayouter;
import org.junit.Before;
import org.junit.Test;

public class MultipleConnectionsCheckTest {

    private ModuleBuilderFactory factory;

    @Before
    public void setUp() {
        factory = new ModuleBuilderFactory();
        factory.setDefaults(new ModelElementDefaults());
        factory.setLayouter(new StandardModuleLayouter());
    }

    @Test
    public void test() {
        Circuit circuit = (Circuit) factory.getCircuitBuilder().build();
        MultipleConnectionsCheck check = new MultipleConnectionsCheck();
        assertTrue(check.test(circuit));
    }
}

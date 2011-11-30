package nandcat.model.factory;

import nandcat.model.element.factory.ModuleBuilderFactory;
import org.junit.Before;
import org.junit.Test;

public class ModuleBuilderFactoryTest {

    private ModuleBuilderFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new ModuleBuilderFactory();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullDefaults() {
        factory.setDefaults(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullLayouter() {
        factory.setLayouter(null);
    }
}

package nandcat.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import nandcat.model.element.AndGate;
import nandcat.model.element.factory.ModuleBuilderFactory;
import org.junit.Test;

/**
 * Unit test for ViewModule.
 */
public class ViewModuleTest {

    /**
     * Test ViewModule.
     */
    @Test
    public void testApp() {
        ViewModule v = new ViewModule("xxx", null, "xxx.xml");
        assertEquals("xxx", v.getName());
        v.setName("yyy");
        assertEquals("yyy", v.getName());
        assertEquals("xxx.xml", v.getFileName());
        ModuleBuilderFactory fack = new ModuleBuilderFactory();
        fack.setDefaults(new ModelElementDefaults());
        v = new ViewModule("AND", fack.getAndGateBuilder().build(), null);
        assertEquals("", v.getFileName());
        if (v.getModule() instanceof AndGate)
            assertTrue(true);
        else
            assertTrue(false);
    }
}

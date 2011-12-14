package nandcat.view.modulelayouter;

import java.awt.Dimension;
import nandcat.ReflectionUtil;
import nandcat.view.StandardModuleLayouter;
import org.junit.Before;

/**
 * Base for layouter tests.
 * 
 */
public abstract class AbstractLayouterTest {

    int PORT_MARGIN_LEFT;

    int PORT_MARGIN_RIGHT;

    int PORT_MARGIN_TOP;

    int PORT_DIAMETER;

    int PORT_MARGIN_BOTTOM;

    Dimension GATE_DIMENSION;

    Dimension CIRCUIT_DIMENSION;

    Dimension LAMP_DIMENSION;

    void applyFields() throws Exception {
        PORT_MARGIN_TOP = (Integer) ReflectionUtil.getPrivateField(StandardModuleLayouter.class, null,
                "PORT_MARGIN_TOP");
        PORT_MARGIN_BOTTOM = (Integer) ReflectionUtil.getPrivateField(StandardModuleLayouter.class, null,
                "PORT_MARGIN_BOTTOM");
        PORT_MARGIN_LEFT = (Integer) ReflectionUtil.getPrivateField(StandardModuleLayouter.class, null,
                "PORT_MARGIN_LEFT");
        PORT_MARGIN_RIGHT = (Integer) ReflectionUtil.getPrivateField(StandardModuleLayouter.class, null,
                "PORT_MARGIN_RIGHT");
        PORT_DIAMETER = (Integer) ReflectionUtil.getPrivateField(StandardModuleLayouter.class, null, "PORT_DIAMETER");

        GATE_DIMENSION = (Dimension) ReflectionUtil.getPrivateField(StandardModuleLayouter.class, null,
                "GATE_DIMENSION");

        CIRCUIT_DIMENSION = (Dimension) ReflectionUtil.getPrivateField(StandardModuleLayouter.class, null,
                "CIRCUIT_DIMENSION");

        LAMP_DIMENSION = (Dimension) ReflectionUtil.getPrivateField(StandardModuleLayouter.class, null,
                "LAMP_DIMENSION");
    }

    /**
     * Set fields up.
     * 
     * @throws Exception
     *             Fail on all exceptions.
     */
    @Before
    public void setUpDrawerAndFields() throws Exception {
        applyFields();
    }
}

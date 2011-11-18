package nandcat.view.modulelayouter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.awt.Dimension;
import java.awt.Point;
import nandcat.model.element.ClockBuilder;
import nandcat.model.element.Module;
import nandcat.model.element.Port;
import nandcat.model.element.SwitchBuilder;
import nandcat.model.element.factory.ElementDefaults;
import nandcat.model.element.factory.ModuleBuilderFactory;
import nandcat.view.StandardModuleLayouter;
import org.junit.Before;
import org.junit.Test;

public class StandardModuleLayouterPortTest extends AbstractLayouterTest {

    private StandardModuleLayouter layouter;

    private ModuleBuilderFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new ModuleBuilderFactory();
        factory.setLayouter(new StandardModuleLayouter());
        factory.setDefaults(new ElementDefaults() {

            @Override
            public void setDefaults(SwitchBuilder b) {
                super.setDefaults(b);
                b.setFrequency(0);
            }

            @Override
            public void setDefaults(ClockBuilder b) {
                super.setDefaults(b);
                b.setFrequency(10);
            }
        });
    }

    private void verifyPortPosition(Module mod) {

        Point position = new Point(0, 0);
        for (Port port : mod.getInPorts()) {
            assertTrue(port.getRectangle().x >= position.x);
            assertTrue(port.getRectangle().x <= mod.getRectangle().x + mod.getRectangle().width / 2);
            assertTrue(port.getRectangle().y > position.y);
            position = port.getRectangle().getLocation();
            assertEquals(new Dimension(PORT_DIAMETER, PORT_DIAMETER), port.getRectangle().getSize());
        }

        // Outgoing ports on the right side.
        position = new Point(mod.getRectangle().x + (mod.getRectangle().width / 2), 0);
        for (Port port : mod.getOutPorts()) {
            assertTrue(port.getRectangle().x >= position.x);
            assertTrue(port.getRectangle().y > position.y);
            position = port.getRectangle().getLocation();
            assertEquals(new Dimension(PORT_DIAMETER, PORT_DIAMETER), port.getRectangle().getSize());
        }
    }

    @Test
    public void testDimensionsAndGate() {
        Module mod = factory.getAndGateBuilder().build();
        verifyPortPosition(mod);
    }

    @Test
    public void testDimensionsOrGate() {
        Module mod = factory.getOrGateBuilder().build();
        verifyPortPosition(mod);
    }

    @Test
    public void testDimensionsNotGate() {
        Module mod = factory.getNotGateBuilder().build();
        verifyPortPosition(mod);
    }

    @Test
    public void testDimensionsIdentityGate() {
        Module mod = factory.getIdentityGateBuilder().build();
        verifyPortPosition(mod);
    }

    @Test
    public void testDimensionsLamp() {
        Module mod = factory.getLampBuilder().build();
        verifyPortPosition(mod);
    }

    @Test
    public void testDimensionsClock() {
        Module mod = factory.getClockBuilder().build();
        verifyPortPosition(mod);
    }

    @Test
    public void testDimensionsSwitch() {
        Module mod = factory.getSwitchBuilder().build();
        verifyPortPosition(mod);
    }

    @Test
    public void testDimensionsFlipFlop() {
        Module mod = factory.getFlipFlopBuilder().build();
        verifyPortPosition(mod);
    }

    @Test
    public void testDimensionsCircuit() {
        Module mod = factory.getCircuitBuilder().build();
        verifyPortPosition(mod);
    }
}

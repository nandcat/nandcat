package nandcat.view.modulelayouter;

import static org.junit.Assert.assertEquals;
import nandcat.model.element.ClockBuilder;
import nandcat.model.element.Module;
import nandcat.model.element.SwitchBuilder;
import nandcat.model.element.factory.ElementDefaults;
import nandcat.model.element.factory.ModuleBuilderFactory;
import nandcat.view.StandardModuleLayouter;
import org.junit.Before;
import org.junit.Test;

public class StandardModuleLayouterDimensionTest extends AbstractLayouterTest {

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

    @Test
    public void testDimensionsAndGate() {
        Module mod = factory.getAndGateBuilder().build();
        assertEquals(GATE_DIMENSION, mod.getRectangle().getSize());
    }

    @Test
    public void testDimensionsOrGate() {
        Module mod = factory.getOrGateBuilder().build();
        assertEquals(GATE_DIMENSION, mod.getRectangle().getSize());
    }

    @Test
    public void testDimensionsNotGate() {
        Module mod = factory.getNotGateBuilder().build();
        assertEquals(GATE_DIMENSION, mod.getRectangle().getSize());

    }

    @Test
    public void testDimensionsIdentityGate() {
        Module mod = factory.getIdentityGateBuilder().build();
        assertEquals(GATE_DIMENSION, mod.getRectangle().getSize());
    }

    @Test
    public void testDimensionsLamp() {
        Module mod = factory.getLampBuilder().build();
        assertEquals(LAMP_DIMENSION, mod.getRectangle().getSize());
    }

    @Test
    public void testDimensionsClock() {
        Module mod = factory.getClockBuilder().build();
        assertEquals(GATE_DIMENSION, mod.getRectangle().getSize());
    }

    @Test
    public void testDimensionsSwitch() {
        Module mod = factory.getSwitchBuilder().build();
        assertEquals(GATE_DIMENSION, mod.getRectangle().getSize());
    }

    @Test
    public void testDimensionsFlipFlop() {
        Module mod = factory.getFlipFlopBuilder().build();
        assertEquals(GATE_DIMENSION, mod.getRectangle().getSize());
    }

    @Test
    public void testDimensionsCircuit() {
        Module mod = factory.getCircuitBuilder().build();
        assertEquals(GATE_DIMENSION, mod.getRectangle().getSize());
    }
}

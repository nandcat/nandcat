package nandcat.model.importexport.sepaf;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import nandcat.model.element.FlipFlop;
import nandcat.model.element.Module;
import nandcat.model.element.Port;

import org.junit.Test;
import org.mockito.Mockito;

public class SEPAFFormatTest {

    @Test
    public void testGetInPortAsString() {
        assertEquals(
                "a",
                SEPAFFormat.getPortAsString(false, 0,
                        Mockito.mock(Module.class)));
        assertEquals(
                "b",
                SEPAFFormat.getPortAsString(false, 1,
                        Mockito.mock(Module.class)));
        assertEquals(
                "c",
                SEPAFFormat.getPortAsString(false, 2,
                        Mockito.mock(Module.class)));
        assertEquals(
                "d",
                SEPAFFormat.getPortAsString(false, 3,
                        Mockito.mock(Module.class)));
        assertEquals(
                "r",
                SEPAFFormat.getPortAsString(false, 0,
                        Mockito.mock(FlipFlop.class)));
        assertEquals(
                "s",
                SEPAFFormat.getPortAsString(false, 1,
                        Mockito.mock(FlipFlop.class)));
    }

    @Test
    public void testGetOutPortAsString() {
        assertEquals("o", SEPAFFormat.getPortAsString(true, 0,
                Mockito.mock(Module.class)));
        assertEquals("p", SEPAFFormat.getPortAsString(true, 1,
                Mockito.mock(Module.class)));
        assertEquals("q", SEPAFFormat.getPortAsString(true, 2,
                Mockito.mock(Module.class)));
        assertEquals("r", SEPAFFormat.getPortAsString(true, 3,
                Mockito.mock(Module.class)));
        assertEquals(
                "q",
                SEPAFFormat.getPortAsString(true, 0,
                        Mockito.mock(FlipFlop.class)));
        assertEquals(
                "nq",
                SEPAFFormat.getPortAsString(true, 1,
                        Mockito.mock(FlipFlop.class)));
    }

    @Test
    public void testGetPortFlipFlop() {
        FlipFlop f = Mockito.mock(FlipFlop.class);
        List<Port> inPorts = new LinkedList<Port>();
        List<Port> outPorts = new LinkedList<Port>();
        // TODO weiter
    }
}

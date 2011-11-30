package nandcat.model.importexport.sepaf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import nandcat.model.element.FlipFlop;
import nandcat.model.element.Module;
import nandcat.model.element.Port;
import nandcat.model.importexport.FormatException;
import org.jdom.Namespace;
import org.junit.Test;
import org.mockito.Mockito;

public class SEPAFFormatTest {

    @Test
    public void testGetInPortAsString() {
        assertEquals("a", SEPAFFormat.getPortAsString(false, 0, Mockito.mock(Module.class)));
        assertEquals("b", SEPAFFormat.getPortAsString(false, 1, Mockito.mock(Module.class)));
        assertEquals("c", SEPAFFormat.getPortAsString(false, 2, Mockito.mock(Module.class)));
        assertEquals("d", SEPAFFormat.getPortAsString(false, 3, Mockito.mock(Module.class)));
        assertEquals("r", SEPAFFormat.getPortAsString(false, 0, Mockito.mock(FlipFlop.class)));
        assertEquals("s", SEPAFFormat.getPortAsString(false, 1, Mockito.mock(FlipFlop.class)));
    }

    @Test
    public void testGetOutPortAsString() {
        assertEquals("o", SEPAFFormat.getPortAsString(true, 0, Mockito.mock(Module.class)));
        assertEquals("p", SEPAFFormat.getPortAsString(true, 1, Mockito.mock(Module.class)));
        assertEquals("q", SEPAFFormat.getPortAsString(true, 2, Mockito.mock(Module.class)));
        assertEquals("r", SEPAFFormat.getPortAsString(true, 3, Mockito.mock(Module.class)));
        assertEquals("q", SEPAFFormat.getPortAsString(true, 0, Mockito.mock(FlipFlop.class)));
        assertEquals("nq", SEPAFFormat.getPortAsString(true, 1, Mockito.mock(FlipFlop.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNegativePortAsString() {
        SEPAFFormat.getPortAsString(true, -1, Mockito.mock(Module.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWrongFlipFlopPortAsString() {
        SEPAFFormat.getPortAsString(true, 2, Mockito.mock(FlipFlop.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPrivateConstructor() throws Throwable {
        Constructor constructor = SEPAFFormat.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        try {
            constructor.newInstance();
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValidationschemaPrivateConstructor() throws Throwable {
        Constructor constructor = SEPAFFormat.VALIDATIONSCHEMA.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        try {
            constructor.newInstance();
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGatedefaultsPrivateConstructor() throws Throwable {
        Constructor constructor = SEPAFFormat.GATEDEFAULTS.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        try {
            constructor.newInstance();
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNamespacePrivateConstructor() throws Throwable {
        Constructor constructor = SEPAFFormat.NAMESPACE.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        try {
            constructor.newInstance();
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Test
    public void testNamespaceType() {
        if (!(SEPAFFormat.NAMESPACE.getDefault() instanceof Namespace)) {
            fail("Namespace not org.jdom.Namespace");
        }
    }

    @Test
    public void testGetInPortFlipFlop() throws FormatException {
        FlipFlop f = Mockito.mock(FlipFlop.class);
        List<Port> inPorts = new LinkedList<Port>();
        List<Port> outPorts = new LinkedList<Port>();
        Port firstPort = Mockito.mock(Port.class);
        inPorts.add(firstPort);
        Port secondPort = Mockito.mock(Port.class);
        inPorts.add(secondPort);

        Mockito.when(f.getInPorts()).thenReturn(inPorts);
        Mockito.when(f.getOutPorts()).thenReturn(outPorts);

        assertEquals(firstPort, SEPAFFormat.getStringAsPort(false, f, "r"));
        assertEquals(secondPort, SEPAFFormat.getStringAsPort(false, f, "s"));
    }

    @Test
    public void testGetOutPortFlipFlop() throws FormatException {
        FlipFlop f = Mockito.mock(FlipFlop.class);
        List<Port> inPorts = new LinkedList<Port>();
        List<Port> outPorts = new LinkedList<Port>();
        Port firstPort = Mockito.mock(Port.class);
        outPorts.add(firstPort);
        Port secondPort = Mockito.mock(Port.class);
        outPorts.add(secondPort);

        Mockito.when(f.getInPorts()).thenReturn(inPorts);
        Mockito.when(f.getOutPorts()).thenReturn(outPorts);

        assertEquals(firstPort, SEPAFFormat.getStringAsPort(true, f, "q"));
        assertEquals(secondPort, SEPAFFormat.getStringAsPort(true, f, "nq"));
    }

    @Test
    public void testGetInPortModule() throws FormatException {
        Module m = Mockito.mock(Module.class);
        List<Port> inPorts = new LinkedList<Port>();
        List<Port> outPorts = new LinkedList<Port>();
        Port firstPort = Mockito.mock(Port.class);
        inPorts.add(firstPort);
        Port secondPort = Mockito.mock(Port.class);
        inPorts.add(secondPort);
        Port thirdPort = Mockito.mock(Port.class);
        inPorts.add(thirdPort);

        Mockito.when(m.getInPorts()).thenReturn(inPorts);
        Mockito.when(m.getOutPorts()).thenReturn(outPorts);

        assertEquals(firstPort, SEPAFFormat.getStringAsPort(false, m, "a"));
        assertEquals(secondPort, SEPAFFormat.getStringAsPort(false, m, "b"));
        assertEquals(thirdPort, SEPAFFormat.getStringAsPort(false, m, "c"));
    }

    @Test
    public void testGetOutPortModule() throws FormatException {
        Module m = Mockito.mock(Module.class);
        List<Port> inPorts = new LinkedList<Port>();
        List<Port> outPorts = new LinkedList<Port>();
        Port firstPort = Mockito.mock(Port.class);
        outPorts.add(firstPort);
        Port secondPort = Mockito.mock(Port.class);
        outPorts.add(secondPort);
        Port thirdPort = Mockito.mock(Port.class);
        outPorts.add(thirdPort);

        Mockito.when(m.getInPorts()).thenReturn(inPorts);
        Mockito.when(m.getOutPorts()).thenReturn(outPorts);

        assertEquals(firstPort, SEPAFFormat.getStringAsPort(true, m, "o"));
        assertEquals(secondPort, SEPAFFormat.getStringAsPort(true, m, "p"));
        assertEquals(thirdPort, SEPAFFormat.getStringAsPort(true, m, "q"));
    }
}

package nandcat.view.elementdrawer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import nandcat.model.element.Module;
import nandcat.model.element.Port;
import nandcat.view.ElementDrawer;
import nandcat.view.StandardElementDrawer;
import org.junit.Before;
import org.junit.Test;

public class ElementDrawerPortPositionTest extends AbstractElementDrawerTest {

    private Graphics graphicMock;

    private Rectangle rec;

    private Module modMock;

    /**
     * SetUp mocked OrGate with 2 ports (active,inactive).
     */
    @Before
    public void setUp() {
        modMock = mock(Module.class);
        graphicMock = mock(Graphics.class);
    }

    @Test
    public void testIntersectionWithWholeModule() {
        Rectangle moduleBounds = new Rectangle(new Point(5, 5), new Dimension(100, 100));

        // prepare incoming port list
        List<Port> incomingPorts = new LinkedList<Port>();
        Port firstInPortMock = mock(Port.class);
        incomingPorts.add(firstInPortMock);

        // stub module
        when(modMock.getRectangle()).thenReturn(moduleBounds);
        when(modMock.getInPorts()).thenReturn(incomingPorts);

        // execute
        ElementDrawer elementDrawer = new StandardElementDrawer();
        elementDrawer.setGraphics(graphicMock);
        Port resultPort = elementDrawer.getPortAt(moduleBounds, modMock);

        // simply verify that port is inside the modules bounds
        assertTrue(resultPort != null);
        assertEquals(firstInPortMock, resultPort);
    }

    @Test
    public void testIntersectionWithLeftAndRight() {
        Rectangle moduleBounds = new Rectangle(new Point(0, 0), new Dimension(100, 100));

        // prepare incoming port list
        List<Port> incomingPorts = new LinkedList<Port>();
        Port firstInPortMock = mock(Port.class);
        incomingPorts.add(firstInPortMock);

        // prepare outgoing port list
        List<Port> outgoingPorts = new LinkedList<Port>();
        Port firstOutPortMock = mock(Port.class);
        outgoingPorts.add(firstOutPortMock);

        // stub module
        when(modMock.getRectangle()).thenReturn(moduleBounds);
        when(modMock.getInPorts()).thenReturn(incomingPorts);
        when(modMock.getOutPorts()).thenReturn(outgoingPorts);

        // create rectangle covering the right side of the module
        Rectangle rightModuleBounds = new Rectangle(new Point(50, 0), new Dimension(50, 100));

        // create rectangle covering the left side of the module
        Rectangle leftModuleBounds = new Rectangle(new Point(0, 0), new Dimension(50, 100));

        // execute
        ElementDrawer elementDrawer = new StandardElementDrawer();
        elementDrawer.setGraphics(graphicMock);
        Port resultPortRight = elementDrawer.getPortAt(rightModuleBounds, modMock);
        Port resultPortLeft = elementDrawer.getPortAt(leftModuleBounds, modMock);

        // left side must return incoming port
        assertTrue(resultPortLeft != null);
        assertEquals(firstInPortMock, resultPortLeft);

        // right side must return outgoing port
        assertTrue(resultPortRight != null);
        assertEquals(firstOutPortMock, resultPortRight);
    }

    @Test
    public void testNoPort() {
        Rectangle moduleBounds = new Rectangle(new Point(0, 0), new Dimension(100, 100));

        // prepare incoming port list
        List<Port> incomingPorts = new LinkedList<Port>();
        Port firstInPortMock = mock(Port.class);
        incomingPorts.add(firstInPortMock);

        // prepare outgoing port list
        List<Port> outgoingPorts = new LinkedList<Port>();
        Port firstOutPortMock = mock(Port.class);
        outgoingPorts.add(firstOutPortMock);

        // stub module
        when(modMock.getRectangle()).thenReturn(moduleBounds);
        when(modMock.getInPorts()).thenReturn(incomingPorts);
        when(modMock.getOutPorts()).thenReturn(outgoingPorts);

        // create rectangle covering none of the ports (at the middle)
        Rectangle atTheMiddle = new Rectangle(new Point(50, 50), new Dimension(5, 5));

        // execute
        ElementDrawer elementDrawer = new StandardElementDrawer();
        elementDrawer.setGraphics(graphicMock);
        Port resultPort = elementDrawer.getPortAt(atTheMiddle, modMock);

        // no port was intersected, must return null
        assertTrue(resultPort == null);
    }
}

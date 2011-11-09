package nandcat.view.elementdrawer;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;
import java.util.LinkedList;
import nandcat.model.element.FlipFlop;
import nandcat.model.element.Port;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

public class ElementDrawerFlipFlopTest extends AbstractModuleElementDrawerTest {

    // private Graphics graphicMock;
    //
    // private Rectangle rec;
    //
    // private FlipFlop gateMock;

    @Before
    public void setUp() {
        setUpForModuleClass(FlipFlop.class);
    }

    @Test
    public void testDrawSelected() {
        when(gateMock.isSelected()).thenReturn(true);
        stubModule();

        // action
        drawer.setGraphics(graphicMock);
        drawer.draw((FlipFlop) gateMock);

        verifyDrawModule();
        InOrder inOrder = inOrder(graphicMock);
        for (Port inPort : inPorts) {
            verifyDrawPort(inPort, inOrder);
        }
    }

    @Test
    public void testDraw() {
        when(gateMock.isSelected()).thenReturn(false);
        stubModule();

        // action
        drawer.setGraphics(graphicMock);
        drawer.draw((FlipFlop) gateMock);

        verifyDrawModule();
        InOrder inOrder = inOrder(graphicMock);
        for (Port inPort : inPorts) {
            verifyDrawPort(inPort, inOrder);
        }
    }

    private void stubModule() {
        when(gateMock.getRectangle()).thenReturn(rec);

        // in ports
        inPorts = new LinkedList<Port>();
        inPorts.add(stubPort(false));
        inPorts.add(stubPort(true));

        // out ports
        outPorts = new LinkedList<Port>();
        outPorts.add(stubPort(false));
        outPorts.add(stubPort(true));

        when(gateMock.getInPorts()).thenReturn(inPorts);
        when(gateMock.getOutPorts()).thenReturn(outPorts);

        // annotation
        when(gateMock.getName()).thenReturn(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDrawNullGate() {
        drawer.draw((FlipFlop) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDrawGateNullRectangle() {
        when(gateMock.getRectangle()).thenReturn(null);
        drawer.setGraphics(graphicMock);
        drawer.draw((FlipFlop) gateMock);
    }

    // @Before
    // public void setUp() {
    // gateMock = EasyMock.createMock(FlipFlop.class);
    // graphicMock = EasyMock.createStrictMock(Graphics.class);
    // }
    //
    // private void gateMockSetGeneralExpectations() {
    // LinkedList<Port> portList = new LinkedList<Port>();
    // Port port1 = EasyMock.createMock(Port.class);
    // EasyMock.expect(port1.getState()).andReturn(false).anyTimes();
    // EasyMock.replay(port1);
    // portList.add(port1);
    //
    // Port activePort = EasyMock.createMock(Port.class);
    // EasyMock.expect(activePort.getState()).andReturn(true).anyTimes();
    // EasyMock.replay(activePort);
    // portList.add(activePort);
    // rec = new Rectangle(21, 23, 100 + PORT_MARGIN_LEFT + PORT_MARGIN_RIGHT, 100 + PORT_MARGIN_TOP
    // + PORT_MARGIN_BOTTOM);
    // EasyMock.expect(gateMock.getRectangle()).andReturn(rec).anyTimes();
    // EasyMock.expect(gateMock.getInPorts()).andReturn(portList).anyTimes();
    // EasyMock.expect(gateMock.getOutPorts()).andReturn(portList).anyTimes();
    // EasyMock.expect(gateMock.getName()).andReturn(null).anyTimes();
    // }
    //
    // @Test
    // public void testDrawFlipFlop() {
    // gateMockSetGeneralExpectations();
    // EasyMock.expect(gateMock.isSelected()).andReturn(false).anyTimes();
    // EasyMock.replay(gateMock);
    // mockDrawRectangle();
    // mockDrawPorts();
    // EasyMock.replay(graphicMock);
    // drawer.setGraphics(graphicMock);
    // drawer.draw(gateMock);
    // EasyMock.verify(graphicMock);
    // }
    //
    // @Test
    // public void testDrawFlipFlopSelected() {
    // gateMockSetGeneralExpectations();
    // EasyMock.expect(gateMock.isSelected()).andReturn(true).anyTimes();
    // EasyMock.replay(gateMock);
    // mockDrawRectangle();
    // mockDrawPorts();
    // EasyMock.replay(graphicMock);
    // drawer.setGraphics(graphicMock);
    // drawer.draw(gateMock);
    // EasyMock.verify(graphicMock);
    // }
    //
    // private void mockDrawRectangle() {
    // if (gateMock.isSelected()) {
    // graphicMock.setColor(EasyMock.eq(GATE_COLOR_SELECTED));
    // } else {
    // graphicMock.setColor(EasyMock.eq(GATE_COLOR));
    // }
    // graphicMock.drawRect(EasyMock.eq(rec.x), EasyMock.eq(rec.y), EasyMock.eq(rec.width), EasyMock.eq(rec.height));
    // }
    //
    // private void mockDrawPorts() {
    // graphicMock.setColor(PORT_COLOR_DEFAULT);
    // graphicMock.drawOval(EasyMock.geq(rec.x), EasyMock.geq(rec.y), EasyMock.eq(PORT_DIAMETER),
    // EasyMock.eq(PORT_DIAMETER));
    // graphicMock.setColor(PORT_COLOR_ACTIVE);
    // graphicMock.drawOval(EasyMock.geq(rec.x), EasyMock.geq(rec.y), EasyMock.eq(PORT_DIAMETER),
    // EasyMock.eq(PORT_DIAMETER));
    // // Draw OutPorts
    // graphicMock.setColor(PORT_COLOR_DEFAULT);
    // graphicMock.drawOval(EasyMock.geq(rec.x), EasyMock.geq(rec.y), EasyMock.eq(PORT_DIAMETER),
    // EasyMock.eq(PORT_DIAMETER));
    // graphicMock.setColor(PORT_COLOR_ACTIVE);
    // graphicMock.drawOval(EasyMock.geq(rec.x), EasyMock.geq(rec.y), EasyMock.eq(PORT_DIAMETER),
    // EasyMock.eq(PORT_DIAMETER));
    // }
    //
    // @Test(expected = IllegalArgumentException.class)
    // public void testDrawNullGate() {
    // drawer.draw((FlipFlop) null);
    // }
    //
    // @Test(expected = IllegalArgumentException.class)
    // public void testDrawGateNullRectangle() {
    // EasyMock.reset(gateMock);
    // EasyMock.expect(gateMock.getRectangle()).andReturn(null);
    // EasyMock.replay(gateMock);
    // drawer.draw(gateMock);
    // }
}

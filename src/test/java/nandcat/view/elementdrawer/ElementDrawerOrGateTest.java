package nandcat.view.elementdrawer;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;
import nandcat.model.element.OrGate;
import nandcat.model.element.Port;
import org.easymock.classextension.EasyMock;
import org.easymock.classextension.IMocksControl;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests drawing of an OrGate.
 */
public class ElementDrawerOrGateTest extends AbstractElementDrawerTest {

    private Graphics graphicMock;

    private Rectangle rec;

    private OrGate gateMock;

    private IMocksControl strictControl;

    /**
     * Setup OrGate.
     */
    @Before
    public void setUp() {
        gateMock = EasyMock.createMock(OrGate.class);
        graphicMock = EasyMock.createStrictMock(Graphics.class);
    }

    private void gateMockSetGeneralExpectations() {
        LinkedList<Port> portList = new LinkedList<Port>();
        // Add ports
        Port port1 = EasyMock.createMock(Port.class);
        EasyMock.expect(port1.getState()).andReturn(false).anyTimes();
        EasyMock.replay(port1);
        portList.add(port1);

        Port activePort = EasyMock.createMock(Port.class);
        EasyMock.expect(activePort.getState()).andReturn(true).anyTimes();
        EasyMock.replay(activePort);
        portList.add(activePort);

        Port port2 = EasyMock.createMock(Port.class);
        EasyMock.expect(port2.getState()).andReturn(false).anyTimes();
        EasyMock.replay(port2);
        portList.add(port2);
        rec = new Rectangle(21, 23, 100 + PORT_MARGIN_LEFT + PORT_MARGIN_RIGHT, 100 + PORT_MARGIN_TOP
                + PORT_MARGIN_BOTTOM);
        EasyMock.expect(gateMock.getRectangle()).andReturn(rec).anyTimes();
        EasyMock.expect(gateMock.getInPorts()).andReturn(portList).anyTimes();
        EasyMock.expect(gateMock.getOutPorts()).andReturn(portList).anyTimes();
        EasyMock.expect(gateMock.getName()).andReturn(null).anyTimes();
    }

    /**
     * Tests drawing of OrGate. Using strict Mocking, the whole drawing process has to be in one test. Important to
     * preserve ordering.
     */
    @Test
    public void testDrawOrGate() {
        gateMockSetGeneralExpectations();
        EasyMock.expect(gateMock.isSelected()).andReturn(false).anyTimes();
        EasyMock.replay(gateMock);
        mockDrawRectangle();
        mockDrawPorts();
        mockDrawLabel();
        EasyMock.replay(graphicMock);
        drawer.setGraphics(graphicMock);
        drawer.draw(gateMock);
        EasyMock.verify(graphicMock);
    }

    @Test
    public void testDrawOrGateSelected() {
        gateMockSetGeneralExpectations();
        EasyMock.expect(gateMock.isSelected()).andReturn(true).anyTimes();
        EasyMock.replay(gateMock);
        mockDrawRectangle();
        mockDrawPorts();
        mockDrawLabel();
        EasyMock.replay(graphicMock);
        drawer.setGraphics(graphicMock);
        drawer.draw(gateMock);
        EasyMock.verify(graphicMock);
    }

    private void mockDrawRectangle() {
        if (gateMock.isSelected()) {
            graphicMock.setColor(EasyMock.eq(GATE_COLOR_SELECTED));
        } else {
            graphicMock.setColor(EasyMock.eq(GATE_COLOR));
        }
        graphicMock.drawRect(EasyMock.eq(rec.x), EasyMock.eq(rec.y), EasyMock.eq(rec.width), EasyMock.eq(rec.height));
    }

    private void mockDrawLabel() {
        graphicMock.setColor(EasyMock.eq(LABEL_COLOR));
        graphicMock.drawString(EasyMock.eq(LABEL_ORGATE), EasyMock.geq(rec.x), EasyMock.geq(rec.y));
    }

    private void mockDrawPorts() {
        // Draw InPorts
        graphicMock.setColor(PORT_COLOR_DEFAULT);
        graphicMock.drawOval(EasyMock.geq(rec.x), EasyMock.geq(rec.y), EasyMock.eq(PORT_DIAMETER),
                EasyMock.eq(PORT_DIAMETER));
        graphicMock.setColor(PORT_COLOR_ACTIVE);
        graphicMock.drawOval(EasyMock.geq(rec.x), EasyMock.geq(rec.y), EasyMock.eq(PORT_DIAMETER),
                EasyMock.eq(PORT_DIAMETER));
        graphicMock.setColor(PORT_COLOR_DEFAULT);
        graphicMock.drawOval(EasyMock.geq(rec.x), EasyMock.geq(rec.y), EasyMock.eq(PORT_DIAMETER),
                EasyMock.eq(PORT_DIAMETER));
        // Draw OutPorts
        graphicMock.setColor(PORT_COLOR_DEFAULT);
        graphicMock.drawOval(EasyMock.geq(rec.x), EasyMock.geq(rec.y), EasyMock.eq(PORT_DIAMETER),
                EasyMock.eq(PORT_DIAMETER));
        graphicMock.setColor(PORT_COLOR_ACTIVE);
        graphicMock.drawOval(EasyMock.geq(rec.x), EasyMock.geq(rec.y), EasyMock.eq(PORT_DIAMETER),
                EasyMock.eq(PORT_DIAMETER));
        graphicMock.setColor(PORT_COLOR_DEFAULT);
        graphicMock.drawOval(EasyMock.geq(rec.x), EasyMock.geq(rec.y), EasyMock.eq(PORT_DIAMETER),
                EasyMock.eq(PORT_DIAMETER));
    }

    private void mockDrawAnnotation() {
        graphicMock.setColor(ANNOTATION_COLOR);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDrawNullGate() {
        drawer.draw((OrGate) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDrawGateNullRectangle() {
        EasyMock.reset(gateMock);
        EasyMock.expect(gateMock.getRectangle()).andReturn(null);
        EasyMock.replay(gateMock);
        drawer.draw(gateMock);
    }
}

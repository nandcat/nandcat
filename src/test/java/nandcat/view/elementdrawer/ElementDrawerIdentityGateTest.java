package nandcat.view.elementdrawer;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;
import nandcat.model.element.IdentityGate;
import nandcat.model.element.Port;
import org.easymock.classextension.EasyMock;
import org.junit.Before;
import org.junit.Test;

public class ElementDrawerIdentityGateTest extends AbstractElementDrawerTest {

    private Graphics graphicMock;

    private Rectangle rec;

    private IdentityGate gateMock;

    @Before
    public void setUp() {
        gateMock = EasyMock.createMock(IdentityGate.class);
        graphicMock = EasyMock.createStrictMock(Graphics.class);
    }

    private void gateMockSetGeneralExpectations() {
        LinkedList<Port> portList = new LinkedList<Port>();
        LinkedList<Port> portListOut = new LinkedList<Port>();
        Port activePort = EasyMock.createMock(Port.class);
        EasyMock.expect(activePort.getState()).andReturn(true).anyTimes();
        EasyMock.replay(activePort);
        portList.add(activePort);
        Port activeOutPort1 = EasyMock.createMock(Port.class);
        EasyMock.expect(activeOutPort1.getState()).andReturn(true).anyTimes();
        EasyMock.replay(activeOutPort1);
        Port activeOutPort2 = EasyMock.createMock(Port.class);
        EasyMock.expect(activeOutPort2.getState()).andReturn(true).anyTimes();
        EasyMock.replay(activeOutPort2);
        portListOut.add(activeOutPort1);
        portListOut.add(activeOutPort2);
        rec = new Rectangle(21, 23, 100 + PORT_MARGIN_LEFT + PORT_MARGIN_RIGHT, 100 + PORT_MARGIN_TOP
                + PORT_MARGIN_BOTTOM);
        EasyMock.expect(gateMock.getRectangle()).andReturn(rec).anyTimes();
        EasyMock.expect(gateMock.getInPorts()).andReturn(portList).anyTimes();
        EasyMock.expect(gateMock.getOutPorts()).andReturn(portListOut).anyTimes();
    }

    @Test
    public void testDrawIdentityGateSelected() {
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

    @Test
    public void testDrawIdentityGate() {
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
        graphicMock.drawString(EasyMock.eq(LABEL_IDENTITYGATE), EasyMock.geq(rec.x), EasyMock.geq(rec.y));
    }

    private void mockDrawPorts() {
        graphicMock.setColor(PORT_COLOR_ACTIVE);
        graphicMock.drawOval(EasyMock.geq(rec.x), EasyMock.geq(rec.y), EasyMock.eq(PORT_DIAMETER),
                EasyMock.eq(PORT_DIAMETER));
        // Draw OutPorts
        graphicMock.setColor(PORT_COLOR_ACTIVE);
        graphicMock.drawOval(EasyMock.geq(rec.x), EasyMock.geq(rec.y), EasyMock.eq(PORT_DIAMETER),
                EasyMock.eq(PORT_DIAMETER));
        graphicMock.setColor(PORT_COLOR_ACTIVE);
        graphicMock.drawOval(EasyMock.geq(rec.x), EasyMock.geq(rec.y), EasyMock.eq(PORT_DIAMETER),
                EasyMock.eq(PORT_DIAMETER));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDrawNullGate() {
        drawer.draw((IdentityGate) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDrawGateNullRectangle() {
        EasyMock.reset(gateMock);
        EasyMock.expect(gateMock.getRectangle()).andReturn(null);
        EasyMock.replay(gateMock);
        drawer.draw(gateMock);
    }
}

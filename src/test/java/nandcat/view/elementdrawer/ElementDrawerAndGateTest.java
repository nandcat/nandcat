package nandcat.view.elementdrawer;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;
import nandcat.model.element.AndGate;
import nandcat.model.element.Port;
import org.easymock.classextension.EasyMock;
import org.junit.Before;
import org.junit.Test;

public class ElementDrawerAndGateTest extends AbstractElementDrawerTest {

    private Graphics graphicMock;

    private Rectangle rec;

    private AndGate gateMock;

    @Before
    public void setUp() {
        gateMock = EasyMock.createMock(AndGate.class);
        graphicMock = EasyMock.createStrictMock(Graphics.class);
        LinkedList<Port> portList = new LinkedList<Port>();
        portList.add(new Port(gateMock));
        Port activePort = EasyMock.createMock(Port.class);
        EasyMock.expect(activePort.getState()).andReturn(true).anyTimes();
        EasyMock.replay(activePort);
        portList.add(activePort);
        portList.add(new Port(gateMock));
        rec = new Rectangle(21, 23, 100 + PORT_MARGIN_LEFT + PORT_MARGIN_RIGHT, 100 + PORT_MARGIN_TOP
                + PORT_MARGIN_BOTTOM);
        EasyMock.expect(gateMock.getRectangle()).andReturn(rec).anyTimes();
        EasyMock.expect(gateMock.getInPorts()).andReturn(portList).anyTimes();
        EasyMock.expect(gateMock.getOutPorts()).andReturn(portList).anyTimes();
        EasyMock.replay(gateMock);
    }
    
    @Test
    public void testDrawAndGate(){
        mockDrawRectangle();
        mockDrawPorts();
        mockDrawLabel();
        EasyMock.replay(graphicMock);
        drawer.setGraphics(graphicMock);
        drawer.draw(gateMock);
        EasyMock.verify(graphicMock);
    }

    private void mockDrawRectangle() {
        graphicMock.setColor(EasyMock.eq(GATE_COLOR));
        graphicMock.drawRect(EasyMock.eq(rec.x), EasyMock.eq(rec.y), EasyMock.eq(rec.width), EasyMock.eq(rec.height));
    }

    private void mockDrawLabel() {
        graphicMock.setColor(EasyMock.eq(LABEL_COLOR));
        graphicMock.drawString(EasyMock.eq(LABEL_ANDGATE), EasyMock.geq(rec.x), EasyMock.geq(rec.y));
    }

    private void mockDrawPorts() {
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

    @Test(expected = IllegalArgumentException.class)
    public void testDrawNullGate() {
        drawer.draw((AndGate) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDrawGateNullRectangle() {
        EasyMock.reset(gateMock);
        EasyMock.expect(gateMock.getRectangle()).andReturn(null);
        EasyMock.replay(gateMock);
        drawer.draw(gateMock);
    }
}

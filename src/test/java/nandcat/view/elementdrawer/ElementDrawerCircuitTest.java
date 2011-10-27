package nandcat.view.elementdrawer;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;
import nandcat.model.element.Circuit;
import nandcat.model.element.Port;
import org.easymock.classextension.EasyMock;
import org.junit.Before;
import org.junit.Test;

public class ElementDrawerCircuitTest extends AbstractElementDrawerTest {

    private Graphics graphicMock;

    private Rectangle rec;

    private Circuit circuitMock;

    @Before
    public void setUp() {
        circuitMock = EasyMock.createMock(Circuit.class);
        graphicMock = EasyMock.createStrictMock(Graphics.class);
        LinkedList<Port> portList = new LinkedList<Port>();
        portList.add(new Port(circuitMock));
        Port activePort = EasyMock.createMock(Port.class);
        EasyMock.expect(activePort.getState()).andReturn(true).anyTimes();
        EasyMock.replay(activePort);
        portList.add(activePort);
        portList.add(new Port(circuitMock));
        portList.add(new Port(circuitMock));
        portList.add(new Port(circuitMock));
        portList.add(new Port(circuitMock));
        rec = new Rectangle(21, 23, 100 + PORT_MARGIN_LEFT + PORT_MARGIN_RIGHT, 100 + PORT_MARGIN_TOP
                + PORT_MARGIN_BOTTOM);
        EasyMock.expect(circuitMock.getRectangle()).andReturn(rec).anyTimes();
        EasyMock.expect(circuitMock.getInPorts()).andReturn(portList).anyTimes();
        EasyMock.expect(circuitMock.getOutPorts()).andReturn(portList).anyTimes();
        EasyMock.expect(circuitMock.getName()).andReturn("Test Circuit").anyTimes();
        EasyMock.replay(circuitMock);
    }

    @Test
    public void testDrawCircuit() {
        mockDrawRectangle();
        mockDrawPorts();
        mockDrawLabel();
        EasyMock.replay(graphicMock);
        drawer.setGraphics(graphicMock);
        drawer.draw(circuitMock);
        EasyMock.verify(graphicMock);
    }

    private void mockDrawRectangle() {
        graphicMock.setColor(EasyMock.eq(GATE_COLOR));
        graphicMock.drawRect(EasyMock.eq(rec.x), EasyMock.eq(rec.y), EasyMock.eq(rec.width), EasyMock.eq(rec.height));
    }

    private void mockDrawLabel() {
        graphicMock.setColor(EasyMock.eq(LABEL_COLOR));
        graphicMock.drawString(EasyMock.eq(circuitMock.getName()), EasyMock.geq(rec.x), EasyMock.geq(rec.y));
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
        graphicMock.setColor(PORT_COLOR_DEFAULT);
        graphicMock.drawOval(EasyMock.geq(rec.x), EasyMock.geq(rec.y), EasyMock.eq(PORT_DIAMETER),
                EasyMock.eq(PORT_DIAMETER));
        graphicMock.setColor(PORT_COLOR_DEFAULT);
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
        graphicMock.setColor(PORT_COLOR_DEFAULT);
        graphicMock.drawOval(EasyMock.geq(rec.x), EasyMock.geq(rec.y), EasyMock.eq(PORT_DIAMETER),
                EasyMock.eq(PORT_DIAMETER));
        graphicMock.setColor(PORT_COLOR_DEFAULT);
        graphicMock.drawOval(EasyMock.geq(rec.x), EasyMock.geq(rec.y), EasyMock.eq(PORT_DIAMETER),
                EasyMock.eq(PORT_DIAMETER));
        graphicMock.setColor(PORT_COLOR_DEFAULT);
        graphicMock.drawOval(EasyMock.geq(rec.x), EasyMock.geq(rec.y), EasyMock.eq(PORT_DIAMETER),
                EasyMock.eq(PORT_DIAMETER));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDrawNullCircuit() {
        drawer.draw((Circuit) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDrawCircuitNullRectangle() {
        EasyMock.reset(circuitMock);
        EasyMock.expect(circuitMock.getRectangle()).andReturn(null);
        EasyMock.replay(circuitMock);
        drawer.draw(circuitMock);
    }
}

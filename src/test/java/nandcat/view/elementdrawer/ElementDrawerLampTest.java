package nandcat.view.elementdrawer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;
import nandcat.model.element.Lamp;
import nandcat.model.element.Port;
import org.easymock.classextension.EasyMock;
import org.junit.Before;
import org.junit.Test;

public class ElementDrawerLampTest extends AbstractElementDrawerTest {

    private Graphics graphicMock;

    private Rectangle rec;

    private Lamp lampMock;

    @Before
    public void setUp() {
        lampMock = EasyMock.createMock(Lamp.class);
        graphicMock = EasyMock.createStrictMock(Graphics.class);
        LinkedList<Port> portList = new LinkedList<Port>();
        LinkedList<Port> portOutList = new LinkedList<Port>();
        Port activePort = EasyMock.createMock(Port.class);
        EasyMock.expect(activePort.getState()).andReturn(true).anyTimes();
        EasyMock.replay(activePort);
        portList.add(activePort);
        rec = new Rectangle(21, 23, 100 + PORT_MARGIN_LEFT + PORT_MARGIN_RIGHT, 100 + PORT_MARGIN_TOP
                + PORT_MARGIN_BOTTOM);
        EasyMock.expect(lampMock.getRectangle()).andReturn(rec).anyTimes();
        EasyMock.expect(lampMock.getInPorts()).andReturn(portList).anyTimes();
        EasyMock.expect(lampMock.getOutPorts()).andReturn(portOutList).anyTimes();
        EasyMock.expect(lampMock.getState()).andReturn(true).anyTimes();
        EasyMock.replay(lampMock);
    }

    @Test
    public void testDrawLamp(){
        mockDrawBorder();
        mockDrawPorts();
        EasyMock.replay(graphicMock);
        drawer.setGraphics(graphicMock);
        drawer.draw(lampMock);
        EasyMock.verify(graphicMock);
    }

    private void mockDrawBorder() {
        graphicMock.setColor(EasyMock.eq(LAMP_COLOR_ACTIVE));
        graphicMock.fillOval(EasyMock.eq(rec.x), EasyMock.eq(rec.y), EasyMock.eq(rec.width), EasyMock.eq(rec.height));
        graphicMock.setColor(EasyMock.eq(LAMP_COLOR));
        graphicMock.drawOval(EasyMock.eq(rec.x), EasyMock.eq(rec.y), EasyMock.eq(rec.width), EasyMock.eq(rec.height));

    }

    private void mockDrawPorts() {
        graphicMock.setColor(PORT_COLOR_ACTIVE);
        graphicMock.drawOval(EasyMock.gt(rec.x), EasyMock.gt(rec.y), EasyMock.eq(PORT_DIAMETER),
                EasyMock.eq(PORT_DIAMETER));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDrawNullLamp() {
        drawer.draw((Lamp) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDrawLampNullRectangle() {
        EasyMock.reset(lampMock);
        EasyMock.expect(lampMock.getRectangle()).andReturn(null);
        EasyMock.replay(lampMock);
        drawer.draw(lampMock);
    }
}

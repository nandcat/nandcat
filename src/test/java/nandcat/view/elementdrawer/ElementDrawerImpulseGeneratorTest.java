package nandcat.view.elementdrawer;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;
import nandcat.model.element.ImpulseGenerator;
import nandcat.model.element.Port;
import org.easymock.classextension.EasyMock;
import org.junit.Before;
import org.junit.Test;

public class ElementDrawerImpulseGeneratorTest extends AbstractElementDrawerTest {

    private Graphics graphicMock;

    private Rectangle rec;

    private ImpulseGenerator igMock;

    @Before
    public void setUp() {
        igMock = EasyMock.createMock(ImpulseGenerator.class);
        graphicMock = EasyMock.createStrictMock(Graphics.class);
        LinkedList<Port> portList = new LinkedList<Port>();
        portList.add(new Port(igMock));
        Port activePort = EasyMock.createMock(Port.class);
        EasyMock.expect(activePort.getState()).andReturn(true).anyTimes();
        EasyMock.replay(activePort);
        portList.add(activePort);
        portList.add(new Port(igMock));
        rec = new Rectangle(21, 23, 100 + PORT_MARGIN_LEFT + PORT_MARGIN_RIGHT, 100 + PORT_MARGIN_TOP
                + PORT_MARGIN_BOTTOM);
        EasyMock.expect(igMock.getRectangle()).andReturn(rec).anyTimes();
        EasyMock.expect(igMock.getInPorts()).andReturn(portList).anyTimes();
        EasyMock.expect(igMock.getOutPorts()).andReturn(portList).anyTimes();
        EasyMock.expect(igMock.getState()).andReturn(true).anyTimes();
        EasyMock.expect(igMock.getFrequency()).andReturn(0).anyTimes();
        EasyMock.replay(igMock);
    }

    @Test
    public void testDrawImpulseGenerator() {
        mockDrawRectangle();
        mockDrawState();
        mockDrawPorts();
        mockDrawLabel();
        EasyMock.replay(graphicMock);
        drawer.setGraphics(graphicMock);
        drawer.draw(igMock);
        EasyMock.verify(graphicMock);
    }

    private void mockDrawRectangle() {
        // Select Color
        graphicMock.setColor(EasyMock.eq(IG_COLOR));
        EasyMock.expectLastCall();
        // Draw Gate rectangle
        graphicMock.drawRect(EasyMock.eq(rec.x), EasyMock.eq(rec.y), EasyMock.eq(rec.width), EasyMock.eq(rec.height));
    }

    private void mockDrawState() {
        graphicMock.setColor(EasyMock.eq(IG_COLOR_ACTIVE));
        graphicMock.fillRect(EasyMock.gt(rec.x), EasyMock.gt(rec.y), EasyMock.lt(rec.width), EasyMock.lt(rec.height));
        graphicMock.setColor(EasyMock.eq(IG_COLOR));
        graphicMock.drawRect(EasyMock.gt(rec.x), EasyMock.gt(rec.y), EasyMock.lt(rec.width), EasyMock.lt(rec.height));
    }

    private void mockDrawLabel() {
        igMock.getFrequency();
        graphicMock.setColor(EasyMock.eq(LABEL_COLOR));
        graphicMock.drawString(EasyMock.contains(Integer.toString(igMock.getFrequency())), EasyMock.geq(rec.x),
                EasyMock.geq(rec.y));
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
    public void testDrawNullImpulseGenerator() {
        drawer.draw((ImpulseGenerator) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDrawImpulseGeneratorNullRectangle() {
        EasyMock.reset(igMock);
        EasyMock.expect(igMock.getRectangle()).andReturn(null);
        EasyMock.replay(igMock);
        drawer.draw(igMock);
    }
}

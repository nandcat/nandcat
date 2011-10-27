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

    private ImpulseGenerator gateMock;

    @Before
    public void setUp() {
        gateMock = EasyMock.createMock(ImpulseGenerator.class);
        graphicMock = EasyMock.createStrictMock(Graphics.class);
    }

    private void gateMockSetGeneralExpectations() {
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
        EasyMock.expect(gateMock.getState()).andReturn(true).anyTimes();
        EasyMock.expect(gateMock.getFrequency()).andReturn(0).anyTimes();
    }

    @Test
    public void testDrawImpulseGenerator() {
        gateMockSetGeneralExpectations();
        EasyMock.expect(gateMock.isSelected()).andReturn(false).anyTimes();
        EasyMock.replay(gateMock);
        mockDrawRectangle();
        mockDrawState();
        mockDrawPorts();
        mockDrawLabel();
        EasyMock.replay(graphicMock);
        drawer.setGraphics(graphicMock);
        drawer.draw(gateMock);
        EasyMock.verify(graphicMock);
    }

    @Test
    public void testDrawImpulseGeneratorSelected() {
        gateMockSetGeneralExpectations();
        EasyMock.expect(gateMock.isSelected()).andReturn(true).anyTimes();
        EasyMock.replay(gateMock);
        mockDrawRectangle();
        mockDrawState();
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
        // Draw Gate rectangle
        graphicMock.drawRect(EasyMock.eq(rec.x), EasyMock.eq(rec.y), EasyMock.eq(rec.width), EasyMock.eq(rec.height));
    }

    private void mockDrawState() {
        graphicMock.setColor(EasyMock.eq(IG_COLOR_ACTIVE));
        graphicMock.fillRect(EasyMock.gt(rec.x), EasyMock.gt(rec.y), EasyMock.lt(rec.width), EasyMock.lt(rec.height));
        graphicMock.setColor(EasyMock.eq(GATE_COLOR));
        graphicMock.drawRect(EasyMock.gt(rec.x), EasyMock.gt(rec.y), EasyMock.lt(rec.width), EasyMock.lt(rec.height));
    }

    private void mockDrawLabel() {
        gateMock.getFrequency();
        graphicMock.setColor(EasyMock.eq(LABEL_COLOR));
        graphicMock.drawString(EasyMock.contains(Integer.toString(gateMock.getFrequency())), EasyMock.geq(rec.x),
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
        EasyMock.reset(gateMock);
        EasyMock.expect(gateMock.getRectangle()).andReturn(null);
        EasyMock.replay(gateMock);
        drawer.draw(gateMock);
    }
}

package nandcat.view.elementdrawer;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;
import nandcat.model.element.OrGate;
import nandcat.model.element.Port;
import org.easymock.classextension.EasyMock;
import org.junit.Before;
import org.junit.Test;

/**
 * Test drawing of an OrGate with accurate port positions and outline.
 */
public class ElementDrawerPortTest extends AbstractElementDrawerTest {

    private Graphics graphicMock;

    private Rectangle rec;

    private OrGate gateMock;

    /**
     * SetUp mocked OrGate with 2 ports (active,inactive).
     */
    @Before
    public void setUp() {
        gateMock = EasyMock.createMock(OrGate.class);
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
        EasyMock.expect(gateMock.getInPorts()).andReturn(portList);
        EasyMock.expect(gateMock.getOutPorts()).andReturn(portList);
    }

    @Test
    public void testDrawOrGateWithPorts() throws Exception {
        gateMockSetGeneralExpectations();
        EasyMock.expect(gateMock.isSelected()).andReturn(false);
        EasyMock.replay(gateMock);
        verifyDrawOrGateWithPorts(false);
    }

    @Test
    public void testDrawOrGateWithPortsSelected() throws Exception {
        gateMockSetGeneralExpectations();
        EasyMock.expect(gateMock.isSelected()).andReturn(true);
        EasyMock.replay(gateMock);
        verifyDrawOrGateWithPorts(true);
    }

    /**
     * Test drawing an OrGate with ports accurate.
     * 
     * @throws Exception
     *             Fail on exception.
     */
    private void verifyDrawOrGateWithPorts(boolean isSelected) throws Exception {
        // Draw outline of the or gate.
        if (isSelected) {
            graphicMock.setColor(EasyMock.eq(GATE_COLOR_SELECTED));
        } else {
            graphicMock.setColor(EasyMock.eq(GATE_COLOR));
        }
        EasyMock.expectLastCall().once();
        graphicMock.drawRect(rec.x, rec.y, rec.height, rec.width);
        EasyMock.expectLastCall().once();
        // Draw InPorts
        graphicMock.setColor(PORT_COLOR_DEFAULT);
        EasyMock.expectLastCall().once();
        graphicMock.drawOval(EasyMock.eq(rec.x + PORT_MARGIN_LEFT),
                EasyMock.eq(PORT_MARGIN_TOP + rec.y + 25 - PORT_DIAMETER / 2), EasyMock.eq(PORT_DIAMETER),
                EasyMock.eq(PORT_DIAMETER));
        EasyMock.expectLastCall().once();
        graphicMock.setColor(PORT_COLOR_ACTIVE);
        EasyMock.expectLastCall().once();
        graphicMock.drawOval(EasyMock.eq(rec.x + PORT_MARGIN_LEFT),
                EasyMock.eq(PORT_MARGIN_TOP + rec.y + 50 - PORT_DIAMETER / 2), EasyMock.eq(PORT_DIAMETER),
                EasyMock.eq(PORT_DIAMETER));
        EasyMock.expectLastCall().once();
        graphicMock.setColor(PORT_COLOR_DEFAULT);
        EasyMock.expectLastCall().once();
        graphicMock.drawOval(EasyMock.eq(rec.x + PORT_MARGIN_LEFT),
                EasyMock.eq(PORT_MARGIN_TOP + rec.y + 75 - PORT_DIAMETER / 2), EasyMock.eq(PORT_DIAMETER),
                EasyMock.eq(PORT_DIAMETER));
        EasyMock.expectLastCall().once();
        // // Draw OutPorts
        graphicMock.setColor(PORT_COLOR_DEFAULT);
        EasyMock.expectLastCall().once();
        graphicMock.drawOval(EasyMock.eq(rec.x + rec.width - PORT_MARGIN_RIGHT - PORT_DIAMETER),
                EasyMock.eq(25 - PORT_DIAMETER / 2 + PORT_MARGIN_TOP + rec.y), EasyMock.eq(PORT_DIAMETER),
                EasyMock.eq(PORT_DIAMETER));
        EasyMock.expectLastCall().once();
        graphicMock.setColor(PORT_COLOR_ACTIVE);
        EasyMock.expectLastCall().once();
        graphicMock.drawOval(EasyMock.eq(rec.x + rec.width - PORT_MARGIN_RIGHT - PORT_DIAMETER),
                EasyMock.eq(50 - PORT_DIAMETER / 2 + PORT_MARGIN_TOP + rec.y), EasyMock.eq(PORT_DIAMETER),
                EasyMock.eq(PORT_DIAMETER));
        EasyMock.expectLastCall().once();
        graphicMock.setColor(PORT_COLOR_DEFAULT);
        EasyMock.expectLastCall().once();
        graphicMock.drawOval(EasyMock.eq(rec.x + rec.width - PORT_MARGIN_RIGHT - PORT_DIAMETER),
                EasyMock.eq(75 - PORT_DIAMETER / 2 + PORT_MARGIN_TOP + rec.y), EasyMock.eq(PORT_DIAMETER),
                EasyMock.eq(PORT_DIAMETER));
        EasyMock.expectLastCall().once();
        graphicMock.setColor(LABEL_COLOR);
        EasyMock.expectLastCall().once();
        graphicMock.drawString(EasyMock.eq(LABEL_ORGATE), EasyMock.geq(rec.x), EasyMock.geq(rec.y));
        EasyMock.expectLastCall().once();
        EasyMock.replay(graphicMock);
        drawer.setGraphics(graphicMock);
        drawer.draw(gateMock);
        EasyMock.verify(graphicMock);
        EasyMock.verify(gateMock);
    }
}

package nandcat.view.elementdrawer;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import nandcat.model.element.Connection;
import nandcat.model.element.Module;
import nandcat.model.element.Port;
import org.easymock.classextension.EasyMock;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests connections drawn right. TODO: Code smells a lot! StrictMocks!
 */
public class ElementDrawerConnectionTest extends AbstractElementDrawerTest {

    /**
     * General setups before every test.
     */
    @Before
    public void setUp() {
    }

    @Ignore
    @Test
    public void testDrawConnection() throws Exception {
        Graphics graphicMock = EasyMock.createNiceMock(Graphics.class);
        Connection con = EasyMock.createNiceMock(Connection.class);
        Module moduleWithInPort = EasyMock.createNiceMock(Module.class);
        Rectangle moduleWithInPortRectangle = new Rectangle(0, 0, 100, 100);
        Module moduleWithOutPort = EasyMock.createNiceMock(Module.class);
        Rectangle moduleWithOutPortRectangle = new Rectangle(100, 100, 100, 100);
        Port outPort = EasyMock.createNiceMock(Port.class);
        Port inPort = EasyMock.createNiceMock(Port.class);
        List<Port> inPorts = new LinkedList<Port>();
        inPorts.add(inPort);
        List<Port> outPorts = new LinkedList<Port>();
        outPorts.add(outPort);
        EasyMock.expect(con.getOutPort()).andReturn(outPort).anyTimes();
        EasyMock.expect(con.getInPort()).andReturn(inPort).anyTimes();
        EasyMock.expect(con.getState()).andReturn(true).anyTimes();
        EasyMock.expect(con.getNextModule()).andReturn(moduleWithInPort).anyTimes();
        EasyMock.expect(con.getPreviousModule()).andReturn(moduleWithOutPort).anyTimes();
        EasyMock.expect(outPort.getModule()).andReturn(moduleWithOutPort).anyTimes();
        EasyMock.expect(inPort.getModule()).andReturn(moduleWithInPort).anyTimes();
        EasyMock.expect(inPort.isOutPort()).andReturn(false).anyTimes();
        EasyMock.expect(outPort.isOutPort()).andReturn(true).anyTimes();
        EasyMock.expect(moduleWithInPort.getInPorts()).andReturn(inPorts).anyTimes();
        EasyMock.expect(moduleWithOutPort.getOutPorts()).andReturn(outPorts).anyTimes();
        EasyMock.expect(moduleWithInPort.getRectangle()).andReturn(moduleWithInPortRectangle).anyTimes();
        EasyMock.expect(moduleWithOutPort.getRectangle()).andReturn(moduleWithOutPortRectangle).anyTimes();
        Point outPortPoint = new Point(moduleWithOutPortRectangle.x + moduleWithOutPortRectangle.width
                - PORT_MARGIN_RIGHT - (PORT_DIAMETER / 2), moduleWithOutPortRectangle.y
                + moduleWithOutPortRectangle.height / 2);
        Point inPortPoint = new Point(PORT_MARGIN_LEFT + (PORT_DIAMETER / 2) + moduleWithInPortRectangle.x,
                moduleWithInPortRectangle.height / 2 + moduleWithInPortRectangle.y);
        EasyMock.replay(con);
        EasyMock.replay(moduleWithInPort);
        EasyMock.replay(moduleWithOutPort);
        EasyMock.replay(outPort);
        EasyMock.replay(inPort);
        graphicMock.drawLine(outPortPoint.x, outPortPoint.y, inPortPoint.x, inPortPoint.y);
        EasyMock.expectLastCall();
        EasyMock.replay(graphicMock);
        drawer.setGraphics(graphicMock);
        drawer.draw(con);
        EasyMock.verify(graphicMock);
    }

    @Ignore
    @Test(expected = IllegalArgumentException.class)
    public void testPortHasNullModule() {
        Graphics graphicMock = EasyMock.createNiceMock(Graphics.class);
        Connection con = EasyMock.createNiceMock(Connection.class);
        Module moduleWithInPort = EasyMock.createNiceMock(Module.class);
        Rectangle moduleWithInPortRectangle = new Rectangle(0, 0, 100, 100);
        Module moduleWithOutPort = EasyMock.createNiceMock(Module.class);
        Rectangle moduleWithOutPortRectangle = new Rectangle(100, 100, 100, 100);
        Port outPort = EasyMock.createNiceMock(Port.class);
        Port inPort = EasyMock.createNiceMock(Port.class);
        List<Port> inPorts = new LinkedList<Port>();
        inPorts.add(inPort);
        List<Port> outPorts = new LinkedList<Port>();
        outPorts.add(outPort);
        EasyMock.expect(con.getOutPort()).andReturn(outPort).anyTimes();
        EasyMock.expect(con.getInPort()).andReturn(inPort).anyTimes();
        EasyMock.expect(con.getState()).andReturn(true).anyTimes();
        EasyMock.expect(con.getNextModule()).andReturn(moduleWithInPort).anyTimes();
        EasyMock.expect(con.getPreviousModule()).andReturn(moduleWithOutPort).anyTimes();
        EasyMock.expect(outPort.getModule()).andReturn(null).anyTimes();
        EasyMock.expect(inPort.getModule()).andReturn(null).anyTimes();
        EasyMock.replay(con);
        EasyMock.replay(moduleWithInPort);
        EasyMock.replay(moduleWithOutPort);
        EasyMock.replay(outPort);
        EasyMock.replay(inPort);
        EasyMock.replay(graphicMock);
        drawer.setGraphics(graphicMock);
        drawer.draw(con);
        EasyMock.verify(graphicMock);
    }

    @Ignore
    @Test(expected = IllegalArgumentException.class)
    public void testPortHasNullModuleRectangle() {
        Graphics graphicMock = EasyMock.createNiceMock(Graphics.class);
        Connection con = EasyMock.createNiceMock(Connection.class);
        Module moduleWithInPort = EasyMock.createNiceMock(Module.class);
        Rectangle moduleWithInPortRectangle = new Rectangle(0, 0, 100, 100);
        Module moduleWithOutPort = EasyMock.createNiceMock(Module.class);
        Rectangle moduleWithOutPortRectangle = new Rectangle(100, 100, 100, 100);
        Port outPort = EasyMock.createNiceMock(Port.class);
        Port inPort = EasyMock.createNiceMock(Port.class);
        List<Port> inPorts = new LinkedList<Port>();
        inPorts.add(inPort);
        List<Port> outPorts = new LinkedList<Port>();
        outPorts.add(outPort);
        EasyMock.expect(con.getOutPort()).andReturn(outPort).anyTimes();
        EasyMock.expect(con.getInPort()).andReturn(inPort).anyTimes();
        EasyMock.expect(con.getState()).andReturn(true).anyTimes();
        EasyMock.expect(con.getNextModule()).andReturn(moduleWithInPort).anyTimes();
        EasyMock.expect(con.getPreviousModule()).andReturn(moduleWithOutPort).anyTimes();
        EasyMock.expect(outPort.getModule()).andReturn(moduleWithOutPort).anyTimes();
        EasyMock.expect(inPort.getModule()).andReturn(moduleWithInPort).anyTimes();
        EasyMock.expect(inPort.isOutPort()).andReturn(false).anyTimes();
        EasyMock.expect(outPort.isOutPort()).andReturn(true).anyTimes();
        EasyMock.expect(moduleWithInPort.getInPorts()).andReturn(inPorts).anyTimes();
        EasyMock.expect(moduleWithOutPort.getOutPorts()).andReturn(outPorts).anyTimes();
        EasyMock.expect(moduleWithInPort.getRectangle()).andReturn(null).anyTimes();
        EasyMock.expect(moduleWithOutPort.getRectangle()).andReturn(null).anyTimes();
        EasyMock.replay(con);
        EasyMock.replay(moduleWithInPort);
        EasyMock.replay(moduleWithOutPort);
        EasyMock.replay(outPort);
        EasyMock.replay(inPort);
        EasyMock.replay(graphicMock);
        drawer.setGraphics(graphicMock);
        drawer.draw(con);
        EasyMock.verify(graphicMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDrawNullConnection() {
        drawer.draw((Connection) null);
    }
}

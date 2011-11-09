package nandcat.view.elementdrawer;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;
import java.util.LinkedList;
import nandcat.model.element.Lamp;
import nandcat.model.element.Port;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

public class ElementDrawerLampTest extends AbstractModuleElementDrawerTest {

    protected Lamp gateMock;

    @Before
    public void setUp() {
        setUpForModuleClass(Lamp.class);
        this.gateMock = (Lamp) super.gateMock;
    }

    @Test
    public void testDrawSelected() {
        when(gateMock.isSelected()).thenReturn(true);
        when(gateMock.getState()).thenReturn(true);
        stubModule();

        // action
        drawer.setGraphics(graphicMock);
        drawer.draw((Lamp) gateMock);

        verifyDrawModule();
        InOrder inOrder = inOrder(graphicMock);
        for (Port inPort : inPorts) {
            verifyDrawPort(inPort, inOrder);
        }
    }

    @Test
    public void testDraw() {
        when(gateMock.isSelected()).thenReturn(true);
        when(gateMock.getState()).thenReturn(false);
        stubModule();

        // action
        drawer.setGraphics(graphicMock);
        drawer.draw((Lamp) gateMock);

        verifyDrawModule();
        InOrder inOrder = inOrder(graphicMock);
        for (Port inPort : inPorts) {
            verifyDrawPort(inPort, inOrder);
        }
    }

    protected void verifyDrawModule() {
        InOrder inOrder = inOrder(graphicMock);
        if (gateMock.getState()) {
            inOrder.verify(graphicMock).setColor(LAMP_COLOR_ACTIVE);
        } else {
            inOrder.verify(graphicMock).setColor(LAMP_COLOR_DEFAULT);
        }
        // LOG.trace("Fill Oval (Lamp Border): x: " + (int) rec.x + " y: " + (int) rec.y + " w: " + rec.width + " h: "
        // + rec.height);
        inOrder.verify(graphicMock).fillOval(rec.x, rec.y, rec.width, rec.height);

        if (gateMock.isSelected()) {
            inOrder.verify(graphicMock).setColor(GATE_COLOR_SELECTED);
        } else {
            inOrder.verify(graphicMock).setColor(GATE_COLOR);
        }
        inOrder.verify(graphicMock).drawOval(eq(rec.x), eq(rec.y), eq(rec.width), eq(rec.height));
    }

    private void stubModule() {
        when(gateMock.getRectangle()).thenReturn(rec);

        // in ports
        inPorts = new LinkedList<Port>();
        inPorts.add(stubPort(true));

        // out ports
        outPorts = new LinkedList<Port>();

        when(gateMock.getInPorts()).thenReturn(inPorts);
        when(gateMock.getOutPorts()).thenReturn(outPorts);

        // annotation
        when(gateMock.getName()).thenReturn(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDrawNullLamp() {
        drawer.draw((Lamp) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDrawLampNullRectangle() {
        when(gateMock.getRectangle()).thenReturn(null);
        drawer.setGraphics(graphicMock);
        drawer.draw((Lamp) gateMock);
    }
}

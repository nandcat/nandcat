package nandcat.view.elementdrawer;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;
import java.util.LinkedList;
import nandcat.model.element.OrGate;
import nandcat.model.element.Port;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

/**
 * Tests drawing of an OrGate.
 */
public class ElementDrawerOrGateTest extends AbstractModuleElementDrawerTest {

    @Before
    public void setUp() {
        setUpForModuleClass(OrGate.class);
    }

    @Test
    public void testDrawSelected() {
        when(gateMock.isSelected()).thenReturn(true);
        stubModule();

        // action
        drawer.setGraphics(graphicMock);
        drawer.draw((OrGate) gateMock);

        verifyDrawModule();
        InOrder inOrder = inOrder(graphicMock);
        for (Port inPort : inPorts) {
            verifyDrawPort(inPort, inOrder);
        }
        verifyDrawLabel(LABEL_ORGATE);
    }

    @Test
    public void testDraw() {
        when(gateMock.isSelected()).thenReturn(true);
        stubModule();

        // action
        drawer.setGraphics(graphicMock);
        drawer.draw((OrGate) gateMock);

        verifyDrawModule();
        InOrder inOrder = inOrder(graphicMock);
        for (Port inPort : inPorts) {
            verifyDrawPort(inPort, inOrder);
        }
        verifyDrawLabel(LABEL_ORGATE);
    }

    private void stubModule() {
        when(gateMock.getRectangle()).thenReturn(rec);

        // in ports
        inPorts = new LinkedList<Port>();
        inPorts.add(stubPort(false));
        inPorts.add(stubPort(true));
        inPorts.add(stubPort(false));

        // out ports
        outPorts = new LinkedList<Port>();
        outPorts.add(stubPort(false));
        outPorts.add(stubPort(true));
        outPorts.add(stubPort(false));

        when(gateMock.getInPorts()).thenReturn(inPorts);
        when(gateMock.getOutPorts()).thenReturn(outPorts);

        // annotation
        when(gateMock.getName()).thenReturn(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDrawNullGate() {
        drawer.draw((OrGate) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDrawGateNullRectangle() {
        when(gateMock.getRectangle()).thenReturn(null);
        drawer.setGraphics(graphicMock);
        drawer.draw((OrGate) gateMock);
    }
}

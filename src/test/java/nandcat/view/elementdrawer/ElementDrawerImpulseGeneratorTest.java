package nandcat.view.elementdrawer;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;
import java.util.LinkedList;
import nandcat.model.element.ImpulseGenerator;
import nandcat.model.element.Port;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

public class ElementDrawerImpulseGeneratorTest extends AbstractModuleElementDrawerTest {

    // private Graphics graphicMock;
    //
    // private Rectangle rec;
    //
    // private ImpulseGenerator gateMock;

    @Before
    public void setUp() {
        setUpForModuleClass(ImpulseGenerator.class);
    }

    @Test
    public void testDrawSelected() {
        when(gateMock.isSelected()).thenReturn(true);
        stubModule();

        // action
        drawer.setGraphics(graphicMock);
        drawer.draw((ImpulseGenerator) gateMock);

        verifyDrawModule();
        InOrder inOrder = inOrder(graphicMock);
        for (Port inPort : inPorts) {
            verifyDrawPort(inPort, inOrder);
        }
    }

    @Test
    public void testDraw() {
        when(gateMock.isSelected()).thenReturn(false);
        stubModule();

        // action
        drawer.setGraphics(graphicMock);
        drawer.draw((ImpulseGenerator) gateMock);

        verifyDrawModule();
        InOrder inOrder = inOrder(graphicMock);
        for (Port inPort : inPorts) {
            verifyDrawPort(inPort, inOrder);
        }
    }

    private void stubModule() {
        when(gateMock.getRectangle()).thenReturn(rec);

        // in ports
        inPorts = new LinkedList<Port>();

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
        drawer.draw((ImpulseGenerator) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDrawGateNullRectangle() {
        when(gateMock.getRectangle()).thenReturn(null);
        drawer.setGraphics(graphicMock);
        drawer.draw((ImpulseGenerator) gateMock);
    }
}

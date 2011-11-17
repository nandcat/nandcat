package nandcat.view.elementdrawer;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import nandcat.model.element.Module;
import nandcat.model.element.Port;
import org.mockito.InOrder;
import org.mockito.Mockito;

public class AbstractModuleElementDrawerTest extends AbstractElementDrawerTest {

    protected Graphics graphicMock;

    protected Rectangle rec;

    protected Module gateMock;

    protected List<Port> inPorts;

    protected List<Port> outPorts;

    @SuppressWarnings("unchecked")
    public void setUpForModuleClass(Class clazz) {
        inPorts = new LinkedList<Port>();
        outPorts = new LinkedList<Port>();
        gateMock = (Module) Mockito.mock(clazz);
        graphicMock = Mockito.mock(Graphics.class);
        rec = new Rectangle(3, 3, 100, 100);
    }

    protected Port stubPort(boolean state) {
        Port p = mock(Port.class);
        when(p.getState()).thenReturn(state);
        when(p.getRectangle()).thenReturn(new Rectangle(4, 4, PORT_DIAMETER, PORT_DIAMETER));
        return p;
    }

    protected void verifyDrawModule() {
        InOrder inOrder = inOrder(graphicMock);
        if (gateMock.isSelected()) {
            inOrder.verify(graphicMock).setColor(GATE_COLOR_SELECTED);
        } else {
            inOrder.verify(graphicMock).setColor(GATE_COLOR);
        }
        inOrder.verify(graphicMock).drawRect(eq(rec.x), eq(rec.y), eq(rec.width), eq(rec.height));
    }

    protected void verifyDrawPort(Port p, InOrder inOrder) {
        if (p.getState()) {
            inOrder.verify(graphicMock).setColor(PORT_COLOR_ACTIVE);
        } else {
            inOrder.verify(graphicMock).setColor(PORT_COLOR_DEFAULT);
        }
        inOrder.verify(graphicMock).drawOval(Mockito.anyInt(), Mockito.anyInt(), Mockito.eq(PORT_DIAMETER),
                Mockito.eq(PORT_DIAMETER));
    }

    protected void verifyDrawLabel(String label) {
        InOrder inOrder = inOrder(graphicMock);
        inOrder.verify(graphicMock).setColor(LABEL_COLOR);
        inOrder.verify(graphicMock).drawString(eq(label), Mockito.anyInt(), Mockito.anyInt());
    }

}

package nandcat.controller.tool;

import static org.junit.Assert.assertTrue;
import java.awt.AWTException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.SwingUtilities;
import nandcat.controller.AnnotationTool;
import nandcat.controller.Controller;
import nandcat.model.Model;
import nandcat.model.element.AndGate;
import nandcat.model.element.DrawElement;
import nandcat.view.View;
import nandcat.view.Workspace;
import nandcat.view.WorkspaceEvent;
import nandcat.view.WorkspaceListener;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;

public class AnnotationToolTest {

    @Test
    public void testRegisterListener() {
        Controller controller = Mockito.mock(Controller.class);
        View view = Mockito.mock(View.class);
        Model model = Mockito.mock(Model.class);
        Workspace workspace = Mockito.mock(Workspace.class);

        Mockito.when(controller.getView()).thenReturn(view);
        Mockito.when(view.getWorkspace()).thenReturn(workspace);
        Mockito.when(controller.getModel()).thenReturn(model);

        AnnotationTool aTool = new AnnotationTool(controller);

        InOrder inOrder = Mockito.inOrder(workspace);
        ArgumentCaptor<WorkspaceListener> argument = ArgumentCaptor.forClass(WorkspaceListener.class);

        // action: activate
        aTool.setActive(true);

        // verify addListener
        inOrder.verify(workspace).addListener(argument.capture());
        assertTrue(argument.getValue() != null);

        // action: deactivate
        aTool.setActive(false);
        inOrder.verify(workspace).removeListener(argument.getValue());
    }

    @Ignore
    @Test
    public void test() throws AWTException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
        Controller controller = Mockito.mock(Controller.class);

        Model model = Mockito.mock(Model.class);
        Workspace workspace = Mockito.mock(Workspace.class);

        View realview = new View(model);
        View view = Mockito.spy(realview);

        Mockito.when(controller.getView()).thenReturn(view);
        Mockito.when(view.getWorkspace()).thenReturn(workspace);
        Mockito.when(controller.getModel()).thenReturn(model);

        AnnotationTool aTool = new AnnotationTool(controller);
        aTool.setActive(true);

        ArgumentCaptor<WorkspaceListener> argument = ArgumentCaptor.forClass(WorkspaceListener.class);
        Mockito.verify(workspace).addListener(argument.capture());

        final WorkspaceListener capturedListener = argument.getValue();
        assertTrue(capturedListener != null);

        // create click event
        final WorkspaceEvent event = Mockito.mock(WorkspaceEvent.class);

        // stub event for click on 10,5
        Mockito.when(event.getLocation()).thenReturn(new Point(10, 5));

        // stub model elements
        Set<DrawElement> elements = new HashSet<DrawElement>();

        // create and gate
        AndGate andGate = Mockito.mock(AndGate.class);
        Mockito.when(andGate.getName()).thenReturn("Old Annotation");
        elements.add(andGate);

        // stub model return andGate
        ArgumentCaptor<Rectangle> requestedRectangleCaptor = ArgumentCaptor.forClass(Rectangle.class);
        Mockito.when(model.getDrawElementsAt(requestedRectangleCaptor.capture())).thenReturn(elements);

        // action: trigger click on capturedListener
        // capturedListener.mouseClicked(event);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                capturedListener.mouseClicked(event);
            }
        });

        Robot robot = new Robot();
        robot.delay(500);
        robot.keyPress(KeyEvent.VK_RIGHT);
        robot.keyRelease(KeyEvent.VK_RIGHT);
        robot.delay(500);
        robot.keyPress(KeyEvent.VK_2);
        robot.keyRelease(KeyEvent.VK_2);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        robot.delay(500);
        robot = null;

        // verify click point is inside the tolerance rectangle
        assertTrue(requestedRectangleCaptor.getValue().intersects(new Rectangle(10, 5, 1, 1)));

        // verify correct name change.
        Mockito.verify(andGate).setName("Old Annotation2");
    }
}

package nandcat.controller.tool;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.awt.AWTException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.SwingUtilities;
import nandcat.controller.Controller;
import nandcat.controller.StateTool;
import nandcat.model.Model;
import nandcat.model.element.DrawElement;
import nandcat.model.element.ImpulseGenerator;
import nandcat.view.View;
import nandcat.view.Workspace;
import nandcat.view.WorkspaceEvent;
import nandcat.view.WorkspaceListener;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;

public class StateToolTest {

    @Test
    public void testRegisterListener() {
        Controller controller = Mockito.mock(Controller.class);
        View view = Mockito.mock(View.class);
        Model model = Mockito.mock(Model.class);
        Workspace workspace = Mockito.mock(Workspace.class);

        Mockito.when(controller.getView()).thenReturn(view);
        Mockito.when(view.getWorkspace()).thenReturn(workspace);
        Mockito.when(controller.getModel()).thenReturn(model);

        StateTool sTool = new StateTool(controller);

        InOrder inOrder = Mockito.inOrder(workspace);
        ArgumentCaptor<WorkspaceListener> argument = ArgumentCaptor.forClass(WorkspaceListener.class);

        // action: activate
        sTool.setActive(true);

        // verify addListener
        inOrder.verify(workspace).addListener(argument.capture());
        assertTrue(argument.getValue() != null);

        // action: deactivate
        sTool.setActive(false);
        inOrder.verify(workspace).removeListener(argument.getValue());
    }

    @Ignore
    @Test
    public void testLeftClick() throws AWTException, IllegalArgumentException, NoSuchFieldException,
            IllegalAccessException {
        Controller controller = Mockito.mock(Controller.class);

        Model model = Mockito.mock(Model.class);
        Workspace workspace = Mockito.mock(Workspace.class);

        View realview = new View(model);
        View view = Mockito.spy(realview);

        Mockito.when(controller.getView()).thenReturn(view);
        Mockito.when(view.getWorkspace()).thenReturn(workspace);
        Mockito.when(controller.getModel()).thenReturn(model);

        StateTool sTool = new StateTool(controller);
        sTool.setActive(true);

        ArgumentCaptor<WorkspaceListener> argument = ArgumentCaptor.forClass(WorkspaceListener.class);
        Mockito.verify(workspace).addListener(argument.capture());

        final WorkspaceListener capturedListener = argument.getValue();
        assertTrue(capturedListener != null);

        // create click event
        final WorkspaceEvent event = Mockito.mock(WorkspaceEvent.class);

        // stub event for click on 10,5
        Mockito.when(event.getLocation()).thenReturn(new Point(10, 5));
        Mockito.when(event.getButton()).thenReturn(MouseEvent.BUTTON1);

        // stub model elements
        Set<DrawElement> elements = new HashSet<DrawElement>();

        // create impulse generator. Set its state to false.
        ImpulseGenerator generator = Mockito.mock(ImpulseGenerator.class);
        Mockito.when(generator.getState()).thenReturn(false);
        elements.add(generator);

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

        // verify click point is inside the tolerance rectangle
        assertTrue(requestedRectangleCaptor.getValue().intersects(new Rectangle(10, 5, 1, 1)));

        // verify correct state change. State should be true.
        Mockito.verify(model).toggleModule(generator);
    }

    @Ignore
    @Test
    public void testRightClick() throws AWTException, IllegalArgumentException, NoSuchFieldException,
            IllegalAccessException {
        Controller controller = Mockito.mock(Controller.class);

        Model model = Mockito.mock(Model.class);
        Workspace workspace = Mockito.mock(Workspace.class);

        View realview = new View(model);
        View view = Mockito.spy(realview);

        Mockito.when(controller.getView()).thenReturn(view);
        Mockito.when(view.getWorkspace()).thenReturn(workspace);
        Mockito.when(controller.getModel()).thenReturn(model);

        StateTool sTool = new StateTool(controller);
        sTool.setActive(true);

        ArgumentCaptor<WorkspaceListener> argument = ArgumentCaptor.forClass(WorkspaceListener.class);
        Mockito.verify(workspace).addListener(argument.capture());

        final WorkspaceListener capturedListener = argument.getValue();
        assertTrue(capturedListener != null);

        // create click event
        final WorkspaceEvent event = Mockito.mock(WorkspaceEvent.class);

        // stub event for click on 10,5
        Mockito.when(event.getLocation()).thenReturn(new Point(10, 5));
        Mockito.when(event.getButton()).thenReturn(MouseEvent.BUTTON3);

        // stub model elements
        Set<DrawElement> elements = new HashSet<DrawElement>();

        // create impulse generator. Set its state to false ant the frequency to 1.
        ImpulseGenerator generator = Mockito.mock(ImpulseGenerator.class);
        Mockito.when(generator.getFrequency()).thenReturn(1);
        elements.add(generator);

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

        // Click right to open the window where to put the value for the frequency.
        Robot robot = new Robot();
        robot.delay(1000); // If waiting time is shorter the window is open for too short.
        robot.keyPress(KeyEvent.VK_2);
        robot.keyRelease(KeyEvent.VK_2);
        robot.delay(500);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        robot.delay(500);
        // I don't know why, but here the error screen appears.
        robot = null;

        // verify click point is inside the tolerance rectangle
        assertTrue(requestedRectangleCaptor.getValue().intersects(new Rectangle(10, 5, 1, 1)));

        // verify correct frequency.
        Mockito.verify(model).setFrequency(generator, 2);
    }
}

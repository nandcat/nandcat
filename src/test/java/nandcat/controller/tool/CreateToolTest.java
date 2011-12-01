package nandcat.controller.tool;

import static org.junit.Assert.assertTrue;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import javax.swing.SwingUtilities;
import nandcat.controller.Controller;
import nandcat.controller.CreateTool;
import nandcat.model.Model;
import nandcat.model.ViewModule;
import nandcat.model.element.AndGate;
import nandcat.model.element.DrawElement;
import nandcat.model.element.OrGate;
import nandcat.model.element.Port;
import nandcat.view.View;
import nandcat.view.Workspace;
import nandcat.view.WorkspaceEvent;
import nandcat.view.WorkspaceListener;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;

public class CreateToolTest {

    @Test
    public void testRegisterListener() {
        Controller controller = Mockito.mock(Controller.class);
        View view = Mockito.mock(View.class);
        Model model = Mockito.mock(Model.class);
        Workspace workspace = Mockito.mock(Workspace.class);

        Mockito.when(controller.getView()).thenReturn(view);
        Mockito.when(view.getWorkspace()).thenReturn(workspace);
        Mockito.when(controller.getModel()).thenReturn(model);

        CreateTool cTool = new CreateTool(controller);

        InOrder inOrder = Mockito.inOrder(workspace);
        ArgumentCaptor<WorkspaceListener> argument = ArgumentCaptor.forClass(WorkspaceListener.class);

        // action: activate
        cTool.setActive(true);

        // verify addListener
        inOrder.verify(workspace).addListener(argument.capture());
        assertTrue(argument.getValue() != null);

        // action: deactivate
        cTool.setActive(false);
        inOrder.verify(workspace).removeListener(argument.getValue());
    }

    @Test
    public void testCreateModule() {
        Controller controller = Mockito.mock(Controller.class);

        Model model = Mockito.mock(Model.class);
        Workspace workspace = Mockito.mock(Workspace.class);

        View realview = new View(model);
        View view = Mockito.spy(realview);

        Mockito.when(controller.getView()).thenReturn(view);
        Mockito.when(view.getWorkspace()).thenReturn(workspace);
        Mockito.when(controller.getModel()).thenReturn(model);

        // Set an AndGate as a ViewModule for the view.
        ViewModule module = Mockito.mock(ViewModule.class);
        AndGate andGate = Mockito.mock(AndGate.class);
        Mockito.when(module.getModule()).thenReturn(andGate);
        Mockito.when(module.getName()).thenReturn("AND");
        LinkedList<ViewModule> modules = new LinkedList<ViewModule>();
        modules.add(module);
        Mockito.when(model.getViewModules()).thenReturn(modules);

        CreateTool cTool = new CreateTool(controller);
        cTool.setActive(true);

        ArgumentCaptor<WorkspaceListener> argument = ArgumentCaptor.forClass(WorkspaceListener.class);
        Mockito.verify(workspace).addListener(argument.capture());

        final WorkspaceListener capturedListener = argument.getValue();
        assertTrue(capturedListener != null);

        // create click event
        final WorkspaceEvent event = Mockito.mock(WorkspaceEvent.class);

        // stub event for click on 10,5
        Mockito.when(event.getLocation()).thenReturn(new Point(40, 25));

        // stub model elements. Is empty.
        Set<DrawElement> elements = new HashSet<DrawElement>();

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

        // verify elements are no longer empty.
        Mockito.verify(model).addModule(module, new Point(10, 5));
    }

    @Ignore
    @Test
    public void testCreateConnection() {
        Controller controller = Mockito.mock(Controller.class);

        Model model = Mockito.mock(Model.class);
        Workspace workspace = Mockito.mock(Workspace.class);

        View realview = new View(model);
        View view = Mockito.spy(realview);

        Mockito.when(controller.getView()).thenReturn(view);
        Mockito.when(view.getWorkspace()).thenReturn(workspace);
        Mockito.when(controller.getModel()).thenReturn(model);

        // Set an AndGate as a ViewModule for the view.
        ViewModule module = Mockito.mock(ViewModule.class);
        AndGate andGate = Mockito.mock(AndGate.class);
        Mockito.when(module.getModule()).thenReturn(andGate);
        Mockito.when(module.getName()).thenReturn("AND");
        LinkedList<ViewModule> modules = new LinkedList<ViewModule>();
        modules.add(module);
        Mockito.when(model.getViewModules()).thenReturn(modules);

        CreateTool cTool = new CreateTool(controller);
        cTool.setActive(true);

        ArgumentCaptor<WorkspaceListener> argument = ArgumentCaptor.forClass(WorkspaceListener.class);
        Mockito.verify(workspace).addListener(argument.capture());

        final WorkspaceListener capturedListener = argument.getValue();
        assertTrue(capturedListener != null);

        // create click event
        final WorkspaceEvent event = Mockito.mock(WorkspaceEvent.class);

        // stub event for click on 10,5
        Mockito.when(event.getLocation()).thenReturn(new Point(10, 5));

        // User clicks on or gate.
        OrGate orGate = Mockito.mock(OrGate.class);

        // Set up the draw elements. They contain an and and an or gate.
        Set<DrawElement> elements = new HashSet<DrawElement>();
        elements.add(andGate);
        Port sourcePort = Mockito.mock(Port.class);
        Mockito.when(model.getPortAt(new Rectangle(10, 5, 1, 1))).thenReturn(sourcePort);

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
        elements.remove(andGate);

        elements.add(orGate);
        Port targetPort = Mockito.mock(Port.class);
        Mockito.when(model.getPortAt(new Rectangle(20, 15, 1, 1))).thenReturn(targetPort);
        // stub event for click on 20, 15
        Mockito.when(event.getLocation()).thenReturn(new Point(20, 15));

        // stub model return orGate. Elements may not be empty. But it doesn't matter what element is in this
        // set.
        requestedRectangleCaptor = ArgumentCaptor.forClass(Rectangle.class);
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

        // verify elements are no longer empty.
        Mockito.verify(model).addConnection(sourcePort, targetPort);
    }
}

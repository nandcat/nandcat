package nandcat.controller.tool;

import static org.junit.Assert.assertTrue;
import nandcat.controller.Controller;
import nandcat.controller.CreateTool;
import nandcat.model.Model;
import nandcat.view.View;
import nandcat.view.Workspace;
import nandcat.view.WorkspaceListener;
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

        CreateTool aTool = new CreateTool(controller);

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
}

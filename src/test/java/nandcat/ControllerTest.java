package nandcat;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import nandcat.controller.Controller;
import nandcat.controller.Tool;
import nandcat.model.Model;
import nandcat.view.View;
import org.easymock.EasyMock;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

public class ControllerTest {

    @Test
    public void testConstructor() {
        Model model = mock(Model.class);
        View view = new View(model);
        Controller contr = new Controller(view, model);
        assertEquals(view, contr.getView());
        assertEquals(model, contr.getModel());
    }

    @Test
    public void testRequestActivation() {
        Model model = mock(Model.class);
        View view = new View(model);
        Controller c = new Controller(view, model);
        Tool toolOne = mock(Tool.class);
        Tool toolTwo = mock(Tool.class);

        // verify order of tool activation
        InOrder inOrder = inOrder(toolOne, toolTwo);
        c.requestActivation(toolOne);
        inOrder.verify(toolOne).setActive(true);
        c.requestActivation(toolTwo);
        inOrder.verify(toolOne).setActive(false);
        inOrder.verify(toolTwo).setActive(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequestActivationNull() {
        Model model = mock(Model.class);
        View view = new View(model);
        Controller c = new Controller(view, model);

        // Verify exception if tool = null
        c.requestActivation(null);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testGiveFunctionalities() {
        Model model = mock(Model.class);
        View view = mock(View.class);
        ExtController c = new ExtController(view, model);
        Tool toolOne = mock(Tool.class);
        Tool toolTwo = mock(Tool.class);

        // add tools to controller (subclassing needed because of this)
        c.addTool(toolOne);
        c.addTool(toolTwo);

        // Build fake functionalities for tools.
        Map<String, ActionListener> firstToolFunc = new HashMap<String, ActionListener>();
        Map<String, ActionListener> secondToolFunc = new HashMap<String, ActionListener>();
        ActionListener actionListenerTool1 = EasyMock.createMock(ActionListener.class);
        ActionListener actionListenerTool2 = EasyMock.createMock(ActionListener.class);
        firstToolFunc.put("func1tool1", actionListenerTool1);
        firstToolFunc.put("func2tool1", actionListenerTool1);
        secondToolFunc.put("func1tool2", actionListenerTool2);
        secondToolFunc.put("func2tool2", actionListenerTool2);

        // Stub tool give functionalities with fake functionalities
        when(toolOne.getFunctionalities()).thenReturn(firstToolFunc);
        when(toolTwo.getFunctionalities()).thenReturn(secondToolFunc);

        // action - c should set functionalities in view.
        c.giveFunctionalities();

        // capture views input
        ArgumentCaptor<Map> mapCapture = ArgumentCaptor.forClass(Map.class);
        verify(view).setFunctionalities(mapCapture.capture());

        // check if functionalities of tool one exist.
        assertEquals(actionListenerTool1, mapCapture.getValue().get("func1tool1"));
        assertEquals(actionListenerTool1, mapCapture.getValue().get("func2tool1"));

        // check if functionalities of tool two exist.
        assertEquals(actionListenerTool2, mapCapture.getValue().get("func1tool2"));
        assertEquals(actionListenerTool2, mapCapture.getValue().get("func2tool2"));

    }

    private class ExtController extends Controller {

        public ExtController(View view, Model model) {
            super(view, model);
        }

        public void addTool(Tool t) {
            tools.add(t);
        }

        @Override
        protected void initTools() {
        }
    }
}

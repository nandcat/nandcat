package nandcat;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nandcat.controller.Controller;
import nandcat.controller.Tool;
import nandcat.model.Model;
import nandcat.view.View;
import org.easymock.classextension.EasyMock;
import org.easymock.classextension.IMocksControl;
import org.junit.Test;

public class ControllerTest {

    @Test
    public void testConstructor() {
        View viewMock = EasyMock.createMock(View.class);
        Model modelMock = EasyMock.createMock(Model.class);
        EasyMock.replay(viewMock);
        EasyMock.replay(modelMock);
        Controller contr = new Controller(viewMock, modelMock);
        assertEquals(viewMock, contr.getView());
        assertEquals(modelMock, contr.getModel());
    }

    @Test
    public void testRequestActivation() {
        View viewMock = EasyMock.createMock(View.class);
        Model modelMock = EasyMock.createMock(Model.class);
        EasyMock.replay(viewMock);
        EasyMock.replay(modelMock);
        Controller contr = new Controller(viewMock, modelMock);
        IMocksControl mockControl = EasyMock.createStrictControl();
        Tool firstTool = mockControl.createMock(Tool.class);
        Tool secondTool = mockControl.createMock(Tool.class);
        firstTool.setActive(true);
        firstTool.setActive(false);
        secondTool.setActive(true);
        mockControl.replay();
        contr.requestActivation(firstTool);
        contr.requestActivation(secondTool);
        mockControl.verify();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequestActivationNull() {
        View viewMock = EasyMock.createMock(View.class);
        Model modelMock = EasyMock.createMock(Model.class);
        EasyMock.replay(viewMock);
        EasyMock.replay(modelMock);
        Controller contr = new Controller(viewMock, modelMock);
        contr.requestActivation(null);
    }

    @Test
    public void testGiveFunctionalities() {
        Model modelMock = EasyMock.createMock(Model.class);
        FakeView viewMock = new FakeView(modelMock);
        EasyMock.replay(modelMock);
        ExtController contr = new ExtController(viewMock, modelMock);
        IMocksControl mockControl = EasyMock.createStrictControl();
        Tool firstTool = mockControl.createMock(Tool.class);
        Tool secondTool = mockControl.createMock(Tool.class);
        Map<String, ActionListener> firstToolFunc = new HashMap<String, ActionListener>();
        Map<String, ActionListener> secondToolFunc = new HashMap<String, ActionListener>();
        ActionListener actionListenerTool1 = EasyMock.createMock(ActionListener.class);
        ActionListener actionListenerTool2 = EasyMock.createMock(ActionListener.class);
        EasyMock.replay(actionListenerTool1);
        EasyMock.replay(actionListenerTool2);
        firstToolFunc.put("func1tool1", actionListenerTool1);
        firstToolFunc.put("func2tool1", actionListenerTool1);
        secondToolFunc.put("func1tool2", actionListenerTool2);
        secondToolFunc.put("func2tool2", actionListenerTool2);
        EasyMock.expect(firstTool.getFunctionalities()).andReturn(firstToolFunc);
        EasyMock.expect(secondTool.getFunctionalities()).andReturn(secondToolFunc);
        mockControl.replay();
        contr.addTool(firstTool);
        contr.addTool(secondTool);
        contr.giveFunctionalities();
        assertTrue(viewMock.functionalities.containsKey("func1tool1"));
        assertEquals(actionListenerTool1, viewMock.functionalities.get("func1tool1"));
        assertTrue(viewMock.functionalities.containsKey("func1tool2"));
        assertTrue(viewMock.functionalities.containsKey("func2tool1"));
        assertEquals(actionListenerTool2, viewMock.functionalities.get("func1tool2"));
        assertTrue(viewMock.functionalities.containsKey("func2tool2"));
        assertEquals(4, viewMock.functionalities.size());
        mockControl.verify();
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

    private class FakeView extends View {

        public Map<String, ActionListener> functionalities;

        public FakeView(Model model) {
            super(model);
        }

        @Override
        public void setFunctionalities(Map<String, ActionListener> map) {
            functionalities = map;
        }
    }
}

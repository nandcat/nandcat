package nandcat.controller.tool;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import nandcat.Nandcat;
import nandcat.ReflectionUtil;
import nandcat.controller.AnnotationTool;
import nandcat.controller.Controller;
import nandcat.controller.CreateTool;
import nandcat.model.Model;
import nandcat.model.element.AndGate;
import nandcat.model.element.DrawElement;
import nandcat.model.element.Element;
import nandcat.view.View;
import nandcat.view.Workspace;
import nandcat.view.WorkspaceEvent;
import nandcat.view.WorkspaceListener;
import org.easymock.classextension.EasyMock;
import org.fest.swing.fixture.FrameFixture;
import org.junit.Before;
import org.junit.Test;

public class CreateToolTest {

@Before
    public void setUp() throws Exception {
    }

    @Test
    public void testRegisterListener() {
        Controller cMock = EasyMock.createMock(Controller.class);
        View vMock = EasyMock.createMock(View.class);
        Model mMock = EasyMock.createMock(Model.class);
        Workspace wMock = EasyMock.createStrictMock(Workspace.class);
        EasyMock.expect(cMock.getModel()).andReturn(mMock).anyTimes();
        EasyMock.expect(cMock.getView()).andReturn(vMock).anyTimes();
        EasyMock.expect(vMock.getWorkspace()).andReturn(wMock).anyTimes();
        wMock.addListener((WorkspaceListener)EasyMock.anyObject());
        wMock.removeListener((WorkspaceListener)EasyMock.anyObject());
        EasyMock.replay(cMock);
        EasyMock.replay(vMock);
        EasyMock.replay(mMock);
        EasyMock.replay(wMock);
        CreateTool cT = new CreateTool(cMock);
        cT.setActive(true);
        cT.setActive(false);
        EasyMock.verify(cMock);
        EasyMock.verify(vMock);
        EasyMock.verify(wMock);
    }
    
    @Test
    public void test() throws AWTException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
        
        // Set up controller, model and view mocks.
        Controller cMock = EasyMock.createMock(Controller.class);
        Model mMock = EasyMock.createNiceMock(Model.class);
        FakeView fakeView = new FakeView(mMock);
        final FakeWorkspace fakeWorkspace = new FakeWorkspace();
        fakeView.w = fakeWorkspace;
        EasyMock.expect(cMock.getModel()).andReturn(mMock).anyTimes();
        EasyMock.expect(cMock.getView()).andReturn(fakeView).anyTimes();
        
        // ActionEvent on toolbar
        final ActionEvent actionEvent = EasyMock.createMock(ActionEvent.class);
        JToolBar toolBar = EasyMock.createMock(JToolBar.class);
        AndGate andGate = EasyMock.createMock(AndGate.class);
        actionEvent.setSource(toolBar);
        
        //fakeWorkspace.dispatchEvent(actionEvent);
        
        
        
        // The point where an action on the workspace occurred.
        Point pointOfClick = new Point(10,50);
        Dimension tolerance = (Dimension) ReflectionUtil.getPrivateField(CreateTool.class, null, "MOUSE_TOLERANCE");
        Rectangle recOfClick = new Rectangle(pointOfClick, tolerance);
        
        // A click on workspace returns a set of DrawElements
        Set<DrawElement> elements = new HashSet<DrawElement>();
        EasyMock.expect(mMock.getDrawElementsAt(EasyMock.eq(recOfClick))).andReturn(elements);
        EasyMock.replay(cMock);
        EasyMock.replay(mMock);
        
        // Tool mock
        CreateTool cT = new CreateTool(cMock);
        cT.setActive(true);
        
        // The action on the workspace delivers a workspace event containing its source.
        final WorkspaceEvent workspaceEvent = EasyMock.createMock(WorkspaceEvent.class);
        EasyMock.expect(workspaceEvent.getLocation()).andReturn(pointOfClick);
        EasyMock.replay(workspaceEvent);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                fakeWorkspace.getWorkspaceListener().mouseClicked(workspaceEvent);
            }
        });
        // TODO finish
    }
    
    private class FakeWorkspace extends Workspace {
        private WorkspaceListener wl;
        
        @Override
        public void addListener(WorkspaceListener l) {
            this.wl = l;
        }
        
        public WorkspaceListener getWorkspaceListener(){
            return wl;
        }
    }
    
    private class FakeView extends View {
        Workspace w;
        public FakeView(Model model) {
            super(model);
        }
        
        @Override
        public Workspace getWorkspace() {
            return w;
        }
        
    }
}

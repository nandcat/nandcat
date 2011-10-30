package nandcat.controller.tool;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.SwingUtilities;
import nandcat.Nandcat;
import nandcat.controller.AnnotationTool;
import nandcat.controller.Controller;
import nandcat.model.Model;
import nandcat.model.element.AndGate;
import nandcat.model.element.Element;
import nandcat.view.View;
import nandcat.view.Workspace;
import nandcat.view.WorkspaceEvent;
import nandcat.view.WorkspaceListener;
import org.easymock.classextension.EasyMock;
import org.fest.swing.fixture.FrameFixture;
import org.junit.Before;
import org.junit.Test;


public class AnnotationToolTest {

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
        AnnotationTool aT = new AnnotationTool(cMock);
        aT.setActive(true);
        aT.setActive(false);
        EasyMock.verify(cMock);
        EasyMock.verify(vMock);
        EasyMock.verify(wMock);
    }
    
    @Test
    public void test() throws AWTException {
        Controller cMock = EasyMock.createMock(Controller.class);
        Model mMock = EasyMock.createNiceMock(Model.class);
        FakeView fakeView = new FakeView(mMock);
        final FakeWorkspace fakeWorkspace = new FakeWorkspace();
        fakeView.w = fakeWorkspace;
        EasyMock.expect(cMock.getModel()).andReturn(mMock).anyTimes();
        EasyMock.expect(cMock.getView()).andReturn(fakeView).anyTimes();
        Point pointOfClick = new Point(10,50);
        AndGate andGate = EasyMock.createMock(AndGate.class);
        EasyMock.expect(andGate.getName()).andReturn("Old Annotation").anyTimes();
        //Hot point
        andGate.setName(EasyMock.eq("Old Annotation2"));
        Set<Element> elements = new HashSet<Element>();
        elements.add(andGate);
        EasyMock.expect(mMock.getElementsAt(EasyMock.eq(pointOfClick))).andReturn(elements);
        EasyMock.replay(andGate);
        EasyMock.replay(cMock);
        EasyMock.replay(mMock);
        AnnotationTool aT = new AnnotationTool(cMock);
        aT.setActive(true);
        final WorkspaceEvent workspaceEvent = EasyMock.createMock(WorkspaceEvent.class);
        EasyMock.expect(workspaceEvent.getLocation()).andReturn(pointOfClick);
        EasyMock.replay(workspaceEvent);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                fakeWorkspace.getWorkspaceListener().mouseClicked(workspaceEvent);
            }
        });
        
        // TODO: Use FEST instead!
//        FrameFixture fixture = new FrameFixture(fakeView);
//        fixture.show();
//        fakeWorkspace.getWorkspaceListener().mouseClicked(workspaceEvent);
//        fixture.optionPane().buttonWithText("OK").click();
        
//        // Test dialogbox using Robot!
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

        EasyMock.verify(cMock);
        EasyMock.verify(andGate);
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

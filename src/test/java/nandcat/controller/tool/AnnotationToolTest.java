package nandcat.controller.tool;

import static org.junit.Assert.*;
import nandcat.controller.AnnotationTool;
import nandcat.controller.Controller;
import nandcat.model.Model;
import nandcat.view.View;
import nandcat.view.Workspace;
import nandcat.view.WorkspaceListener;
import org.easymock.classextension.EasyMock;
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
        Workspace wMock = EasyMock.createMock(Workspace.class);
        EasyMock.expect(cMock.getModel()).andReturn(mMock).anyTimes();
        EasyMock.expect(cMock.getView()).andReturn(vMock).anyTimes();
        EasyMock.expect(vMock.getWorkspace()).andReturn(wMock);
        wMock.addListener((WorkspaceListener)EasyMock.anyObject());
        EasyMock.replay(cMock);
        EasyMock.replay(vMock);
        EasyMock.replay(mMock);
        EasyMock.replay(wMock);
        AnnotationTool aT = new AnnotationTool(cMock);
        aT.setActive(true);
        EasyMock.verify(cMock);
        EasyMock.verify(vMock);
        EasyMock.verify(wMock);
    }
}

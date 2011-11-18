package nandcat.model.importexport;

import static org.junit.Assert.assertTrue;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import nandcat.model.ModelElementDefaults;
import nandcat.model.element.Circuit;
import nandcat.model.element.factory.ModuleBuilderFactory;
import nandcat.view.StandardElementDrawer;
import nandcat.view.StandardModuleLayouter;
import org.junit.Before;
import org.junit.Test;

public class DrawExporterTest {

    private ModuleBuilderFactory factory;

    @Before
    public void setUp() {
        factory = new ModuleBuilderFactory();
        factory.setDefaults(new ModelElementDefaults());
        factory.setLayouter(new StandardModuleLayouter());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test() throws IOException {
        DrawExporter drawExporter = new DrawExporter();
        File file = File.createTempFile("test", ".png");
        drawExporter.setFile(file);
        drawExporter.setElementDrawer(new StandardElementDrawer());
        drawExporter.exportCircuit();
    }

    @Test
    public void testOutput() throws IOException {
        DrawExporter drawExporter = new DrawExporter();
        File file = File.createTempFile("test", ".png");
        drawExporter.setFile(file);
        drawExporter.setElementDrawer(new StandardElementDrawer());
        Circuit c = (Circuit) factory.getCircuitBuilder().build();
        c.addModule(factory.getAndGateBuilder().setLocation(new Point(50, 20)).build());
        c.addModule(factory.getOrGateBuilder().setLocation(new Point(10, 80)).build());
        drawExporter.setCircuit(c);
        assertTrue(drawExporter.exportCircuit());
        // System.out.println(file.getAbsolutePath());
    }
}

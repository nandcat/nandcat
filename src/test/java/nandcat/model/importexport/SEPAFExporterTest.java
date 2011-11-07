package nandcat.model.importexport;

import static org.junit.Assert.assertTrue;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import nandcat.model.element.AndGate;
import nandcat.model.element.Circuit;
import nandcat.model.element.OrGate;
import nandcat.model.importexport.sepaf.SEPAFExporter;
import org.junit.Test;

public class SEPAFExporterTest {

    @Test
    public void test() throws IOException {
        SEPAFExporter export = new SEPAFExporter();
        File file = File.createTempFile("export", ".xml");
        export.setFile(file);
        Circuit c = new Circuit(null);
        AndGate andGate = new AndGate();
        andGate.setLocation(new Point(1, 1));
        c.addModule(andGate);

        OrGate orGate = new OrGate();
        orGate.setLocation(new Point(2, 2));
        orGate.setName("OrGate");
        c.addModule(orGate);

        c.addConnection(andGate.getOutPorts().get(0), orGate.getInPorts().get(0));

        export.setCircuit(c);
        assertTrue(export.exportCircuit());
        printFileContent(file);
    }

    private void printFileContent(File file) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String strLine;
        while ((strLine = in.readLine()) != null) {
            // Print the content on the console
            System.out.println(strLine);
        }
        // Close the input stream
        in.close();
    }

}

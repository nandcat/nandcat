package nandcat.model.importexport.sepaf;

import java.io.IOException;
import nandcat.NandcatTest;
import nandcat.model.importexport.FormatException;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.junit.Test;

public class SEPAFCheckReferenceTest {

    @Test(expected = FormatException.class)
    public void testMissingCircuit() throws Exception {
        Document doc = getDocument("../formattest/sepaf-example-invalid-missingcircuitref.xml");
        XMLCheck check = new SEPAFCheckCircuitReference();
        check.setDocument(doc);
        check.validate();
    }

    @Test(expected = FormatException.class)
    public void testCircuitRefOnMain() throws Exception {
        Document doc = getDocument("../formattest/sepaf-example-invalid-circuitrefonmain.xml");
        XMLCheck check = new SEPAFCheckCircuitReference();
        check.setDocument(doc);
        check.validate();
    }

    @Test(expected = FormatException.class)
    public void testCircuitSingleRecursion() throws Exception {
        Document doc = getDocument("../formattest/sepaf-example-invalid-circuitsinglerecursion.xml");
        XMLCheck check = new SEPAFCheckCircuitReference();
        check.setDocument(doc);
        check.validate();
    }

    @Test(expected = FormatException.class)
    public void testCircuitDeepRecursionSingle() throws Exception {
        Document doc = getDocument("../formattest/sepaf-example-invalid-circuitdeepsinglerecursion.xml");
        XMLCheck check = new SEPAFCheckCircuitReference();
        check.setDocument(doc);
        check.validate();
    }

    @Test
    public void testValidCircuitDeepWideRecursion() throws Exception {
        Document doc = getDocument("../formattest/sepaf-example-valid-circuitdeepwiderecursion.xml");
        XMLCheck check = new SEPAFCheckCircuitReference();
        check.setDocument(doc);
        check.validate();
    }

    @Test(expected = FormatException.class)
    public void testInValidCircuitDeepWideRecursion() throws Exception {
        Document doc = getDocument("../formattest/sepaf-example-invalid-circuitdeepwiderecursion.xml");
        XMLCheck check = new SEPAFCheckCircuitReference();
        check.setDocument(doc);
        check.validate();
    }

    @Test
    public void testValidCircuitDeepRecursionSingle() throws Exception {
        Document doc = getDocument("../formattest/sepaf-example-valid-circuitdeepsinglerecursion.xml");
        XMLCheck check = new SEPAFCheckCircuitReference();
        check.setDocument(doc);
    }

    @Test
    public void testValid() throws Exception {
        Document doc = getDocument("../formattest/sepaf-example-valid.xml");
        XMLCheck check = new SEPAFCheckCircuitReference();
        check.setDocument(doc);
        check.validate();
    }

    private Document getDocument(String path) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder();
        builder.setValidation(false);
        builder.setIgnoringElementContentWhitespace(true);
        return builder.build(NandcatTest.class.getResourceAsStream(path));
    }

}

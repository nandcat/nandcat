package nandcat.model.importexport;

import java.io.IOException;
import nandcat.NandcatTest;
import nandcat.model.importexport.sepaf.SEPAFCheckCircuitReference;
import nandcat.model.importexport.sepaf.XMLCheck;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.junit.Ignore;
import org.junit.Test;

public class SEPAFCheckReferenceTest {

    @Test(expected = FormatCheckException.class)
    public void testMissingCircuit() throws Exception {
        Document doc = getDocument("../formattest/sepaf-example-invalid-missingcircuitref.xml");
        XMLCheck check = new SEPAFCheckCircuitReference();
        check.setDocument(doc);
        check.validate();
    }

    @Test(expected = FormatCheckException.class)
    public void testCircuitRefOnMain() throws Exception {
        Document doc = getDocument("../formattest/sepaf-example-invalid-circuitrefonmain.xml");
        XMLCheck check = new SEPAFCheckCircuitReference();
        check.setDocument(doc);
        check.validate();
    }

    @Test(expected = FormatCheckException.class)
    public void testCircuitSingleRecursion() throws Exception {
        Document doc = getDocument("../formattest/sepaf-example-invalid-circuitsinglerecursion.xml");
        XMLCheck check = new SEPAFCheckCircuitReference();
        check.setDocument(doc);
        check.validate();
    }

    @Ignore
    @Test
    public void testCircuitDeepRecursion() {
        // TODO Implement me
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

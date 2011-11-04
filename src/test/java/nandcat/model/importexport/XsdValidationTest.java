package nandcat.model.importexport;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import nandcat.Nandcat;
import nandcat.NandcatTest;
import nandcat.model.XsdValidation;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XsdValidationTest {

    private Source[] xsdSources;

    private ErrorHandler throwingErrorHandler;

    @Before
    public void setUp() throws Exception {
        xsdSources = new Source[] { new StreamSource(Nandcat.class.getResourceAsStream("../sepaf-extension.xsd")),
                new StreamSource(Nandcat.class.getResourceAsStream("../circuits-1.0.xsd")) };
        throwingErrorHandler = new ErrorHandler() {

            public void warning(SAXParseException exception) throws SAXException {
                throw exception;
            }

            public void fatalError(SAXParseException exception) throws SAXException {
                throw exception;
            }

            public void error(SAXParseException exception) throws SAXException {
                throw exception;
            }
        };
    }

    @Test(expected = SAXParseException.class)
    public void testInvalidMissingEndTag() throws SAXException, IOException {
        InputStream invalid1 = NandcatTest.class
                .getResourceAsStream("../formattest/sepaf-example-invalid-missingendtag.xml");
        invalid1.available();
        XsdValidation.validate(new StreamSource(invalid1), xsdSources, throwingErrorHandler);
    }

    @Test
    public void test() throws SAXException, IOException {
        InputStream valid = NandcatTest.class.getResourceAsStream("../formattest/sepaf-example-valid.xml");
        valid.available();
        XsdValidation.validate(new StreamSource(valid), xsdSources, throwingErrorHandler);
    }
}

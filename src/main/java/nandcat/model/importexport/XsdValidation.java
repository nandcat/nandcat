package nandcat.model.importexport;

import java.io.IOException;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/**
 * Validation utility functions.
 */
public final class XsdValidation {

    /**
     * Private constructor for utility class.
     */
    private XsdValidation() {
        throw new IllegalArgumentException();
    }

    /**
     * Validates a given xml against serveral xsd schemas.
     * 
     * @param s
     *            XML source to validate.
     * @param asource
     *            XSD sources.
     * @param handler
     *            Error handler.
     * @throws SAXException
     *             XML Exceptions.
     * @throws IOException
     *             File based exceptions.
     */
    public static void validate(Source s, Source[] asource, ErrorHandler handler) throws SAXException, IOException {
        SchemaFactory schemafactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        Schema schema = schemafactory.newSchema(asource);
        Validator validator = schema.newValidator();
        validator.setErrorHandler(handler);
        validator.validate(s);
    }
}

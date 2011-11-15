package nandcat.model.importexport.sepaf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import nandcat.Nandcat;
import nandcat.model.importexport.XsdValidation;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ImportExportUtil {

    public static String getFileContent(File file) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String strLine;
        StringBuilder content = new StringBuilder();
        while ((strLine = in.readLine()) != null) {
            content.append(strLine);
            content.append("\n");
        }
        // Close the input stream
        in.close();

        return content.toString();
    }

    public static void testValidOutput(File file) throws FileNotFoundException, SAXException, IOException {
        Source[] xsdSources = new Source[] {
                new StreamSource(Nandcat.class.getResourceAsStream("../sepaf-extension.xsd")),
                new StreamSource(Nandcat.class.getResourceAsStream("../circuits-1.0.xsd")) };
        ErrorHandler throwingErrorHandler = new ErrorHandler() {

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
        XsdValidation.validate(new StreamSource(new FileInputStream(file)), xsdSources, throwingErrorHandler);
    }
}

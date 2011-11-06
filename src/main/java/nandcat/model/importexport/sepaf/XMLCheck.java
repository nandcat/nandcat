package nandcat.model.importexport.sepaf;

import nandcat.model.importexport.FormatException;
import org.jdom.Document;

/**
 * Checks if a given JDOM Document is valid.
 */
public interface XMLCheck {

    /**
     * Sets the document to validate.
     * 
     * @param doc
     *            Document to validate.
     */
    void setDocument(Document doc);

    /**
     * Validate the document.
     * 
     * @throws FormatException
     *             Exception if validation was not successful.
     */
    void validate() throws FormatException;
}

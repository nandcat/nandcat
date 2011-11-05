package nandcat.model.importexport.sepaf;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nandcat.model.importexport.FormatCheckException;
import org.jdom.Document;
import org.jdom.Element;

/**
 * Checks if all references exist.
 */
public class SEPAFCheckReferences implements XMLCheck {

    /**
     * Document.
     */
    private Document doc = null;

    /**
     * {@inheritDoc}
     */
    public void setDocument(Document doc) {
        if (doc == null) {
            throw new IllegalArgumentException();
        }
        this.doc = doc;
    }

    public void validate() throws FormatCheckException {
        Element root = doc.getRootElement();
        String mainCircuit = root.getAttributeValue("main");
        if (mainCircuit == null) {
            throw new FormatCheckException("No Maincircuit");
        }

        List rootChildren = root.getChildren();

        // Build set of available circuit references.
        Set<String> availableRefs = new HashSet<String>();
        for (Object circuit : rootChildren) {
            if (circuit instanceof Element && ((Element) circuit).getName().equals("circuit")) {
                String name = ((Element) circuit).getAttributeValue("name");
                if (name == null) {
                    throw new FormatCheckException("Circuit without name");
                }
                availableRefs.add(name);
            }
        }

        for (Object child : rootChildren) {
            if (child instanceof Element && ((Element) child).getName().equals("circuit")) {
                Element circuit = (Element) child;
                String circuitName = circuit.getAttributeValue("name");

                for (Object subChild : circuit.getChildren()) {
                    if (subChild instanceof Element) {
                        Element subChildElement = (Element) subChild;
                        if (subChildElement.getName().equals("component")) {
                            String type = subChildElement.getAttributeValue("type");
                            if (type != null && type.equals("circuit")) {

                            }
                        }
                    }
                }

            }
        }
    }
}

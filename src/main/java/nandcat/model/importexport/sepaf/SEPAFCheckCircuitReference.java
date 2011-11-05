package nandcat.model.importexport.sepaf;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nandcat.model.importexport.FormatCheckException;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

/**
 * Checks if all references are correct.
 */
public class SEPAFCheckCircuitReference implements XMLCheck {

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

    /**
     * Validates if references inside the document are set correctly.
     * 
     * @throws FormatCheckException
     *             Exception with error message if validating fails.
     */
    public void validate() throws FormatCheckException {
        Element root = doc.getRootElement();
        String mainCircuit = root.getAttributeValue("main");
        if (mainCircuit == null) {
            throw new FormatCheckException("No Maincircuit");
        }

        try {
            checkMissingReferences();
            checkReferenceOnMainCircuit();
            // TODO implement: check deep recursion!
            checkSingleRecursion();
        } catch (JDOMException e) {
            throw new FormatCheckException(e);
        }

    }

    /**
     * Checks if a circuit contains reference on itself.
     * 
     * @throws JDOMException
     *             Internal XML Exception
     * @throws FormatCheckException
     *             Exception with error message if validating fails.
     */
    private void checkSingleRecursion() throws JDOMException, FormatCheckException {
        XPath xpathCircuits = XPath.newInstance("/c:circuits/c:circuit");
        XPath xpathRefs = XPath.newInstance("c:component[@type='circuit']/attribute::type2");

        xpathRefs.addNamespace("c", "http://www.sosy-lab.org/Teaching/2011-WS-SEP/xmlns/circuits-1.0");
        xpathCircuits.addNamespace("c", "http://www.sosy-lab.org/Teaching/2011-WS-SEP/xmlns/circuits-1.0");
        List availableCircuitsNodes = xpathCircuits.selectNodes(doc);
        // Set<String> availableCircuitNames = new HashSet<String>();
        if (availableCircuitsNodes.size() == 0) {
            throw new FormatCheckException("No Circuits available");
        }
        for (Object circuit : availableCircuitsNodes) {
            if (circuit instanceof Element) {
                List refs = xpathRefs.selectNodes((Element) circuit);
                for (Object ref : refs) {
                    if (ref instanceof Attribute) {
                        if (((Attribute) ref).getValue().equals(((Element) circuit).getAttributeValue("name"))) {
                            throw new FormatCheckException("Self recursion inside circuit '"
                                    + ((Element) circuit).getAttributeValue("name") + "'");
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks if a circuit contains a reference on the main circuit.
     * 
     * @throws JDOMException
     *             Internal XML Exception
     * @throws FormatCheckException
     *             Exception with error message if validating fails.
     */
    private void checkReferenceOnMainCircuit() throws JDOMException, FormatCheckException {
        Element root = doc.getRootElement();
        String mainCircuit = root.getAttributeValue("main");
        XPath xpathRefs = XPath.newInstance("/c:circuits/c:circuit/c:component[@type='circuit']/attribute::type2");
        xpathRefs.addNamespace("c", "http://www.sosy-lab.org/Teaching/2011-WS-SEP/xmlns/circuits-1.0");
        List circuitRefs = xpathRefs.selectNodes(doc);
        for (Object ref : circuitRefs) {
            if (ref instanceof Attribute) {
                if (((Attribute) ref).getValue().equals(mainCircuit)) {
                    throw new FormatCheckException("Circuit reference on main circuit '" + mainCircuit + "'");
                }
            }
        }

    }

    /**
     * Checks if a referenced circuit is missing.
     * 
     * @throws JDOMException
     *             Internal XML Exception
     * @throws FormatCheckException
     *             Exception with error message if validating fails.
     */
    private void checkMissingReferences() throws JDOMException, FormatCheckException {
        XPath xpath = XPath.newInstance("/c:circuits/c:circuit/attribute::name");
        xpath.addNamespace("c", "http://www.sosy-lab.org/Teaching/2011-WS-SEP/xmlns/circuits-1.0");
        List availableCircuitsNodes = xpath.selectNodes(doc);
        Set<String> availableCircuitNames = new HashSet<String>();
        if (availableCircuitsNodes.size() == 0) {
            throw new FormatCheckException("No Circuits available");
        }
        for (Object circuit : availableCircuitsNodes) {
            if (circuit instanceof Attribute) {
                availableCircuitNames.add(((Attribute) circuit).getValue());
            }
        }

        XPath xpathRefs = XPath.newInstance("/c:circuits/c:circuit/c:component[@type='circuit']/attribute::type2");
        xpathRefs.addNamespace("c", "http://www.sosy-lab.org/Teaching/2011-WS-SEP/xmlns/circuits-1.0");
        List circuitRefs = xpathRefs.selectNodes(doc);
        for (Object ref : circuitRefs) {
            if (ref instanceof Attribute) {
                if (!availableCircuitNames.contains(((Attribute) ref).getValue())) {
                    throw new FormatCheckException("Circuit reference '" + ((Attribute) ref).getValue() + "' not found");
                }
            }
        }
    }
}

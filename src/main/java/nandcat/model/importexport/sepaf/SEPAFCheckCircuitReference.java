package nandcat.model.importexport.sepaf;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import nandcat.model.importexport.FormatException;
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
     * @throws FormatException
     *             Exception with error message if validation fails.
     */
    public void validate() throws FormatException {
        Element root = doc.getRootElement();
        String mainCircuit = root.getAttributeValue("main");
        if (mainCircuit == null) {
            throw new FormatException("No Maincircuit");
        }

        try {
            checkMissingReferences();
            checkReferenceOnMainCircuit();
            checkRecursion();
        } catch (JDOMException e) {
            throw new FormatException(e);
        }

    }

    /**
     * Checks for recursion inside the main circuit. Checks deep recursions. Needed because parser would not stop in
     * this case.
     * 
     * @throws JDOMException
     *             Internal XML Exception.
     * @throws FormatException
     *             Exception with error message if validation fails.
     */
    private void checkRecursion() throws JDOMException, FormatException {
        Element root = doc.getRootElement();
        String mainCircuit = root.getAttributeValue("main");

        // Algo works with stack using DFS
        Deque<String> toVisit = new LinkedList<String>();
        Set<String> completed = new HashSet<String>();
        Set<String> visited = new HashSet<String>();
        toVisit.addFirst(mainCircuit);
        while (!toVisit.isEmpty()) {
            String currentCircuit = toVisit.peekFirst();

            // Second pass, mark path as completed.
            if (visited.contains(currentCircuit)) {
                completed.add(currentCircuit);
                toVisit.pollFirst();
            } else {
                visited.add(currentCircuit);

                List<String> refs = getRefsOfCircuit(currentCircuit);
                for (String ref : refs) {

                    // Ref already completed, no further work needed
                    if (!completed.contains(ref)) {

                        // Current path ends with a cycle.
                        if (visited.contains(ref)) {
                            throw new FormatException("Reference '" + ref + "' inside circuit '" + currentCircuit
                                    + "' would result in a cycle");
                        } else {
                            toVisit.addFirst(ref);
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets circuit references inside a given circuit.
     * 
     * @param name
     *            Circuit's name to search circuits in.
     * @return List of circuit names referenced inside the given circuit.
     * @throws JDOMException
     *             Internal XML Exception.
     */
    private List<String> getRefsOfCircuit(String name) throws JDOMException {
        @SuppressWarnings("rawtypes")
        List refs = SEPAFFormat.getXPathInstance(
                "/c:circuits/c:circuit[@name='" + name + "']/c:component[@type='circuit']").selectNodes(doc);
        LinkedList<String> refNames = new LinkedList<String>();
        for (Object object : refs) {
            if (object instanceof Element) {
                refNames.add(((Element) object).getAttributeValue("type2"));
            }
        }
        return refNames;
    }

    /**
     * Checks if a circuit contains a reference on the main circuit.
     * 
     * @throws JDOMException
     *             Internal XML Exception
     * @throws FormatException
     *             Exception with error message if validating fails.
     */
    private void checkReferenceOnMainCircuit() throws JDOMException, FormatException {
        Element root = doc.getRootElement();
        String mainCircuit = root.getAttributeValue("main");
        XPath xpathRefs = XPath.newInstance("/c:circuits/c:circuit/c:component[@type='circuit']/attribute::type2");
        xpathRefs.addNamespace("c", "http://www.sosy-lab.org/Teaching/2011-WS-SEP/xmlns/circuits-1.0");
        @SuppressWarnings("rawtypes")
        List circuitRefs = xpathRefs.selectNodes(doc);
        for (Object ref : circuitRefs) {
            if (ref instanceof Attribute) {
                if (((Attribute) ref).getValue().equals(mainCircuit)) {
                    throw new FormatException("Circuit reference on main circuit '" + mainCircuit + "'");
                }
            }
        }

    }

    /**
     * Checks if a referenced circuit is missing.
     * 
     * @throws JDOMException
     *             Internal XML Exception
     * @throws FormatException
     *             Exception with error message if validating fails.
     */
    private void checkMissingReferences() throws JDOMException, FormatException {
        XPath xpath = XPath.newInstance("/c:circuits/c:circuit/attribute::name");
        xpath.addNamespace("c", "http://www.sosy-lab.org/Teaching/2011-WS-SEP/xmlns/circuits-1.0");
        @SuppressWarnings("rawtypes")
        List availableCircuitsNodes = xpath.selectNodes(doc);
        Set<String> availableCircuitNames = new HashSet<String>();
        if (availableCircuitsNodes.size() == 0) {
            throw new FormatException("No Circuits available");
        }
        for (Object circuit : availableCircuitsNodes) {
            if (circuit instanceof Attribute) {
                availableCircuitNames.add(((Attribute) circuit).getValue());
            }
        }

        XPath xpathRefs = XPath.newInstance("/c:circuits/c:circuit/c:component[@type='circuit']/attribute::type2");
        xpathRefs.addNamespace("c", "http://www.sosy-lab.org/Teaching/2011-WS-SEP/xmlns/circuits-1.0");
        @SuppressWarnings("rawtypes")
        List circuitRefs = xpathRefs.selectNodes(doc);
        for (Object ref : circuitRefs) {
            if (ref instanceof Attribute) {
                if (!availableCircuitNames.contains(((Attribute) ref).getValue())) {
                    throw new FormatException("Circuit reference '" + ((Attribute) ref).getValue() + "' not found");
                }
            }
        }
    }
}

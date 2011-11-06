package nandcat.model.importexport.sepaf;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import nandcat.Nandcat;
import nandcat.model.element.AndGate;
import nandcat.model.element.Circuit;
import nandcat.model.element.IdentityGate;
import nandcat.model.element.Module;
import nandcat.model.element.NotGate;
import nandcat.model.element.OrGate;
import nandcat.model.importexport.FormatException;
import nandcat.model.importexport.Importer;
import nandcat.model.importexport.XsdValidation;
import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * StandardImporter.
 * 
 * Supports standard SEP format SEPAF.
 */
public class SEPAFImporter implements Importer {

    /**
     * Handle of file to import from.
     */
    private File file = null;

    /**
     * Error message of the import process.
     */
    private String errorMsg = null;

    /**
     * SEPAF namespace.
     */
    private static final Namespace NS_SEPAF = Namespace.getNamespace("c",
            "http://www.sosy-lab.org/Teaching/2011-WS-SEP/xmlns/circuits-1.0");

    /**
     * Custom NANDCat namespace.
     */
    private static final Namespace NS_NANDCAT = Namespace.getNamespace("nandcat",
            "http://www.nandcat.de/xmlns/sepaf-extension");

    /**
     * Checks used to validate xml.
     */
    private XMLCheck[] checks = { new SEPAFCheckCircuitReference() };

    /**
     * Namespaces used in the format.
     */
    private static final Namespace[] namespaces = { NS_SEPAF, NS_NANDCAT };

    /**
     * Defines which errors should result in an invalid validation result. If a exception is thrown the validation
     * result is invalid.
     */
    private static final ErrorHandler VALIDATION_ERROR_HANDLER = new ErrorHandler() {

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

    /**
     * Map of supported file extensions connected with the format description.
     */
    private static final Map<String, String> SUPPORTED_FORMATS = new HashMap<String, String>();
    static {
        SUPPORTED_FORMATS.put("xml", "SEPAF");
    }

    /**
     * Imported circuit.
     */
    private Circuit importedCircuit = null;

    /**
     * XSD files to validate XML against.
     */
    private static final Source[] XSD_SOURCES = new Source[] {
            new StreamSource(Nandcat.class.getResourceAsStream("../sepaf-extension.xsd")),
            new StreamSource(Nandcat.class.getResourceAsStream("../circuits-1.0.xsd")) };

    /**
     * {@inheritDoc}
     */
    public void setFile(File file) {
        if (file == null) {
            throw new IllegalArgumentException();
        }
        this.file = file;
    }

    /**
     * {@inheritDoc}
     */
    public Circuit getCircuit() {
        return importedCircuit;
    }

    /**
     * {@inheritDoc}
     */
    public boolean importCircuit() {
        try {
            if (validateXML()) {

                Document doc = getDocument(file);
                runXMLChecks(doc);
                importFromDocument(doc);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }

    /**
     * Imports from given document.
     * 
     * @param doc
     *            Document used for import.
     * @throws FormatException
     *             if format is not valid.
     */
    private void importFromDocument(Document doc) throws FormatException {
        Element root = doc.getRootElement();
        String mainCircuitName = root.getAttributeValue("main");
        this.importedCircuit = buildCircuit(mainCircuitName, doc);

    }

    /**
     * Builds a circuit from the document identified by given name.
     * 
     * @param name
     *            Name of the circuit inside the document.
     * @param doc
     *            Document holds circuit.
     * @return Constructed circuit with all sub elements.
     * @throws FormatException
     *             if format is not valid.
     */
    @SuppressWarnings("rawtypes")
    private Circuit buildCircuit(String name, Document doc) throws FormatException {
        if (name == null) {
            throw new IllegalArgumentException();
        }
        List mainComponents = null;
        try {
            mainComponents = getXPathInstance("/c:circuits/c:circuit[@name='" + name + "']/c:component").selectNodes(
                    doc);
        } catch (JDOMException e) {
            // Does not happen!
            e.printStackTrace();
        }
        if (mainComponents == null || mainComponents.isEmpty()) {
            throw new FormatException("Circuit empty: '" + name + "'");
        }
        Circuit circuit = new Circuit(null);

        for (Object c : mainComponents) {
            if (c instanceof Element) {
                Element compE = (Element) c;
                circuit.addModule(buildModule(compE));
            }

        }

        return circuit;
    }

    /**
     * Builds a module from a given DOM Element.
     * 
     * @param el
     *            Dom Element to build module from.
     * @return Constructed module.
     * @throws FormatException
     *             if format is not valid.
     */
    private Module buildModule(Element el) throws FormatException {
        if (el == null) {
            throw new IllegalArgumentException();
        }
        Attribute aType = el.getAttribute("type", NS_SEPAF);
        Attribute aSubtype = el.getAttribute("type2", NS_SEPAF);
        Attribute aName = el.getAttribute("name", NS_SEPAF);
        Attribute aPosX = el.getAttribute("posx", NS_SEPAF);
        Attribute aPosY = el.getAttribute("posy", NS_SEPAF);
        Point location = null;
        try {
            location = new Point(aPosX.getIntValue(), aPosY.getIntValue());
        } catch (DataConversionException e) {
            throw new FormatException("Coordinates not integer", e);
        }
        Attribute aAnnotation = el.getAttribute("annotation", NS_NANDCAT);
        Attribute aPortsIn = el.getAttribute("ports_in", NS_NANDCAT);
        Attribute aPortsOut = el.getAttribute("ports_out", NS_NANDCAT);
        Integer portsIn = null;
        Integer portsOut = null;
        if (aPortsIn != null) {
            try {
                portsIn = aPortsIn.getIntValue();
            } catch (DataConversionException e) {
                throw new FormatException("ports_in not integer", e);
            }
        }
        if (aPortsOut != null) {
            try {
                portsOut = aPortsOut.getIntValue();
            } catch (DataConversionException e) {
                throw new FormatException("ports_in not integer", e);
            }
        }

        Attribute aInState = el.getAttribute("in_state", NS_NANDCAT);
        Module module = null;
        if (aType.getValue().equals("and")) {
            AndGate andGate = null;
            if (portsIn != null && portsOut != null) {
                andGate = new AndGate(portsIn, portsOut);
            } else {
                andGate = new AndGate();
            }
            module = andGate;
        } else if (aType.getValue().equals("or")) {
            OrGate orGate = null;
            if (portsIn != null && portsOut != null) {
                orGate = new OrGate(portsIn, portsOut);
            } else {
                orGate = new OrGate();
            }
            module = orGate;
        } else if (aType.getValue().equals("id")) {
            IdentityGate idGate = null;
            if (portsIn != null && portsOut != null) {
                idGate = new IdentityGate(portsIn, portsOut);
            } else {
                idGate = new IdentityGate();
            }
            module = idGate;
        } else if (aType.getValue().equals("not")) {
            NotGate notGate = null;
            if (portsIn != null && portsOut != null) {
                notGate = new NotGate(portsOut);
            } else {
                notGate = new NotGate();
            }
            module = notGate;
        } else {
            throw new FormatException("Not a supported component type: '" + aType.getValue() + "'");
        }

        module.setLocation(location);
        if (aAnnotation != null) {
            String annotation = aAnnotation.getValue();
            module.setName(annotation);
        }
        return module;

    }

    /**
     * Runs all custom xml checks against the document.
     * 
     * @param doc
     *            Document to check.
     * @throws FormatException
     *             FormatException Thrown if format is not valid.
     */
    private void runXMLChecks(Document doc) throws FormatException {
        for (XMLCheck xmlCheck : checks) {
            xmlCheck.setDocument(doc);
            xmlCheck.validate();
        }
    }

    /**
     * Gets the JDom document from the given file.
     * 
     * @param file
     *            File to parse.
     * @return JDom Document of the XML file.
     * @throws JDOMException
     *             JDom exception occurred while parsing.
     * @throws IOException
     *             IOException occurred.
     */
    private Document getDocument(File file) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder();
        builder.setValidation(false);
        builder.setIgnoringElementContentWhitespace(true);
        return builder.build(file);
    }

    /**
     * Validates the file against given xsd files.
     * 
     * Errors can be configured by the VALIDATION_ERROR_HANDLER.
     * 
     * @return True iff XML validates well.
     */
    private boolean validateXML() {
        try {
            XsdValidation.validate(new StreamSource(file), XSD_SOURCES, VALIDATION_ERROR_HANDLER);
        } catch (SAXException e) {
            setErrorMessage(e.getLocalizedMessage());
            return false;
        } catch (IOException e) {
            setErrorMessage(e.getLocalizedMessage());
            return false;
        }
        return true;
    }

    /**
     * Creates a XPath instance with given path and added namespaces.
     * 
     * @param path
     *            Path to instantiate xpath with.
     * @return XPath instance with namespaces.
     * @throws JDOMException
     *             Exception if path is wrong.
     */
    private XPath getXPathInstance(String path) throws JDOMException {
        XPath xpath = XPath.newInstance(path);
        for (Namespace ns : namespaces) {
            xpath.addNamespace(ns);
        }
        return xpath;
    }

    /**
     * {@inheritDoc}
     */
    public String getErrorMessage() {
        return errorMsg;
    }

    /**
     * Sets the error message.
     * 
     * @param errorMsg
     *            Error message to set.
     */
    private void setErrorMessage(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, String> getFileFormats() {
        return SUPPORTED_FORMATS;
    }
}

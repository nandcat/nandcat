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
import nandcat.model.element.FlipFlop;
import nandcat.model.element.IdentityGate;
import nandcat.model.element.ImpulseGenerator;
import nandcat.model.element.Lamp;
import nandcat.model.element.Module;
import nandcat.model.element.NotGate;
import nandcat.model.element.OrGate;
import nandcat.model.element.Port;
import nandcat.model.importexport.FormatException;
import nandcat.model.importexport.Importer;
import nandcat.model.importexport.XsdValidation;
import org.apache.log4j.Logger;
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
     * Class logger instance.
     */
    private static final Logger LOG = Logger.getLogger(SEPAFImporter.class);

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
    // static final results in null pointer exception at XSD validation (oracle bug?)
    private Source[] xsdSources = new Source[] {
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
                if (doc == null) {
                    setErrorMessage("File not found");
                    return false;
                }
                runXMLChecks(doc);
                importFromDocument(doc);

            } else {
                setErrorMessage("XML Validation failed");
                return false;
            }
        } catch (FormatException f) {
            System.out.println(f.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
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
            throw new FormatException("Circuit has no components: '" + name + "'");
        }
        Circuit circuit = new Circuit(null);

        // Index of imported modules used for connections
        Map<String, Module> moduleIndex = new HashMap<String, Module>();

        for (Object c : mainComponents) {
            if (c instanceof Element) {
                Element compE = (Element) c;
                buildModule(circuit, compE, moduleIndex);
            }
        }

        // Connections between modules
        List connections = null;
        try {
            connections = getXPathInstance("/c:circuits/c:circuit[@name='" + name + "']/c:connection").selectNodes(doc);
        } catch (JDOMException e) {
            // Does not happen!
            e.printStackTrace();
        }
        if (connections == null || connections.isEmpty()) {
            LOG.debug("Circuit '" + name + "' has no connections");
        } else {
            for (Object c : connections) {
                if (c instanceof Element) {
                    Element connE = (Element) c;
                    buildConnection(circuit, connE, moduleIndex);
                }
            }
        }

        return circuit;
    }

    /**
     * Builds a module from a given DOM Element and adds it to the circuit.
     * 
     * @param c
     *            Parent Circuit of the module.
     * @param el
     *            Dom Element to build module of.
     * @param index
     *            Index of modules to add the parsed module to.
     * @throws FormatException
     *             if format is not valid.
     */
    private void buildModule(Circuit c, Element el, Map<String, Module> index) throws FormatException {
        if (el == null) {
            throw new IllegalArgumentException();
        }
        LOG.debug("Build module: " + el.getQualifiedName() + " : " + el.getAttributeValue("name"));
        Attribute aType = el.getAttribute("type");
        Attribute aSubtype = el.getAttribute("type2");
        Attribute aName = el.getAttribute("name");
        Attribute aPosX = el.getAttribute("posx");
        Attribute aPosY = el.getAttribute("posy");
        if (aPosX == null) {
            throw new FormatException("posx not valid at :" + el.getQualifiedName());
        }
        if (aPosY == null) {
            throw new FormatException("posy not valid at :" + el.getQualifiedName());
        }

        Point location = null;
        try {
            location = new Point(aPosX.getIntValue(), aPosY.getIntValue());
        } catch (DataConversionException e) {
            throw new FormatException("Coordinates not integer", e);
        }
        Attribute aAnnotation = el.getAttribute("annotation", NS_NANDCAT);
        Attribute aPortsIn = el.getAttribute("ports_in", NS_NANDCAT);
        Attribute aPortsOut = el.getAttribute("ports_out", NS_NANDCAT);
        Attribute inState = el.getAttribute("in_state", NS_NANDCAT);
        Attribute inTiming = el.getAttribute("in_timing", NS_NANDCAT);

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
        } else if (aType.getValue().equals("out")) {
            Lamp lamp = null;

            if (portsIn != null && portsOut != null) {
                LOG.debug("Component 'out' does not support 'ports_in' or 'ports_out'");
            }
            lamp = new Lamp();
            module = lamp;
        } else if (aType.getValue().equals("flipflop")) {
            FlipFlop ff = null;

            if (portsIn != null && portsOut != null) {
                LOG.debug("Component 'flipflop' does not support 'ports_in' or 'ports_out'");
            }
            // TODO FlipFlop und Circuit sollte kein Point im Konstruktor haben
            ff = new FlipFlop(null);
            module = ff;
        } else if (aType.getValue().equals("in")) {
            ImpulseGenerator ig = null;

            if (portsIn != null && portsOut != null) {
                LOG.debug("Component 'in' does not support 'ports_in' or 'ports_out'");
            }
            // TODO DocComment in ImpulseGenerator
            ig = new ImpulseGenerator(0);
            if (inState != null) {
                Boolean state = null;
                try {
                    state = inState.getBooleanValue();
                } catch (DataConversionException e) {
                    throw new FormatException("'in_state' not boolean", e);
                }
                if (state != null && state) {
                    ig.toggleState();
                }
            } else {
                LOG.debug("Component 'in' does not has 'in_state', use default");
            }
            module = ig;
        } else if (aType.getValue().equals("clock")) {
            ImpulseGenerator ig = null;

            if (portsIn != null && portsOut != null) {
                LOG.debug("Component 'clock' does not support 'ports_in' or 'ports_out'");
            }
            if (inTiming != null) {
                Integer timing = null;
                try {
                    timing = inTiming.getIntValue();
                } catch (DataConversionException e) {
                    throw new FormatException("'in_timinig' not integer", e);
                }
                if (timing != null) {
                    ig = new ImpulseGenerator(timing);
                }
            } else {
                LOG.debug("Component 'clock' does not has 'in_timing', use default");
                // FIXME Default constructor benötigt für ImpulseGenerator.
                ig = new ImpulseGenerator(50);
            }

            if (inState != null) {
                Boolean state = null;
                try {
                    state = inState.getBooleanValue();
                } catch (DataConversionException e) {
                    throw new FormatException("'in_state' not boolean", e);
                }
                if (state != null && state) {
                    ig.toggleState();
                }
            } else {
                LOG.debug("Component 'clock' does not has a state, use default");
            }
            module = ig;
        } else {
            throw new FormatException("Not a supported component type: '" + aType.getValue() + "'");
        }

        module.setLocation(location);
        if (aAnnotation != null) {
            String annotation = aAnnotation.getValue();
            module.setName(annotation);
        }
        index.put(el.getAttributeValue("name"), module);
        c.addModule(module);
    }

    /**
     * Builds a connection from given DOM element and adds it to the circuit.
     * 
     * @param c
     *            Circuit to add connection to.
     * @param el
     *            DOM element to build connection of.
     * @param moduleIndex
     *            Holding all modules connection with their identifiers, used for quick referencing.
     * @throws FormatException
     *             if format is not valid.
     */
    private void buildConnection(Circuit c, Element el, Map<String, Module> moduleIndex) throws FormatException {
        String source = el.getAttributeValue("source");
        String target = el.getAttributeValue("target");
        String sourcePort = el.getAttributeValue("sourcePort");
        String targetPort = el.getAttributeValue("targetPort");
        if (source == null || source.isEmpty()) {
            throw new FormatException("Connection has no source");
        }
        if (target == null || target.isEmpty()) {
            throw new FormatException("Connection has no target");
        }
        if (sourcePort == null || sourcePort.isEmpty()) {
            throw new FormatException("Connection has no sourcePort");
        }
        if (targetPort == null || targetPort.isEmpty()) {
            throw new FormatException("Connection has no targetPort");
        }
        Module sourceModule = moduleIndex.get(source);
        Module targetModule = moduleIndex.get(target);
        if (sourceModule == null) {
            throw new FormatException("Connection: source module '" + source + "' not found");
        }
        if (targetModule == null) {
            throw new FormatException("Connection: target module '" + target + "' not found");
        }

        c.addConnection(getSourcePort(sourceModule, sourcePort), getTargetPort(targetModule, targetPort));
    }

    /**
     * Gets the target port of a connection (inPort of a module) depending on the port name (string).
     * 
     * @param module
     *            Module to get port of.
     * @param portname
     *            Name of the port, a character identifying the number of the port.
     * @return Selected port.
     * @throws FormatException
     *             If portname is wrong.
     */
    private Port getTargetPort(Module module, String portname) throws FormatException {
        int portnr = ((int) portname.charAt(0)) - ((int) 'a');
        if (portnr < 0) {
            throw new FormatException("Connection: wrong targetport, requested: " + portnr);
        }
        if (portnr < module.getInPorts().size()) {
            return module.getInPorts().get(portnr);
        } else {
            throw new FormatException("Connection: wrong targetport, module has only " + module.getInPorts().size()
                    + " inports, requested: " + portnr);
        }
    }

    /**
     * Gets the source port of a connection (outPort of a module) depending on the port name (string).
     * 
     * @param module
     *            Module to get port of.
     * @param portname
     *            Name of the port, a character identifying the number of the port.
     * @return Selected port.
     * @throws FormatException
     *             If portname is wrong.
     */
    private Port getSourcePort(Module module, String portname) throws FormatException {
        int portnr = ((int) portname.charAt(0)) - ((int) 'o');
        if (portnr < 0) {
            throw new FormatException("Connection: wrong targetport, requested: " + portnr);
        }
        if (portnr < module.getOutPorts().size()) {
            return module.getOutPorts().get(portnr);
        } else {
            throw new FormatException("Connection: wrong sourceport, module has only " + module.getOutPorts().size()
                    + " inports, requested: " + portnr);
        }
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
            XsdValidation.validate(new StreamSource(file), xsdSources, VALIDATION_ERROR_HANDLER);
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

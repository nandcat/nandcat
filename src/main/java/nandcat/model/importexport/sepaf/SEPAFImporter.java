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
import nandcat.model.FastDeepCopy;
import nandcat.model.element.Circuit;
import nandcat.model.element.Module;
import nandcat.model.element.Port;
import nandcat.model.element.factory.ModuleBuilder;
import nandcat.model.element.factory.ModuleBuilderFactory;
import nandcat.model.importexport.ExternalCircuitSource;
import nandcat.model.importexport.FormatErrorHandler;
import nandcat.model.importexport.FormatException;
import nandcat.model.importexport.Importer;
import nandcat.model.importexport.XsdValidation;
import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
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
     * Checks used to validate xml.
     */
    private XMLCheck[] checks = { new SEPAFCheckCircuitReference() };

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
     * Circuit index for fast copy.
     */
    // TODO Reset needed if reused
    private Map<String, Circuit> circuitIndex;

    /**
     * XSD files to validate XML against.
     */
    // static final results in null pointer exception at XSD validation (oracle
    // bug?)
    private Source[] xsdSources;

    /**
     * Source of external circuits.
     */
    private ExternalCircuitSource externalCircuitSource;

    /**
     * Factory to create Elements with.
     */
    private ModuleBuilderFactory factory;

    /**
     * Errorhandler to handle errors.
     */
    private FormatErrorHandler errorHandler;

    /**
     * Sets the instance up.
     */
    public SEPAFImporter() {
        reset();
    }

    /**
     * {@inheritDoc}
     */
    public void setFile(File file) {
        reset();
        if (file == null) {
            throw new IllegalArgumentException();
        }
        this.file = file;
    }

    /**
     * Resets internal state.
     */
    public void reset() {
        circuitIndex = new HashMap<String, Circuit>();
        xsdSources = new Source[] {
                new StreamSource(Nandcat.class.getResourceAsStream(SEPAFFormat.VALIDATIONSCHEMA.SCHEMA_NANDCAT)),
                new StreamSource(Nandcat.class.getResourceAsStream(SEPAFFormat.VALIDATIONSCHEMA.SCHEMA_SEPAF)) };
        importedCircuit = null;
        file = null;
        errorMsg = null;
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
        if (file == null) {
            throw new IllegalArgumentException("File not set");
        }
        if (factory == null) {
            throw new IllegalArgumentException("Factory not set");
        }
        try {
            if (validateXML()) {
                try {
                    Document doc = getDocument(file);
                    if (doc == null) {
                        throwFatalError(new FormatException("File not found: " + file.getAbsolutePath()));
                        return false;
                    }
                    runXMLChecks(doc);
                    importFromDocument(doc);
                } catch (JDOMException e) {
                    throwFatalError(new FormatException("XML could not be parsed: " + file.getAbsolutePath(), e));
                    return false;
                } catch (IOException e) {
                    throwFatalError(new FormatException("File could not be read:" + file.getAbsolutePath(), e));
                    return false;
                }

            } else {
                throwFatalError(new FormatException("XML Validation failed"));
                return false;
            }
        } catch (FormatException e) {
            // Exception already handled through error handler.
            return false;
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
        Circuit circuit = null;

        // Used cached circuit if parsed second time.
        if (circuitIndex.containsKey(name)) {

            // Create deep copy of circuit
            circuit = (Circuit) FastDeepCopy.copy(circuitIndex.get(name));
        } else {
            if (name == null) {
                // TODO Name null possible?
                throw new IllegalArgumentException();
            }
            List mainComponents = null;
            try {
                mainComponents = SEPAFFormat
                        .getXPathInstance("/c:circuits/c:circuit[@name='" + name + "']/c:component").selectNodes(doc);
            } catch (JDOMException e) {
                // Does not happen!
                e.printStackTrace();
            }
            if (mainComponents == null || mainComponents.isEmpty()) {
                throwWarning(new FormatException("Circuit has no components: '" + name + "'"));
            }
            circuit = (Circuit) factory.getCircuitBuilder().setUUID(name).build();

            // Index of imported modules used for connections
            Map<String, Module> moduleIndex = new HashMap<String, Module>();

            for (Object c : mainComponents) {
                if (c instanceof Element) {
                    Element compE = (Element) c;
                    buildModule(circuit, compE, doc, moduleIndex);
                }
            }

            // Connections between modules
            List connections = null;
            try {
                connections = SEPAFFormat.getXPathInstance("/c:circuits/c:circuit[@name='" + name + "']/c:connection")
                        .selectNodes(doc);
            } catch (JDOMException e) {
                // Does not happen!
                e.printStackTrace();
            }
            if (connections == null || connections.isEmpty()) {
                throwWarning(new FormatException("Circuit has no connections: '" + name + "'"));
            } else {
                for (Object c : connections) {
                    if (c instanceof Element) {
                        Element connE = (Element) c;
                        buildConnection(circuit, connE, moduleIndex);
                    }
                }
            }

            // Parse symbol if existing
            try {
                Object symbol = SEPAFFormat.getXPathInstance(
                        "/c:circuits/c:circuit[@name='" + name + "']/nandcat:symbol").selectSingleNode(doc);
                if (symbol != null && symbol instanceof Element) {
                    Element symbolElement = (Element) symbol;
                    circuit.setSymbol(SEPAFFormat.decodeImage(symbolElement.getText()));
                    LOG.debug("Symbol set to circuit successfully");
                }
            } catch (JDOMException e) {
                // Does not happen!
                e.printStackTrace();
            }

            // Add constructed circuit to cache index
            circuitIndex.put(name, circuit);
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
     * @param doc
     *            Document to work on.
     * @param index
     *            Index of modules to add the parsed module to.
     * @throws FormatException
     *             if format is not valid.
     */
    private void buildModule(Circuit c, Element el, Document doc, Map<String, Module> index) throws FormatException {
        if (el == null) {
            throw new IllegalArgumentException();
        }
        LOG.trace("Build module: " + el.getQualifiedName() + " : " + el.getAttributeValue("name"));
        Attribute aType = el.getAttribute("type");
        Attribute aPosX = el.getAttribute("posx");
        Attribute aPosY = el.getAttribute("posy");

        // Position x and y needed for all components.
        if (aPosX == null) {
            throwError(new FormatException("No X coordinate found for component:" + el.getQualifiedName()));
            return;
        }
        if (aPosY == null) {
            throwError(new FormatException("No Y coordinate found for component:" + el.getQualifiedName()));
            return;
        }

        // Build point from coordinates.
        Point location = null;
        try {
            location = new Point(aPosX.getIntValue(), aPosY.getIntValue());
        } catch (DataConversionException e) {
            throwError(new FormatException("Coordinate found for component are not valid numbers:"
                    + el.getQualifiedName()));
            return;
        }

        // Get attributes from nandcat extension. null if not present.
        Attribute aAnnotation = el.getAttribute("annotation", SEPAFFormat.NAMESPACE.NANDCAT);
        Attribute aPortsIn = el.getAttribute("ports_in", SEPAFFormat.NAMESPACE.NANDCAT);
        Attribute aPortsOut = el.getAttribute("ports_out", SEPAFFormat.NAMESPACE.NANDCAT);
        Attribute inTiming = el.getAttribute("in_timing", SEPAFFormat.NAMESPACE.NANDCAT);

        // Parse attribute for amount of incoming and outgoing ports if
        // available.
        Integer portsIn = null;
        Integer portsOut = null;
        if (aPortsIn != null) {
            try {
                portsIn = aPortsIn.getIntValue();
            } catch (DataConversionException e) {
                throwWarning(new FormatException("Number of incoming ports can not be parsed for component:"
                        + el.getQualifiedName()));
            }
        }
        if (aPortsOut != null) {
            try {
                portsOut = aPortsOut.getIntValue();
            } catch (DataConversionException e) {
                throwWarning(new FormatException("Number of outgoing ports can not be parsed for component:"
                        + el.getQualifiedName()));
            }
        }

        // Instantiate specified module.
        Module module = null;
        if (aType.getValue().equals("and")) {
            ModuleBuilder b = factory.getAndGateBuilder();
            if (portsIn != null) {
                b.setInPorts(portsIn);
            }

            if (portsOut != null) {
                b.setOutPorts(portsOut);
            }
            b.setLocation(location);
            module = b.build();
        } else if (aType.getValue().equals("or")) {
            ModuleBuilder b = factory.getOrGateBuilder();
            if (portsIn != null) {
                b.setInPorts(portsIn);
            }

            if (portsOut != null) {
                b.setOutPorts(portsOut);
            }
            b.setLocation(location);
            module = b.build();
        } else if (aType.getValue().equals("id")) {
            ModuleBuilder b = factory.getIdentityGateBuilder();
            if (portsOut != null) {
                b.setOutPorts(portsOut);
            }
            b.setLocation(location);
            module = b.build();
        } else if (aType.getValue().equals("not")) {
            ModuleBuilder b = factory.getNotGateBuilder();
            if (portsOut != null) {
                b.setOutPorts(portsOut);
            }
            b.setLocation(location);
            module = b.build();
        } else if (aType.getValue().equals("out")) {
            ModuleBuilder b = factory.getLampBuilder();
            b.setLocation(location);
            module = b.build();
        } else if (aType.getValue().equals("flipflop")) {
            ModuleBuilder b = factory.getFlipFlopBuilder();
            b.setLocation(location);
            module = b.build();
        } else if (aType.getValue().equals("in")) {
            ModuleBuilder b = factory.getSwitchBuilder();
            b.setLocation(location);
            module = b.build();
        } else if (aType.getValue().equals("clock")) {
            ModuleBuilder b = factory.getClockBuilder();
            b.setLocation(location);
            if (inTiming != null) {
                Integer timing = null;
                try {
                    timing = inTiming.getIntValue();
                } catch (DataConversionException e) {
                    throwWarning(new FormatException("Timing of clock can not be parsed for component:"
                            + el.getQualifiedName()));
                }
                if (timing != null) {
                    b.setFrequency(timing);
                }
            }

            module = b.build();
        } else if (aType.getValue().equals("circuit")) {
            module = buildCircuit(el.getAttributeValue("type2"), doc);
            module.getRectangle().setLocation(location);
            factory.getLayouter().layout((Circuit) module);
        } else if (aType.getValue().equals("missing-circuit")) {
            String externalIdentifier = el.getAttributeValue("type2");
            if (externalCircuitSource != null) {
                Circuit externalCircuit = externalCircuitSource.getExternalCircuit(externalIdentifier);
                if (externalCircuit != null) {
                    module = (Circuit) FastDeepCopy.copy(externalCircuit);
                } else {
                    throwError(new FormatException("External circuit cannot be found: " + externalIdentifier));
                    return;
                }
            } else {
                throwError(new FormatException(
                        "External circuit cannot be found because external circuits are not available: "
                                + externalIdentifier));
                return;
            }
            module.getRectangle().setLocation(location);
            factory.getLayouter().layout((Circuit) module);
        } else {
            throwWarning(new FormatException("Not a supported component type: '" + aType.getValue() + "'"));
            return;
        }

        if (aAnnotation != null) {
            String annotation = aAnnotation.getValue();
            module.setName(annotation);
        }

        // Put module in the index for referencing later.
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
            throwError(new FormatException("Connection has no source"));
            return;
        }
        if (target == null || target.isEmpty()) {
            throwError(new FormatException("Connection has no target"));
            return;
        }
        if (sourcePort == null || sourcePort.isEmpty()) {
            throwError(new FormatException("Connection has no sourcePort"));
            return;
        }
        if (targetPort == null || targetPort.isEmpty()) {
            throwError(new FormatException("Connection has no targetPort"));
            return;
        }

        Module sourceModule = moduleIndex.get(source);
        Module targetModule = moduleIndex.get(target);
        if (sourceModule == null) {
            throwError(new FormatException("Connection: source module '" + source + "' not found"));
            return;
        }
        if (targetModule == null) {
            throwError(new FormatException("Connection: target module '" + target + "' not found"));
            return;
        }

        Port startPort = null;
        try {
            startPort = SEPAFFormat.getStringAsPort(true, sourceModule, sourcePort);
        } catch (Exception e) {
            throwError(new FormatException("Connection not created from '" + source + "' to '" + target + "'", e));
            return;
        }

        Port endPort = null;
        try {
            endPort = SEPAFFormat.getStringAsPort(false, targetModule, targetPort);
        } catch (Exception e) {
            throwError(new FormatException("Connection not created from '" + source + "' to '" + target + "'", e));
            return;
        }
        if (startPort != null && endPort != null) {
            c.addConnection(startPort, endPort);
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
            XsdValidation.validate(new StreamSource(file), xsdSources, new ErrorHandler() {

                public void warning(SAXParseException exception) throws SAXException {
                    try {
                        throwWarning(new FormatException("Warning while validation", exception));
                    } catch (FormatException e) {
                        throw new SAXException(e);
                    }
                }

                public void fatalError(SAXParseException exception) throws SAXException {
                    try {
                        throwFatalError(new FormatException("Fatal Error while validation", exception));
                    } catch (FormatException e) {
                        throw new SAXException(e);
                    }

                }

                public void error(SAXParseException exception) throws SAXException {
                    try {
                        throwError(new FormatException("Error while validation", exception));
                    } catch (FormatException e) {
                        throw new SAXException(e);
                    }
                }
            });
        } catch (SAXException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return true;
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
        LOG.debug("Error: " + errorMsg);
        this.errorMsg = errorMsg + " | " + this.errorMsg;
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, String> getFileFormats() {
        return SUPPORTED_FORMATS;
    }

    /**
     * {@inheritDoc}
     */
    public void setExternalCircuitSource(ExternalCircuitSource source) {
        this.externalCircuitSource = source;
    }

    /**
     * {@inheritDoc}
     */
    public void setFactory(ModuleBuilderFactory factory) {
        if (factory == null) {
            throw new IllegalArgumentException();
        }
        this.factory = factory;
    }

    /**
     * Throws a warning using the Error Handler. If error handler decides to throw exception, processing is stopped by
     * this exception.
     * 
     * @param e
     *            Exception with information about warning.
     * @throws FormatException
     *             FormatException, reason for stop processing.
     */
    private void throwWarning(FormatException e) throws FormatException {
        if (this.errorHandler != null) {
            this.errorHandler.warning(e);
        } else {
            throw e;
        }
    }

    /**
     * Throws a error using the Error Handler. If error handler decides to throw exception, processing is stopped by
     * this exception.
     * 
     * @param e
     *            Exception with information about error.
     * @throws FormatException
     *             FormatException, reason for stop processing.
     */
    private void throwError(FormatException e) throws FormatException {
        if (this.errorHandler != null) {
            this.errorHandler.error(e);
        } else {
            throw e;
        }
    }

    /**
     * Throws a fatal error using the Error Handler. If error handler decides to throw exception, processing is stopped
     * by this exception.
     * 
     * @param e
     *            Exception with information about fatal error.
     * @throws FormatException
     *             FormatException, reason for stop processing.
     */
    private void throwFatalError(FormatException e) throws FormatException {
        if (this.errorHandler != null) {
            this.errorHandler.warning(e);
        } else {
            throw e;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setErrorHandler(FormatErrorHandler h) {
        if (h == null) {
            throw new IllegalArgumentException();
        }
        this.errorHandler = h;
    }
}

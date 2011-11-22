package nandcat.model.importexport.sepaf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nandcat.model.element.AndGate;
import nandcat.model.element.Circuit;
import nandcat.model.element.Connection;
import nandcat.model.element.FlipFlop;
import nandcat.model.element.IdentityGate;
import nandcat.model.element.ImpulseGenerator;
import nandcat.model.element.Lamp;
import nandcat.model.element.Module;
import nandcat.model.element.NotGate;
import nandcat.model.element.OrGate;
import nandcat.model.importexport.Exporter;
import nandcat.model.importexport.FormatErrorHandler;
import nandcat.model.importexport.FormatException;
import org.apache.log4j.Logger;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * StandardExporter.
 * 
 * Supports standard SEP format SEPAF.
 */
public class SEPAFExporter implements Exporter {

    /**
     * Class logger instance.
     */
    private static final Logger LOG = Logger.getLogger(SEPAFExporter.class);

    /**
     * File to save exported circuit in.
     */
    private File file;

    /**
     * Circuit to export.
     */
    private Circuit circuit;

    /**
     * Error message of the export process.
     */
    private String errorMsg;

    /**
     * Map of supported file extensions connected with the format description.
     */
    private static final Map<String, String> SUPPORTED_FORMATS = new HashMap<String, String>();
    static {
        SUPPORTED_FORMATS.put("xml", "SEPAF");
    }

    /**
     * Index of already generated inner circuits. Indexed by the uuid. Used to avoid double generating.
     */
    private Set<String> innerCircuitsIndex = new LinkedHashSet<String>();

    /**
     * Root element of the current document.
     */
    private Element root = null;

    /**
     * External circuits mapped with their uuid and external uuid.
     */
    private Map<String, String> externalCircuits = new HashMap<String, String>();

    private FormatErrorHandler errorHandler;

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
        innerCircuitsIndex = new LinkedHashSet<String>();
        file = null;
        errorMsg = null;
    }

    /**
     * {@inheritDoc}
     */
    public boolean exportCircuit() {
        if (circuit == null) {
            throw new IllegalArgumentException("Circuit is null");
        }
        if (file == null) {
            throw new IllegalArgumentException("File is null");
        }
        Document doc = new Document(new Element("circuits", SEPAFFormat.NAMESPACE.SEPAF));
        Element root = doc.getRootElement();
        root.setAttribute("schemaLocation", SEPAFFormat.NAMESPACE.SCHEMA_LOCATION, SEPAFFormat.NAMESPACE.XSI);
        this.root = root;
        root.addNamespaceDeclaration(SEPAFFormat.NAMESPACE.NANDCAT);
        try {
            root.setAttribute("main", circuit.getUuid());
            root.addContent(buildCircuit(circuit, true));
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            outputter.output(doc, new FileOutputStream(file));
            return true;
        } catch (FileNotFoundException e) {
            setErrorMessage("File not found");
        } catch (IOException e) {
            setErrorMessage("Can not read file");
        } catch (FormatException e) {
            setErrorMessage("Internal error: " + e.getMessage());
        }

        return false;
    }

    /**
     * Builds an DOM element of given circuit.
     * 
     * @param c
     *            Circuit to build DOM element of.
     * @param mainCircuit
     *            True iff circuit is main circuit.
     * @return Element representing the circuit.
     * @throws FormatException
     *             If generation of sub elements went wrong.
     */
    private Element buildCircuit(Circuit c, boolean mainCircuit) throws FormatException {
        org.jdom.Element e = new Element("circuit", SEPAFFormat.NAMESPACE.SEPAF);
        e.setAttribute("name", c.getUuid());

        // Connections have to be after all components.
        List<Connection> cachedConnections = new LinkedList<Connection>();
        Set<Module> cachedModules = new HashSet<Module>();
        for (nandcat.model.element.Element circuitE : c.getElements()) {
            if (circuitE instanceof Connection) {
                cachedConnections.add((Connection) circuitE);
            } else {
                e.addContent(buildModule(circuitE));
                cachedModules.add((Module) circuitE);
            }
        }
        for (Connection connection : cachedConnections) {
            e.addContent(buildConnection(connection, cachedModules));
        }
        if (mainCircuit && c.getSymbol() != null) {
            org.jdom.Element symbol = new Element("symbol", SEPAFFormat.NAMESPACE.NANDCAT);
            symbol.setAttribute("format", "png", SEPAFFormat.NAMESPACE.NANDCAT);
            symbol.setText(SEPAFFormat.encodeImage(c.getSymbol(), "png"));
            e.addContent(symbol);
        }
        return e;
    }

    /**
     * Builds a DOM element of given circuit element.
     * 
     * Builds an component or connection or circuit dom element depending on element type.
     * 
     * If element is a circuit, the component tag is generated and the circuit will be build and added to documents root
     * element.
     * 
     * @param e
     *            Element to build DOM element of.
     * @return DOM Element representing the element.
     * @throws FormatException
     *             If generation of sub elements went wrong.
     */
    private Content buildModule(nandcat.model.element.Element e) throws FormatException {
        Content c = null;
        if (e instanceof Circuit && !(e instanceof FlipFlop)) {
            c = buildComponent((Module) e);
            if (!innerCircuitsIndex.contains(((Circuit) e).getUuid())
                    && !externalCircuits.containsKey(((Circuit) e).getUuid())) {

                root.addContent(buildCircuit((Circuit) e, false));
                innerCircuitsIndex.add(((Circuit) e).getUuid());
            }
        }

        if (e instanceof Module) {
            c = buildComponent((Module) e);
        } else if (e instanceof Connection) {
            LOG.error("Should not happen");
            throw new IllegalStateException();
        }
        return c;
    }

    /**
     * Builds a DOM element of given module.
     * 
     * @param m
     *            Module to build DOM element of.
     * @return DOM Element representing the module.
     * @throws FormatException
     *             if DOM Element can not be generated.
     */
    private Content buildComponent(Module m) throws FormatException {
        Element e = new Element("component", SEPAFFormat.NAMESPACE.SEPAF);
        setComponentAttributesLocation(e, m);
        setComponentName(e, m);
        setComponentAttributesAnnotation(e, m);

        // Set specific attributes.
        setModuleSpecificAttributes(e, m);
        setComponentPorts(e, m);

        return e;
    }

    /**
     * Sets the amount of in and out ports if necessary.
     * 
     * @param e
     *            Element to add content to.
     * @param m
     *            Module to get amount of ports from.
     */
    private void setComponentPorts(Element e, Module m) {
        if (!SEPAFFormat.hasDefaultAmountOfInPorts(m)) {
            e.setAttribute("ports_in", Integer.toString(m.getInPorts().size()), SEPAFFormat.NAMESPACE.NANDCAT);
        }
        if (!SEPAFFormat.hasDefaultAmountOfOutPorts(m)) {
            e.setAttribute("ports_out", Integer.toString(m.getOutPorts().size()), SEPAFFormat.NAMESPACE.NANDCAT);
        }
    }

    /**
     * Sets the component attributes for location.
     * 
     * @param e
     *            Element to set attribute at.
     * @param m
     *            Module to get the attribute value from.
     */
    private void setComponentAttributesLocation(Element e, Module m) {
        e.setAttribute("posx", Integer.toString(m.getRectangle().getLocation().x));
        e.setAttribute("posy", Integer.toString(m.getRectangle().getLocation().y));
    }

    /**
     * Sets the component attributes for annotation.
     * 
     * @param e
     *            Element to set attribute at.
     * @param m
     *            Module to get the attribute value from.
     */
    private void setComponentAttributesAnnotation(Element e, Module m) {
        if (m.getName() != null) {
            e.setAttribute("annotation", m.getName(), SEPAFFormat.NAMESPACE.NANDCAT);
        }
    }

    /**
     * Sets the component attributes for name.
     * 
     * @param e
     *            Element to set attribute at.
     * @param m
     *            Module to get the attribute value from.
     */
    private void setComponentName(Element e, Module m) {
        e.setAttribute("name", SEPAFFormat.getObjectAsUniqueString(m));
    }

    /**
     * Sets module specific attributes depending of module type.
     * 
     * @param e
     *            Element to add attributes to.
     * @param m
     *            Module to get attributes from.
     * @throws FormatException
     *             if attributes are not valid.
     */
    private void setModuleSpecificAttributes(Element e, Module m) throws FormatException {

        // ImpulseGenerator: Separate clock and switch.
        if (m instanceof ImpulseGenerator) {
            if (((ImpulseGenerator) m).getFrequency() == 0) {
                e.setAttribute("type", "in");
            } else {
                e.setAttribute("type", "clock");
                e.setAttribute("in_timing", Integer.toString(((ImpulseGenerator) m).getFrequency()),
                        SEPAFFormat.NAMESPACE.NANDCAT);
            }
        } else if (m instanceof AndGate) {
            e.setAttribute("type", "and");
        } else if (m instanceof OrGate) {
            e.setAttribute("type", "or");
        } else if (m instanceof IdentityGate) {
            e.setAttribute("type", "id");
        } else if (m instanceof Lamp) {
            e.setAttribute("type", "out");
        } else if (m instanceof NotGate) {
            e.setAttribute("type", "not");
        } else if (m instanceof FlipFlop) {
            e.setAttribute("type", "flipflop");
        } else if (m instanceof Circuit) {
            if (externalCircuits.containsKey(((Circuit) m).getUuid())) {
                e.setAttribute("type", "missing-circuit");
                e.setAttribute("type2", externalCircuits.get(((Circuit) m).getUuid()));
            } else {
                e.setAttribute("type", "circuit");
                e.setAttribute("type2", ((Circuit) m).getUuid());
            }
        } else {
            LOG.debug("Not a supported component type: '" + m.getClass().getName() + "'");
        }
    }

    /**
     * Builds an element of the given connection.
     * 
     * @param c
     *            Connection to build element of.
     * @param modules
     *            Available set of modules in this circuit layer.
     * @return Build element.
     */
    private Content buildConnection(Connection c, Set<Module> modules) {
        Element e = new Element("connection", SEPAFFormat.NAMESPACE.SEPAF);

        // Connection may point to element inside a circuit. Search module in this layer.
        Module sourceModule = getSourceModule(c, modules);

        if (sourceModule == null) {
            LOG.error("Connection has no source Module");
        }
        e.setAttribute("source", SEPAFFormat.getObjectAsUniqueString(sourceModule));
        e.setAttribute("sourcePort",
                SEPAFFormat.getPortAsString(true, sourceModule.getOutPorts().indexOf(c.getInPort()), sourceModule));

        // Connection may point to element inside a circuit. Search module in this layer.
        Module targetModule = getTargetModule(c, modules);

        if (targetModule == null) {
            LOG.error("Connection has no target Module");
        }
        e.setAttribute("target", SEPAFFormat.getObjectAsUniqueString(targetModule));

        e.setAttribute("targetPort",
                SEPAFFormat.getPortAsString(false, targetModule.getInPorts().indexOf(c.getOutPort()), targetModule));

        return e;
    }

    /**
     * Connections source module may lead inside another circuit. This method finds the module inside the given set of
     * modules.
     * 
     * @param c
     *            Connection to get source module for.
     * @param modules
     *            Set of modules to search in.
     * @return Source module, otherwise null.
     */
    private Module getSourceModule(Connection c, Set<Module> modules) {
        if (modules.contains(c.getPreviousModule())) {
            return c.getPreviousModule();
        }
        for (Module module : modules) {
            if (module.getOutPorts().contains(c.getInPort())) {
                return module;
            }
        }
        return null;
    }

    /**
     * Connections target module may lead inside another circuit. This method finds the module inside the given set of
     * modules.
     * 
     * @param c
     *            Connection to get target module for.
     * @param modules
     *            Set of modules to search in.
     * @return Source module, otherwise null.
     */
    private Module getTargetModule(Connection c, Set<Module> modules) {
        if (modules.contains(c.getNextModule())) {
            return c.getNextModule();
        }
        for (Module module : modules) {
            if (module.getInPorts().contains(c.getOutPort())) {
                return module;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void setCircuit(Circuit c) {
        if (c == null) {
            throw new IllegalArgumentException();
        }
        this.circuit = c;
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
    public String getErrorMessage() {
        return errorMsg;
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
    public void setExternalCircuits(Map<String, String> circuits) {
        externalCircuits = circuits;
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

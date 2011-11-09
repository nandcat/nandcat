package nandcat.model.importexport.sepaf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
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
import nandcat.model.importexport.FormatException;
import org.apache.log4j.Logger;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
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
    public boolean exportCircuit() {
        Document doc = new Document(new Element("circuits", SEPAFFormat.NAMESPACE.SEPAF));
        Element root = doc.getRootElement();
        this.root = root;
        root.addNamespaceDeclaration(SEPAFFormat.NAMESPACE.NANDCAT);
        try {
            root.setAttribute("main", circuit.getUuid());
            root.addContent(buildCircuit(circuit));
            XMLOutputter outputter = new XMLOutputter();

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
     * @return Element representing the circuit.
     * @throws FormatException
     *             If generation of sub elements went wrong.
     */
    private Element buildCircuit(Circuit c) throws FormatException {
        org.jdom.Element e = new Element("circuit", SEPAFFormat.NAMESPACE.SEPAF);
        e.setAttribute("name", c.getUuid());
        for (nandcat.model.element.Element circuitE : c.getElements()) {
            e.addContent(buildElement(circuitE));
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
    private Content buildElement(nandcat.model.element.Element e) throws FormatException {
        Content c = null;
        if (e instanceof Circuit) {
            c = buildComponent((Module) e);
            if (!innerCircuitsIndex.contains(((Circuit) e).getUuid())) {
                root.addContent(buildCircuit((Circuit) e));
                innerCircuitsIndex.add(((Circuit) e).getUuid());
            }
        }

        if (e instanceof Module) {
            c = buildComponent((Module) e);
        } else if (e instanceof Connection) {
            c = buildConnection((Connection) e);
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

        // Set incoming and outgoing ports.
        if (m.getInPorts() != null) {
            e.setAttribute("ports_in", Integer.toString(m.getInPorts().size()), SEPAFFormat.NAMESPACE.NANDCAT);
        }

        if (m.getInPorts() != null) {
            e.setAttribute("ports_out", Integer.toString(m.getInPorts().size()), SEPAFFormat.NAMESPACE.NANDCAT);
        }

        return e;
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
            String inState = "";
            if (((ImpulseGenerator) m).getState()) {
                inState = "true";
            } else {
                inState = "false";
            }
            e.setAttribute("in_state", inState, SEPAFFormat.NAMESPACE.NANDCAT);
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
            e.setAttribute("type", "circuit");
            e.setAttribute("type2", ((Circuit) m).getUuid());
        } else {
            LOG.debug("Not a supported component type: '" + m.getClass().getName() + "'");
        }
    }

    /**
     * Builds an element of the given connection.
     * 
     * @param c
     *            Connection to build element of.
     * @return Build element.
     */
    private Content buildConnection(Connection c) {
        Element e = new Element("connection", SEPAFFormat.NAMESPACE.SEPAF);
        e.setAttribute("source", SEPAFFormat.getObjectAsUniqueString(c.getPreviousModule()));
        e.setAttribute("target", SEPAFFormat.getObjectAsUniqueString(c.getNextModule()));
        e.setAttribute("sourcePort",
                SEPAFFormat.getPortAsString(true, c.getPreviousModule().getInPorts().indexOf(c.getInPort())));
        e.setAttribute("targetPort",
                SEPAFFormat.getPortAsString(false, c.getNextModule().getOutPorts().indexOf(c.getOutPort())));
        return e;
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
}

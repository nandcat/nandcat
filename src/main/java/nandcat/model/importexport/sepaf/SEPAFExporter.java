package nandcat.model.importexport.sepaf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
import nandcat.model.element.Port;
import nandcat.model.importexport.Exporter;
import nandcat.model.importexport.FormatException;
import org.apache.log4j.Logger;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
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
     * SEPAF namespace.
     */
    private static final Namespace NS_SEPAF = Namespace
            .getNamespace("http://www.sosy-lab.org/Teaching/2011-WS-SEP/xmlns/circuits-1.0");

    /**
     * Custom NANDCat namespace.
     */
    private static final Namespace NS_NANDCAT = Namespace.getNamespace("nandcat",
            "http://www.nandcat.de/xmlns/sepaf-extension");

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
        Document doc = new Document(new Element("circuits", NS_SEPAF));
        Element root = doc.getRootElement();
        root.addNamespaceDeclaration(NS_NANDCAT);
        try {
            root.setAttribute("main", getIdentifier(circuit));
            root.addContent(buildCircuit(circuit));
            XMLOutputter outputter = new XMLOutputter();

            outputter.output(doc, new FileOutputStream(file));
            return true;
        } catch (FileNotFoundException e) {
            setErrorMessage("File not found");
        } catch (IOException e) {
            setErrorMessage("Can not read file");
        } catch (FormatException e) {
            setErrorMessage("Verarbeitung nicht möglich: " + e.getMessage());
        }

        return false;
    }

    private Element buildCircuit(Circuit c) throws FormatException {
        org.jdom.Element e = new Element("circuit", NS_SEPAF);
        e.setAttribute("name", getIdentifier(c));
        for (nandcat.model.element.Element circuitE : c.getElements()) {
            e.addContent(buildElement(circuitE));
        }
        return e;
    }

    private Content buildElement(nandcat.model.element.Element circuitE) throws FormatException {
        Content c = null;
        if (circuitE instanceof Circuit) {
            LOG.fatal("Circuit export not implemented");
        }

        if (circuitE instanceof Module) {
            c = buildComponent((Module) circuitE);
        } else if (circuitE instanceof Connection) {
            c = buildConnection((Connection) circuitE);
        }
        return c;
    }

    private Content buildComponent(Module circuitM) throws FormatException {
        Element e = new Element("component", NS_SEPAF);
        e.setAttribute("posx", Integer.toString(circuitM.getLocation().x));
        e.setAttribute("posy", Integer.toString(circuitM.getLocation().y));
        e.setAttribute("name", getIdentifier(circuitM));
        if (circuitM.getName() != null) {
            e.setAttribute("annotation", circuitM.getName(), NS_NANDCAT);
        }
        setModuleSpecificAttributes(e, circuitM);
        if (circuitM.getInPorts() != null) {
            e.setAttribute("ports_in", Integer.toString(circuitM.getInPorts().size()), NS_NANDCAT);
        }

        if (circuitM.getInPorts() != null) {
            e.setAttribute("ports_out", Integer.toString(circuitM.getInPorts().size()), NS_NANDCAT);
        }

        return e;
    }

    private void setModuleSpecificAttributes(Element e, Module m) throws FormatException {
        if (m instanceof ImpulseGenerator) {
            if (((ImpulseGenerator) m).getFrequency() == 0) {
                e.setAttribute("type", "in");
            } else {
                e.setAttribute("type", "clock");
                e.setAttribute("in_timing", Integer.toString(((ImpulseGenerator) m).getFrequency()), NS_NANDCAT);
            }
            e.setAttribute("in_state", ((((ImpulseGenerator) m).getState()) ? "true" : "false"), NS_NANDCAT);
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
        } else {
            throw new FormatException("Not a supported component type: '" + m.getClass().getName() + "'");
        }

    }

    private Content buildConnection(Connection circuitC) {
        Element e = new Element("connection", NS_SEPAF);
        e.setAttribute("source", getIdentifier(circuitC.getPreviousModule()));
        e.setAttribute("target", getIdentifier(circuitC.getNextModule()));
        e.setAttribute("sourcePort", getPortIdentifier(true, circuitC.getPreviousModule(), circuitC.getInPort()));
        e.setAttribute("targetPort", getPortIdentifier(false, circuitC.getNextModule(), circuitC.getOutPort()));
        return e;
    }

    private String getPortIdentifier(boolean isOutPort, Module m, Port p) {
        if (isOutPort) {

            char t = (char) (((int) m.getOutPorts().indexOf(p)) + ((int) 'o'));
            LOG.trace("Port is outPort and number " + m.getOutPorts().indexOf(p) + " with identifier: " + t);
            return new String(new char[] { t });
        } else {
            char t = (char) (((int) m.getInPorts().indexOf(p)) + ((int) 'a'));
            LOG.trace("Port is inPort and number " + m.getInPorts().indexOf(p) + " with identifier: " + t);
            return new String(new char[] { t });
        }
    }

    private String getIdentifier(Object o) {
        return Integer.toHexString(System.identityHashCode(o));
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

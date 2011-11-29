package nandcat.model.importexport.sepaf;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
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
import org.apache.log4j.Logger;
import org.apache.xerces.impl.dv.util.Base64;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.xpath.XPath;

/**
 * SEPAF Format. Holds all information about the SEPAF format to share information between importer and exporter.
 */
public final class SEPAFFormat {

    /**
     * Representing schema files.
     */
    public static class VALIDATIONSCHEMA {

        /**
         * Path to SEPAF schema used for validation.
         */
        public static final String SCHEMA_SEPAF = "circuits-1.0.xsd";

        /**
         * Path to NANDcat schema used for validation.
         */
        public static final String SCHEMA_NANDCAT = "sepaf-extension.xsd";
    }

    /**
     * Representing namespace of sepaf format.
     */
    public static class NAMESPACE {

        /**
         * Get default namespace.
         * 
         * @return default namespace.
         */
        public static final org.jdom.Namespace getDefault() {
            return org.jdom.Namespace.getNamespace("http://www.sosy-lab.org/Teaching/2011-WS-SEP/xmlns/circuits-1.0");
        }

        /**
         * XSI namespace.
         */
        public static final org.jdom.Namespace XSI = org.jdom.Namespace.getNamespace("xsi",
                "http://www.w3.org/2001/XMLSchema-instance");

        /**
         * Circuit namespace (c).
         */
        public static final org.jdom.Namespace DEFAULT = org.jdom.Namespace.getNamespace("c",
                "http://www.sosy-lab.org/Teaching/2011-WS-SEP/xmlns/circuits-1.0");

        /**
         * SEPAF namespace.
         */
        public static final org.jdom.Namespace SEPAF = org.jdom.Namespace
                .getNamespace("http://www.sosy-lab.org/Teaching/2011-WS-SEP/xmlns/circuits-1.0");

        /**
         * Custom NANDCat namespace.
         */
        public static final org.jdom.Namespace NANDCAT = org.jdom.Namespace.getNamespace("nandcat",
                "http://www.nandcat.de/xmlns/sepaf-extension");

        /**
         * Namespaces used in the format.
         */
        public static final org.jdom.Namespace[] ALL = new org.jdom.Namespace[] { DEFAULT, SEPAF, NANDCAT };

        /**
         * Schema location string of used namespaces.
         */
        public static final String SCHEMA_LOCATION = "http://www.sosy-lab.org/Teaching/2011-WS-SEP/xmlns/circuits-1.0"
                + " " + "http://www.sosy-lab.org/Teaching/2011-WS-SEP/xmlns/circuits-1.0.xsd" + " "
                + "http://www.nandcat.de/xmlns/sepaf-extension" + " "
                + "http://www.nandcat.de/xmlns/sepaf-extension.xsd";
    }

    /**
     * Gate defaults.
     */
    public static class GATEDEFAULTS {

        /**
         * Default amount of incoming ports: and.
         */
        public static final int DEFAULT_INPORTS_AND = 2;

        /**
         * Default amount of outgoing ports: and.
         */
        public static final int DEFAULT_OUTPORTS_AND = 1;

        /**
         * Default amount of incoming ports: or.
         */
        public static final int DEFAULT_INPORTS_OR = 2;

        /**
         * Default amount of outgoing ports: or.
         */
        public static final int DEFAULT_OUTPORTS_OR = 1;

        /**
         * Default amount of incoming ports: not.
         */
        public static final int DEFAULT_INPORTS_NOT = 1;

        /**
         * Default amount of outgoing ports: not.
         */
        public static final int DEFAULT_OUTPORTS_NOT = 1;

        /**
         * Default amount of incoming ports: lamp.
         */
        public static final int DEFAULT_INPORTS_LAMP = 1;

        /**
         * Default amount of outgoing ports: lamp.
         */
        public static final int DEFAULT_OUTPORTS_LAMP = 0;

        /**
         * Default amount of incoming ports: identity.
         */
        public static final int DEFAULT_INPORTS_IDENTITY = 1;

        /**
         * Default amount of outgoing ports: identity.
         */
        public static final int DEFAULT_OUTPORTS_IDENTITY = 2;

        /**
         * Default amount of incoming ports: clock.
         */
        public static final int DEFAULT_INPORTS_CLOCK = 0;

        /**
         * Default amount of outgoing ports: clock.
         */
        public static final int DEFAULT_OUTPORTS_CLOCK = 1;

        /**
         * Default amount of incoming ports: switch.
         */
        public static final int DEFAULT_INPORTS_SWITCH = 0;

        /**
         * Default amount of outgoing ports: switch.
         */
        public static final int DEFAULT_OUTPORTS_SWITCH = 1;

        /**
         * Default amount of incoming ports: flipflop.
         */
        public static final int DEFAULT_INPORTS_FLIPFLOP = 2;

        /**
         * Default amount of outgoing ports: flipflop.
         */
        public static final int DEFAULT_OUTPORTS_FLOPFLOP = 2;
    }

    /**
     * Class logger instance.
     */
    private static final Logger LOG = Logger.getLogger(SEPAFFormat.class);

    /**
     * Private constructor.
     */
    private SEPAFFormat() {
        throw new IllegalArgumentException();
    }

    /**
     * Gets the string representation of a port.
     * 
     * @param isOutPort
     *            True iff port is an outgoing port of the module.
     * @param index
     *            Integer index (0..) as the number of the port in his section (out, in)
     * @param m
     *            Module to create Port for.
     * @return Port as string representation.
     */
    public static String getPortAsString(boolean isOutPort, int index, Module m) {
        if (index < 0) {
            throw new IllegalArgumentException("Port index < 0");
        }
        if (m instanceof FlipFlop) {
            if (index > 1) {
                throw new IllegalArgumentException("Port index > 1 for flipflop not allowed");
            }
            if (isOutPort) {
                // q and nq
                if (index == 0) {
                    return "q";
                } else {
                    return "nq";
                }
            } else {
                // R and S as inports
                return String.valueOf((char) (index + ((int) 'r')));
            }
        } else {
            if (isOutPort) {
                return String.valueOf((char) (index + ((int) 'o')));
            } else {
                return String.valueOf((char) (index + ((int) 'a')));
            }
        }
    }

    /**
     * Gets the target port of a connection (inPort of a module) depending on the port name (string).
     * 
     * @param isOutPort
     *            True iff port is an outgoing port of the module.
     * @param module
     *            Module to get port of.
     * @param portname
     *            Name of the port, a character identifying the number of the port.
     * @return Selected port.
     * @throws FormatException
     *             If portname is wrong.
     */
    public static Port getStringAsPort(boolean isOutPort, Module module, String portname) throws FormatException {
        int portnr = -1;

        if (isOutPort) {
            if (module instanceof FlipFlop) {
                if (portname.equals("q")) {
                    portnr = 0;
                } else if (portname.equals("nq")) {
                    portnr = 1;
                } else {
                    throw new FormatException("FlipFlop only has q and nq input ports");
                }
            } else {
                portnr = ((int) portname.charAt(0)) - ((int) 'o');
            }
            if (portnr < 0) {
                throw new FormatException("Connection: wrong targetport, requested: " + portnr);
            }
            if (portnr < module.getOutPorts().size()) {
                return module.getOutPorts().get(portnr);
            } else {
                throw new FormatException("Connection: wrong sourceport, module has only "
                        + module.getOutPorts().size() + " inports, requested: " + portnr);
            }
        } else {

            if (module instanceof FlipFlop) {
                portnr = ((int) portname.charAt(0)) - ((int) 'r');
            } else {
                portnr = ((int) portname.charAt(0)) - ((int) 'a');
            }
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
    public static XPath getXPathInstance(String path) throws JDOMException {
        XPath xpath = XPath.newInstance(path);
        for (Namespace ns : SEPAFFormat.NAMESPACE.ALL) {
            xpath.addNamespace(ns);
        }
        return xpath;
    }

    /**
     * Gets the unique string representation of an object. The String is unique for the given object inside the current
     * VM.
     * 
     * @param o
     *            Object to build string of.
     * @return Unique string representation of object.
     */
    public static String getObjectAsUniqueString(Object o) {
        return Integer.toHexString(System.identityHashCode(o));
    }

    /**
     * Encodes an image to a base64 encoded string.
     * 
     * @param im
     *            Image to encode.
     * @param format
     *            ImageFormat to export image as.
     * @return String as a result of the encoding.
     */
    public static String encodeImage(BufferedImage im, String format) {
        if (im == null) {
            throw new IllegalArgumentException();
        }
        if (format == null) {
            throw new IllegalArgumentException();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            if (!ImageIO.write(im, format, baos)) {
                return null;
            }
            baos.flush();
        } catch (IOException e) {
            LOG.warn("Image could not be encoded" + e.getMessage());
            LOG.warn(e.getStackTrace());
            return null;
        }
        byte[] imageInByte = baos.toByteArray();
        return Base64.encode(imageInByte);
    }

    /**
     * Decodes a base64 encoded string to an image.
     * 
     * @param base64string
     *            String to decode.
     * @return BufferedImage as a result of the decoding.
     */
    public static BufferedImage decodeImage(String base64string) {
        if (base64string == null) {
            throw new IllegalArgumentException();
        }
        byte[] decoded = Base64.decode(base64string);
        try {
            return ImageIO.read(new ByteArrayInputStream(decoded));
        } catch (IOException e) {
            LOG.warn("Image could not be encoded" + e.getMessage());
            LOG.warn(e.getStackTrace());
            return null;
        }
    }

    /**
     * Determines if the amount of in ports is the default and has not to be specified.
     * 
     * @param m
     *            Module to check incoming ports of.
     * @return True iff amount of incoming ports is a valid default value.
     */
    public static boolean hasDefaultAmountOfInPorts(Module m) {
        int amountOfPorts = m.getInPorts().size();
        Integer defaultAmountOfPorts = null;

        if (m instanceof AndGate) {
            defaultAmountOfPorts = SEPAFFormat.GATEDEFAULTS.DEFAULT_INPORTS_AND;
        } else if (m instanceof OrGate) {
            defaultAmountOfPorts = SEPAFFormat.GATEDEFAULTS.DEFAULT_INPORTS_OR;
        } else if (m instanceof NotGate) {
            defaultAmountOfPorts = SEPAFFormat.GATEDEFAULTS.DEFAULT_INPORTS_NOT;
        } else if (m instanceof IdentityGate) {
            defaultAmountOfPorts = SEPAFFormat.GATEDEFAULTS.DEFAULT_INPORTS_IDENTITY;
        } else if (m instanceof ImpulseGenerator) {

            // type in
            if (((ImpulseGenerator) m).getFrequency() == 0) {
                defaultAmountOfPorts = SEPAFFormat.GATEDEFAULTS.DEFAULT_INPORTS_SWITCH;
            } else { // type clock
                defaultAmountOfPorts = SEPAFFormat.GATEDEFAULTS.DEFAULT_INPORTS_CLOCK;
            }
        } else if (m instanceof Lamp) {
            defaultAmountOfPorts = SEPAFFormat.GATEDEFAULTS.DEFAULT_INPORTS_LAMP;
        } else if (m instanceof FlipFlop) {
            defaultAmountOfPorts = SEPAFFormat.GATEDEFAULTS.DEFAULT_INPORTS_FLIPFLOP;
        } else if (m instanceof Circuit) {
            return true;
        }
        // No default values for type circuit

        if (defaultAmountOfPorts != null && amountOfPorts == defaultAmountOfPorts) {
            return true;
        }
        return false;
    }

    /**
     * Determines if the amount of out ports is the default and has not to be specified.
     * 
     * @param m
     *            Module to check outgoing ports of.
     * @return True iff amount of outgoing ports is a valid default value.
     */
    public static boolean hasDefaultAmountOfOutPorts(Module m) {
        int amountOfPorts = m.getOutPorts().size();
        Integer defaultAmountOfPorts = null;

        if (m instanceof AndGate) {
            defaultAmountOfPorts = SEPAFFormat.GATEDEFAULTS.DEFAULT_OUTPORTS_AND;
        } else if (m instanceof OrGate) {
            defaultAmountOfPorts = SEPAFFormat.GATEDEFAULTS.DEFAULT_OUTPORTS_OR;
        } else if (m instanceof NotGate) {
            defaultAmountOfPorts = SEPAFFormat.GATEDEFAULTS.DEFAULT_OUTPORTS_NOT;
        } else if (m instanceof IdentityGate) {
            defaultAmountOfPorts = SEPAFFormat.GATEDEFAULTS.DEFAULT_OUTPORTS_IDENTITY;
        } else if (m instanceof ImpulseGenerator) {

            // type in
            if (((ImpulseGenerator) m).getFrequency() == 0) {
                defaultAmountOfPorts = SEPAFFormat.GATEDEFAULTS.DEFAULT_OUTPORTS_SWITCH;
            } else { // type clock
                defaultAmountOfPorts = SEPAFFormat.GATEDEFAULTS.DEFAULT_OUTPORTS_CLOCK;
            }
        } else if (m instanceof Lamp) {
            defaultAmountOfPorts = SEPAFFormat.GATEDEFAULTS.DEFAULT_OUTPORTS_LAMP;
        } else if (m instanceof FlipFlop) {
            defaultAmountOfPorts = SEPAFFormat.GATEDEFAULTS.DEFAULT_OUTPORTS_FLOPFLOP;
        } else if (m instanceof Circuit) {
            return true;
        }
        // No default values for type circuit

        if (defaultAmountOfPorts != null && amountOfPorts == defaultAmountOfPorts) {
            return true;
        }
        return false;
    }
}

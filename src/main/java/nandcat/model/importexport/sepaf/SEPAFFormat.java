package nandcat.model.importexport.sepaf;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.log4j.Logger;
import org.apache.xerces.impl.dv.util.Base64;

public class SEPAFFormat {

    /**
     * Representing namespace of sepaf format.
     */
    public static class NAMESPACE {

        public static final org.jdom.Namespace getDefault() {
            return org.jdom.Namespace.getNamespace("http://www.sosy-lab.org/Teaching/2011-WS-SEP/xmlns/circuits-1.0");
        }

        /**
         * Default namespace (xmlns) without prefix.
         */
        public static final org.jdom.Namespace XSI = org.jdom.Namespace.getNamespace("xsi",
                "http://www.w3.org/2001/XMLSchema-instance");

        /**
         * Default namespace (xmlns) without prefix.
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
     * Class logger instance.
     */
    private static final Logger LOG = Logger.getLogger(SEPAFFormat.class);

    /**
     * Gets the string representation of a port.
     * 
     * @param isOutPort
     *            True iff port is an outgoing port of the module.
     * @param index
     *            Integer index (0..) as the number of the port in his section (out, in)
     * @return
     */
    public static String getPortAsString(boolean isOutPort, int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Port index < 0");
        }
        char t;
        if (isOutPort) {
            t = (char) (index + ((int) 'o'));
        } else {
            t = (char) (index + ((int) 'a'));
        }
        return String.valueOf(t);
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
}

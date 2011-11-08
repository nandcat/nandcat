package nandcat.model.importexport.sepaf;

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
    }

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
}

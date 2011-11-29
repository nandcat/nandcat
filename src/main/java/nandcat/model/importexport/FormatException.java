package nandcat.model.importexport;

/**
 * Format exception. Thrown if an error occurred while importing or exporting.
 */
public class FormatException extends Exception {

    /**
     * Default serial version uid.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public FormatException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message.
     * 
     * @param msg
     *            Message.
     */
    public FormatException(String msg) {
        super(msg);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * 
     * @param msg
     *            Message.
     * @param cause
     *            Cause.
     */
    public FormatException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs a new exception with the specified cause and a detail message of (cause==null ? null :
     * cause.toString()) (which typically contains the class and detail message of cause)
     * 
     * @param cause
     *            Cause.
     */
    public FormatException(Throwable cause) {
        super(cause);
    }
}

package nandcat.model.importexport;

/**
 * Recursion exception. Thrown if an given recursion depth of circuits is reached. This exception it thrown all levels
 * of imports to the top where it should be caught with an error message. All lower levels like used at
 * externalCircuitSource must not be caught, otherwise no recursion can be detected.
 */
public class RecursionException extends Exception {

    /**
     * Default serial version uid.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public RecursionException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message.
     * 
     * @param msg
     *            Message.
     */
    public RecursionException(String msg) {
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
    public RecursionException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs a new exception with the specified cause and a detail message of (cause==null ? null :
     * cause.toString()) (which typically contains the class and detail message of cause)
     * 
     * @param cause
     *            Cause.
     */
    public RecursionException(Throwable cause) {
        super(cause);
    }
}

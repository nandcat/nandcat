package nandcat.model.importexport;

/**
 * FormatErrorHandler. Used to handle errors occurring while importing or exporting.
 */
public interface FormatErrorHandler {

    /**
     * Handles a warning.
     * 
     * @param exception
     *            Exception as a cause of this warning.
     * @throws FormatException
     *             Throw Exception to stop current process.
     */
    void warning(FormatException exception) throws FormatException;

    /**
     * Handles a error.
     * 
     * @param exception
     *            Exception as a cause of this error.
     * @throws FormatException
     *             Throw Exception to stop current process.
     */
    void error(FormatException exception) throws FormatException;

    /**
     * Handles a fatal error. Fatal errors should stop current process. Results are not predicatable.
     * 
     * @param exception
     *            Exception as a cause of this fatal error.
     * @throws FormatException
     *             Throw Exception to stop current process. Fatal errors have to throw the given Exception.
     */
    void fatal(FormatException exception) throws FormatException;
}

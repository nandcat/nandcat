package nandcat.model.importexport;

public interface FormatErrorHandler {

    void warning(FormatException exception) throws FormatException;

    void error(FormatException exception) throws FormatException;

    void fatal(FormatException exception) throws FormatException;
}

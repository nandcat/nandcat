package nandcat.model.importexport;

import java.io.File;
import java.util.Map;
import nandcat.model.element.Circuit;
import nandcat.model.element.factory.ModuleBuilderFactory;

/**
 * Importer.
 * 
 * Used for loading a persistent circuit using a specialized format.
 */
public interface Importer {

    /**
     * Sets the import file handler.
     * 
     * @param file
     *            File handler of file to import.
     */
    void setFile(File file);

    /**
     * Sets an instance of ExternalCircuitSource which is used to find a missing circuit while importing.
     * 
     * @param source
     *            Source of missing circuits.
     */
    void setExternalCircuitSource(ExternalCircuitSource source);

    /**
     * Gets the imported circuit.
     * 
     * @return Imported circuit.
     */
    Circuit getCircuit();

    /**
     * Imports circuit from file.
     * 
     * @return True iff circuit was correctly imported.
     */
    boolean importCircuit();

    /**
     * Gets the supported file extensions mapped with a description of the format.
     * 
     * @return Map of supported file extensions mapped with a description of the format.
     */
    Map<String, String> getFileFormats();

    /**
     * Gets the error message if import failed.
     * 
     * @return String Error message if import failed, null if no error occurred.
     */
    String getErrorMessage();

    /**
     * Sets the factory used to create modules.
     * 
     * @param factory
     *            Factory to build modules.
     */
    void setFactory(ModuleBuilderFactory factory);

    /**
     * Sets the handler called if warnings, errors, fatal errors occur.
     * 
     * @param h
     *            ErrorHandler
     */
    void setErrorHandler(FormatErrorHandler h);
}

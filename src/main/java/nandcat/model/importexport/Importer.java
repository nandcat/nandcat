package nandcat.model.importexport;

import java.io.File;
import java.util.Map;
import nandcat.model.element.Circuit;

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
}

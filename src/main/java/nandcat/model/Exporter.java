package nandcat.model;

import java.io.File;
import nandcat.model.element.Circuit;

/**
 * Exporter.
 * 
 * Used for storing a circuit persistent using a specialized format.
 * 
 * @version 0.1
 * 
 */
public interface Exporter {

    /**
     * Sets the output file handler.
     * 
     * @param file
     *            File handler of file to export.
     */
    void setFile(File file);

    /**
     * Exports circuit from file.
     * 
     * @return True iff circuit was correctly exported.
     */
    boolean exportCircuit();

    /**
     * Sets the circuit to export.
     * 
     * @param c
     *            Circuit to export.
     */
    void setCircuit(Circuit c);

    /**
     * Gets the supported file extensions.
     * 
     * @return Array of supported file extensions.
     */
    String[] getFileExtension();

    /**
     * Gets the description of the supported file extensions.
     * 
     * @return String description of the supported file extensions.
     */
    String getFileDescription();

    /**
     * Gets the error message if export failed.
     * @return String Error message if export failed.
     */
    String getErrorMessage();
}

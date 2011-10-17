package de.unipassau.sep.nandcat.model;

import java.io.File;
import de.unipassau.sep.nandcat.model.element.Circuit;

/**
 * Importer.
 * 
 * Used for loading a persistent circuit using a specialized format.
 * 
 * @version 0.1
 * 
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
}

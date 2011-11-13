package nandcat.model.importexport;

import java.io.File;
import java.util.Map;
import nandcat.model.element.Circuit;

/**
 * Exporter.
 * 
 * Used for storing a circuit persistent using a specialized format.
 */
public interface Exporter {

    /**
     * Sets the output file handler.
     * 
     * The file points to the file to store the data in.
     * 
     * @param file
     *            File handler of file to export.
     */
    void setFile(File file);

    /**
     * Sets a list of available external circuits, which can be used while exporting a file. Should be a set of circuits
     * mapped with the circuits unique identifier. The Circuits will not be exported inside the file.
     * 
     * @param circuits
     *            Circuits uuid mapped with the circuit object. Circuit object is the prototype for further instances of
     *            this circuit.
     */
    void setExternalCircuits(Map<String, Circuit> circuits);

    /**
     * Exports circuit from file.
     * 
     * @return True iff circuit was correctly exported.
     */
    boolean exportCircuit();

    /**
     * Sets the circuit to export.
     * 
     * The given circuit will be stored persistently.
     * 
     * @param c
     *            Circuit to export.
     */
    void setCircuit(Circuit c);

    /**
     * Gets the supported file extensions mapped with a description of the format.
     * 
     * @return Map of supported file extensions mapped with a description of the format.
     */
    Map<String, String> getFileFormats();

    /**
     * Gets the error message if export failed.
     * 
     * @return String Error message if export failed.
     */
    String getErrorMessage();
}

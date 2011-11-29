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
     * Sets a map of external circuits. The <b>key</b> represents the internal uuid of the circuit. The <b>value</b>
     * represents the reference to use when exporting. This is needed to know which circuits should not be exported into
     * the file. E.g. if there is a circuit with uuid="513" which is referenced inside the current circuit and this
     * circuit should not be exported, but the file which holds the external circuit is named "andgate" there should be
     * a Map entry Enry<"513","andgate">.
     * 
     * @param circuits
     *            Circuits uuid mapped with the circuit object. Circuit object is the prototype for further instances of
     *            this circuit.
     */
    void setExternalCircuits(Map<String, String> circuits);

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
     * Sets the handler called if warnings, errors, fatal errors occur.
     * 
     * @param h
     *            ErrorHandler
     */
    void setErrorHandler(FormatErrorHandler h);
}

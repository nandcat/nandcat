package nandcat.model.importexport;

import nandcat.model.element.Circuit;

/**
 * Represents the source of external circuits. The importer gets missing circuits using an instance of this class.
 */
public interface ExternalCircuitSource {

    /**
     * Gets an external circuit referenced by an identifier.
     * 
     * @param identifier
     *            Identifier used to reference the needed circuit.
     * @param recursiveDepth
     *            Recursive Depth to start with. This number should increase if recursively called.
     * @return The external circuit, otherwise null.
     * @throws RecursionException
     *             Used to detect a violation of the allowed recursion depth.
     */
    Circuit getExternalCircuit(String identifier, int recursiveDepth) throws RecursionException;
}

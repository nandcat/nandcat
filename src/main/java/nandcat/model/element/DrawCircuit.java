package nandcat.model.element;

import java.awt.Rectangle;
import java.util.List;

/**
 * This class represents a circuit. It could be a customized Module or the main circuit displayed in the GUI. A circuit
 * representing a custom Module will not be displayed in its full extent. Note that this leads to a recursive
 * datastructure, where a circuit could contain more circuits. TODO comment
 * 
 * @version 7
 */
public interface DrawCircuit extends DrawModule {

    /**
     * {@inheritDoc}
     */
    String getName();

    /**
     * Getter for Elements.
     * 
     * @return The Elements
     */
    List<Element> getElements();

    /**
     * {@inheritDoc}
     */
    List<Port> getInPorts();

    /**
     * {@inheritDoc}
     */
    List<Port> getOutPorts();

    /**
     * {@inheritDoc}
     */
    Rectangle getRectangle();

    /**
     * Returns base64-encoded PNG symbol representation of the circuit. Conversion to a BufferedImage via<br />
     * <b>BufferedImage image = ImageIO.read(new ByteArrayInputStream(circuit.getSymbol()));</b>
     * 
     * @return byte[] representation of PNG symbol
     */
    byte[] getSymbol();

    /**
     * {@inheritDoc}
     */
    boolean isSelected();

    // NOTE nice to have: durchlauf sortiert nach Y-Koordinate, sodass die in/outPorts entsprechend
    // als Ein/Ausgaenge eines (Circuit)Bausteins auftauchen
    /**
     * Returns the "first" Modules in this Circuit.
     * 
     * @return List<Module> containing the starting Modules of this Circuit.
     */
    List<Module> getStartingModules();
}

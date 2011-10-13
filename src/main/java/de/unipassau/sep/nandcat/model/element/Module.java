package de.unipassau.sep.nandcat.model.element;

import java.util.Set;
import de.unipassau.sep.nandcat.model.ClockListener;

/**
 * Module interface.
 * 
 * @version 0.1
 * 
 */
public interface Module extends ClockListener, Element {

    // TODO diskussion: was ist mit set{In,Out}Ports hier?
    // nicht alle Elemente haben EingangsPorts
    // nicht alle Elemente haben AusgangsPorts
    /**
     * Gets incoming ports.
     * 
     * @return incoming ports.
     */
    Set<Port> getInPorts();

    /**
     * Gets outgoing ports.
     * 
     * @return outgoing ports.
     */
    Set<Port> getOutPorts();
}

package de.unipassau.sep.nandcat.model.element;

import java.util.Set;

/**
 * Gate.
 * 
 * @version 0.1
 * 
 */
public abstract class Gate implements Module {
    /**
     * Return Elements connected to outgoing port(s).
     * 
     * @return Set containing the Next Elements
     */
    Set<Module> getNextElements() {
        return null;
    }
}

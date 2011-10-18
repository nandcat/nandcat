package de.unipassau.sep.nandcat.model;

/**
 * Modellistener.
 * 
 * @version 0.1
 * 
 */
public interface ModelListener {

    /**
     * Invoked when elements changes.
     * @param e ModelEvent which holds changed elements and further information.
     */
    void elementsChanged(ModelEvent e);
}

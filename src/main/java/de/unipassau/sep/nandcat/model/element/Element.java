package de.unipassau.sep.nandcat.model.element;

import de.unipassau.sep.nandcat.model.ClockListener;

/**
 * Element.
 * 
 * @version 0.1
 * 
 */
public interface Element extends ClockListener {

    void setName(String name);

    String getName();
}

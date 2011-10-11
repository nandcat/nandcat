package de.unipassau.sep.nandcat.view;

import de.unipassau.sep.nandcat.model.element.Connection;
import de.unipassau.sep.nandcat.model.element.Gate;

/**
 * ElementDrawer.
 * 
 * @version 0.1
 * 
 */
public interface ElementDrawer {
    // TODO holds an instance of workspace. Constructor or setter?
    // TODO Add a lot of draw methods.

    public void draw(Connection connection);

    public void draw(Gate gate);

}

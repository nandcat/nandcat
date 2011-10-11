package de.unipassau.sep.nandcat.controller;

/**
 * Tool.
 * 
 * @version 0.1
 * 
 */
public interface Tool {

    // TODO Implements workspace listener anonym.
    // TODO Add methods: getListener
    // TODO Create ListenerInterface for getListener
    // TODO Tool needs an instance of Controller for requestActivation.
    /**
     * Activates or deactivates tool.
     * 
     * @param active
     *            true to activate tool.
     */
    public void setActive(boolean active);
}

package de.unipassau.sep.nandcat.view;

import java.awt.Graphics;
import de.unipassau.sep.nandcat.model.element.AndGate;
import de.unipassau.sep.nandcat.model.element.Circuit;
import de.unipassau.sep.nandcat.model.element.Connection;
import de.unipassau.sep.nandcat.model.element.Gate;
import de.unipassau.sep.nandcat.model.element.IdentityGate;
import de.unipassau.sep.nandcat.model.element.Lamp;
import de.unipassau.sep.nandcat.model.element.NotGate;
import de.unipassau.sep.nandcat.model.element.OrGate;
import de.unipassau.sep.nandcat.model.element.Port;

/**
 * ElementDrawer.
 * 
 * Draws elements on a given graphics object to support different drawing styles.
 * 
 * @version 0.1
 * 
 */
public interface ElementDrawer {

    /**
     * Sets graphics object to draw on.
     * 
     * @param g
     *            Graphics object to draw on.
     */
    void setGraphics(Graphics g);

    /**
     * Draws a Connection.
     * 
     * @param connection
     *            Connection to draw.
     */
    void draw(Connection connection);

    /**
     * Draws a Gate.
     * 
     * @param gate
     *            Gate to draw.
     */
    void draw(Gate gate);

    /**
     * Draws a Circuit.
     * 
     * @param circuit
     *            Circuit to draw.
     */
    void draw(Circuit circuit);

    /**
     * Draws a Port.
     * 
     * @param port
     *            Port to draw.
     */
    void draw(Port port);

    /**
     * Draws an IdentityGate.
     * 
     * @param gate
     *            IdentityGate to draw.
     */
    void draw(IdentityGate gate);

    /**
     * Draws a NotGate.
     * 
     * @param gate
     *            NotGate to draw.
     */
    void draw(NotGate gate);

    /**
     * Draws an AndGate.
     * 
     * @param gate
     *            AndGate to draw.
     */
    void draw(AndGate gate);

    /**
     * Draws an OrGate.
     * 
     * @param gate
     *            OrGate to draw.
     */
    void draw(OrGate gate);

    /**
     * Draws a Lamp.
     * 
     * @param lamp
     *            Lamp to draw.
     */
    void draw(Lamp lamp);
}

package nandcat.view;

import java.awt.Graphics;
import nandcat.model.element.AndGate;
import nandcat.model.element.Circuit;
import nandcat.model.element.Connection;
import nandcat.model.element.FlipFlop;
import nandcat.model.element.IdentityGate;
import nandcat.model.element.ImpulseGenerator;
import nandcat.model.element.Lamp;
import nandcat.model.element.NotGate;
import nandcat.model.element.OrGate;

/**
 * ElementDrawer.
 * 
 * Draws elements on a given graphics object to support different drawing styles.
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
     * Draws a Circuit.
     * 
     * @param circuit
     *            Circuit to draw.
     */
    void draw(Circuit circuit);

    /**
     * Draws a flipflop.
     * 
     * @param flipflop
     *            FlipFlop element to draw.
     */
    void draw(FlipFlop flipflop);

    /**
     * Draws an ImpulseGenerator.
     * 
     * @param generator
     *            Impulsegenerator to draw.
     */
    void draw(ImpulseGenerator generator);

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

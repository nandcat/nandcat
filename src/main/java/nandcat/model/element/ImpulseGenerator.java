package nandcat.model.element;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import nandcat.model.Clock;

/**
 * Impulse Generator. Cycles between outgoing signals. An Impulse Generator with frequency 0 is basically a Switch.
 */
public class ImpulseGenerator implements Module {

    /**
     * Point specifying the Location of the Gate.
     */
    private Point location;

    /**
     * Impulsegenerator's name.
     */
    private String name;

    /**
     * Impulsegenerator's frequency.
     */
    private int frequency;

    /**
     * Outgoing port used for signal propagation.
     */
    private Port outPort;

    /**
     * Impulsegenerator's state.
     */
    private boolean state;

    /**
     * Rectangle specifying the ImpulseGenerator's shape.
     */
    private Rectangle rectangle;

    /**
     * Constructor with frequency.
     * 
     * @param frequency
     *            frequency of the generator
     */
    public ImpulseGenerator(int frequency) {
        if (frequency < 0) {
            new IllegalArgumentException("invalid frequency for impulsegenerator");
        }
        this.frequency = frequency;
        outPort = new Port(this);
    }

    /**
     * Set impulsegenerator's name.
     * 
     * @param name
     *            String to set impulsegenerator's name to
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return impulsegenerator's name.
     * 
     * @return String representing impulsegenerator's name
     */
    public String getName() {
        return name;
    }

    /**
     * Get state of the impulsegenerator.
     * 
     * @return state of the impulsegenerator
     */
    public boolean getState() {
        return state;
    }

    /**
     * {@inheritDoc}
     * 
     * Always empty.
     */
    public List<Port> getInPorts() {
        return new LinkedList<Port>();
    }

    /**
     * {@inheritDoc}
     */
    public List<Port> getOutPorts() {
        LinkedList<Port> outPorts = new LinkedList<Port>();
        outPorts.add(outPort);
        return outPorts;
    }

    /**
     * {@inheritDoc}
     */
    public void clockTicked(Clock clock) {
        if (outPort == null) {
            return;
        }
        outPort.setState(state, clock);
        toggleState();
    }

    /**
     * {@inheritDoc}
     */
    public void setLocation(Point p) {
        location = p;
    }

    /**
     * {@inheritDoc}
     */
    public Point getLocation() {
        return location;
    }

    /**
     * Return the frequency of this Imp.Gen.
     * 
     * @return the frequency
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * Toggle state.
     */
    public void toggleState() {
        state = !state;
    }

    /**
     * {@inheritDoc}
     */
    public Rectangle getRectangle() {
        return rectangle;
    }

    /**
     * {@inheritDoc}
     */
    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    /**
     * {@inheritDoc}
     */
    public void setSelected(boolean b) {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSelected() {
        // TODO Auto-generated method stub
        return false;
    }
}

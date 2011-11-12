package nandcat.model.element;

import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import nandcat.model.Clock;

/**
 * Impulse Generator. Cycles between outgoing signals. An Impulse Generator with frequency 0 is basically a Switch.
 */
public class ImpulseGenerator implements Module {

    /**
     * Default serial version uid.
     */
    private static final long serialVersionUID = 1L;

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
     * Selection state of Impulsegeneator.
     */
    private boolean selected;

    /**
     * Constructor with frequency. The frequency is basically the necessary amount of ticks of the clock to toggle the
     * state of this ImpulseGenerator. With frequency 0, the ImpulseGenerator will not change its state. This has
     * implications for the clock's register-procedure.
     * 
     * @param frequency
     *            frequency of the generator
     */
    public ImpulseGenerator(int frequency) {
        state = false;
        if (frequency < 0) {
            new IllegalArgumentException("invalid frequency for impulsegenerator");
        }
        this.frequency = frequency;
        rectangle = new Rectangle(EXTENT, EXTENT);
        outPort = new Port(this);
        outPort.locateOnStandardPosition();
    }

    /**
     * Default constructor. The frequency will be 1.
     */
    public ImpulseGenerator() {
        this(1);
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
        // note that outPort and state are asynchronous
        outPort.setState(state, clock);
        toggleState();
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
        selected = b;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSelected() {
        return selected;
    }
}

package de.unipassau.sep.nandcat.model.element;

import java.awt.Point;
import java.util.LinkedHashSet;
import java.util.Set;
import de.unipassau.sep.nandcat.model.Clock;

/**
 * Impulse Generator.
 * 
 * @version 0.1
 * 
 */
// TODO wie wird manuell klicken zur Zustandsaenderung weiterpropagiert?
// gibt es da eine Uhr?
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
     * Constructor with frequency.
     * 
     * @param frequency
     *            frequency of the generator
     */
    public ImpulseGenerator(int frequency) {
        this.frequency = frequency;
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
    public Set<Port> getInPorts() {
        return new LinkedHashSet<Port>();
    }

    /**
     * {@inheritDoc}
     */
    public Set<Port> getOutPorts() {
        LinkedHashSet<Port> outPorts = new LinkedHashSet<Port>();
        outPorts.add(outPort);
        return outPorts;
    }

    /**
     * {@inheritDoc}
     */
    public void clockTicked(Clock clock) {
        // if (clock.getCycle() % frequency) == 0)
        // stub to remove warning...
        if (frequency == 0) {
            frequency = 1;
        }
        outPort.setState(!state, clock);
    }

    /**
     * {@inheritDoc}
     */
    public void setLocation(Point p) {
        location = p;
    }
}

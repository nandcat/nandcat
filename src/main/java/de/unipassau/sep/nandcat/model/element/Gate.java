package de.unipassau.sep.nandcat.model.element;

import java.awt.Point;
import java.util.LinkedHashSet;
import java.util.Set;
import de.unipassau.sep.nandcat.model.Clock;

/**
 * Gate class. Representing the minimum consent between the basic gates.
 *
 * @version 0.1
 *
 */
public abstract class Gate implements Module {

    /**
     * Advanced constructor. Creates new Gate with inPorts incoming and outPorts outgoing Ports.
     *
     * @param inPorts
     *            int <b>positive</b> number of inPorts to append
     * @param outPorts
     *            int <b>positive</b> number of outPorts to append
     */
    public Gate(int inPorts, int outPorts) {
        if (!inBoundaries(inPorts) || !outBoundaries(outPorts)) {
            throw new IllegalArgumentException("Illegal amount of in or out ports.");
        }
        createPorts(inPorts, outPorts);
    }

    /**
     * Point specifying the Location of the Gate.
     */
    private Point location;
    
    /**
     * String defining the Gates' name.
     */
    private String name;
    /**
     * Set containing outgoing ports.
     */
    private Set<Port> outPorts;
    /**
     * Set containing incoming ports.
     */
    private Set<Port> inPorts;

    /**
     * Trigger calculation of new output values.
     * 
     * @param clock
     *            Clock that ticked
     */
    protected abstract void calculate(Clock clock);

    /**
     * Return Elements connected to outgoing port(s).
     * 
     * @return Set containing the Next Elements
     */
    Set<Module> getNextElements() {
        Set<Module> result = new LinkedHashSet<Module>();
        for (Port p : getOutPorts()) {
            result.add(p.getConnection().getNextModule());
        }
        return result;
    }

    /**
     * Set Gates' name.
     * 
     * @param name
     *            String that specifies the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return Gates' name.
     * 
     * @return String containing gates' name
     */
    public String getName() {
        return this.name;
    }

    /**
     * {@inheritDoc}
     */
    public void clockTicked(Clock clock) {
        calculate(clock);
    }

    /**
     * Return out ports.
     * 
     * @return Set containing all outgoing ports
     */
    public Set<Port> getOutPorts() {
        return outPorts;
    }

    /**
     * Return in ports.
     * 
     * @return Set containing all incoming ports
     */
    public Set<Port> getInPorts() {
        return inPorts;
    }

    /**
     * Check if given number of ports contains a legal number of outPorts.
     * 
     * @param outPorts
     *            number of ports to check for legality
     * @return given outPorts are within our boundaries
     */
    protected abstract boolean outBoundaries(int outPorts);

    /**
     * Check if given number of ports contains a legal number of inPorts.
     * 
     * @param inPorts
     *            number of ports to check for legality
     * @return given inPorts are within our boundaries
     */
    protected abstract boolean inBoundaries(int inPorts);

    /**
     * Set arbitrary (positive) number of <b>new</b> in and out ports to append. Note that any existing ports will be
     * lost.
     * 
     * @param inPorts
     *            int number of inports to append
     * @param outPorts
     *            int number of outports to append
     */
    private void createPorts(int inPorts, int outPorts) {
        LinkedHashSet<Port> ports = new LinkedHashSet<Port>();
        for (int i = 0; i < inPorts; i++) {
            ports.add(new Port(this));
        }
        this.inPorts = ports;
        ports = new LinkedHashSet<Port>();
        for (int i = 0; i < outPorts; i++) {
            ports.add(new Port(this));
        }
        this.outPorts = ports;
    }

    /**
     * {@inheritDoc}
     */
    public void setLocation(Point p) {
        this.location = p;
    }
}

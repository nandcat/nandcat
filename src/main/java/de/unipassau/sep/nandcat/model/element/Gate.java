package de.unipassau.sep.nandcat.model.element;

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
        if (inPorts <= 0 || outPorts <= 0) {
            throw new IllegalArgumentException("Illegal amount of in or out ports.");
        }
        createPorts(inPorts, outPorts);
    }

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
    protected abstract void calculate(Clock clock); // called more often than calculate in circuit.

    /**
     * Return Elements connected to outgoing port(s).
     * 
     * @return Set containing the Next Elements
     */
    Set<Module> getNextElements() {
        Set<Module> result = new LinkedHashSet<Module>();
        for (Port p : getOutPorts()) {
            result.add(p.getConnection().getNextElement());
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
     * Set outgoing ports.
     * 
     * @param outPorts
     *            Set containing outgoing ports to set
     */
    public void setOutPorts(Set<Port> outPorts) {
        this.outPorts = outPorts;
    }

    /**
     * Set incoming ports.
     * 
     * @param inPorts
     *            Set containing incoming ports to set
     */
    public void setInPorts(Set<Port> inPorts) {
        this.inPorts = inPorts;
    }

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
        setInPorts(new LinkedHashSet<Port>());
        for (int i = 0; i < inPorts; i++) {
            setInPorts(new LinkedHashSet<Port>());
            getInPorts().add(new Port(this));
        }
        setOutPorts(new LinkedHashSet<Port>());
        for (int i = 0; i < outPorts; i++) {
            setOutPorts(new LinkedHashSet<Port>());
            getOutPorts().add(new Port(this));
        }
    }
}

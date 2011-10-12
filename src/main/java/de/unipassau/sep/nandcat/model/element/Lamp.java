package de.unipassau.sep.nandcat.model.element;

import java.util.Set;
import de.unipassau.sep.nandcat.model.Clock;

/**
 * Lamp.
 * 
 * @version 0.1
 * 
 */
public class Lamp implements Module {

    /**
     * Lamp's name.
     */
    private String name;

    /**
     * Lamp's port.
     */
    private Port port;
    /**
     * Lamp's state.
     */
    private boolean state;

    public Set<Port> getInPorts() {
        // TODO Auto-generated method stub
        return null;
    }

    public Set<Port> getOutPorts() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Set lamp's name.
     * 
     * @param name
     *            String to set lamp's name to
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return lamp's name.
     * 
     * @return String representing lamp's name
     */
    public String getName() {
        return name;
    }

    public void clockTicked(Clock clock) {
    }
}

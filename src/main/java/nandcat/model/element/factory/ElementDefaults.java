package nandcat.model.element.factory;

import nandcat.model.element.AndGateBuilder;
import nandcat.model.element.CircuitBuilder;
import nandcat.model.element.ClockBuilder;
import nandcat.model.element.FlipFlopBuilder;
import nandcat.model.element.IdentityGateBuilder;
import nandcat.model.element.LampBuilder;
import nandcat.model.element.NotGateBuilder;
import nandcat.model.element.OrGateBuilder;
import nandcat.model.element.SwitchBuilder;

/**
 * Represents a set of default settings for elements.
 */
public abstract class ElementDefaults {

    /**
     * Sets default settings for an AndGateBuilder.
     * 
     * @param b
     *            Builder to set defaults for.
     */
    public void setDefaults(AndGateBuilder b) {
        b.setInPorts(2);
        b.setOutPorts(1);
    }

    /**
     * Sets default settings for an OrGateBuilder.
     * 
     * @param b
     *            Builder to set defaults for.
     */
    public void setDefaults(OrGateBuilder b) {
        b.setInPorts(2);
        b.setOutPorts(1);
    }

    /**
     * Sets default settings for a NotGateBuilder.
     * 
     * @param b
     *            Builder to set defaults for.
     */
    public void setDefaults(NotGateBuilder b) {
        b.setInPorts(1);
        b.setOutPorts(1);
    }

    /**
     * Sets default settings for an IdentityGateBuilder.
     * 
     * @param b
     *            Builder to set defaults for.
     */
    public void setDefaults(IdentityGateBuilder b) {
        b.setInPorts(1);
        b.setOutPorts(2);
    }

    /**
     * Sets default settings for a LampBuilder.
     * 
     * @param b
     *            Builder to set defaults for.
     */
    public void setDefaults(LampBuilder b) {
        b.setInPorts(1);
        b.setOutPorts(0);
    }

    /**
     * Sets default settings for a ClockBuilder.
     * 
     * @param b
     *            Builder to set defaults for.
     */
    public void setDefaults(ClockBuilder b) {
        b.setInPorts(0);
        b.setOutPorts(1);
    }

    /**
     * Sets default settings for a SwitchBuilder.
     * 
     * @param b
     *            Builder to set defaults for.
     */
    public void setDefaults(SwitchBuilder b) {
        b.setInPorts(0);
        b.setOutPorts(1);
    }

    /**
     * Sets default settings for a FlipFlopBuilder.
     * 
     * @param b
     *            Builder to set defaults for.
     */
    public void setDefaults(FlipFlopBuilder b) {

    }

    /**
     * Sets default settings for a CircuitBuilder.
     * 
     * @param b
     *            Builder to set defaults for.
     */
    public void setDefaults(CircuitBuilder b) {

    }
}

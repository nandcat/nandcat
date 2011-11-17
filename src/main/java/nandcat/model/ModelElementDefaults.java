package nandcat.model;

import nandcat.model.element.AndGateBuilder;
import nandcat.model.element.CircuitBuilder;
import nandcat.model.element.ClockBuilder;
import nandcat.model.element.FlipFlopBuilder;
import nandcat.model.element.IdentityGateBuilder;
import nandcat.model.element.LampBuilder;
import nandcat.model.element.NotGateBuilder;
import nandcat.model.element.OrGateBuilder;
import nandcat.model.element.SwitchBuilder;
import nandcat.model.element.factory.ElementDefaults;

/**
 * ElementDefaults used in the model.
 * 
 */
public class ModelElementDefaults extends ElementDefaults {

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaults(AndGateBuilder b) {
        super.setDefaults(b);
        b.setInPorts(2);
        b.setOutPorts(1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaults(CircuitBuilder b) {
        super.setDefaults(b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaults(ClockBuilder b) {
        super.setDefaults(b);
        b.setFrequency(1);
        b.setInPorts(0);
        b.setOutPorts(1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaults(FlipFlopBuilder b) {
        super.setDefaults(b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaults(IdentityGateBuilder b) {
        super.setDefaults(b);
        b.setInPorts(1);
        b.setOutPorts(2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaults(LampBuilder b) {
        super.setDefaults(b);
        b.setInPorts(1);
        b.setOutPorts(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaults(NotGateBuilder b) {
        super.setDefaults(b);
        b.setInPorts(1);
        b.setOutPorts(1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaults(OrGateBuilder b) {
        super.setDefaults(b);
        b.setInPorts(2);
        b.setOutPorts(1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaults(SwitchBuilder b) {
        super.setDefaults(b);
        b.setOutPorts(1);
        b.setInPorts(1);
        b.setFrequency(0);
    }
}

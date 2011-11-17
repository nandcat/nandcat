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

public class ModelElementDefaults extends ElementDefaults {

    @Override
    public void setDefaults(AndGateBuilder b) {
        b.setInPorts(2);
        b.setOutPorts(1);
    }

    @Override
    public void setDefaults(CircuitBuilder b) {
    }

    @Override
    public void setDefaults(ClockBuilder b) {
        b.setFrequency(50);
        b.setInPorts(0);
        b.setOutPorts(1);
    }

    @Override
    public void setDefaults(FlipFlopBuilder b) {

    }

    @Override
    public void setDefaults(IdentityGateBuilder b) {
        b.setInPorts(1);
        b.setOutPorts(2);
    }

    @Override
    public void setDefaults(LampBuilder b) {
        b.setInPorts(1);
        b.setOutPorts(0);
    }

    @Override
    public void setDefaults(NotGateBuilder b) {
        b.setInPorts(1);
        b.setOutPorts(1);
    }

    @Override
    public void setDefaults(OrGateBuilder b) {
        b.setInPorts(2);
        b.setOutPorts(1);
    }

    @Override
    public void setDefaults(SwitchBuilder b) {
        b.setOutPorts(1);
        b.setInPorts(1);
        b.setFrequency(0);
    }
}

package nandcat.model.element.factory;

import nandcat.model.element.factory.builder.AndGateBuilder;
import nandcat.model.element.factory.builder.CircuitBuilder;
import nandcat.model.element.factory.builder.ClockBuilder;
import nandcat.model.element.factory.builder.FlipFlopBuilder;
import nandcat.model.element.factory.builder.IdentityGateBuilder;
import nandcat.model.element.factory.builder.LampBuilder;
import nandcat.model.element.factory.builder.NotGateBuilder;
import nandcat.model.element.factory.builder.OrGateBuilder;
import nandcat.model.element.factory.builder.SwitchBuilder;

public abstract class ElementDefaults {

    public void setDefaults(AndGateBuilder b) {

    }

    public void setDefaults(OrGateBuilder b) {

    }

    public void setDefaults(NotGateBuilder b) {

    }

    public void setDefaults(IdentityGateBuilder b) {

    }

    public void setDefaults(LampBuilder b) {

    }

    public void setDefaults(ClockBuilder b) {

    }

    public void setDefaults(SwitchBuilder b) {

    }

    public void setDefaults(FlipFlopBuilder b) {

    }

    public void setDefaults(CircuitBuilder b) {

    }
}

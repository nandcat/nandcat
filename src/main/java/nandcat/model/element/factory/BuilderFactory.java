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

public class BuilderFactory {

    private ModuleLayouter layouter;

    private ElementDefaults defaults;

    public void setLayouter(ModuleLayouter layouter) {
        this.layouter = layouter;
    }

    public void setDefaults(ElementDefaults defaults) {
        this.defaults = defaults;
    }

    public ModuleBuilder getAndGateBuilder() {
        return getAndGateBuilder(this.defaults);
    }

    public ModuleBuilder getAndGateBuilder(ElementDefaults defaults) {
        return new AndGateBuilder(defaults, this.layouter);
    }

    public ModuleBuilder getOrGateBuilder() {
        return getOrGateBuilder(this.defaults);
    }

    public ModuleBuilder getOrGateBuilder(ElementDefaults defaults) {
        return new OrGateBuilder(defaults, this.layouter);
    }

    public ModuleBuilder getNotGateBuilder() {
        return getNotGateBuilder(this.defaults);
    }

    public ModuleBuilder getNotGateBuilder(ElementDefaults defaults) {
        return new NotGateBuilder(defaults, this.layouter);
    }

    public ModuleBuilder getLampBuilder() {
        return getLampBuilder(this.defaults);
    }

    public ModuleBuilder getLampBuilder(ElementDefaults defaults) {
        return new LampBuilder(defaults, this.layouter);
    }

    public ModuleBuilder getIdentityGateBuilder() {
        return getIdentityGateBuilder(this.defaults);
    }

    public ModuleBuilder getIdentityGateBuilder(ElementDefaults defaults) {
        return new IdentityGateBuilder(defaults, this.layouter);
    }

    public ModuleBuilder getFlipFlopBuilder() {
        return getFlipFlopBuilder(this.defaults);
    }

    public ModuleBuilder getFlipFlopBuilder(ElementDefaults defaults) {
        return new FlipFlopBuilder(defaults, this.layouter);
    }

    public ModuleBuilder getClockBuilder() {
        return getClockBuilder(this.defaults);
    }

    public ModuleBuilder getClockBuilder(ElementDefaults defaults) {
        return new ClockBuilder(defaults, this.layouter);
    }

    public ModuleBuilder getSwitchBuilder() {
        return getSwitchBuilder(this.defaults);
    }

    public ModuleBuilder getSwitchBuilder(ElementDefaults defaults) {
        return new SwitchBuilder(defaults, this.layouter);
    }

    public ModuleBuilder getCircuitBuilder() {
        return getCircuitBuilder(this.defaults);
    }

    public ModuleBuilder getCircuitBuilder(ElementDefaults defaults) {
        return new CircuitBuilder(defaults, this.layouter);
    }

}

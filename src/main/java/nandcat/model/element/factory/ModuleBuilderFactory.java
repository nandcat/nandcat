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
 * A Factory used to create Builder Objects for creating Circuit elements. To create a circuit element, this factory is
 * used to get the correct builder for the creation process. The Factory can be replaced or extended to support further
 * builders.
 */
public class ModuleBuilderFactory {

    /**
     * Holds the ModuleLayouter instance. Each builder gets the ModuleLayouter to set the layout of the created elements
     * uniformly.
     */
    private ModuleLayouter layouter = null;

    /**
     * Holds the ElementDefaults instance. Each builder gets this ElementDefaults if not replaced with a specific
     * ElementDefaults during creation. The Builder uses this ElementDefaults to set Elements defaults before accepting
     * user settings.
     */
    private ElementDefaults defaults;

    /**
     * Sets the ModuleLayouter used to set the Layout of Elements build by the specific Builder instance. All Builders
     * created in this factory are using the same ModuleLayouter, which makes it easy to replace all ModuleLayouter at
     * once.
     * 
     * @param layouter
     *            ModuleLayouter to set for all created Builders.
     */
    public void setLayouter(ModuleLayouter layouter) {
        this.layouter = layouter;
    }

    /**
     * Sets the ElementDefaults used to set the Defaults of Elements build by the specific Builder instance. All Builder
     * created in this factory are using the same ElementDefaults if not created with a special ElementDefaults object.
     * 
     * @param defaults
     *            ElementDefaults to set for all created Builders.
     */
    public void setDefaults(ElementDefaults defaults) {
        this.defaults = defaults;
    }

    /**
     * Gets a Builder for creating AndGates. The builder is created with factory's ElementDefaults and factory's
     * ModuleLayouter.
     * 
     * @return Builder for creating AndGates.
     */
    public ModuleBuilder getAndGateBuilder() {
        return getAndGateBuilder(this.defaults);
    }

    /**
     * Gets a Builder for creating AndGates. The builder is created with factory's ModuleLayouter, but the factory's
     * ElementDefaults are overridden.
     * 
     * @param defaults
     *            ElementDefaults to override factory's ElementDefaults.
     * @return Builder for creating AndGates.
     */
    public ModuleBuilder getAndGateBuilder(ElementDefaults defaults) {
        return new AndGateBuilder(defaults, this.layouter);
    }

    /**
     * Gets a Builder for creating OrGates. The builder is created with factory's ElementDefaults and factory's
     * ModuleLayouter.
     * 
     * @return Builder for creating OrGates.
     */
    public ModuleBuilder getOrGateBuilder() {
        return getOrGateBuilder(this.defaults);
    }

    /**
     * Gets a Builder for creating OrGates. The builder is created with factory's ModuleLayouter, but the factory's
     * ElementDefaults are overridden.
     * 
     * @param defaults
     *            ElementDefaults to override factory's ElementDefaults.
     * @return Builder for creating OrGates.
     */
    public ModuleBuilder getOrGateBuilder(ElementDefaults defaults) {
        return new OrGateBuilder(defaults, this.layouter);
    }

    /**
     * Gets a Builder for creating NotGates. The builder is created with factory's ElementDefaults and factory's
     * ModuleLayouter.
     * 
     * @return Builder for creating NotGates.
     */
    public ModuleBuilder getNotGateBuilder() {
        return getNotGateBuilder(this.defaults);
    }

    /**
     * Gets a Builder for creating NotGates. The builder is created with factory's ModuleLayouter, but the factory's
     * ElementDefaults are overridden.
     * 
     * @param defaults
     *            ElementDefaults to override factory's ElementDefaults.
     * @return Builder for creating NotGates.
     */
    public ModuleBuilder getNotGateBuilder(ElementDefaults defaults) {
        return new NotGateBuilder(defaults, this.layouter);
    }

    /**
     * Gets a Builder for creating Lamps. The builder is created with factory's ElementDefaults and factory's
     * ModuleLayouter.
     * 
     * @return Builder for creating Lamps.
     */
    public ModuleBuilder getLampBuilder() {
        return getLampBuilder(this.defaults);
    }

    /**
     * Gets a Builder for creating Lamps. The builder is created with factory's ModuleLayouter, but the factory's
     * ElementDefaults are overridden.
     * 
     * @param defaults
     *            ElementDefaults to override factory's ElementDefaults.
     * @return Builder for creating Lamps.
     */
    public ModuleBuilder getLampBuilder(ElementDefaults defaults) {
        return new LampBuilder(defaults, this.layouter);
    }

    /**
     * Gets a Builder for creating IdentityGates. The builder is created with factory's ElementDefaults and factory's
     * ModuleLayouter.
     * 
     * @return Builder for creating IdentityGates.
     */
    public ModuleBuilder getIdentityGateBuilder() {
        return getIdentityGateBuilder(this.defaults);
    }

    /**
     * Gets a Builder for creating IdentityGates. The builder is created with factory's ModuleLayouter, but the
     * factory's ElementDefaults are overridden.
     * 
     * @param defaults
     *            ElementDefaults to override factory's ElementDefaults.
     * @return Builder for creating IdentityGates.
     */
    public ModuleBuilder getIdentityGateBuilder(ElementDefaults defaults) {
        return new IdentityGateBuilder(defaults, this.layouter);
    }

    /**
     * Gets a Builder for creating FlipFlops. The builder is created with factory's ElementDefaults and factory's
     * ModuleLayouter.
     * 
     * @return Builder for creating FlipFlops.
     */
    public ModuleBuilder getFlipFlopBuilder() {
        return getFlipFlopBuilder(this.defaults);
    }

    /**
     * Gets a Builder for creating FlipFlops. The builder is created with factory's ModuleLayouter, but the factory's
     * ElementDefaults are overridden.
     * 
     * @param defaults
     *            ElementDefaults to override factory's ElementDefaults.
     * @return Builder for creating FlipFlops.
     */
    public ModuleBuilder getFlipFlopBuilder(ElementDefaults defaults) {
        return new FlipFlopBuilder(defaults, this.layouter);
    }

    /**
     * Gets a Builder for creating Clocks. A Clock is an ImpulseGenerator with a frequency > 0. The builder is created
     * with factory's ElementDefaults and factory's ModuleLayouter.
     * 
     * @return Builder for creating Clocks.
     */
    public ModuleBuilder getClockBuilder() {
        return getClockBuilder(this.defaults);
    }

    /**
     * Gets a Builder for creating Clocks. A Clock is an ImpulseGenerator with a frequency > 0. The builder is created
     * with factory's ModuleLayouter, but the factory's ElementDefaults are overridden.
     * 
     * @param defaults
     *            ElementDefaults to override factory's ElementDefaults.
     * @return Builder for creating Clocks.
     */
    public ModuleBuilder getClockBuilder(ElementDefaults defaults) {
        return new ClockBuilder(defaults, this.layouter);
    }

    /**
     * Gets a Builder for creating Switches. A Switch is an ImpulseGenerator with a frequency = 0. The builder is
     * created with factory's ElementDefaults and factory's ModuleLayouter.
     * 
     * @return Builder for creating Switches.
     */
    public ModuleBuilder getSwitchBuilder() {
        return getSwitchBuilder(this.defaults);
    }

    /**
     * Gets a Builder for creating Switches. A Switch is an ImpulseGenerator with a frequency = 0. The builder is
     * created with factory's ModuleLayouter, but the factory's ElementDefaults are overridden.
     * 
     * @param defaults
     *            ElementDefaults to override factory's ElementDefaults.
     * @return Builder for creating Switches.
     */
    public ModuleBuilder getSwitchBuilder(ElementDefaults defaults) {
        return new SwitchBuilder(defaults, this.layouter);
    }

    /**
     * Gets a Builder for creating Circuits. The builder is created with factory's ElementDefaults and factory's
     * ModuleLayouter.
     * 
     * @return Builder for creating Circuits.
     */
    public ModuleBuilder getCircuitBuilder() {
        return getCircuitBuilder(this.defaults);
    }

    /**
     * Gets a Builder for creating Circuits. The builder is created with factory's ModuleLayouter, but the factory's
     * ElementDefaults are overridden.
     * 
     * @param defaults
     *            ElementDefaults to override factory's ElementDefaults.
     * @return Builder for creating Circuits.
     */
    public ModuleBuilder getCircuitBuilder(ElementDefaults defaults) {
        return new CircuitBuilder(defaults, this.layouter);
    }

}

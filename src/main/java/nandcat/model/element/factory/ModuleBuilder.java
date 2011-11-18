package nandcat.model.element.factory;

import java.awt.Point;
import nandcat.model.element.Module;

/**
 * Abstract superclass of all ModuleBuilders. It defines set of operations possible on the Builder to setup an Element.
 * Because all ModuleBuilders inherit from it, the methods should be a superset for all needed Builders. Methods called
 * but not supported by the specific Builder should be ignored.
 */
public abstract class ModuleBuilder {

    /**
     * Number of outPorts used to create Module.
     */
    private Integer outPorts = null;

    /**
     * Number of inPorts used to create Module.
     */
    private Integer inPorts = null;

    /**
     * ModuleLayouter used to layout the created Module.
     */
    private ModuleLayouter layouter = null;

    /**
     * ElementDefaults used to setup default values for the created Module.
     */
    private ElementDefaults defaults = null;

    /**
     * Annotation to set for the created Module.
     */
    private String annotation = null;

    /**
     * Location to set for the created Module.
     */
    private Point location = null;

    /**
     * Frequency to set for the created Module. Used for ImpulseGenerators.
     */
    private Integer frequency = null;

    /**
     * UUID to set for the created Module. Used for Circuits.
     */
    private String uuid = null;

    /**
     * Sets the number of incoming ports.
     * 
     * @param inPorts
     *            Number of incoming ports.
     * @return Instance of ModuleBuilder, used for chaining commands.
     */
    public ModuleBuilder setInPorts(int inPorts) {
        this.inPorts = inPorts;
        return this;
    }

    /**
     * Sets the number of outgoing ports.
     * 
     * @param outPorts
     *            Number of outgoing ports.
     * @return Instance of ModuleBuilder, used for chaining commands.
     */
    public ModuleBuilder setOutPorts(int outPorts) {
        this.outPorts = outPorts;
        return this;
    }

    /**
     * Sets the ModuleLayouter used for layouting created Module.
     * 
     * @param layouter
     *            ModuleLayouter used for layouting created Module.
     * @return Instance of ModuleBuilder, used for chaining commands.
     */
    public ModuleBuilder setLayouter(ModuleLayouter layouter) {
        this.layouter = layouter;
        return this;
    }

    /**
     * Sets the ElementDefaults used to set defaults for the created Module.
     * 
     * @param defaults
     *            ElementDefaults used to set defaults for the created Module.
     * @return Instance of ModuleBuilder, used for chaining commands.
     */
    public ModuleBuilder setDefaults(ElementDefaults defaults) {
        this.defaults = defaults;
        return this;
    }

    /**
     * Sets the Annotation for the created Module.
     * 
     * @param annotation
     *            Annotation to set.
     * @return Instance of ModuleBuilder, used for chaining commands.
     */
    public ModuleBuilder setAnnotation(String annotation) {
        this.annotation = annotation;
        return this;
    }

    /**
     * Sets the location of the created Module.
     * 
     * @param location
     *            Location to set.
     * @return Instance of ModuleBuilder, used for chaining commands.
     */
    public ModuleBuilder setLocation(Point location) {
        this.location = location;
        return this;
    }

    /**
     * Sets the UUID of the created Module.
     * 
     * @param uuid
     *            UUID to set.
     * @return Instance of ModuleBuilder, used for chaining commands.
     */
    public ModuleBuilder setUUID(String uuid) {
        this.uuid = uuid;
        return this;
    }

    /**
     * Sets the frequency of the created Module.
     * 
     * @param frequency
     *            Frequency to set.
     * @return Instance of ModuleBuilder, used for chaining commands.
     */
    public ModuleBuilder setFrequency(int frequency) {
        this.frequency = frequency;
        return this;
    }

    /**
     * Builds the module with set attributes and defaults, layouts it using the ModuleLayouter and returns a new
     * instance.
     * 
     * @return Instance of created Module.
     */
    public abstract Module build();

    /**
     * Gets the number of outgoing ports.
     * 
     * @return Number of outgoing ports.
     */
    protected Integer getOutPorts() {
        return outPorts;
    }

    /**
     * Gets the number of incoming ports.
     * 
     * @return Number of incoming ports.
     */
    protected Integer getInPorts() {
        return inPorts;
    }

    /**
     * Gets the ModuleLayouter.
     * 
     * @return the set ModuleLayouter.
     */
    protected ModuleLayouter getLayouter() {
        return layouter;
    }

    /**
     * Gets the ElementDefaults.
     * 
     * @return the ElementDefaults.
     */
    protected ElementDefaults getDefaults() {
        return defaults;
    }

    /**
     * Gets the set Annotation.
     * 
     * @return the Annotation.
     */
    protected String getAnnotation() {
        return annotation;
    }

    /**
     * Gets the set Location.
     * 
     * @return the Location.
     */
    protected Point getLocation() {
        return location;
    }

    /**
     * Gets the set Frequency.
     * 
     * @return the frequency.
     */
    protected int getFrequency() {
        return frequency;
    }

    /**
     * Gets the set UUID.
     * 
     * @return the UUID.
     */
    protected String getUUID() {
        return uuid;
    }

}

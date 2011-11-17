package nandcat.model.element.factory;

import java.awt.Point;
import nandcat.model.element.Module;

public abstract class ModuleBuilder {

    private Integer outPorts = null;

    private Integer inPorts = null;

    private ModuleLayouter layouter = null;

    private ElementDefaults defaults = null;

    private String annotation = null;

    private Point location = null;

    private Integer frequency = null;

    private String uuid = null;

    public ModuleBuilder() {

    }

    public ModuleBuilder setInPorts(int inPorts) {
        this.inPorts = inPorts;
        return this;
    }

    public ModuleBuilder setOutPorts(int outPorts) {
        this.outPorts = outPorts;
        return this;
    }

    public ModuleBuilder setLayouter(ModuleLayouter layouter) {
        this.layouter = layouter;
        return this;
    }

    public ModuleBuilder setDefaults(ElementDefaults defaults) {
        this.defaults = defaults;
        return this;
    }

    public ModuleBuilder setAnnotation(String annotation) {
        this.annotation = annotation;
        return this;
    }

    public ModuleBuilder setLocation(Point location) {
        this.location = location;
        return this;
    }

    public ModuleBuilder setUUID(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public ModuleBuilder setFrequency(int frequency) {
        this.frequency = frequency;
        return this;
    }

    public abstract Module build();

    protected Integer getOutPorts() {
        return outPorts;
    }

    protected Integer getInPorts() {
        return inPorts;
    }

    protected ModuleLayouter getLayouter() {
        return layouter;
    }

    protected ElementDefaults getDefaults() {
        return defaults;
    }

    protected String getAnnotation() {
        return annotation;
    }

    protected Point getLocation() {
        return location;
    }

    protected int getFrequency() {
        return frequency;
    }

    /**
     * @return the uuid
     */
    protected String getUUID() {
        return uuid;
    }

}

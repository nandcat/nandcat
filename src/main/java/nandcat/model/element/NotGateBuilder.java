package nandcat.model.element;

import nandcat.model.element.factory.ElementDefaults;
import nandcat.model.element.factory.ModuleBuilder;
import nandcat.model.element.factory.ModuleLayouter;

/**
 * Builder used to build a NotGate.
 */
public class NotGateBuilder extends ModuleBuilder {

    /**
     * Constructs a NotGate Builder with Defaults and Layouter.
     * 
     * @param defaults
     *            Defaults used to set modules attributes.
     * @param layouter
     *            Layouter used to layout the module after creation.
     */
    public NotGateBuilder(ElementDefaults defaults, ModuleLayouter layouter) {
        this.setDefaults(defaults);
        if (defaults != null) {
            defaults.setDefaults(this);
        }
        this.setLayouter(layouter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Module build() {
        NotGate m = new NotGate(getOutPorts());
        m.setName(getAnnotation());
        if (getLocation() != null) {
            m.getRectangle().setLocation(getLocation());
        }
        if (getLayouter() != null) {
            getLayouter().layout(m);
        }
        return m;
    }

}

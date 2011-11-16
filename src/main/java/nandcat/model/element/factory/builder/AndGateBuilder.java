package nandcat.model.element.factory.builder;

import nandcat.model.element.AndGate;
import nandcat.model.element.Module;
import nandcat.model.element.factory.ElementDefaults;
import nandcat.model.element.factory.ModuleBuilder;
import nandcat.model.element.factory.ModuleLayouter;

public class AndGateBuilder extends ModuleBuilder {

    public AndGateBuilder(ElementDefaults defaults, ModuleLayouter layouter) {
        this.setDefaults(defaults);
        if (defaults != null) {
            defaults.setDefaults(this);
        }
        this.setLayouter(layouter);
    }

    @Override
    public Module getModule() {
        AndGate m = new AndGate(getInPorts(), getOutPorts());
        m.setName(getAnnotation());
        if (getLayouter() != null) {
            getLayouter().layout(m);
            if (getLocation() != null) {
                m.getRectangle().setLocation(getLocation());
            }
        }
        return m;
    }
}

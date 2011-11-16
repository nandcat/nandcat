package nandcat.model.element.factory.builder;

import nandcat.model.element.IdentityGate;
import nandcat.model.element.Module;
import nandcat.model.element.factory.ElementDefaults;
import nandcat.model.element.factory.ModuleBuilder;
import nandcat.model.element.factory.ModuleLayouter;

public class IdentityGateBuilder extends ModuleBuilder {

    public IdentityGateBuilder(ElementDefaults defaults, ModuleLayouter layouter) {
        this.setDefaults(defaults);
        if (defaults != null) {
            defaults.setDefaults(this);
        }
        this.setLayouter(layouter);
    }

    @Override
    public Module getModule() {
        IdentityGate m = new IdentityGate(1, getOutPorts());
        m.setName(getAnnotation());
        if (getLayouter() != null) {
            getLayouter().layout(m);
        }
        if (getLocation() != null) {
            m.getRectangle().setLocation(getLocation());
        }
        return m;
    }

}

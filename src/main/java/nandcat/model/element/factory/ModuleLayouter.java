package nandcat.model.element.factory;

import nandcat.model.element.AndGate;
import nandcat.model.element.Circuit;
import nandcat.model.element.FlipFlop;
import nandcat.model.element.IdentityGate;
import nandcat.model.element.ImpulseGenerator;
import nandcat.model.element.Lamp;
import nandcat.model.element.NotGate;
import nandcat.model.element.OrGate;

/**
 * Abstract superclass of all ModuleLayouters. This class is used to layout given Modules and their components. It's the
 * counterpart of the ElementDrawer which draws the Modules layouted before. In this step Port bounds and bounds of the
 * element should be set.
 */
public interface ModuleLayouter {

    /**
     * Layouts an AndGate.
     * 
     * @param m
     *            AndGate to layout.
     */
    void layout(AndGate m);

    /**
     * Layouts an OrGate.
     * 
     * @param m
     *            OrGate to layout.
     */
    void layout(OrGate m);

    /**
     * Layouts a NotGate.
     * 
     * @param m
     *            NotGate to layout.
     */
    void layout(NotGate m);

    /**
     * Layouts an IdentityGate.
     * 
     * @param m
     *            IdentityGate to layout.
     */
    void layout(IdentityGate m);

    /**
     * Layouts a Lamp.
     * 
     * @param m
     *            Lamp to layout.
     */
    void layout(Lamp m);

    /**
     * Layouts an ImpulseGenerator.
     * 
     * @param m
     *            ImpulseGenerator to layout.
     */
    void layout(ImpulseGenerator m);

    /**
     * Layouts a FlipFlop.
     * 
     * @param m
     *            FlipFlop to layout.
     */
    void layout(FlipFlop m);

    /**
     * Layouts a Circuit.
     * 
     * @param m
     *            Circuit to layout.
     */
    void layout(Circuit m);
}

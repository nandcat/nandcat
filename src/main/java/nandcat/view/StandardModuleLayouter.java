package nandcat.view;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.List;
import nandcat.model.element.AndGate;
import nandcat.model.element.Circuit;
import nandcat.model.element.FlipFlop;
import nandcat.model.element.IdentityGate;
import nandcat.model.element.ImpulseGenerator;
import nandcat.model.element.Lamp;
import nandcat.model.element.Module;
import nandcat.model.element.NotGate;
import nandcat.model.element.OrGate;
import nandcat.model.element.Port;
import nandcat.model.element.factory.ModuleLayouter;

/**
 * Standard ModuleLayouter used to layout all Modules.
 */
public class StandardModuleLayouter implements ModuleLayouter {

    /**
     * Left margin of the port inside the bounds.
     */
    private static final int PORT_MARGIN_LEFT = 2;

    /**
     * Right margin of the port inside the bounds.
     */
    private static final int PORT_MARGIN_RIGHT = 2;

    /**
     * Top margin of the port inside the bounds.
     */
    private static final int PORT_MARGIN_TOP = 2;

    /**
     * Bottom margin of the port inside the bounds.
     */
    private static final int PORT_MARGIN_BOTTOM = 2;

    /**
     * Default gate dimension.
     */
    private static final Dimension GATE_DIMENSION = new Dimension(60, 40);

    /**
     * Default lamp dimension.
     */
    private static final Dimension LAMP_DIMENSION = new Dimension(40, 40);

    /**
     * Diameter of the port.
     */
    private static final int PORT_DIAMETER = 4;

    /**
     * {@inheritDoc}
     */
    public void layout(AndGate m) {
        setGateDimension(m);
        layoutPorts(m);
    }

    /**
     * {@inheritDoc}
     */
    public void layout(FlipFlop m) {
        setGateDimension(m);
        layoutPorts(m);
    }

    /**
     * {@inheritDoc}
     */
    public void layout(IdentityGate m) {
        setGateDimension(m);
        layoutPorts(m);
    }

    /**
     * {@inheritDoc}
     */
    public void layout(ImpulseGenerator m) {
        setGateDimension(m);
        layoutPorts(m);
    }

    /**
     * {@inheritDoc}
     */
    public void layout(Lamp m) {
        m.getRectangle().setSize(LAMP_DIMENSION);
        layoutPorts(m);
    }

    /**
     * {@inheritDoc}
     */
    public void layout(NotGate m) {
        setGateDimension(m);
        layoutPorts(m);
    }

    /**
     * {@inheritDoc}
     */
    public void layout(OrGate m) {
        setGateDimension(m);
        layoutPorts(m);
    }

    /**
     * {@inheritDoc}
     */
    public void layout(Circuit m) {
        setGateDimension(m);
        layoutPorts(m);
    }

    /**
     * Layouts the ports.
     * 
     * @param module
     *            Module to layout ports of.
     */
    private void layoutPorts(Module module) {
        Rectangle rec = module.getRectangle();

        // Layout InPorts
        List<Port> inPorts = module.getInPorts();
        int i = 0;
        int numberOfPorts = inPorts.size();
        for (Port port : inPorts) {
            port.setRectangle(getPortBounds(rec, false, i, numberOfPorts));
            i++;
        }

        // Layout OutPorts
        List<Port> outPorts = module.getOutPorts();
        i = 0;
        numberOfPorts = outPorts.size();
        for (Port port : outPorts) {
            port.setRectangle(getPortBounds(rec, true, i, numberOfPorts));
            i++;
        }
    }

    /**
     * Gets the port bounds calculated from environment.
     * 
     * @param bounds
     *            Bounds of the area around the port.
     * @param outPort
     *            True to draw an outgoing port.
     * @param i
     *            Position of the port in the column (0 .. (count-1))
     * @param numberOfPorts
     *            Amount of ports in the column.
     * @return Rectangle representing position and dimension of the port.
     */
    private Rectangle getPortBounds(Rectangle bounds, boolean outPort, int i, int numberOfPorts) {
        int avHeight = bounds.height - PORT_MARGIN_TOP - PORT_MARGIN_BOTTOM;
        float partY = avHeight / (numberOfPorts + 1);
        float positionY = partY * (i + 1) - PORT_DIAMETER;
        positionY += PORT_MARGIN_TOP;
        int positionX = 0;
        if (!outPort) {
            positionX = PORT_MARGIN_LEFT;
        } else {
            positionX = bounds.width - PORT_MARGIN_RIGHT - PORT_DIAMETER;
        }
        positionY += bounds.y;
        positionX += bounds.x;
        return new Rectangle((int) positionX, (int) positionY, PORT_DIAMETER, PORT_DIAMETER);
    }

    /**
     * Sets the size of the given module to default dimensions.
     * 
     * @param module
     *            Module to set dimensions of.
     */
    private void setGateDimension(Module module) {
        module.getRectangle().setSize(GATE_DIMENSION);
    }
}

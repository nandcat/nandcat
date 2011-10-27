package nandcat.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import nandcat.model.element.AndGate;
import nandcat.model.element.Circuit;
import nandcat.model.element.Connection;
import nandcat.model.element.FlipFlop;
import nandcat.model.element.IdentityGate;
import nandcat.model.element.ImpulseGenerator;
import nandcat.model.element.Lamp;
import nandcat.model.element.Module;
import nandcat.model.element.NotGate;
import nandcat.model.element.OrGate;
import nandcat.model.element.Port;
import org.apache.log4j.Logger;

/**
 * Standard element drawer.
 * 
 * Draws all elements.
 */
public class StandardElementDrawer implements ElementDrawer {

    /**
     * Label for the And-Gate.
     */
    private static final String LABEL_ANDGATE = "AND";

    /**
     * Label for the Or-Gate.
     */
    private static final String LABEL_ORGATE = "OR";

    /**
     * Label for the Not-Gate.
     */
    private static final String LABEL_NOTGATE = "NOT";

    /**
     * Label for the Identity-Gate.
     */
    private static final String LABEL_IDENTITYGATE = "SPLITTER";

    /**
     * Currently used graphics object to draw on.
     */
    private Graphics g;

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
     * Top margin of the state indicating area of the impulse generator.
     */
    private static final int IG_STATE_MARGIN_TOP = 5;

    /**
     * Left margin of the state indicating area of the impulse generator.
     */
    private static final int IG_STATE_MARGIN_LEFT = 5;

    /**
     * Percentage of the state indicating area of the impulse generator in comparison to the bounds.
     */
    private static final int IG_STATE_PERC = 30;

    /**
     * Percentage of the bounds.
     */
    private static final int IG_STATE_PERC_FULL = 100;

    /**
     * Diameter of the port.
     */
    private static final int PORT_DIAMETER = 4;

    /**
     * Color of a port with inactive state.
     */
    private static final Color PORT_COLOR_DEFAULT = Color.BLACK;

    /**
     * Color of a port with active state.
     */
    private static final Color PORT_COLOR_ACTIVE = Color.RED;

    /**
     * Color of an inactive connection.
     */
    private static final Color CONNECTION_COLOR_DEFAULT = Color.BLACK;

    /**
     * Color of an active connection.
     */
    private static final Color CONNECTION_COLOR_ACTIVE = Color.RED;

    /**
     * Height of a drawn label. Used for calculations, not for setting height.
     */
    private static final int LABEL_HEIGHT = 10;

    /**
     * Width of a drawn label per char. Used for calculations, not for setting width.
     */
    private static final int LABEL_WIDTH_PER_CHAR = 6;

    /**
     * Color of the label.
     */
    private static final Color LABEL_COLOR = Color.BLACK;

    /**
     * Outline color of gate.
     */
    private static final Color GATE_COLOR = Color.BLACK;

    /**
     * Outline color of selected gate.
     */
    private static final Color GATE_COLOR_SELECTED = Color.RED;

    /**
     * Outline color of a lamp.
     */
    private static final Color LAMP_COLOR = Color.BLACK;

    /**
     * Fill color of an inactive lamp.
     */
    private static final Color LAMP_COLOR_DEFAULT = Color.WHITE;

    /**
     * Fill color of an active lamp.
     */
    private static final Color LAMP_COLOR_ACTIVE = Color.YELLOW;

    /**
     * Fill color of an inactive impulse generator.
     */
    private static final Color IG_COLOR_DEFAULT = Color.WHITE;

    /**
     * Fill color of an active impulse generator.
     */
    private static final Color IG_COLOR_ACTIVE = Color.YELLOW;

    /**
     * Class logger instance.
     */
    private static Logger LOG = Logger.getLogger(StandardElementDrawer.class);

    /**
     * {@inheritDoc}
     */
    public void draw(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection is null");
        }
        Port inPort = connection.getInPort();
        Point inPoint = getPortCenter(inPort);
        Port outPort = connection.getOutPort();
        Point outPoint = getPortCenter(outPort);
        if (connection.getState()) {
            g.setColor(CONNECTION_COLOR_ACTIVE);
        } else {
            g.setColor(CONNECTION_COLOR_DEFAULT);
        }
        LOG.trace("Draw Line: " + outPoint.x + "," + outPoint.y + "," + inPoint.x + "," + inPoint.y);
        g.drawLine(outPoint.x, outPoint.y, inPoint.x, inPoint.y);
    }

    /**
     * {@inheritDoc}
     */
    public void setGraphics(Graphics g) {
        this.g = g;
    }

    /**
     * {@inheritDoc}
     */
    public void draw(Circuit circuit) {
        if (circuit == null) {
            throw new IllegalArgumentException();
        }
        if (circuit.getRectangle() == null) {
            throw new IllegalArgumentException();
        }
        drawModuleOutline(circuit);
        drawModulePorts(circuit);
        drawLabel(circuit.getName(), circuit.getRectangle());
    }

    /**
     * {@inheritDoc}
     */
    public void draw(IdentityGate gate) {
        if (gate == null) {
            throw new IllegalArgumentException();
        }
        if (gate.getRectangle() == null) {
            throw new IllegalArgumentException();
        }
        drawModuleOutline(gate);
        drawModulePorts(gate);
        drawLabel(LABEL_IDENTITYGATE, gate.getRectangle());
    }

    /**
     * {@inheritDoc}
     */
    public void draw(NotGate gate) {
        if (gate == null) {
            throw new IllegalArgumentException();
        }
        if (gate.getRectangle() == null) {
            throw new IllegalArgumentException();
        }
        drawModuleOutline(gate);
        drawModulePorts(gate);
        drawLabel(LABEL_NOTGATE, gate.getRectangle());
    }

    /**
     * {@inheritDoc}
     */
    public void draw(AndGate gate) {
        if (gate == null) {
            throw new IllegalArgumentException();
        }
        if (gate.getRectangle() == null) {
            throw new IllegalArgumentException();
        }
        drawModuleOutline(gate);
        drawModulePorts(gate);
        drawLabel(LABEL_ANDGATE, gate.getRectangle());
    }

    /**
     * {@inheritDoc}
     */
    public void draw(OrGate gate) {
        if (gate == null) {
            throw new IllegalArgumentException();
        }
        if (gate.getRectangle() == null) {
            throw new IllegalArgumentException();
        }
        drawModuleOutline(gate);
        drawModulePorts(gate);
        drawLabel(LABEL_ORGATE, gate.getRectangle());
    }

    /**
     * Draws a given label inside the bounds.
     * 
     * @param label
     *            String label to draw.
     * @param bounds
     *            Bounds of the area to draw on.
     */
    private void drawLabel(String label, Rectangle bounds) {
        int width = label.length() * LABEL_WIDTH_PER_CHAR;
        int height = LABEL_HEIGHT;
        int recWidthCenter = bounds.x + bounds.width / 2;
        int recHeightCenter = bounds.y + bounds.height / 2;
        int positionY = recHeightCenter + height / 2;
        int positionX = recWidthCenter - width / 2;
        g.setColor(LABEL_COLOR);
        g.drawString(label, positionX, positionY);
    }

    /**
     * Draws the module outline.
     * 
     * @param module
     *            Module to draw outline of.
     */
    private void drawModuleOutline(Module module) {
        if (module == null) {
            throw new IllegalArgumentException();
        }
        Rectangle rec = module.getRectangle();
        if (rec == null) {
            throw new IllegalArgumentException();
        }
        if (module.isSelected()) {
            g.setColor(GATE_COLOR_SELECTED);
        } else {
            g.setColor(GATE_COLOR);
        }
        g.drawRect(rec.x, rec.y, rec.width, rec.height);
    }

    /**
     * Draws the module ports.
     * 
     * @param module
     *            Module to draw ports of.
     */
    private void drawModulePorts(Module module) {
        if (module == null) {
            throw new IllegalArgumentException();
        }
        Rectangle rec = module.getRectangle();
        if (rec == null) {
            throw new IllegalArgumentException();
        }
        // Draw InPorts
        List<Port> inPorts = module.getInPorts();
        int i = 0;
        for (Port port : inPorts) {
            drawPort(module.getRectangle(), false, i, inPorts.size(), port.getState());
            i++;
        }
        // Draw OutPorts
        List<Port> outPorts = module.getOutPorts();
        i = 0;
        for (Port port : outPorts) {
            drawPort(module.getRectangle(), true, i, outPorts.size(), port.getState());
            i++;
        }
    }

    /**
     * Gets the center point of the given port.
     * 
     * @param port
     *            Port to calculate center of.
     * @return Center Point object of the given port.
     */
    private Point getPortCenter(Port port) {
        Module module = port.getModule();
        if (module == null) {
            throw new IllegalArgumentException("Port has no module");
        }
        Rectangle rec = module.getRectangle();
        if (rec == null) {
            throw new IllegalArgumentException("Portmodule has no rectangle");
        }
        int indexOfPort;
        int numberOfPortsInColumn;
        if (port.isOutPort()) {
            indexOfPort = module.getOutPorts().indexOf(port);
            numberOfPortsInColumn = module.getOutPorts().size();
        } else {
            indexOfPort = module.getInPorts().indexOf(port);
            numberOfPortsInColumn = module.getInPorts().size();
        }
        if (indexOfPort == -1) {
            throw new IllegalArgumentException("Port not found in modules ports");
        }
        if (numberOfPortsInColumn == 0) {
            throw new IllegalArgumentException("Module has no (in/out) ports");
        }
        Rectangle position = getPortBounds(rec, port.isOutPort(), indexOfPort, numberOfPortsInColumn);
        return new Point(position.x + (position.width / 2), position.y + (position.height / 2));
    }

    /**
     * Gets the port bounds calculated from environment.
     * 
     * @param bounds
     *            Bounds of the area around the port.
     * @param outPort
     *            True to draw an outgoing port.
     * @param i
     *            Position of the port in the column (0 - (count-1))
     * @param count
     *            Amount of ports in the column.
     * @return Rectangle representing position and dimension of the port.
     */
    private Rectangle getPortBounds(Rectangle bounds, boolean outPort, int i, int count) {
        assert bounds != null;
        int avHeight = bounds.height - PORT_MARGIN_TOP - PORT_MARGIN_BOTTOM;
        float partY = avHeight / (count + 1);
        float positionY = partY * (i + 1) - (PORT_DIAMETER / 2);
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
     * Draws a port on the class graphics object.
     * 
     * @param bounds
     *            Bounds of the area around the port.
     * @param outPort
     *            True to draw an outgoing port.
     * @param i
     *            Position of the port in the column (0 - (count-1))
     * @param count
     *            Amount of ports in the column.
     * @param state
     *            State of the port. True iff active.
     */
    private void drawPort(Rectangle bounds, boolean outPort, int i, int count, boolean state) {
        assert bounds != null;
        Rectangle rec = getPortBounds(bounds, outPort, i, count);
        if (state) {
            g.setColor(PORT_COLOR_ACTIVE);
        } else {
            g.setColor(PORT_COLOR_DEFAULT);
        }
        LOG.trace("Draw Oval: x: " + (int) rec.x + " y: " + (int) rec.y + " w: " + rec.width + " h: " + rec.height);
        g.drawOval((int) rec.x, (int) rec.y, rec.width, rec.height);
    }

    /**
     * {@inheritDoc}
     */
    public void draw(Lamp lamp) {
        if (lamp == null) {
            throw new IllegalArgumentException();
        }
        Rectangle rec = lamp.getRectangle();
        if (rec == null) {
            throw new IllegalArgumentException();
        }
        if (lamp.getState()) {
            g.setColor(LAMP_COLOR_ACTIVE);
        } else {
            g.setColor(LAMP_COLOR_DEFAULT);
        }
        LOG.trace("Fill Oval (Lamp Border): x: " + (int) rec.x + " y: " + (int) rec.y + " w: " + rec.width + " h: "
                + rec.height);
        g.fillOval(rec.x, rec.y, rec.width, rec.height);
        if (lamp.isSelected()) {
            g.setColor(GATE_COLOR_SELECTED);
        } else {
            g.setColor(GATE_COLOR);
        }
        LOG.trace("Draw Oval (Lamp Border): x: " + (int) rec.x + " y: " + (int) rec.y + " w: " + rec.width + " h: "
                + rec.height);
        g.drawOval(rec.x, rec.y, rec.width, rec.height);
        drawModulePorts(lamp);
    }

    /**
     * {@inheritDoc}
     */
    public void draw(FlipFlop flipflop) {
        if (flipflop == null) {
            throw new IllegalArgumentException();
        }
        if (flipflop.getRectangle() == null) {
            throw new IllegalArgumentException();
        }
        drawModuleOutline(flipflop);
        drawModulePorts(flipflop);
    }

    /**
     * {@inheritDoc}
     */
    public void draw(ImpulseGenerator ig) {
        if (ig == null) {
            throw new IllegalArgumentException();
        }
        Rectangle rec = ig.getRectangle();
        if (rec == null) {
            throw new IllegalArgumentException();
        }
        drawModuleOutline(ig);
        if (ig.getState()) {
            g.setColor(IG_COLOR_ACTIVE);
        } else {
            g.setColor(IG_COLOR_DEFAULT);
        }
        g.fillRect(rec.x + IG_STATE_MARGIN_LEFT, rec.y + IG_STATE_MARGIN_TOP,
                (int) (rec.width * IG_STATE_PERC / IG_STATE_PERC_FULL),
                (int) (rec.height * IG_STATE_PERC / IG_STATE_PERC_FULL));
        g.setColor(GATE_COLOR);
        g.drawRect(rec.x + IG_STATE_MARGIN_LEFT, rec.y + IG_STATE_MARGIN_TOP,
                (int) (rec.width * IG_STATE_PERC / IG_STATE_PERC_FULL),
                (int) (rec.height * IG_STATE_PERC / IG_STATE_PERC_FULL));
        drawModulePorts(ig);
        drawLabel(Integer.toString(ig.getFrequency()), rec);
    }
}

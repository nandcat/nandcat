package nandcat.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;
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
    private static final String LABEL_IDENTITYGATE = "SPLIT";

    /**
     * Currently used graphics object to draw on.
     */
    private Graphics g;

    /**
     * Left margin of the port inside the bounds.
     */
    private static final int PORT_MARGIN_LEFT = 3;

    /**
     * Right margin of the port inside the bounds.
     */
    private static final int PORT_MARGIN_RIGHT = 3;

    /**
     * Top margin of the port inside the bounds.
     */
    private static final int PORT_MARGIN_TOP = 3;

    /**
     * Bottom margin of the port inside the bounds.
     */
    private static final int PORT_MARGIN_BOTTOM = 3;

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
    private static final int PORT_DIAMETER = 6;

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
     * Color of an selected connection.
     */
    private static final Color CONNECTION_COLOR_SELECTED = Color.RED;

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
     * Color of the annotation text.
     */
    private static final Color ANNOTATION_COLOR = Color.BLACK;

    /**
     * Font used for annotation drawing.
     */
    private static final Font ANNOTATION_FONT = new Font("Dialog", Font.PLAIN, 9);

    /**
     * Padding of annotation bounds in relation to gate bounds.
     */
    private static final int ANNOTATION_PADDING = 2;

    /**
     * Outline color of gate.
     */
    private static final Color GATE_COLOR = Color.BLACK;

    /**
     * Outline color of selected gate.
     */
    private static final Color GATE_COLOR_SELECTED = Color.RED;

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
     * Color of rectangle.
     */
    private static final Color RECTANGLE_COLOR = Color.BLACK;

    /**
     * Default gate dimension.
     */
    private static final Dimension GATE_DIMENSION = new Dimension(70, 50);

    /**
     * Default lamp dimension.
     */
    private static final Dimension LAMP_DIMENSION = new Dimension(60, 60);

    /**
     * Color of the line drawn using draw(Line).
     */
    private static final Color LINE_COLOR = Color.BLACK;

    /**
     * Class logger instance.
     */
    private static final Logger LOG = Logger.getLogger(StandardElementDrawer.class);

    /**
     * Label for Flipflop.
     */
    private static final String LABEL_FLIPFLOP = "RS-FF";

    /**
     * {@inheritDoc}
     */
    public void draw(Connection connection) {
        LOG.trace("Draw: " + connection);
        if (connection == null) {
            throw new IllegalArgumentException("Connection is null");
        }
        Port inPort = connection.getInPort();
        Point inPoint = inPort.getCenter();
        Port outPort = connection.getOutPort();
        Point outPoint = outPort.getCenter();
        LOG.trace("InPort point of connection:" + inPoint.toString());
        LOG.trace("OutPort point of connection:" + outPoint.toString());

        if (connection.getState()) {
            g.setColor(CONNECTION_COLOR_ACTIVE);
        } else {
            g.setColor(CONNECTION_COLOR_DEFAULT);
        }

        if (connection.isSelected()) {
            g.setColor(CONNECTION_COLOR_SELECTED);
        }
        LOG.trace("Draw line: " + outPoint.x + ", " + outPoint.y + ", " + inPoint.x + ", " + inPoint.y);
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
        LOG.trace("Draw: " + circuit);
        if (circuit == null) {
            throw new IllegalArgumentException();
        }
        if (circuit.getRectangle() == null) {
            throw new IllegalArgumentException();
        }
        setModuleDefaultDimension(circuit);
        drawModuleOutline(circuit);
        drawCircuitSymbol(circuit);
        drawAndSetModulePorts(circuit);
        if (circuit.getName() != null && !circuit.getName().isEmpty()) {
            drawLabel(circuit.getName(), circuit.getRectangle());
        }
    }

    /**
     * Draws the symbol of the circuit in the background of the circuit if a symbol exists.
     * 
     * @param circuit
     *            Circuit to draw symbol of.
     */
    private void drawCircuitSymbol(Circuit circuit) {
        if (circuit.getSymbol() != null) {
            g.drawImage(circuit.getSymbol(), circuit.getRectangle().x + 1, circuit.getRectangle().y + 1,
                    circuit.getRectangle().width - 1, circuit.getRectangle().height - 1, null);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void draw(IdentityGate gate) {
        LOG.trace("Draw: " + gate);
        if (gate == null) {
            throw new IllegalArgumentException();
        }
        if (gate.getRectangle() == null) {
            throw new IllegalArgumentException();
        }
        setModuleDefaultDimension(gate);
        drawModuleOutline(gate);
        drawAndSetModulePorts(gate);
        drawLabel(LABEL_IDENTITYGATE, gate.getRectangle());
        if (gate.getName() != null && !gate.getName().isEmpty()) {
            drawAnnotation(gate.getName(), gate.getRectangle());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void draw(NotGate gate) {
        LOG.trace("Draw: " + gate);
        if (gate == null) {
            throw new IllegalArgumentException();
        }
        if (gate.getRectangle() == null) {
            throw new IllegalArgumentException();
        }
        setModuleDefaultDimension(gate);
        drawModuleOutline(gate);
        drawAndSetModulePorts(gate);
        drawLabel(LABEL_NOTGATE, gate.getRectangle());
        if (gate.getName() != null && !gate.getName().isEmpty()) {
            drawAnnotation(gate.getName(), gate.getRectangle());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void draw(AndGate gate) {
        LOG.trace("Draw: " + gate);
        if (gate == null) {
            throw new IllegalArgumentException();
        }
        if (gate.getRectangle() == null) {
            throw new IllegalArgumentException();
        }
        setModuleDefaultDimension(gate);
        drawModuleOutline(gate);
        drawAndSetModulePorts(gate);
        drawLabel(LABEL_ANDGATE, gate.getRectangle());
        if (gate.getName() != null && !gate.getName().isEmpty()) {
            drawAnnotation(gate.getName(), gate.getRectangle());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void draw(OrGate gate) {
        LOG.trace("Draw: " + gate);
        if (gate == null) {
            throw new IllegalArgumentException();
        }
        if (gate.getRectangle() == null) {
            throw new IllegalArgumentException();
        }
        setModuleDefaultDimension(gate);
        drawModuleOutline(gate);
        drawAndSetModulePorts(gate);
        drawLabel(LABEL_ORGATE, gate.getRectangle());
        if (gate.getName() != null && !gate.getName().isEmpty()) {
            drawAnnotation(gate.getName(), gate.getRectangle());
        }
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
    private void drawAndSetModulePorts(Module module) {
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
            port.setRectangle(getPortBounds(module.getRectangle(), false, i, inPorts.size()));
            drawPort(port.getRectangle(), port.getState());
            i++;
        }
        // Draw OutPorts
        List<Port> outPorts = module.getOutPorts();
        i = 0;
        for (Port port : outPorts) {
            port.setRectangle(getPortBounds(module.getRectangle(), true, i, outPorts.size()));
            drawPort(port.getRectangle(), port.getState());
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
     * @param portBounds
     *            Bounds of the port.
     * @param state
     *            State of the port. True iff active.
     */
    private void drawPort(Rectangle portBounds, boolean state) {
        assert portBounds != null;
        if (state) {
            g.setColor(PORT_COLOR_ACTIVE);
        } else {
            g.setColor(PORT_COLOR_DEFAULT);
        }
        g.drawOval((int) portBounds.x, (int) portBounds.y, portBounds.width, portBounds.height);
    }

    /**
     * Sets the size of the given module to default dimensions.
     * 
     * @param module
     *            Module to set dimensions of.
     */
    private void setModuleDefaultDimension(Module module) {
        module.getRectangle().setSize(GATE_DIMENSION);
    }

    /**
     * Sets the size of the given lamp to default dimensions.
     * 
     * @param lamp
     *            Module to set dimensions of.
     */
    private void setLampDefaultDimension(Lamp lamp) {
        lamp.getRectangle().setSize(LAMP_DIMENSION);
    }

    /**
     * Draws an annotation string near the bounds object.
     * 
     * @param text
     *            Text as String to draw.
     * @param bounds
     *            Bounds of the object to draw the annotation for.
     */
    private void drawAnnotation(String text, Rectangle bounds) {
        assert text != null;
        assert bounds != null;
        int recWidthCenter = bounds.x + bounds.width / 2;
        g.setColor(ANNOTATION_COLOR);
        FontRenderContext frc = ((Graphics2D) g).getFontRenderContext();
        TextLayout layout = new TextLayout(text, ANNOTATION_FONT, frc);
        // Text Above
        // layout.draw(((Graphics2D) g), (float)recWidthCenter-(float) layout.getBounds().getWidth()/2,
        // (float)bounds.getY());
        layout.draw(((Graphics2D) g), (float) recWidthCenter - (float) layout.getBounds().getWidth() / 2,
                (float) bounds.getY() + (float) bounds.getHeight() + (float) layout.getBounds().getHeight()
                        + ANNOTATION_PADDING);
    }

    /**
     * {@inheritDoc}
     */
    public void draw(Lamp lamp) {
        LOG.trace("Draw: " + lamp);
        if (lamp == null) {
            throw new IllegalArgumentException();
        }
        Rectangle rec = lamp.getRectangle();
        if (rec == null) {
            throw new IllegalArgumentException();
        }
        setLampDefaultDimension(lamp);
        if (lamp.getState()) {
            g.setColor(LAMP_COLOR_ACTIVE);
        } else {
            g.setColor(LAMP_COLOR_DEFAULT);
        }
        // LOG.trace("Fill Oval (Lamp Border): x: " + (int) rec.x + " y: " + (int) rec.y + " w: " + rec.width + " h: "
        // + rec.height);
        g.fillOval(rec.x, rec.y, rec.width, rec.height);
        if (lamp.isSelected()) {
            g.setColor(GATE_COLOR_SELECTED);
        } else {
            g.setColor(GATE_COLOR);
        }
        // LOG.trace("Draw Oval (Lamp Border): x: " + (int) rec.x + " y: " + (int) rec.y + " w: " + rec.width + " h: "
        // + rec.height);
        g.drawOval(rec.x, rec.y, rec.width, rec.height);
        drawAndSetModulePorts(lamp);
        if (lamp.getName() != null && !lamp.getName().isEmpty()) {
            drawAnnotation(lamp.getName(), rec);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void draw(FlipFlop flipflop) {
        LOG.trace("Draw: " + flipflop);
        if (flipflop == null) {
            throw new IllegalArgumentException();
        }
        if (flipflop.getRectangle() == null) {
            throw new IllegalArgumentException();
        }
        setModuleDefaultDimension(flipflop);
        drawModuleOutline(flipflop);
        drawAndSetModulePorts(flipflop);
        drawLabel(LABEL_FLIPFLOP, flipflop.getRectangle());
        if (flipflop.getName() != null && !flipflop.getName().isEmpty()) {
            drawAnnotation(flipflop.getName(), flipflop.getRectangle());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void draw(ImpulseGenerator ig) {
        LOG.trace("Draw: " + ig);
        if (ig == null) {
            throw new IllegalArgumentException();
        }
        Rectangle rec = ig.getRectangle();
        if (rec == null) {
            throw new IllegalArgumentException();
        }
        setModuleDefaultDimension(ig);
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
        drawAndSetModulePorts(ig);
        drawLabel(Integer.toString(ig.getFrequency()), rec);
        if (ig.getName() != null && !ig.getName().isEmpty()) {
            drawAnnotation(ig.getName(), ig.getRectangle());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void draw(Rectangle r) {
        if (r == null) {
            throw new IllegalArgumentException();
        }
        g.setColor(RECTANGLE_COLOR);
        g.drawRect(r.x, r.y, r.width, r.height);
    }

    /**
     * {@inheritDoc}
     */
    public void draw(Line2D l) {
        if (l == null) {
            throw new IllegalArgumentException();
        }
        g.setColor(LINE_COLOR);
        g.drawLine((int) l.getX1(), (int) l.getY1(), (int) l.getX2(), (int) l.getY2());
    }
}

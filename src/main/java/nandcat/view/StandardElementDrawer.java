package nandcat.view;

import java.awt.Color;
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
     * Debug: Amount of objects needed to reach to print progress.
     */
    private static final int DEBUG_PRINT_THRESHOLD = 100;

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
     * Color of a port with inactive state.
     */
    private static final Color PORT_COLOR_DEFAULT = Color.BLACK;

    /**
     * Color of a port within a selected module.
     */
    private static final Color PORT_COLOR_SELECTED = Color.RED;

    /**
     * Color of a port with active state.
     */
    private static final Color PORT_COLOR_ACTIVE = Color.ORANGE;

    /**
     * Color of an inactive connection.
     */
    private static final Color CONNECTION_COLOR_DEFAULT = Color.BLACK;

    /**
     * Color of an active connection.
     */
    private static final Color CONNECTION_COLOR_ACTIVE = Color.ORANGE;

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
     * Color of a label inside a selected module.
     */
    private static final Color LABEL_COLOR_SELECTED = Color.RED;

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
     * Fill color of gates.
     */
    private static final Color GATE_FILL_COLOR = Color.WHITE;

    /**
     * Color of the line drawn using draw(Line).
     */
    private static final Color LINE_COLOR = Color.BLACK;

    /**
     * Class logger instance.
     */
    private static final Logger LOG = Logger.getLogger(StandardElementDrawer.class);

    /**
     * Label for FlipFlop.
     */
    private static final String LABEL_FLIPFLOP = "RS-FF";

    /**
     * DEBUG: Object counter.
     */
    private int objectCounter = 0;

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
        } else if (connection.isSelected()) {
            g.setColor(CONNECTION_COLOR_SELECTED);
        } else {
            g.setColor(CONNECTION_COLOR_DEFAULT);
        }
        LOG.trace("Draw line: " + outPoint.x + ", " + outPoint.y + ", " + inPoint.x + ", " + inPoint.y);
        g.drawLine(outPoint.x, outPoint.y, inPoint.x, inPoint.y);
    }

    /**
     * {@inheritDoc}
     */
    public void setGraphics(Graphics g) {
        LOG.debug("Set new Graphics object");
        objectCounter = 0;
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
        drawModuleOutline(circuit);
        drawCircuitSymbol(circuit);
        drawModulePorts(circuit);
        if (circuit.getName() != null && !circuit.getName().isEmpty()) {
            drawLabel(circuit.getName(), circuit.getRectangle(), circuit.isSelected());
        }
    }

    /**
     * Debug component counter and printer.
     */
    private void debugComponentCounter() {
        objectCounter++;
        if ((objectCounter % DEBUG_PRINT_THRESHOLD) == 0) {
            LOG.debug("Object drawn: " + objectCounter);
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
            g.drawImage(circuit.getSymbol().getImage(), circuit.getRectangle().x + 1, circuit.getRectangle().y + 1,
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
        drawModuleOutline(gate);
        drawModulePorts(gate);
        drawLabel(LABEL_IDENTITYGATE, gate.getRectangle(), gate.isSelected());
        if (gate.getName() != null && !gate.getName().isEmpty()) {
            drawAnnotation(gate.getName(), gate.getRectangle());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void draw(NotGate gate) {
        debugComponentCounter();
        LOG.trace("Draw: " + gate);
        if (gate == null) {
            throw new IllegalArgumentException();
        }
        if (gate.getRectangle() == null) {
            throw new IllegalArgumentException();
        }
        drawModuleOutline(gate);
        drawModulePorts(gate);
        drawLabel(LABEL_NOTGATE, gate.getRectangle(), gate.isSelected());
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
        drawModuleOutline(gate);
        drawModulePorts(gate);
        drawLabel(LABEL_ANDGATE, gate.getRectangle(), gate.isSelected());
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
        drawModuleOutline(gate);
        drawModulePorts(gate);
        drawLabel(LABEL_ORGATE, gate.getRectangle(), gate.isSelected());
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
     * @param selected
     *            True if module is selected
     */
    private void drawLabel(String label, Rectangle bounds, boolean selected) {
        int width = label.length() * LABEL_WIDTH_PER_CHAR;
        int height = LABEL_HEIGHT;
        int recWidthCenter = bounds.x + bounds.width / 2;
        int recHeightCenter = bounds.y + bounds.height / 2;
        int positionY = recHeightCenter + height / 2;
        int positionX = recWidthCenter - width / 2;
        if (selected) {
            g.setColor(LABEL_COLOR_SELECTED);
        } else {
            g.setColor(LABEL_COLOR);
        }
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
        g.setColor(GATE_FILL_COLOR);
        g.fillRect(rec.x, rec.y, rec.width, rec.height);

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
        for (Port port : inPorts) {
            drawPort(port.getRectangle(), port.getState(), module.isSelected());
        }

        // Draw OutPorts
        List<Port> outPorts = module.getOutPorts();
        for (Port port : outPorts) {
            drawPort(port.getRectangle(), port.getState(), module.isSelected());
        }
    }

    /**
     * Draws a port on the class graphics object.
     * 
     * @param portBounds
     *            Bounds of the port.
     * @param state
     *            State of the port. True iff active.
     * @param selected
     *            True iff port is selected.
     */
    private void drawPort(Rectangle portBounds, boolean state, boolean selected) {
        assert portBounds != null;

        if (state) {
            g.setColor(PORT_COLOR_ACTIVE);
        } else if (selected) {
            g.setColor(PORT_COLOR_SELECTED);
        } else {
            g.setColor(PORT_COLOR_DEFAULT);
        }
        g.drawOval((int) portBounds.x, (int) portBounds.y, portBounds.width, portBounds.height);
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
        drawModulePorts(lamp);
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
        drawModuleOutline(flipflop);
        drawModulePorts(flipflop);
        drawLabel(LABEL_FLIPFLOP, flipflop.getRectangle(), flipflop.isSelected());
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
        drawModuleOutline(ig);
        if (ig.getState()) {
            g.setColor(IG_COLOR_ACTIVE);
        } else {
            g.setColor(IG_COLOR_DEFAULT);
        }
        g.fillRect(rec.x + IG_STATE_MARGIN_LEFT, rec.y + IG_STATE_MARGIN_TOP,
                (int) (rec.width * IG_STATE_PERC / IG_STATE_PERC_FULL),
                (int) (rec.height * IG_STATE_PERC / IG_STATE_PERC_FULL));
        if (ig.isSelected()) {
            g.setColor(GATE_COLOR_SELECTED);
        } else {
            g.setColor(GATE_COLOR);
        }
        g.drawRect(rec.x + IG_STATE_MARGIN_LEFT, rec.y + IG_STATE_MARGIN_TOP,
                (int) (rec.width * IG_STATE_PERC / IG_STATE_PERC_FULL),
                (int) (rec.height * IG_STATE_PERC / IG_STATE_PERC_FULL));
        drawModulePorts(ig);
        drawLabel(Integer.toString(ig.getFrequency()), rec, ig.isSelected());
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

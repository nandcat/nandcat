package nandcat.model.element;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import org.fest.util.VisibleForTesting;
import nandcat.model.Clock;
import nandcat.model.ClockListener;

/**
 * This class represents a circuit. It could be a customized Module or the main circuit displayed in the GUI. A circuit
 * representing a custom Module will not be displayed in its full extent. Note that this leads to a recursive
 * datastructure, where a circuit could contain more circuits.
 * 
 * @version 7
 */
public class Circuit implements ClockListener, Module {

    /**
     * Contains the Location in this Circuit.
     */
    private Point location;

    /**
     * The name of this Circuit.
     */
    private String name;

    /**
     * Contains the Elements in this circuit.
     */
    private List<Element> elements;

    /**
     * Rectangle representing the circuit's shape.
     */
    private Rectangle rectangle;

    /**
     * (Base64 encoded) byte-arrray representation of the Circuit symbol (if used as Module).
     */
    private byte[] symbol;

    /**
     * Selection state of the circuit.
     */
    private boolean selected;

    /**
     * Default constructor.
     * 
     * @parm p Point specifying the circuit's location
     */
    public Circuit(Point p) {
        location = p;
        name = "";
        elements = new LinkedList<Element>();
        rectangle = new Rectangle();
        symbol = new byte[0];
        selected = false;
    }

    /**
     * {@inheritDoc}
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for Elements.
     * 
     * @return The Elements
     */
    public List<Element> getElements() {
        return elements;
    }

    /**
     * Setter for Elements.
     * 
     * @param elements
     *            The Elements to set
     */
    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    /**
     * {@inheritDoc}
     */
    public List<Port> getInPorts() {
        List<Port> result = new LinkedList<Port>();
        for (Module m : getStartingModules()) {
            for (Port p : m.getInPorts()) {
                // Port is not connected or connted to a module outside the circuit or a impulsegenerator
                if (p.getConnection() == null || !elements.contains(p.getConnection().getPreviousModule())
                        || p.getConnection().getNextModule() instanceof ImpulseGenerator) {
                    result.add(p);
                }
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public List<Port> getOutPorts() {
        List<Port> result = new LinkedList<Port>();
        for (Element e : elements) {
            if (e instanceof Module) {
                Module m = (Module) e;
                for (Port p : m.getOutPorts()) {
                    // empty ports are outPorts
                    // not-emptyports with connections leading to modules outside the circuit or lamps are also outPorts
                    if ((p.getConnection() == null) || !(this.elements.contains(p.getConnection().getNextModule()))
                            || p.getConnection().getNextModule() instanceof Lamp) {
                        result.add(p);
                    }
                }
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public void setLocation(Point p) {
        this.location = p;
    }

    /**
     * {@inheritDoc}
     */
    public Point getLocation() {
        return location;
    }

    /**
     * {@inheritDoc}
     */
    public Rectangle getRectangle() {
        return rectangle;
    }

    /**
     * {@inheritDoc}
     */
    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    /**
     * Returns base64-encoded PNG symbol representation of the circuit. Conversion to a BufferedImage via<br />
     * <b>BufferedImage image = ImageIO.read(new ByteArrayInputStream(circuit.getSymbol()));</b>
     * 
     * @return byte[] representation of PNG symbol
     */
    public byte[] getSymbol() {
        return symbol;
    }

    /**
     * Set the Circuit's <b>base64-encoded</b> symbol.
     * 
     * @param symbol
     *            byte[] encoded PNG symbol
     */
    public void setSymbol(byte[] symbol) {
        this.symbol = symbol;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * {@inheritDoc}
     */
    public void setSelected(boolean b) {
        selected = b;
    }

    // NOTE nice to have: durchlauf sortiert nach Y-Koordinate, sodass die in/outPorts entsprechend
    // als Ein/Ausgaenge eines (Circuit)Bausteins auftauchen
    /**
     * Returns the "first" Modules in this Circuit.
     * 
     * @return List<Module> containing the starting Modules of this Circuit.
     */
    public List<Module> getStartingModules() {
        List<Module> result = new LinkedList<Module>();
        for (Element e : elements) {
            if (e instanceof Module) {
                boolean isStartingModule = false;
                Module m = (Module) e;
                if (m instanceof ImpulseGenerator) {
                    isStartingModule = true;
                } else {
                    for (Port p : m.getInPorts()) {
                        if (p.getConnection() == null) {
                            isStartingModule = true;
                            break;
                        }
                    }
                }
                if (isStartingModule) {
                    result.add(m);
                }
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public void clockTicked(Clock clock) {
        // nothing to be done here, faggit.
    }

    /**
     * Remove element from circuit.
     * 
     * @param e
     *            Element to remove
     */
    public void removeElement(Element e) {
        if (e == null) {
            return;
        }
        if (e instanceof Connection) {
            Connection c = (Connection) e;
            c.getInPort().setConnection(null);
            c.getOutPort().setConnection(null);
            elements.remove(c);
        }
        if (e instanceof Module) {
            Module m = (Module) e;
            for (Port p : m.getInPorts()) {
                removeElement(p.getConnection());
            }
            for (Port p : m.getOutPorts()) {
                removeElement(p.getConnection());
            }
            elements.remove(m);
        }
    }

    /**
     * Selects or deselects a Module. Note that this should not be used to set <code>this</code> Module's selected
     * state. Use <code>setSelected(boolean b)</code> for that.
     * 
     * @param m
     *            Module that will be selected
     * @param b
     *            true if selected, false if not selected
     */
    public void setModuleSelected(Module m, boolean b) {
        m.setSelected(b);
    }

    /**
     * Adds a connection between two ports to this Circuit.
     * 
     * @param inPort
     *            Port the Connection will get attached to
     * @param outPort
     *            Port the Connection will get attached to
     * @return the added Connection
     */
    public Connection addConnection(Port inPort, Port outPort) {
        Connection connection = new Connection(inPort, outPort);
        elements.add(connection);
        return connection;
    }

    /**
     * Adds a Module to the Circuit.
     * 
     * @param m
     *            Module to add
     * @param p
     *            Point specifying the location of the Module
     */
    public void addModule(Module m, Point p) {
        if (m instanceof Circuit) {
            Circuit c = (Circuit) m;
            for (Element e : c.getElements()) {
                if (e instanceof Lamp || e instanceof ImpulseGenerator) {
                    removeElement(e);
                }
            }
        }
        // one module may not appear more than once in elements (ensured by Set<>)
        elements.add(m);
        m.setLocation(p);
    }
}

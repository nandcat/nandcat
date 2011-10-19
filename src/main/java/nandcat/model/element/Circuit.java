package nandcat.model.element;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import nandcat.model.Clock;
import nandcat.model.ClockListener;

/**
 * Circuit.
 * 
 * @version 0.1
 * 
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
    private Set<Element> elements;

    /**
     * Rectangle representing the circuit's shape.
     */
    private Rectangle rectangle;

    /**
     * (Base64 encoded) byte-arrray representation of the Circuit symbol (if used as Module).
     */
    private byte[] symbol;

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
                Module m = (Module) e;
                boolean isStartingModule = false;
                for (Port p : m.getInPorts()) {
                    if (p.getConnection() == null) {
                        break;
                    }
                    isStartingModule = true;
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
     * {@inheritDoc}
     */
    public void clockTicked(Clock clock) {
        // TODO Auto-generated method stub
    }

    /**
     * Getter for Elements.
     * 
     * @return The Elements
     */
    public Set<Element> getElements() {
        return elements;
    }

    /**
     * Setter for Elements.
     * 
     * @param elements
     *            The Elements to set
     */
    // TODO check if needed. Maybe for importing.
    public void setElements(Set<Element> elements) {
        this.elements = elements;
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
        // one module may not appear more than once in elements (ensured by Set<>)
        elements.add(m);
        m.setLocation(p);
    }

    /**
     * {@inheritDoc}
     */
    public List<Port> getInPorts() {
        List<Port> result = new LinkedList<Port>();
        for (Module m : getStartingModules()) {
            for (Port p : m.getInPorts()) {
                // Port is not connected or connted to a module outside the circuit
                if (p.getConnection() == null || !elements.contains(p.getConnection().getPreviousModule())) {
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
                    // not-emptyports with connections leading to modules outside the circuit are also outPorts
                    if ((p.getConnection() == null) || !(this.elements.contains(p.getConnection().getNextModule()))) {
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
                if (p.getConnection() != null) {
                    removeElement(p.getConnection());
                }
            }
            for (Port p : m.getOutPorts()) {
                if (p.getConnection() != null) {
                    removeElement(p.getConnection());
                }
            }
            elements.remove(m);
        }
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
}

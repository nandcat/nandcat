package nandcat.model.element;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import nandcat.model.Clock;
import nandcat.model.ClockListener;

/**
 * This class represents a circuit. It could be a customized Module or the main circuit displayed in the GUI. A circuit
 * representing a custom Module will not be displayed in its full extent. Note that this leads to a recursive
 * datastructure, where a circuit could contain more circuits.
 * 
 * @version 7
 */
public class Circuit implements ClockListener, Module, DrawCircuit, Serializable {

    /**
     * Default version uid.
     */
    private static final long serialVersionUID = 1L;

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
     * Image representation of the Circuit symbol (if used as Module).
     */
    private BufferedImage symbol;

    /**
     * Selection state of the circuit.
     */
    private boolean selected;

    /**
     * The unique identifier of the circuit.
     */
    private String uuid;

    /**
     * Gets the unique identifier of the circuit.
     * 
     * @return The unique identifier.
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Set the uuid.
     * 
     * @param uuid
     *            String representing the uuid
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * Usual constructor for circuits. The UUID will be extracted from the Importer.
     * 
     * @param uuid
     *            String containing the uuid of this circuit
     */
    public Circuit(String uuid) {
        this.uuid = uuid;
        name = "";
        elements = new LinkedList<Element>();
        rectangle = new Rectangle(Module.EXTENT, Module.EXTENT);
        symbol = null;
        selected = false;
    }

    /**
     * Constructs the circuit with a new random uuid.
     */
    public Circuit() {
        this(UUID.randomUUID().toString());
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
     * Get only Modules.
     * 
     * @return Set containing all Modules
     */
    public Set<Module> getModules() {
        Set<Module> modsAt = new HashSet<Module>();
        for (Element e : getElements()) {
            if (e instanceof Module) {
                Module c = (Module) e;
                modsAt.add(c);
            }
        }
        return modsAt;
    }

    /**
     * Get only Connections.
     * 
     * @return Set containing all Connections
     */
    public Set<Connection> getConnections() {
        Set<Connection> consAt = new HashSet<Connection>();
        for (Element e : getElements()) {
            if (e instanceof Connection) {
                Connection c = (Connection) e;
                consAt.add(c);
            }
        }
        return consAt;
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
    public Rectangle getRectangle() {
        return rectangle;
    }

    /**
     * {@inheritDoc}
     */
    public void setRectangle(Rectangle rectangle) {
        // TODO check: set alle child element's rectangles, too?
        this.rectangle = rectangle;
    }

    /**
     * Returns PNG symbol representation of the circuit.
     * 
     * @return BufferedImage representation of PNG symbol
     */
    public BufferedImage getSymbol() {
        return symbol;
    }

    /**
     * Set the Circuit's symbol.
     * 
     * @param symbol
     *            BufferedImage PNG symbol.
     */
    public void setSymbol(BufferedImage symbol) {
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
     * Adds a connection between two ports to this Circuit. Note that this function will also set the port's connection
     * reference. <br/>
     * <b>Note:</b> the inPort of the connection has to be of type outPort and vice versa.<br/>
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
        addModule(m);
        m.getRectangle().setLocation(p);
    }

    /**
     * Adds a Module to the Circuit.
     * 
     * @param m
     *            Module to add
     */
    public void addModule(Module m) {
        if (m instanceof Circuit) {
            Circuit c = (Circuit) m;
            for (Element e : c.getElements()) {
                if (e instanceof Lamp || e instanceof ImpulseGenerator) {
                    removeElement(e);
                }
            }
        }
        for (Port in : m.getInPorts()) {
            in.locateOnStandardPosition(this);
        }
        for (Port out : m.getOutPorts()) {
            out.locateOnStandardPosition(this);
        }
        // one module may not appear more than once in elements (guaranteed by Set<>)
        elements.add(m);
    }

    /**
     * Checks if circuit has unsaved changes.
     * 
     * @return True if circuit has unsaved changes.
     */
    public boolean isDirty() {
        // TODO Implementierung fehlt. Zurücksetzen auf false bei exportToFile
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        String x = "";
        x = "Circuit: " + this.getClass().getSimpleName() + "(" + rectangle.x + "/" + rectangle.y + ")" + "\n";
        x += " inPorts ";
        x += "(In) ";
        for (Port in : getInPorts()) {
            if (in.getRectangle() != null) {
                x += in.getRectangle().x + "/" + in.getRectangle().y + ", ";
            }
        }
        x += "(Out) ";
        for (Port out : getOutPorts()) {
            if (out.getRectangle() != null) {
                x += out.getRectangle().x + "/" + out.getRectangle().y + ", ";
            }
        }
        x += "\n";
        for (Element e : elements) {
            x = x + e + "\n";
        }
        return x + "-----------------------------------------------------------------------";
    }
}

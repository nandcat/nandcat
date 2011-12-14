package nandcat.model.element;

import java.awt.Rectangle;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.swing.ImageIcon;
import nandcat.model.Clock;
import nandcat.model.ClockListener;
import nandcat.model.FastDeepCopy;

/**
 * This class represents a circuit. It could be a customized Module or the main circuit displayed in the GUI. A circuit
 * representing a custom Module will not be displayed in its full extent. Note that this leads to a recursive
 * datastructure, where a circuit could contain more circuits.
 * 
 * @version 7
 */
public class Circuit implements ClockListener, Module, DrawCircuit, Serializable {

    /**
     * Original circuit.
     */
    private Circuit original;

    /**
     * Set containing ports that sould not be shown.
     */
    private Set<Port> hiddenPorts;

    /**
     * Hashmap storing the Modules that got stripped during the addition of the circuit to the circuit (yo dawg).
     */
    private HashMap<Port, Module> strippedModules;

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
     * Set of incoming ports, initialized during construction.
     */
    private List<Port> inPorts;

    /**
     * Set of outcoming ports, initialized during construction.
     */
    private List<Port> outPorts;

    /**
     * Rectangle representing the circuit's shape.
     */
    private Rectangle rectangle;

    /**
     * Image representation of the Circuit symbol (if used as Module).
     */
    private ImageIcon symbol;

    /**
     * Selection state of the circuit.
     */
    private boolean selected;

    /**
     * The unique identifier of the circuit.
     */
    private String uuid;

    /**
     * Usual constructor for circuits. The UUID will be extracted from the Importer.
     * 
     * @param uuid
     *            String containing the uuid of this circuit, null to generate new random uuid.
     */
    protected Circuit(String uuid) {
        if (uuid == null) {
            this.uuid = UUID.randomUUID().toString();
        } else {
            this.uuid = uuid;
        }
        name = "";
        elements = new LinkedList<Element>();
        inPorts = new LinkedList<Port>();
        outPorts = new LinkedList<Port>();
        rectangle = new Rectangle();
        symbol = null;
        selected = false;
        strippedModules = new HashMap<Port, Module>();
        hiddenPorts = new HashSet<Port>();
        original = null;
    }

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
     * Initialize inPorts (invoked after each {add,remove}{Module,Connection} action i.e. manipulation of List<Element>
     * elements).
     */
    private void createInPorts() {
        inPorts.clear();
        for (Port p : strippedModules.keySet()) {
            if (strippedModules.get(p) instanceof ImpulseGenerator && !hiddenPorts.contains(p)) {
                inPorts.add(p);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<Port> getInPorts() {
        return inPorts;
    }

    /**
     * Initialize outPorts (invoked after each {add,remove}{Module,Connection} action i.e. manipulation of List<Element>
     * elements).
     */
    private void createOutPorts() {
        outPorts.clear();
        for (Port p : strippedModules.keySet()) {
            if (strippedModules.get(p) instanceof Lamp && !hiddenPorts.contains(p)) {
                outPorts.add(p);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<Port> getOutPorts() {
        return outPorts;
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
     * Returns PNG symbol representation of the circuit.
     * 
     * @return Image representation of PNG symbol
     */
    public ImageIcon getSymbol() {
        return symbol;
    }

    /**
     * Set the Circuit's symbol.
     * 
     * @param symbol
     *            BufferedImage PNG symbol.
     */
    public void setSymbol(ImageIcon symbol) {
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
        // check if a rescan for in/outPorts is needed
        if (inPort != null && elements.contains(inPort.getModule())) {
            createOutPorts();
        }
        if (outPort != null && elements.contains(outPort.getModule())) {
            createInPorts();
        }
        return connection;
    }

    /**
     * Adds a Module to the Circuit.
     * 
     * @param m
     *            Module to add
     */
    public void addModule(Module m) {
        if (m instanceof Circuit && !(m instanceof FlipFlop)) {
            ((Circuit) m).deconstruct();
        }

        // one module may not appear more than once in elements (guaranteed by Set<>)
        elements.add(m);
        // scan for new potential in/outPorts
        createInPorts();
        createOutPorts();
    }

    /**
     * Reset all Element states to false.
     */
    public void reset() {
        for (Element e : getElements()) {
            if (e instanceof Circuit) {
                ((Circuit) e).reset();
            } else if (e instanceof Module) {
                Module m = (Module) e;
                for (Port p : m.getOutPorts()) {
                    p.setState(false, null);
                }
                for (Port p : m.getInPorts()) {
                    p.setState(false, null);
                }
            }
            // Note: no impulsegenerators can be encountered in this stage
            // as they all were removed (in case of circuits added as modules)

            if (e instanceof Connection) {
                ((Connection) e).setState(false, null);
            }
        }
    }

    /**
     * This method modifies the circuit. It removes all {@link Lamp}s and {@link ImpulseGenerator}s and stores them in a
     * HashMap. The information what {@link Port} held the {@link Connection} leading towards those specified
     * {@link Module}s will be conserved. Also creates a copy for later.
     * 
     * @return this Circuit after the Modifications
     */
    public Circuit deconstruct() {

        Circuit originalC = (Circuit) FastDeepCopy.copy(this);
        this.original = originalC;

        for (Module m : getModules()) {
            if (m instanceof Lamp) {
                if (m.getInPorts().get(0) != null && m.getInPorts().get(0).getConnection() != null) {

                    // Set Portname
                    m.getInPorts().get(0).getConnection().getInPort().setAnnotation(m.getName());

                    /*
                     * just in case someone wants to take a look at it, but it is highly discouraged: map stores
                     * Port-Module pairs, where the port is the port the connection from the module was leading to/from
                     * unfortunately, this might pe superterribad if a circuit haz imp->lamp connections, since both
                     * will be stored in the map and therefore listed as valid in/out ports (since thats the way we roll
                     * (for in/out ports)- but we cant just remove those modules without storing, since we need to be
                     * able to reconstruct the circuit. to avoid this nonsense, an additional set was created and keeps
                     * the ports that should be hidden. danke das wa eignlich ales.
                     */
                    if (m.getInPorts().get(0).getConnection().getInPort().getModule() instanceof ImpulseGenerator) {
                        Module target = m.getInPorts().get(0).getConnection().getInPort().getModule();
                        strippedModules.put(m.getInPorts().get(0).getConnection().getInPort(), m);
                        strippedModules.put(m.getInPorts().get(0), target);
                        hiddenPorts.add(m.getInPorts().get(0).getConnection().getInPort());
                        hiddenPorts.add(m.getInPorts().get(0));
                        this.removeElement(m);
                        this.removeElement(target);
                        this.removeElement(m.getInPorts().get(0).getConnection());
                    } else {
                        strippedModules.put(m.getInPorts().get(0).getConnection().getInPort(), m);
                        this.removeElement(m.getInPorts().get(0).getConnection());
                        this.removeElement(m);
                    }
                }
            } else if (m instanceof ImpulseGenerator) {
                if (m.getOutPorts().get(0) != null && m.getOutPorts().get(0).getConnection() != null) {

                    // Set Portname
                    m.getOutPorts().get(0).getConnection().getOutPort().setAnnotation(m.getName());

                    if (m.getOutPorts().get(0).getConnection().getOutPort().getModule() instanceof Lamp) {
                        Module target = m.getOutPorts().get(0).getConnection().getOutPort().getModule();
                        strippedModules.put(m.getOutPorts().get(0).getConnection().getOutPort(), m);
                        strippedModules.put(m.getOutPorts().get(0), target);
                        hiddenPorts.add(m.getOutPorts().get(0).getConnection().getOutPort());
                        hiddenPorts.add(m.getOutPorts().get(0));
                        this.removeElement(m);
                        this.removeElement(target);
                        this.removeElement(m.getOutPorts().get(0).getConnection());
                    } else {
                        strippedModules.put(m.getOutPorts().get(0).getConnection().getOutPort(), m);
                        this.removeElement(m.getOutPorts().get(0).getConnection());
                        this.removeElement(m);
                    }
                }
            }
        }
        this.createInPorts();
        this.createOutPorts();
        return this;
    }

    /**
     * Get the HashSet containing the information if and which Modules got stripped from the circuit.
     * 
     * @return HashSet containing the information if and which Modules got stripped from the circuit
     */
    public HashMap<Port, Module> getStrippedInfo() {
        return strippedModules;
    }

    /**
     * This method returns the original version of the current circuit, enriched with the {@link Lamp}s and
     * {@link ImpulseGenerator}s previously stripped. Note that this is a completely different Object.
     * 
     * @return a Circuit beeing the original of this circuit
     */
    public Circuit reconstruct() {
        // Circuit result = (Circuit) FastDeepCopy.copy(this);
        // for (Port p : strippedModules.keySet()) {
        // Module mod = strippedModules.get(p);
        // result.addModule(mod);
        // }
        // // 2 loops because impy->lamp would be omgwtf otherwise
        // for (Port p : result.strippedModules.keySet()) {
        // Module mod = strippedModules.get(p);
        // if (mod instanceof Lamp) {
        // result.addConnection(p, mod.getInPorts().get(0));
        // } else if (mod instanceof ImpulseGenerator) {
        // result.addConnection(mod.getOutPorts().get(0), p);
        // }
        // result.strippedModules.clear();
        // result.hiddenPorts.clear();
        // }
        // return result;
        if (original != null) {
            return original;
        } else {
            return this;
        }
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

    // /**
    // * DO NEVER EVER USE THIS METHOD Set new inports
    // *
    // * @param inProts
    // * new list of inports
    // */
    // protected void overrideInPorts(List<Port> inProts) {
    // this.inPorts = inProts;
    // }
    //
    // /**
    // * DO NEVER EVER USE THIS METHOD Set new outports
    // *
    // * @param outProts
    // * new list of outports
    // */
    // protected void overrideOutPorts(List<Port> outProts) {
    // this.outPorts = outProts;
    // }

}

package nandcat.model;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nandcat.model.check.CircuitCheck;
import nandcat.model.element.AndGate;
import nandcat.model.element.Circuit;
import nandcat.model.element.Connection;
import nandcat.model.element.DrawElement;
import nandcat.model.element.Element;
import nandcat.model.element.ImpulseGenerator;
import nandcat.model.element.Module;
import nandcat.model.element.Port;
import nandcat.model.importexport.Exporter;
import nandcat.model.importexport.Importer;

/**
 * The model class contains the logic and data of the program as well as methods to manipulate said data. It is one of
 * the big three parts in NANDcat. Every query regarding data will be directed to this class.
 */
public class Model implements ClockListener {

    /**
     * A set of checks which can be performed on the circuit.
     */
    private Set<CircuitCheck> checks;

    /**
     * Set of all model listeners on the model. The listener informs the implementing class about changes in the model.
     */
    private Set<ModelListener> listeners;

    /**
     * Map containing import formats with <b>key:</b> file extension and <b>value:</b> description.
     */
    private Map<String, String> importFormats;

    /**
     * Map containing ixport formats with <b>key:</b> file extension and <b>value:</b> description.
     */
    private Map<String, String> exportFormats;

    /**
     * Importer map with <b>key:</b> file extension and <b>value:</b> Importer instance.
     */
    private Map<String, Importer> importers;

    /**
     * Exporter map with <b>key:</b> file extension and <b>value:</b> Exporter instance.
     */
    private Map<String, Exporter> exporters;

    /**
     * The current circuit of the model.
     */
    private Circuit circuit;

    /**
     * The current clock instance used for simulation.
     */
    private Clock clock;

    /**
     * Map ViewModules (view representation) to Modules (datastructure).
     */
    private Set<ViewModule> viewModules;

    /**
     * List of all custom Modules.
     */
    private List<Module> loadedModules;

    /**
     * The constructor for the model class.
     */
    public Model() {
        checks = new LinkedHashSet<CircuitCheck>();
        listeners = new LinkedHashSet<ModelListener>();
        importFormats = new HashMap<String, String>();
        exportFormats = new HashMap<String, String>();
        importers = new HashMap<String, Importer>();
        exporters = new HashMap<String, Exporter>();
        circuit = new Circuit(new Point(0, 0));
        clock = new Clock(0, this);
        initView2Module();
        loadedModules = new LinkedList<Module>();
    }

    /**
     * Fill viewModule2Module data structure with default Gates and custom circuits.
     */
    private void initView2Module() {
        viewModules = new HashSet<ViewModule>();
        // TODO fix this
        ViewModule andGate = new ViewModule("AND", AndGate.class, "", null);
        viewModules.add(andGate);

        try {
            andGate.getModule().newInstance();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // FIXME walk through circuit-Ordner and fill viewModule2Module
    }

    /**
     * Start the selected checks on the current circuit.
     */
    public void startChecks() {
        // TODO implement
        // for (CircuitCheck check : checks) {
        // if (check.isActive()) {
        // check.test(circuit);
        // }
        // }
    }

    /**
     * Set a given check on the circuit to active or inactive.
     * 
     * @param check
     *            The CircuitCheck to be set to active.
     * @param isActive
     *            TRUE if the check is set to active, FALSE otherwise.
     */
    // TODO public method? why?
    public void setCheckActive(CircuitCheck check, boolean isActive) {
        // TODO implement
        // check.setActive(isActive);
    }

    /**
     * Load an existing file into the program. The file has to be imported by an importer.
     * 
     * @param fileName
     *            String defining the name of the file to be loaded.
     */
    public void loadFile(String fileName) {
        // TODO implement
        // Importer anstoßen.
    }

    /**
     * Save a file to the system.
     * 
     * @param fileName
     *            String defining the name of the file to be saved.
     */
    public void saveFile(String fileName) {
        // TODO implement
        // Exporter anstoßen.
    }

    /**
     * Returns the list of ViewModules that are necessary for the view.
     * 
     * @return List of ViewModules
     */
    public List<ViewModule> getViewModules() {
        return null;
    }

    /**
     * Adds a listener to the set of listeners, which will be notified using events.
     * 
     * @param l
     *            Modellistener
     */
    public void addListener(ModelListener l) {
        listeners.add(l);
    }

    /**
     * Loads or reloads the List containing the custom-circuits.
     */
    public void loadCustomList() {
    }

    /**
     * Removes a listener from the set of listeners.
     * 
     * @param l
     *            Modellistener
     */
    public void removeListener(ModelListener l) {
        listeners.remove(l);
    }

    /**
     * Selects or deselects a Module.
     * 
     * @param m
     *            Module that will be selected
     * @param b
     *            true if selected, false if not selected
     */
    public void setModuleSelected(Module m, boolean b) {
    }

    /**
     * Get a set of elements within a specific rectangle.
     * 
     * @param rect
     *            Rectangle containing the x- and y-coordinate.
     * @return Set of Elements at the given location.
     */
    // TODO set protected
    public Set<Element> getElementsAt(Rectangle rect) {
        // Set<Element> elementsAt = new HashSet<Element>();
        // for (Element element : circuit.getElements()) {
        // if (element.getRectangle.contains(point) {
        // TODO Get position of Element
        // elementsAt.add(element);
        // }
        return null;
    }

    // TODO recheck, faggit!
    /**
     * Get a set of elements within a specific rectangle.
     * 
     * @param rect
     *            Rectangle containing the x- and y-coordinate.
     * @return Set of Elements at the given location.
     */
    public Set<DrawElement> getDrawElementsAt(Rectangle rect) {
        Set<DrawElement> elementsAt = new HashSet<DrawElement>();
        for (Element element : circuit.getElements()) {
            if (element instanceof Module) {
                Module m = (Module) element;
                if (m.getRectangle().intersects(rect)) {
                    elementsAt.add((DrawElement) element);
                }
            }
            if (element instanceof Connection) {
                Connection c = (Connection) element;
                if (c.getLine().intersects(rect)) {
                    elementsAt.add((DrawElement) element);
                }
            }
        }
        return elementsAt;
    }

    /**
     * Gets the current clock.
     * 
     * @return Clock used for simulation.
     */
    public Clock getClock() {
        return clock;
    }

    /**
     * Select elements from the circuit. An element is selected when it lies within a given rectangle.
     * 
     * @param rect
     *            The Rectangle defining the zone where elements are selected.
     */
    public void selectElements(Rectangle rect) {
        // TODO implement
        // Set<Element> selectedElements = new HashSet<Element>();
        // for (Element element : circuit.getElements()) {
        // if (rect.contains(element.getRectangle)){
        // selectedElements.add(element);
        // }
        // }
    }

    /**
     * Get all elements from the current circuit.
     * 
     * @return A Set of all elements.
     */
    // TODO set protected
    public List<Element> getElements() {
        return circuit.getElements();
    }

    /**
     * Get all elements from the current circuit.
     * 
     * @return A Set of all elements.
     */
    public List<DrawElement> getDrawElements() {
        List<DrawElement> drawElements = new LinkedList<DrawElement>();
        for (Element element : circuit.getElements()) {
            drawElements.add((DrawElement) element);
        }
        return drawElements;
    }

    /**
     * Start the simulation on the current circuit. The starting elements are registered at the clock and compute their
     * output.
     */
    public void startSimulation() {
        for (Module m : circuit.getStartingModules()) {
            clock.addListener(m);
        }
        // TODO auskommentiert für simulation
        // clock.startSimulation();
    }

    /**
     * Stops the simulation on the current circuit.
     */
    public void stopSimulation() {
        // TODO Auto-generated method stub
    }

    /**
     * Remove all objects from the current circuit.
     */
    public void clearCircuit() {
        circuit.getElements().clear();
    }

    /**
     * Adds a Connection between two Ports to this Model. <br/>
     * <b>Note:</b> the inPort of the connection has to be of type outPort and vice versa.<br/>
     * 
     * @param inPort
     *            the Port carrying the input Signal of the Connection
     * @param outPort
     *            the Port carrying the output Signal of the Connection
     */
    public void addConnection(Port inPort, Port outPort) {
        if (inPort == null || !inPort.isOutPort()) {
            throw new IllegalArgumentException();
        }
        if (outPort == null || outPort.isOutPort()) {
            throw new IllegalArgumentException();
        }
        // TODO Testen ob die Bausteine dieser Verbindung auch im Model enthalten?
        Connection connection = circuit.addConnection(inPort, outPort);
        inPort.setConnection(connection);
        outPort.setConnection(connection);
    }

    /**
     * Adds a new Module to the Model.
     * 
     * @param m
     *            ViewModule to be added
     * @param p
     *            Point Location of the Module
     */
    public void addModule(Module m, Point p) {
        circuit.addModule(m, p);
    }

    /**
     * Adds a new Module derived from a ViewModule to the Model.
     * 
     * @param m
     *            ViewModule to be added
     * @param p
     *            Point Location of the Module
     */
    public void addModule(ViewModule m, Point p) {
        // TODO Fehlerpruefung++
        // circuit.addModule(viewModules.get(m), p);
    }

    /**
     * Remove given element.
     * 
     * @param e
     *            Element to remove
     */
    public void removeElement(Element e) {
        circuit.removeElement(e);
    }

    /**
     * {@inheritDoc}
     */
    public void clockTicked(Clock clock) {
        for (ModelListener l : listeners) {
            ModelEvent e = new ModelEvent();
            l.elementsChanged(e);
        }
    }

    /**
     * Gets available checks.
     * 
     * @return Available CircuitChecks.
     */
    public Set<CircuitCheck> getChecks() {
        return checks;
    }

    /**
     * Instruct model to move given modules' position by point p.
     * 
     * @param module
     *            Module that needs relocation
     * @param p
     *            Point specifying the relative positional change
     * 
     * @return boolean specifying if moveoperation was successful
     */
    public boolean moveBy(Module module, Point p) {
        // check if module won't intersect after the move
        Rectangle r = module.getRectangle();
        for (Element e : circuit.getElements()) {
            if (e instanceof Module && ((Module) e).getRectangle().intersects(r)) {
                return false;
            }
        }

        module.getRectangle().getLocation().translate(p.x, p.y);
        ModelEvent e = new ModelEvent(module);
        for (ModelListener l : listeners) {
            l.elementsChanged(e);
        }
        return true;
    }

    /**
     * Instruct model to move given modules' positions by point p.
     * 
     * @param modules
     *            Module that needs relocation
     * @param p
     *            Point specifying the relative positional change
     * 
     * @return boolean specifying if moveoperation for each module was successful
     */
    public boolean moveBy(Set<Module> modules, Point p) {
        boolean result = true;
        for (Module m : modules) {
            if (!moveBy(m, p)) {
                result = false;
            }
        }
        return result;
    }

    /**
     * Toggle state of given module (if possible).
     * 
     * @param m
     *            Module to toggle
     */
    public void toggleModule(Module m) {
        if (m instanceof ImpulseGenerator) {
            ((ImpulseGenerator) m).toggleState();
        }
        ModelEvent e = new ModelEvent(m);
        for (ModelListener l : listeners) {
            l.elementsChanged(e);
        }
    }

    /**
     * Return map containing valid file extensions and description for import.
     * 
     * @return Map with <b>key:</b> file extension and <b>value:</b> description
     */
    public Map<String, String> getImportFormats() {
        // TODO implement
        return null;
    }

    /**
     * Return map containing valid file extensions and description for export.
     * 
     * @return Map with <b>key:</b> file extension and <b>value:</b> description
     */
    public Map<String, String> getExportFormats() {
        // TODO implement
        return null;
    }

    /**
     * Import a Circuit as the new top-level Circuit and all its elements from a file.
     * 
     * @param file
     *            File to import top-level Circuit from
     */
    public void importFromFile(File file) {
        // TODO implement
    }

    /**
     * Export the top-level Circuit and all its elements to a file.
     * 
     * @param file
     *            File to export top-level Circuit from
     */
    public void exportToFile(File file) {
        // TODO implement
    }

    /**
     * Returns parsed circuit from given fileName, iff it exists and is falid.
     * 
     * @param fileName
     *            String file to load circuit from
     * @return Module instantiated circuit, null on failure
     */
    public Module getCircuitByFileName(String fileName) {
        // TODO implement
        return null;
    }

    /**
     * Move the specific port according to the x + y values stored in the point. Throws an Exception if one parameter is
     * null.
     * 
     * @param distance
     *            Point containing the x and y
     * @param port
     *            Port that will be moved
     */
    // public void movePortBy(Point distance, Port port) {
    // if (port == null || distance == null) {
    // throw (new IllegalArgumentException("port and distancepoint must not be null!"));
    // }
    // Rectangle old = port.getRectangle();
    // port.setRectangle(new Rectangle(old.x + distance.x, old.y + distance.y, old.width, old.height));
    // }
}

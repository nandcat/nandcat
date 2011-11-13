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
import nandcat.model.check.CountCheck;
import nandcat.model.check.FeedbackCheck;
import nandcat.model.check.IllegalConnectionCheck;
import nandcat.model.check.OrphanCheck;
import nandcat.model.check.SinkCheck;
import nandcat.model.check.SourceCheck;
import nandcat.model.element.AndGate;
import nandcat.model.element.Circuit;
import nandcat.model.element.Connection;
import nandcat.model.element.DrawElement;
import nandcat.model.element.Element;
import nandcat.model.element.FlipFlop;
import nandcat.model.element.IdentityGate;
import nandcat.model.element.ImpulseGenerator;
import nandcat.model.element.Lamp;
import nandcat.model.element.Module;
import nandcat.model.element.NotGate;
import nandcat.model.element.OrGate;
import nandcat.model.element.Port;
import nandcat.model.importexport.Exporter;
import nandcat.model.importexport.Importer;
import nandcat.model.importexport.sepaf.SEPAFExporter;
import nandcat.model.importexport.sepaf.SEPAFImporter;
import org.apache.log4j.Logger;

/**
 * The model class contains the logic and data of the program as well as methods to manipulate said data. It is one of
 * the big three parts in NANDcat. Every query regarding data will be directed to this class.
 */
public class Model implements ClockListener {

    /**
     * A set of checks which can be performed on the circuit.
     */
    private Set<CircuitCheck> checks = new LinkedHashSet<CircuitCheck>() {

        {
            add(new CountCheck());
            add(new FeedbackCheck());
            add(new IllegalConnectionCheck());
            add(new OrphanCheck());
            add(new SinkCheck());
            add(new SourceCheck());
        }
    };

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
    private List<ViewModule> viewModules;

    /**
     * List of all custom Modules.
     */
    private List<Module> loadedModules;

    /**
     * Class logger instance.
     */
    private static final Logger LOG = Logger.getLogger(Model.class);

    /**
     * The constructor for the model class.
     */
    public Model() {
        // checks = new LinkedHashSet<CircuitCheck>();
        listeners = new LinkedHashSet<ModelListener>();
        importFormats = new HashMap<String, String>();
        exportFormats = new HashMap<String, String>();
        importers = new HashMap<String, Importer>();
        exporters = new HashMap<String, Exporter>();
        circuit = new Circuit();
        clock = new Clock(0, this);
        initView2Module();
        loadedModules = new LinkedList<Module>();
        initExporters();
        initImporters();
    }

    /**
     * Fill viewModule2Module data structure with default Gates and custom circuits.
     */
    private void initView2Module() {
        viewModules = new LinkedList<ViewModule>();
        // TODO fix this
        ViewModule andGate = new ViewModule("AND", new AndGate(), "", null);
        viewModules.add(andGate);
        ViewModule orGate = new ViewModule("OR", new OrGate(), "", null);
        viewModules.add(orGate);
        ViewModule flipFlop = new ViewModule("FlipFlop", new FlipFlop(), "", null);
        viewModules.add(flipFlop);
        ViewModule id = new ViewModule("ID", new IdentityGate(), "", null);
        viewModules.add(id);
        ViewModule lamp = new ViewModule("Lampe", new Lamp(), "", null);
        viewModules.add(lamp);
        ViewModule not = new ViewModule("NOT", new NotGate(), "", null);
        viewModules.add(not);
        ViewModule impy = new ViewModule("ImpulseGenerator", new ImpulseGenerator(), "", null);
        viewModules.add(impy);
        ViewModule andGate3 = new ViewModule("AND-3", new AndGate(3, 1), "", null);
        viewModules.add(andGate3);
        ViewModule orGate3 = new ViewModule("OR-3", new OrGate(3, 1), "", null);
        viewModules.add(orGate3);
        // FIXME walk through circuit-Ordner and fill viewModule2Module
        ViewModule circ = new ViewModule("SomeCircuit", null, "circuit.xml", null);
        viewModules.add(circ);
    }

    /**
     * Start the selected checks on the current circuit.
     */
    public void startChecks() {
        // TODO implement
        for (CircuitCheck check : checks) {
            if (check.isActive()) {
                check.test(circuit);
            }
        }
    }

    /**
     * Returns the Port at the Position. If multiple Ports are intersecting the Rectangle, no specific behaviour can be
     * assured. KTHXBYE.
     * 
     * @param rect
     *            Rectangle containing the x- and y-coordinate
     * @return the Port at this position, null if no Port is there
     */
    public Port getPortAt(Rectangle rect) {
        for (Module m : getModsAt(rect)) {
            for (Port p : m.getInPorts()) {
                if (p.getRectangle() != null && p.getRectangle().intersects(rect)) {
                    return p;
                }
            }
            for (Port p : m.getOutPorts()) {
                if (p.getRectangle() != null && p.getRectangle().intersects(rect)) {
                    return p;
                }
            }
        }
        return null;
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
     * Returns the list of ViewModules that are necessary for the view.
     * 
     * @return List of ViewModules
     */
    public List<ViewModule> getViewModules() {
        return viewModules;
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
        // TODO
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
        m.setSelected(b);
        ModelEvent e = new ModelEvent(m);
        for (ModelListener l : listeners) {
            l.elementsChanged(e);
        }
    }

    /**
     * Get a set of elements within a specific rectangle.
     * 
     * @param rect
     *            Rectangle containing the x- and y-coordinate.
     * @return Set of Elements at the given location.
     */
    private Set<Element> getElementsAt(Rectangle rect) {
        Set<Element> elementsAt = new HashSet<Element>();
        elementsAt.addAll(getConnsAt(rect));
        elementsAt.addAll(getModsAt(rect));
        return elementsAt;
    }

    /**
     * Get all connections intersecting a rectangle.
     * 
     * @param rect
     *            Rectangle containing the x- and y-coordinate
     * @return Set of Connections intersecting the given location
     */
    private Set<Connection> getConnsAt(Rectangle rect) {
        Set<Connection> connsAt = new HashSet<Connection>();
        for (Connection c : circuit.getConnections()) {
            if (c.getLine().intersects(rect)) {
                connsAt.add(c);
            }
        }
        return connsAt;
    }

    /**
     * Get all modules intersecting a rectangle.
     * 
     * @param rect
     *            Rectangle containing the x- and y-coordinate
     * @return Set of Modules intersecting the given location
     */
    private Set<Module> getModsAt(Rectangle rect) {
        Set<Module> connsAt = new HashSet<Module>();
        for (Module m : circuit.getModules()) {
            if (m.getRectangle().intersects(rect)) {
                connsAt.add(m);
            }
        }
        return connsAt;
    }

    /**
     * Get a set of elements within a specific rectangle.
     * 
     * @param rect
     *            Rectangle containing the x- and y-coordinate.
     * @return Set of Elements at the given location.
     */
    public Set<DrawElement> getDrawElementsAt(Rectangle rect) {
        Set<DrawElement> elementsAt = new HashSet<DrawElement>();
        for (Module m : getModsAt(rect)) {
            elementsAt.add((DrawElement) m);
        }
        for (Connection c : getConnsAt(rect)) {
            elementsAt.add((DrawElement) c);
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
     * Select elements from the circuit. An element is selected when it lies within a given rectangle. Note that does
     * not deselect previously selected elements! Use this for multiple selections, e.g. via SHIFT.
     * 
     * @param rect
     *            The Rectangle defining the zone where elements are selected
     */
    public void selectElements(Rectangle rect) {
        Set<DrawElement> DrawElements = new HashSet<DrawElement>();
        for (Element e : getElementsAt(rect)) {
            e.setSelected(true);
            DrawElements.add((DrawElement) e);
        }
        // TODO jaja Codeduplikation checken wir spaeter
        ModelEvent e = new ModelEvent();
        e.setElements(DrawElements);
        for (ModelListener l : listeners) {
            l.elementsChanged(e);
        }
    }

    /**
     * Select elements from the circuit. An element is selected when it lies within a given rectangle. Warning - this
     * will deselect all previously selected elements!
     * 
     * @param rect
     *            The Rectangle defining the zone where elements are selected
     */
    public void exclusiveSelectElements(Rectangle rect) {
        deselectAll();
        selectElements(rect);
    }

    /**
     * Deselects all Elements on the top level circuit.
     */
    public void deselectAll() {
        Set<DrawElement> elements = new HashSet<DrawElement>();
        for (Element e : getElements()) {
            e.setSelected(false);
            elements.add(e);
        }
        ModelEvent e = new ModelEvent();
        e.setElements(elements);
        for (ModelListener l : listeners) {
            l.simulationStopped(e);
        }

    }

    /**
     * Get all elements from the current circuit.
     * 
     * @return A Set of all elements.
     */
    protected List<Element> getElements() {
        return circuit.getElements();
    }

    /**
     * Get all selected elements from the main circuit.
     * 
     * @return Set<Element> containing, oh the magic, all selected elements
     */
    private Set<Element> getSelectedElements() {
        Set<Element> selectitt = new HashSet<Element>();
        for (Element e : getElements()) {
            if (e.isSelected())
                selectitt.add(e);
        }
        return selectitt;
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
        ModelEvent e = new ModelEvent();
        for (ModelListener l : listeners) {
            l.simulationStarted(e);
        }
    }

    /**
     * Stops the simulation on the current circuit.
     */
    public void stopSimulation() {
        clock.stopSimulation();
        ModelEvent e = new ModelEvent();
        for (ModelListener l : listeners) {
            l.simulationStopped(e);
        }
    }

    /**
     * Remove all objects from the current circuit.
     */
    public void clearCircuit() {
        circuit.getElements().clear();
        ModelEvent e = new ModelEvent();
        for (ModelListener l : listeners) {
            l.elementsChanged(e);
        }

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
        // NOTE not sure - importer muss zb in untercircuits verbindungen schaffen. und circuit contains würde da
        // vmtl(!) false zurückgeben.
        Connection connection = circuit.addConnection(inPort, outPort);
        inPort.setConnection(connection);
        outPort.setConnection(connection);
        ModelEvent e = new ModelEvent(connection);
        for (ModelListener l : listeners) {
            l.elementsChanged(e);
        }
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
        ModelEvent e = new ModelEvent(m);
        for (ModelListener l : listeners) {
            l.elementsChanged(e);
        }
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
        Module module = null;
        // spawn new circuit / element _object_
        if (m.getFileName() != "") {
            module = getCircuitByFileName(m.getFileName());
        } else {
            module = m.getModule();
        }

        if (module != null) {
            circuit.addModule(module, p);
        }
        ModelEvent e = new ModelEvent(module);
        for (ModelListener l : listeners) {
            l.elementsChanged(e);
        }
    }

    /**
     * Remove given element.
     * 
     * @param e
     *            Element to remove
     */
    public void removeElement(Element e) {
        circuit.removeElement(e);
        ModelEvent event = new ModelEvent();
        for (ModelListener l : listeners) {
            l.elementsChanged(event);
        }
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
    private boolean moveBy(Module module, Point p) {
        // check if module won't intersect after the move
        Rectangle r = module.getRectangle();
        for (Element e : circuit.getElements()) {
            if (e instanceof Module && ((Module) e).getRectangle().intersects(r)) {
                return false;
            }
        }

        module.getRectangle().getLocation().translate(p.x, p.y);
        ModelEvent e = new ModelEvent(module);
        // ports auch bewegen
        for (Port pörtli : module.getInPorts()) {
            pörtli.getRectangle().getLocation().translate(p.x, p.y);
        }
        for (Port pörtli : module.getOutPorts()) {
            pörtli.getRectangle().getLocation().translate(p.x, p.y);
        }

        for (ModelListener l : listeners) {
            l.elementsChanged(e);
        }
        return true;
    }

    /**
     * Instruct model to move given modules' positions by point p.<br>
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
        ModelEvent e = new ModelEvent();
        for (ModelListener l : listeners) {
            l.elementsChanged(e);
        }
        return result;
    }

    /**
     * Instruct model to move selected modules' positions by point p.
     * 
     * @param p
     *            Point specifying the relative positional change
     * 
     * @return boolean specifying if moveoperation for each module was successful
     */
    public boolean moveBy(Point p) {
        Set<Module> selectedModules = new HashSet<Module>();
        for (Element element : getSelectedElements()) {
            if (element instanceof Module) {
                Module m = (Module) element;
                selectedModules.add(m);
            }
        }
        return moveBy(selectedModules, p);
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
     * Initializes all importers.
     */
    private void initImporters() {

        // Can be extended to multiple importers by merging maps. No need for it right now.
        importers.clear();
        importFormats.clear();
        Importer sepafImporter = new SEPAFImporter();
        Map<String, String> sepafFormats = sepafImporter.getFileFormats();
        for (Map.Entry<String, String> format : sepafFormats.entrySet()) {
            importers.put(format.getKey(), sepafImporter);
        }

        importFormats = sepafFormats;
    }

    /**
     * Initializes all importers.
     */
    private void initExporters() {

        // Can be extended to multiple importers by merging maps. No need for it right now.
        exporters.clear();
        exportFormats.clear();
        Exporter sepafExporter = new SEPAFExporter();
        Map<String, String> sepafFormats = sepafExporter.getFileFormats();
        for (Map.Entry<String, String> format : sepafFormats.entrySet()) {
            exporters.put(format.getKey(), sepafExporter);
        }

        exportFormats = sepafFormats;
    }

    /**
     * Return map containing valid file extensions and description for import.
     * 
     * @return Map with <b>key:</b> file extension and <b>value:</b> description
     */
    public Map<String, String> getImportFormats() {
        return importFormats;
    }

    /**
     * Return map containing valid file extensions and description for export.
     * 
     * @return Map with <b>key:</b> file extension and <b>value:</b> description
     */
    public Map<String, String> getExportFormats() {
        return exportFormats;
    }

    /**
     * Get the extension of a file.
     * 
     * @param f
     *            File to get extension from.
     * @return Extension of given file.
     */
    private static String getFileExtension(File f) {
        String ext = null;
        String s = f.getName();

        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    /**
     * Import a Circuit as the new top-level Circuit and all its elements from a file.
     * 
     * @param file
     *            File to import top-level Circuit from
     */
    public void importFromFile(File file) {
        if (file == null) {
            throw new IllegalArgumentException();
        }
        String ext = getFileExtension(file);
        if (importers.containsKey(ext)) {
            Importer im = importers.get(ext);
            im.setFile(file);
            if (im.importCircuit()) {
                this.circuit = im.getCircuit();
            } else {
                LOG.warn("File import failed! File: " + file.getAbsolutePath());
                // TODO Fehlermeldung an View?
            }
        }
    }

    /**
     * Export the top-level Circuit and all its elements to a file.
     * 
     * @param file
     *            File to export top-level Circuit from
     */
    public void exportToFile(File file) {
        if (file == null) {
            throw new IllegalArgumentException();
        }
        String ext = getFileExtension(file);
        if (exporters.containsKey(ext)) {
            Exporter ex = exporters.get(ext);
            ex.setFile(file);
            ex.setCircuit(circuit);
            if (ex.exportCircuit()) {
                LOG.debug("File exported successfully");
            } else {
                LOG.warn("File export failed! File: " + file.getAbsolutePath());
                // TODO Fehlermeldung an View?
            }
        }
    }

    /**
     * Returns parsed circuit from given fileName, iff it exists and is valid.
     * 
     * @param fileName
     *            String file to load circuit from
     * @return Module instantiated circuit, null on failure
     */
    public Circuit getCircuitByFileName(String fileName) {
        // TODO implement
        // Finde passenden importer
        Circuit c = null;
        // Importer importer;
        // File file = new File(fileName);
        // importer.setFile(file);
        // if (importer.importCircuit()) {
        // c = importer.getCircuit();
        // }
        return c;
    }

    // /**
    // * Move the specific port according to the x + y values stored in the point. Throws an Exception if one parameter
    // is
    // * null.
    // *
    // * @param distance
    // * Point containing the x and y
    // * @param port
    // * Port that will be moved
    // */
    // public void movePortBy(Point distance, Port port) {
    // if (port == null || distance == null) {
    // throw (new IllegalArgumentException("port and distancepoint must not be null!"));
    // }
    // Rectangle old = port.getRectangle();
    // port.setRectangle(new Rectangle(old.x + distance.x, old.y + distance.y, old.width, old.height));
    // }

    /**
     * Gets the current circuit.
     * 
     * @return Circuit, null if there is no circuit.
     */
    public Circuit getCircuit() {
        return circuit;
    }
}

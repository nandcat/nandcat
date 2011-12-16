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
import nandcat.model.element.Circuit;
import nandcat.model.element.Connection;
import nandcat.model.element.DrawElement;
import nandcat.model.element.Element;
import nandcat.model.element.FlipFlop;
import nandcat.model.element.ImpulseGenerator;
import nandcat.model.element.Module;
import nandcat.model.element.Port;
import nandcat.model.element.factory.ModuleBuilderFactory;
import nandcat.model.element.factory.ModuleLayouter;
import nandcat.model.importexport.DrawExporter;
import nandcat.model.importexport.Exporter;
import nandcat.model.importexport.ExternalCircuitSource;
import nandcat.model.importexport.FormatErrorHandler;
import nandcat.model.importexport.FormatException;
import nandcat.model.importexport.Importer;
import nandcat.model.importexport.RecursionException;
import nandcat.model.importexport.sepaf.SEPAFExporter;
import nandcat.model.importexport.sepaf.SEPAFImporter;
import nandcat.view.ElementDrawer;
import org.apache.log4j.Logger;

/**
 * The model class contains the logic and data of the program as well as methods to manipulate said data. It is one of
 * the big three parts in NANDcat. Every query regarding data will be directed to this class.
 */
public class Model implements ClockListener {

    /**
     * Allowed length of an Annotation generated from the filename of a circuit.
     */
    private static final int FILENAME_AS_ANNOTATION_LENGTH = 8;

    /**
     * Set of {@link ImpulseGenerator}s that got toggled by the user. Those will keep their status even if the
     * simulation stopped.
     */
    private Set<ImpulseGenerator> activeImps;

    /**
     * The current paused-status of the simulation.
     */
    private boolean paused;

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
    private List<ViewModule> viewModules;

    /**
     * Changes to circuit are not yet saved.
     */
    private boolean dirty;

    /**
     * Factory used to create elements.
     */
    private ModuleBuilderFactory factory;

    /**
     * Simulation is running.
     */
    private boolean simIsRunning;

    /**
     * Class logger instance.
     */
    private static final Logger LOG = Logger.getLogger(Model.class);

    /**
     * Search Path for circuit files.
     */
    private static final String CIRCUIT_PATH = ".";

    /**
     * Import and Export Error messages from last import/export.
     */
    private List<String> importExportErrorMessages = new LinkedList<String>();

    /**
     * Error handler used to handle errors while importing or exporting.
     */
    private FormatErrorHandler importExportErrorHandler = new FormatErrorHandler() {

        public void warning(FormatException exception) throws FormatException {
            addMessage(exception.getMessage());
            if (exception.getCause() != null) {
                addMessage("Reason: " + exception.getCause().getMessage());
            }
        }

        public void fatal(FormatException exception) throws FormatException {
            addMessage(exception.getMessage());
            if (exception.getCause() != null) {
                addMessage("Reason: " + exception.getCause().getMessage());
            }
            throw exception;

        }

        public void error(FormatException exception) throws FormatException {
            addMessage(exception.getMessage());
            if (exception.getCause() != null) {
                addMessage("Reason: " + exception.getCause().getMessage());
            }
            throw exception;
        }

        /**
         * @param messages
         *            the messages to set
         */
        private void addMessage(String msg) {
            importExportErrorMessages.add(msg);
        }
    };

    /**
     * External Circuitsource used to import missing circuits while importing a circuit from file.
     */
    private ExternalCircuitSource externalCircuitSource = new ExternalCircuitSource() {

        public Circuit getExternalCircuit(String identifier, int depth) throws RecursionException {
            File dir = new File(CIRCUIT_PATH);
            for (File f : dir.listFiles()) {
                if (f.isFile() && f.canRead() && identifier.equals(getFileWithOutExtension(f.getName()))) {
                    return importFromFile(f, depth);
                }
            }
            // backward-compat: try even harder to find the file (missing-circuit has identifier + file-extension)
            for (File f : dir.listFiles()) {
                if (f.isFile() && f.canRead() && identifier.equals(f.getName())) {
                    return importFromFile(f, depth);
                }
            }
            return null;
        }
    };

    /**
     * The constructor for the model class.
     */
    public Model() {
        factory = new ModuleBuilderFactory();
        factory.setDefaults(new ModelElementDefaults());
        checks = new LinkedHashSet<CircuitCheck>();
        listeners = new LinkedHashSet<ModelListener>();
        importFormats = new HashMap<String, String>();
        exportFormats = new HashMap<String, String>();
        importers = new HashMap<String, Importer>();
        exporters = new HashMap<String, Exporter>();
        circuit = (Circuit) factory.getCircuitBuilder().build();
        clock = new Clock(0, this);
        dirty = false;
        simIsRunning = false;
        initExporters();
        initImporters();
        initChecks();
        paused = false;
        activeImps = new HashSet<ImpulseGenerator>();
    }

    /**
     * Adapts the selected Elements of the Circuit to a Grid with given Size.
     * 
     * @param gridSize
     *            int the Size of a Grid-Cell.
     */
    public void adaptToGrid(int gridSize) {
        Set<Element> elementsToAdapt = getSelectedElements();
        adaptToGrid(gridSize, elementsToAdapt);
    }

    /**
     * Adapts the all Elements of the Circuit to a Grid with given Size.
     * 
     * @param gridSize
     *            int the Size of a Grid-Cell.
     */
    public void adaptAllToGrid(int gridSize) {
        List<Element> elements = getElements();
        Set<Element> elementsToAdapt = new HashSet<Element>(elements);
        adaptToGrid(gridSize, elementsToAdapt);
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
            throw new IllegalArgumentException(inPort.toString());
        }
        if (outPort == null || outPort.isOutPort()) {
            throw new IllegalArgumentException(outPort.toString());
        }
        // remove old connections
        circuit.removeElement(inPort.getConnection());
        // this is NOT redundant because one new connection can destroy two old ones
        circuit.removeElement(outPort.getConnection());
        Connection connection = circuit.addConnection(inPort, outPort);
        inPort.setConnection(connection);
        outPort.setConnection(connection);
        notifyForChangedElems();
        dirty = true;
        // System.out.println(circuit);
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
        moveBy(m, new Point(m.getRectangle().x - p.x, m.getRectangle().y - p.y));
        if (m instanceof Circuit && ((Circuit) m).getUuid().equals(circuit.getUuid())) {
            ModelEvent event = new ModelEvent();
            event.setCircuitUuid(((Circuit) m).getUuid());
            for (ModelListener l : listeners) {
                l.addCircuitFailedRecursion(event);
            }
        } else {
            circuit.addModule(m);
            notifyForChangedElems();
            dirty = true;
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
            try {
                module = importFromFile(new File(m.getFileName()), 0);
            } catch (RecursionException e) {
                ModelEvent e2 = new ModelEvent();
                e2.setMessage("Starting file was: " + new File(m.getFileName()).getAbsolutePath() + "\n"
                        + e.getLocalizedMessage());
                e2.setMessage("Recursion too deep");
                for (ModelListener l : listeners) {
                    l.importFailed(e2);
                }
            }
            Circuit c = (Circuit) module;

            // if there's no symbol and no annotation add filename as annotation.
            if (c.getSymbol() == null && c.getName() == null) {
                String filename = new File(m.getFileName()).getName();
                int sep = filename.lastIndexOf(".");
                if (sep != -1) {
                    filename = filename.substring(0, sep);
                }
                if (filename.length() > FILENAME_AS_ANNOTATION_LENGTH) {
                    filename = filename.substring(0, FILENAME_AS_ANNOTATION_LENGTH) + "..";
                }
                c.setName(filename);
            }
        } else {
            module = m.getModule();
        }
        if (module != null) {
            addModule(module, p);

            /*
             * Circuit has to be layouted after inserted using circuit.addModule, because thats the point it gets
             * deconstructed.
             */
            if (module instanceof Circuit && !(module instanceof FlipFlop)) {
                factory.getLayouter().layout((Circuit) module);
            }
        }
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
     * Remove all objects from the current circuit.
     */
    public void clearCircuit() {
        circuit.getElements().clear();
        notifyForChangedElems();
    }

    /**
     * {@inheritDoc}
     */
    public void clockTicked(Clock clock) {
        notifyForChangedElems();
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
        notifyForChangedElems();
    }

    /**
     * Export the given circuit to a file.
     * 
     * @param c
     *            Circuit to export.
     * @param file
     *            File to export to.
     * @param drawer
     *            Drawer to use if exporter supports this. Null otherwise.
     */
    public void exportToFile(Circuit c, File file, ElementDrawer drawer) {
        if (file == null) {
            throw new IllegalArgumentException();
        }
        String ext = getFileExtension(file);
        if (exporters.containsKey(ext)) {
            Exporter ex = exporters.get(ext);
            ex.setErrorHandler(importExportErrorHandler);
            ex.setFile(file);
            Map<String, String> uuid2filename = new HashMap<String, String>();
            for (ViewModule v : viewModules) {
                if (!v.getFileName().equals("")) {
                    try {
                        Circuit tmp = importFromFile(new File(v.getFileName()), 0);
                        uuid2filename.put(tmp.getUuid(), getFileWithOutExtension(v.getFileName()));
                    } catch (RecursionException e) {
                        LOG.warn("Recursion too deep");
                    }

                }
            }

            ex.setExternalCircuits(uuid2filename);
            ex.setCircuit(c);
            importExportErrorMessages = new LinkedList<String>();
            if (ex instanceof DrawExporter) {
                ((DrawExporter) ex).setElementDrawer(drawer);
            }
            if (ex.exportCircuit()) {
                LOG.debug("File exported successfully");
                dirty = false;
                ModelEvent e = new ModelEvent();
                e.setFile(file);
                e.setCircuitUuid(c.getUuid());
                if (drawer != null) {
                    e.setDrawerExport(true);
                }
                for (ModelListener l : listeners) {
                    l.exportSucceeded(e);
                }
            } else {
                LOG.warn("File export failed! File: " + file.getAbsolutePath());
                StringBuilder errorMsgBuilder = new StringBuilder();
                for (String msg : importExportErrorMessages) {
                    errorMsgBuilder.append(msg);
                    errorMsgBuilder.append("\n");
                }
                ModelEvent e = new ModelEvent();
                e.setMessage(errorMsgBuilder.toString());
                e.setFile(file);
                e.setCircuitUuid(c.getUuid());
                if (drawer != null) {
                    e.setDrawerExport(true);
                }
                for (ModelListener l : listeners) {
                    l.exportFailed(e);
                }
                LOG.warn("Export to " + file.getAbsolutePath() + " failed");
            }
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
     * Get all {@link ImpulseGenerator}s activated before starting the simulation.
     * 
     * @return a Set of all {@link ImpulseGenerator}s selected before starting the simulation
     */
    public Set<ImpulseGenerator> getActiveImpulseGens() {
        return activeImps;
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
     * Gets the current circuit.
     * 
     * @return Circuit, <code>NULL</code> if there is no circuit
     */
    public Circuit getCircuit() {
        return circuit;
    }

    /**
     * Creates a new Circuit containing selected Elements only. Connections leading to a Module outside the new Circuit
     * won't be accepted.
     * 
     * @return the Circuit containing the selected Elements
     */
    public Circuit getCircuitFromSelected() {
        Circuit result = (Circuit) factory.getCircuitBuilder().build();
        for (Module m : circuit.getModules()) {
            if (m.isSelected()) {
                result.addModule(m);
            }
        }
        for (Connection c : circuit.getConnections()) {
            if (c.isSelected()) {
                Module targetModule = getTargetModule(c, result.getModules());
                Module sourceModule = getSourceModule(c, result.getModules());
                if (targetModule != null && sourceModule != null) {
                    if (result.getModules().contains(targetModule) && result.getModules().contains(sourceModule)) {
                        result.addConnection(c.getInPort(), c.getOutPort());
                    }
                }
            }
        }
        return result;
    }

    /**
     * Connections source module may lead inside another circuit. This method finds the module inside the given set of
     * modules.
     * 
     * @param c
     *            Connection to get source module for.
     * @param modules
     *            Set of modules to search in.
     * @return Source module, otherwise null.
     */
    private Module getSourceModule(Connection c, Set<Module> modules) {
        if (modules.contains(c.getPreviousModule())) {
            return c.getPreviousModule();
        }
        for (Module module : modules) {
            if (module.getOutPorts().contains(c.getInPort())) {
                return module;
            }
        }
        return null;
    }

    /**
     * Connections target module may lead inside another circuit. This method finds the module inside the given set of
     * modules.
     * 
     * @param c
     *            Connection to get target module for.
     * @param modules
     *            Set of modules to search in.
     * @return Source module, otherwise null.
     */
    private Module getTargetModule(Connection c, Set<Module> modules) {
        if (modules.contains(c.getNextModule())) {
            return c.getNextModule();
        }
        for (Module module : modules) {
            if (module.getInPorts().contains(c.getOutPort())) {
                return module;
            }
        }
        return null;
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
     * Get the number of the current cycle.
     * 
     * @return the number of the current cycle
     */
    public int getCycle() {
        return clock.getCycle();
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
     * Get all elements from the current circuit.
     * 
     * @return A Set of all elements.
     */
    protected List<Element> getElements() {
        return circuit.getElements();
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
     * Return map containing valid file extensions and description for import.
     * 
     * @return Map with <b>key:</b> file extension and <b>value:</b> description
     */
    public Map<String, String> getImportFormats() {
        return importFormats;
    }

    /**
     * Returns the Port at the Position. If multiple Ports are intersecting the Rectangle, no specific behaviour can be
     * assured.
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
     * Returns the list of ViewModules that are necessary for the view.
     * 
     * @return List of ViewModules
     */
    public List<ViewModule> getViewModules() {
        if (viewModules == null) {
            initView2Module();
        }
        return viewModules;
    }

    /**
     * Import a Circuit as the new top-level Circuit and all its elements from a file.
     * 
     * @param file
     *            File to import top-level Circuit from
     */
    public void importRootFromFile(File file) {
        LOG.debug("Start importing");
        ModelEvent e = new ModelEvent();
        // Let listeners interrupt. If interrupted don't create a new circuit.
        for (ModelListener l : listeners) {
            if (l.changeCircuitRequested(e)) {
                return;
            }
        }
        importExportErrorMessages = new LinkedList<String>();
        boolean recursiveError = false;
        try {
            this.circuit = importFromFile(file, 0);
        } catch (RecursionException recException) {
            ModelEvent e2 = new ModelEvent();
            e2.setMessage("Starting file was: " + file.getAbsolutePath() + "\n" + recException.getLocalizedMessage());
            for (ModelListener l : listeners) {
                l.importFailed(e2);
            }
            recursiveError = true;
        }
        LOG.debug("Import finished");
        ModelEvent e2 = new ModelEvent();
        e2.setFile(file);
        // import failed
        if (circuit == null || recursiveError) {
            // Listeners already notified in 'importFromFile(..)'
            newCircuit();

        } else {
            dirty = false;
            e2.setCircuitUuid(this.circuit.getUuid());
            for (ModelListener l : listeners) {
                l.importSucceeded(e2);
            }
        }
        LOG.debug("Notifing listeners");
        notifyForChangedElems();
        LOG.debug("Listeners notified");
    }

    /**
     * Fill viewModule2Module data structure with default Gates and custom circuits.
     */
    public void initView2Module() {
        viewModules = new LinkedList<ViewModule>();
        viewModules.add(new ViewModule("AND", factory.getAndGateBuilder().build(), ""));
        viewModules.add(new ViewModule("OR", factory.getOrGateBuilder().build(), ""));
        viewModules.add(new ViewModule("FlipFlop", factory.getFlipFlopBuilder().build(), ""));
        viewModules.add(new ViewModule("ID", factory.getIdentityGateBuilder().build(), ""));
        viewModules.add(new ViewModule("Lampe", factory.getLampBuilder().build(), ""));
        viewModules.add(new ViewModule("NOT", factory.getNotGateBuilder().build(), ""));
        viewModules.add(new ViewModule("ImpulseGenerator", factory.getClockBuilder().build(), ""));
        // kthxbye
        // viewModules.add(new ViewModule("AND-3", factory.getAndGateBuilder().setInPorts(3).build(), "", null));
        // viewModules.add(new ViewModule("OR-3", factory.getOrGateBuilder().setInPorts(3).build(), "", null));
        // viewModules.add(new ViewModule("ID-3", factory.getIdentityGateBuilder().setOutPorts(3).build(), "", null));
        loadCustomList();
    }

    /**
     * Checks if circuit has unsaved changes.
     * 
     * @return True if circuit has unsaved changes.
     */
    public boolean isDirty() {
        // we won't ask if the user wants to save empty an empty circuit
        if (circuit == null || circuit.getElements().size() == 0) {
            return false;
        }
        return dirty;
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
        notifyForChangedElems();
        if (result) {
            dirty = true;
        }
        return result;
    }

    /**
     * Replaces the current circuit with a new one. All Elements will be lost.
     */
    public void newCircuit() {
        ModelEvent e = new ModelEvent();
        // Let listeners interrupt. If interrupted don't create a new circuit.
        for (ModelListener l : listeners) {
            if (l.changeCircuitRequested(e)) {
                return;
            }
        }
        dirty = false;
        this.circuit = (Circuit) factory.getCircuitBuilder().build();
        e.setCircuitUuid(this.circuit.getUuid());
        for (ModelListener l : listeners) {
            l.newCircuitCreated(e);
        }
        notifyForChangedElems();
    }

    /**
     * Notifies ModelListeners about changed Elements.
     * 
     */
    protected void notifyForChangedElems() {
        ModelEvent e = new ModelEvent();
        HashSet<DrawElement> eventSet = new HashSet<DrawElement>();
        for (Element ele : getSelectedElements()) {
            eventSet.add((DrawElement) ele);
        }
        e.setElements(eventSet);
        for (ModelListener l : listeners) {
            l.elementsChanged(e);
        }
    }

    /**
     * Notifies ModelListeners about the stopped simulation.
     */
    protected void notifyForStoppedSim() {
        ModelEvent e = new ModelEvent();
        simIsRunning = false;
        for (ModelListener l : listeners) {
            l.simulationStopped(e);
        }
    }

    /**
     * Halts the current simulation without resetting it. Can be started again via unpause.
     */
    public void pause() {
        if (paused) {
            throw new IllegalStateException("Unpause Simulation first!");
        }
        paused = true;
        synchronized (clock) {
            clock.setPaused(true);
        }
    }

    /**
     * Restarts a previously halted simulation.
     */
    public void unpause() {
        if (!paused) {
            throw new IllegalStateException("Pause Simulation first!");
        }
        synchronized (clock) {
            clock.setPaused(false);
            clock.notify();
        }
        paused = false;
    }

    /**
     * Remove given element.
     * 
     * @param e
     *            Element to remove
     */
    public void removeElement(Element e) {
        circuit.removeElement(e);
        notifyForChangedElems();
        dirty = true;
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
     * Resets all active {@link ImpulseGenerator}s' states to <code>false</code> and clears the set.
     */
    public void resetActiveImpulseGenerators() {
        for (ImpulseGenerator i : activeImps) {
            if (i.getState()) {
                i.toggleState();
            } else {
                throw new IllegalArgumentException();
            }
        }
        activeImps.clear();
        notifyForChangedElems();
    }

    /**
     * Selects or deselects an Element.
     * 
     * @param m
     *            Element that will be selected
     * @param b
     *            true if selected, false if not selected
     */
    public void setElementSelected(Element m, boolean b) {
        m.setSelected(b);
        notifyForChangedElems();
    }

    /**
     * Select elements from the circuit. An element is selected when it lies within a given rectangle. Note that does
     * not deselect previously selected elements! Use this for multiple selections, e.g. via SHIFT.
     * 
     * @param rect
     *            The Rectangle defining the zone where elements are selected
     * @return true iff at least one element has been selected
     */
    public boolean selectElements(Rectangle rect) {
        boolean result = false;
        Set<DrawElement> drawElements = new HashSet<DrawElement>();
        for (Element e : getElementsAt(rect)) {
            e.setSelected(true);
            drawElements.add((DrawElement) e);
            result = true;
        }
        notifyForChangedElems();
        return result;
    }

    /**
     * Selects one single element from the circuit. Connections have higher priority than other elements. An element is
     * selected when it lies within a given rectangle. Note that does not deselect previously selected elements! Use
     * this for multiple selections, e.g. via SHIFT.
     * 
     * @param rect
     *            The Rectangle defining the zone where elements are selected
     * @return true iff one element has been selected
     */
    public boolean selectElement(Rectangle rect) {
        for (Element e : getConnsAt(rect)) {
            e.setSelected(true);
            notifyForChangedElems();
            return true;
        }
        for (Element e : getElementsAt(rect)) {
            e.setSelected(true);
            notifyForChangedElems();
            return true;
        }
        return false;
    }

    /**
     * Set the Module layouter.
     * 
     * @param layouter
     *            Used Module Layouter
     */
    public void setLayouter(ModuleLayouter layouter) {
        if (layouter == null) {
            throw new IllegalArgumentException();
        }
        factory.setLayouter(layouter);
    }

    /**
     * Changes the frequency of the Module to the given value.
     * 
     * @param m
     *            the Module to be modified
     * @param i
     *            the new value of m's frequency
     * @return true iff (m instanceof ImpulseGenerator returns true && i >= 0)
     */
    public boolean setFrequency(Module m, int i) {
        if (m instanceof ImpulseGenerator && i >= 0) {
            ((ImpulseGenerator) m).setFrequency(i);
            notifyForChangedElems();
            return true;
        }
        return false;
    }

    /**
     * Start the selected checks on the current circuit.
     */
    public void startChecks() {
        ModelEvent e = new ModelEvent();
        for (ModelListener l : listeners) {
            l.checksStarted(e);
        }
        boolean allChecksPassed = true;
        boolean currentCheckPassed = true;
        for (CircuitCheck check : checks) {
            if (check.isActive()) {
                currentCheckPassed = check.test(circuit);
            }
            if (!currentCheckPassed) {
                allChecksPassed = false;
            }
        }
        e.setChecksPassed(allChecksPassed);
        for (ModelListener l : listeners) {
            l.checksStopped(e);
        }
    }

    /**
     * Start the simulation on the current circuit. The starting elements are registered at the clock and compute their
     * output.
     */
    public void startSimulation() {
        if (simIsRunning) {
            throw new IllegalStateException("End running Simulation first!");
        }
        simIsRunning = true;
        for (ModelListener l : listeners) {
            l.simulationStarted(new ModelEvent());
        }
        for (Module m : circuit.getStartingModules()) {
            clock.addListener(m);
        }
        clock.startSimulation();
        new Thread(clock).start();
    }

    /**
     * Stops the simulation on the current circuit.
     */
    public void stopSimulation() {
        clock.stopSimulation();
        if (paused) {
            unpause();
        }
    }

    /**
     * Toggle state of given module (if possible).
     * 
     * @param m
     *            Module to toggle
     */
    public void toggleModule(Module m) {
        if (m instanceof ImpulseGenerator) {
            ImpulseGenerator imp = (ImpulseGenerator) m;
            imp.toggleState();
            if (imp.getState()) {
                activeImps.add(imp);
            } else {
                activeImps.remove(imp);
            }
        }
        notifyForChangedElems();
    }

    /**
     * Adapts the given Elements to a Grid with given Size.
     * 
     * @param gridSize
     *            int the Size of a Grid-Cell.
     * @param elementsToAdapt
     *            Set<Element> the elements to be adapted.
     */
    private void adaptToGrid(int gridSize, Set<Element> elementsToAdapt) {
        for (Element element : elementsToAdapt) {
            if (element instanceof Module) {
                Module m = (Module) element;
                Point p = m.getRectangle().getLocation();
                Point mp = new Point(p);
                p.x = (p.x % gridSize);
                p.y = (p.y % gridSize);
                m.getRectangle().setLocation(mp.x - p.x, mp.y - p.y);
                // ports auch bewegen
                for (Port port : m.getInPorts()) {
                    port.getRectangle().setLocation(port.getRectangle().getLocation().x - p.x,
                            port.getRectangle().getLocation().y - p.y);
                }
                for (Port port : m.getOutPorts()) {
                    port.getRectangle().setLocation(port.getRectangle().getLocation().x - p.x,
                            port.getRectangle().getLocation().y - p.y);
                }
            }
        }
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
     * Get the extension of a file.
     * 
     * @param f
     *            File to get extension from
     * @return Extension of given file
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
     * Get all modules intersecting a rectangle.
     * 
     * @param rect
     *            Rectangle containing the x- and y-coordinate
     * @return Set of Modules intersecting the given location
     */
    private Set<Module> getModsAt(Rectangle rect) {
        Set<Module> modsAt = new HashSet<Module>();
        for (Module m : circuit.getModules()) {
            if (m.getRectangle().intersects(rect)) {
                modsAt.add(m);
            }
        }
        return modsAt;
    }

    /**
     * Get all selected elements from the main circuit.
     * 
     * @return Set<Element> containing, oh the magic, all selected elements
     */
    private Set<Element> getSelectedElements() {
        Set<Element> selectitt = new HashSet<Element>();
        for (Element e : getElements()) {
            if (e.isSelected()) {
                selectitt.add(e);
            }
        }
        return selectitt;
    }

    /**
     * Loads or reloads the List containing the custom-circuits.
     */
    private void loadCustomList() {
        // search PATH for circuits, non-recursive.
        File dir = new File(CIRCUIT_PATH);
        LOG.debug("Load custom circuits: " + dir.getAbsolutePath());
        Importer importer = new SEPAFImporter();
        importer.setFactory(factory);
        importer.setErrorHandler(importExportErrorHandler);
        Map<String, String> formats = importer.getFileFormats();
        for (File f : dir.listFiles()) {
            if (f.isFile() && f.canRead() && getFileExtension(f) != null) {
                if (formats.containsKey(getFileExtension(f))) {
                    importer.setFile(f);
                    importExportErrorMessages = new LinkedList<String>();
                    importer.setExternalCircuitSource(externalCircuitSource);
                    boolean importSuccess = false;
                    try {
                        importSuccess = importer.importCircuit();
                        if (importSuccess) {
                            viewModules.add(new ViewModule(f.getName(), null, f.getName()));
                        } else {
                            LOG.warn("File import failed! File: " + f.getAbsolutePath());
                            StringBuilder errorMsgBuilder = new StringBuilder();
                            errorMsgBuilder.append(f.getAbsolutePath());
                            errorMsgBuilder.append(":\n");
                            for (String msg : importExportErrorMessages) {
                                errorMsgBuilder.append(msg);
                                errorMsgBuilder.append("\n");
                            }
                            ModelEvent e = new ModelEvent();
                            e.setMessage(errorMsgBuilder.toString());
                            for (ModelListener l : listeners) {
                                l.importCustomCircuitFailed(e);
                            }
                        }

                    } catch (RecursionException e1) {
                        ModelEvent e2 = new ModelEvent();
                        e2.setMessage("Starting file was: " + f.getAbsolutePath() + "\n" + e1.getLocalizedMessage());
                        for (ModelListener l : listeners) {
                            l.importCustomCircuitFailed(e2);
                        }
                    }
                }
            }
        }
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
        Rectangle r = new Rectangle(module.getRectangle());
        r.setLocation(r.x - p.x, r.y - p.y);
        for (Element e : circuit.getElements()) {
            if (e instanceof Module && ((Module) e).getRectangle().intersects(r) && e != module) {
                return false;
            }
        }
        // check for negative coords
        if (r.x <= 1 || r.y <= 1) {
            return false;
        }
        Point pr = module.getRectangle().getLocation();
        module.getRectangle().setLocation(pr.x - p.x, pr.y - p.y);
        module.setRectangle(r);
        // ports auch bewegen
        for (Port port : module.getInPorts()) {
            port.getRectangle().setLocation(port.getRectangle().getLocation().x - p.x,
                    port.getRectangle().getLocation().y - p.y);
        }
        for (Port port : module.getOutPorts()) {
            port.getRectangle().setLocation(port.getRectangle().getLocation().x - p.x,
                    port.getRectangle().getLocation().y - p.y);
        }
        notifyForChangedElems();
        dirty = true;
        return true;
    }

    /**
     * Import a Circuit and all its elements from a file.
     * 
     * @param file
     *            File to import Circuit from
     * @param depth
     *            Recursive Depth to start with. This number should increase if recursively called.
     * @return Circuit created by parsing given file. <b>Note:</b> May be null
     * @throws RecursionException
     *             Throws a RecursionException, should only be caught if it's the highest level (last step at the model
     *             layer), internals should throw this, like at externalCircuitSource where this is needed to detect
     *             recursion.
     */
    private Circuit importFromFile(File file, int depth) throws RecursionException {
        if (file == null) {
            throw new IllegalArgumentException();
        }
        Circuit m = null;
        String ext = getFileExtension(file);
        if (importers.containsKey(ext)) {
            Importer im = importers.get(ext);
            im.setFile(file);
            im.setCurrentRecursiveDepth(depth);
            importExportErrorMessages = new LinkedList<String>();
            im.setErrorHandler(importExportErrorHandler);
            im.setExternalCircuitSource(externalCircuitSource);
            if (im.importCircuit()) {
                m = im.getCircuit();
                if (m == null) {
                    LOG.error("circuit from " + file.getAbsolutePath() + " was null");
                }
            } else {
                LOG.warn("File import failed! File: " + file.getAbsolutePath());
                StringBuilder errorMsgBuilder = new StringBuilder();
                for (String msg : importExportErrorMessages) {
                    errorMsgBuilder.append(msg);
                    errorMsgBuilder.append("\n");
                }
                ModelEvent e = new ModelEvent();
                e.setMessage(errorMsgBuilder.toString());
                for (ModelListener l : listeners) {
                    l.importFailed(e);
                }
            }
        }
        return m;
    }

    /**
     * Initialize list of available checks.
     */
    private void initChecks() {
        checks.add(new CountCheck());
        checks.add(new FeedbackCheck());
        checks.add(new IllegalConnectionCheck());
        checks.add(new OrphanCheck());
        checks.add(new SinkCheck());
        checks.add(new SourceCheck());
    }

    /**
     * Initializes all importers.
     */
    private void initExporters() {
        // Can be extended to multiple importers by merging maps. No need for it right now.
        exporters.clear();
        exportFormats.clear();
        Exporter sepafExporter = new SEPAFExporter();
        Exporter drawExporter = new DrawExporter();
        Map<String, String> sepafFormats = sepafExporter.getFileFormats();
        for (Map.Entry<String, String> format : sepafFormats.entrySet()) {
            exporters.put(format.getKey(), sepafExporter);
            exportFormats.put(format.getKey(), format.getValue());
        }
        Map<String, String> drawFormats = drawExporter.getFileFormats();
        for (Map.Entry<String, String> format : drawFormats.entrySet()) {
            exporters.put(format.getKey(), drawExporter);
            exportFormats.put(format.getKey(), format.getValue());
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
        sepafImporter.setFactory(factory);
        Map<String, String> sepafFormats = sepafImporter.getFileFormats();
        for (Map.Entry<String, String> format : sepafFormats.entrySet()) {
            importers.put(format.getKey(), sepafImporter);
        }
        importFormats = sepafFormats;
    }

    /**
     * Return filename without file extension.
     * 
     * @param fileName
     *            String containing the file's name
     * @return filename without file extension
     */
    private String getFileWithOutExtension(String fileName) {
        if (fileName == null) {
            return "";
        }
        int pos = fileName.lastIndexOf(".");
        if (pos == -1) {
            return fileName;
        }
        return fileName.substring(0, pos);
    }
}

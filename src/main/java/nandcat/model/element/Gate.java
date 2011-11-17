package nandcat.model.element;

import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import nandcat.model.Clock;

/**
 * Gate class. Representing the minimum consent between the basic gates.
 */
public abstract class Gate implements Module {

    /**
     * Default serial version uid.
     */
    private static final long serialVersionUID = 1L;

    /**
     * String defining the Gates' name.
     */
    private String name;

    /**
     * List containing outgoing ports.
     */
    private List<Port> outPorts;

    /**
     * List containing incoming ports.
     */
    private List<Port> inPorts;

    /**
     * Rectangle representing the gate's shape.
     */
    private Rectangle rectangle;

    /**
     * Selection state of gate.
     */
    private boolean selected;

    /**
     * Advanced constructor. Creates new Gate with inPorts incoming and outPorts outgoing Ports.
     * 
     * @param inPorts
     *            int <b>positive</b> number of inPorts to append
     * @param outPorts
     *            int <b>positive</b> number of outPorts to append
     */
    protected Gate(int inPorts, int outPorts) {
        if (!isValidInBoundary(inPorts) || !isValidOutBoundary(outPorts)) {
            throw new IllegalArgumentException("Illegal amount of in or out ports.");
        }
        rectangle = new Rectangle();
        createPorts(inPorts, outPorts);
    }

    /**
     * Trigger calculation of new output values.
     * 
     * @param clock
     *            Clock that ticked
     */
    protected abstract void calculate(Clock clock);

    /**
     * Return Elements connected to outgoing port(s).
     * 
     * @return List containing the Next Elements
     */
    List<Module> getNextElements() {
        List<Module> result = new LinkedList<Module>();
        for (Port p : getOutPorts()) {
            if (p.getConnection() != null) {
                result.add(p.getConnection().getNextModule());
            }
        }
        return result;
    }

    /**
     * Set Gates' name.
     * 
     * @param name
     *            String that specifies the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return Gates' name.
     * 
     * @return String containing gates' name
     */
    public String getName() {
        return this.name;
    }

    /**
     * {@inheritDoc}
     */
    public void clockTicked(Clock clock) {
        calculate(clock);
    }

    /**
     * Return out ports.
     * 
     * @return List containing all outgoing ports
     */
    public List<Port> getOutPorts() {
        return outPorts;
    }

    /**
     * Return in ports.
     * 
     * @return List containing all incoming ports
     */
    public List<Port> getInPorts() {
        return inPorts;
    }

    /**
     * Check if given number of ports contains a legal number of outPorts.
     * 
     * @param outPorts
     *            number of ports to check for legality
     * @return given outPorts are within our boundaries
     */
    protected abstract boolean isValidOutBoundary(int outPorts);

    /**
     * Check if given number of ports contains a legal number of inPorts.
     * 
     * @param inPorts
     *            number of ports to check for legality
     * @return given inPorts are within our boundaries
     */
    protected abstract boolean isValidInBoundary(int inPorts);

    /**
     * Set arbitrary (positive) number of <b>new</b> in and out ports to append. Note that any existing ports will be
     * lost. This method may only be called <b>after</b> the module's boundary has been set up.
     * 
     * @param inPorts
     *            int number of inports to append
     * @param outPorts
     *            int number of outports to append
     */
    private void createPorts(int inPorts, int outPorts) {
        LinkedList<Port> ports = new LinkedList<Port>();
        this.inPorts = ports;
        for (int i = 0; i < inPorts; i++) {
            ports.add(new Port(this));
        }
        ports = new LinkedList<Port>();
        this.outPorts = ports;
        for (int i = 0; i < outPorts; i++) {
            ports.add(new Port(this));
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
     * {@inheritDoc}
     */
    public void setSelected(boolean b) {
        selected = b;
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
    public String toString() {
        String x = this.getClass().getSimpleName() + "(" + getRectangle().x + "/" + getRectangle().y + ") ";
        x += "(In) ";
        for (Port in : inPorts) {
            x += in + ", ";
        }
        x += "(Out) ";
        for (Port out : outPorts) {
            x += out + ", ";
        }
        return x;
    }
}

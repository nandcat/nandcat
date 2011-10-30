package nandcat.view.elementdrawer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import nandcat.model.element.AndGate;
import nandcat.model.element.Circuit;
import nandcat.model.element.Connection;
import nandcat.model.element.Element;
import nandcat.model.element.FlipFlop;
import nandcat.model.element.ImpulseGenerator;
import nandcat.model.element.Lamp;
import nandcat.model.element.NotGate;
import nandcat.model.element.OrGate;
import nandcat.view.StandardElementDrawer;

public class ElementDrawerViewer extends JFrame {

    private MyWorkbench workbench;

    private StandardElementDrawer elementDrawer;

    private Set<Element> elements = new HashSet<Element>();

    /**
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                ElementDrawerViewer view = new ElementDrawerViewer();
                view.initUI();
                view.setVisible(true);
            }
        });
    }

    public ElementDrawerViewer() {
        this.setSize(new Dimension(300, 400));
        this.setLocationRelativeTo(null);
        setTitle("ElementDrawer - Viewer");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void initUI() {
        elementDrawer = new StandardElementDrawer();
        createElementDrawerElements();
        workbench = new MyWorkbench(this);
        this.add(workbench);
    }

    private void createElementDrawerElements() {
        AndGate andGate1 = new AndGate();
        andGate1.setRectangle(new Rectangle(5, 5, 60, 40));
        elements.add(andGate1);
        AndGate andGate2 = new AndGate();
        andGate2.getInPorts().get(0).setState(true, null);
        andGate2.setRectangle(new Rectangle(100, 5, 60, 40));
        andGate2.setName("Meine Annotation");
        elements.add(andGate2);
        AndGate andGate3 = new AndGate();
        andGate3.getInPorts().get(0).setState(true, null);
        andGate3.getInPorts().get(1).setState(true, null);
        andGate3.getOutPorts().get(0).setState(true, null);
        andGate3.setSelected(true);
        andGate3.setRectangle(new Rectangle(200, 5, 60, 40));
        elements.add(andGate3);
        // OR Gates
        OrGate orGate1 = new OrGate();
        orGate1.setRectangle(new Rectangle(5, 80, 60, 40));
        orGate1.setName("Meine Annotation");
        elements.add(orGate1);
        OrGate orGate2 = new OrGate();
        orGate2.getInPorts().get(0).setState(true, null);
        orGate2.setRectangle(new Rectangle(100, 80, 60, 40));
        orGate2.setName("Meine Annotation");
        elements.add(orGate2);
        OrGate orGate3 = new OrGate();
        orGate3.getInPorts().get(0).setState(true, null);
        orGate3.getInPorts().get(1).setState(true, null);
        orGate3.getOutPorts().get(0).setState(true, null);
        orGate3.setSelected(true);
        orGate3.setRectangle(new Rectangle(200, 80, 60, 40));
        elements.add(orGate3);
        // OrGate orGate4 = new OrGate(2,5);
        // orGate4.getInPorts().get(0).setState(true, null);
        // orGate4.getInPorts().get(1).setState(true, null);
        // orGate4.getOutPorts().get(0).setState(true, null);
        // orGate4.setRectangle(new Rectangle(300, 80, 60, 40));
        // elements.add(orGate4);
        // Connection between and2 and or3
        Connection con1 = new Connection(orGate3.getInPorts().get(0), andGate2.getOutPorts().get(0));
        orGate3.getInPorts().get(0).setConnection(con1);
        andGate3.getOutPorts().get(0).setConnection(con1);
        elements.add(con1);
        Lamp lamp1 = new Lamp();
        lamp1.setRectangle(new Rectangle(5, 160, 40, 40));
        lamp1.setName("Eine Lampe");
        elements.add(lamp1);
        Lamp lamp2 = new Lamp();
        lamp2.getInPorts().get(0).setState(true, null);
        lamp2.setRectangle(new Rectangle(100, 160, 40, 40));
        elements.add(lamp2);
        Lamp lamp3 = new Lamp();
        lamp3.getInPorts().get(0).setState(true, null);
        lamp3.setSelected(true);
        lamp3.setRectangle(new Rectangle(200, 160, 40, 40));
        elements.add(lamp3);
        NotGate notGate1 = new NotGate();
        notGate1.setRectangle(new Rectangle(5, 240, 60, 40));
        elements.add(notGate1);
        NotGate notGate2 = new NotGate();
        notGate2.getInPorts().get(0).setState(true, null);
        notGate2.setRectangle(new Rectangle(100, 240, 60, 40));
        notGate2.setName("Ein NotGate");
        elements.add(notGate2);
        NotGate notGate3 = new NotGate();
        notGate3.getOutPorts().get(0).setState(true, null);
        notGate3.setSelected(true);
        notGate3.setRectangle(new Rectangle(200, 240, 60, 40));
        elements.add(notGate3);
        // FlipFlop flipFlop1 = new FlipFlop();
        // flipFlop1.setRectangle(new Rectangle(5, 320, 60, 40));
        // elements.add(flipFlop1);
        // Circuit circuit1 = new Circuit();
        // circuit1.getOutPorts().add(new Port(circuit1));
        // circuit1.getOutPorts().get(0).setState(true, null);
        // circuit1.setRectangle(new Rectangle(5, 400, 60, 40));
        // elements.add(circuit1);
        // IdentityGate identityGate1 = new IdentityGate();
        // identityGate1.setRectangle(new Rectangle(5, 480, 60, 40));
        // elements.add(identityGate1);
        ImpulseGenerator ig1 = new ImpulseGenerator(0);
        ig1.setRectangle(new Rectangle(5, 320, 60, 40));
        ig1.setName("Ein Schalter");
        elements.add(ig1);
        ImpulseGenerator ig2 = new ImpulseGenerator(0);
        ig2.toggleState();
        ig2.getOutPorts().get(0).setState(true, null);
        ig2.setRectangle(new Rectangle(100, 320, 60, 40));
        elements.add(ig2);
        ImpulseGenerator ig3 = new ImpulseGenerator(200);
        ig3.toggleState();
        ig3.getOutPorts().get(0).setState(true, null);
        ig3.setSelected(true);
        ig3.setRectangle(new Rectangle(200, 320, 60, 40));
        elements.add(ig3);
        // FlipFlop ff1 = new FlipFlop();
        // ff1.setRectangle(new Rectangle(200, 400, 60, 40));
        // elements.add(ff1);
    }

    public void drawElements(Graphics g) {
        elementDrawer.setGraphics(g);
        for (Element element : elements) {
            if (element instanceof AndGate)
                elementDrawer.draw((AndGate) element);
            if (element instanceof OrGate)
                elementDrawer.draw((OrGate) element);
            if (element instanceof Lamp)
                elementDrawer.draw((Lamp) element);
            if (element instanceof NotGate)
                elementDrawer.draw((NotGate) element);
            if (element instanceof Circuit)
                elementDrawer.draw((Circuit) element);
            if (element instanceof FlipFlop)
                elementDrawer.draw((FlipFlop) element);
            if (element instanceof ImpulseGenerator)
                elementDrawer.draw((ImpulseGenerator) element);
            if (element instanceof Connection)
                elementDrawer.draw((Connection) element);
        }
    }

    public class MyWorkbench extends JPanel {

        private ElementDrawerViewer viewer;

        public MyWorkbench(ElementDrawerViewer viewer) {
            setSize(new Dimension(300, 300));
            setBackground(Color.white);
            this.viewer = viewer;
        }

        public void paint(Graphics g) {
            super.paint(g);
            viewer.drawElements(g);
        }
    }
}

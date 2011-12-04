package nandcat.view.elementdrawer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import nandcat.NandcatTest;
import nandcat.model.ModelElementDefaults;
import nandcat.model.element.AndGate;
import nandcat.model.element.Circuit;
import nandcat.model.element.Connection;
import nandcat.model.element.Element;
import nandcat.model.element.FlipFlop;
import nandcat.model.element.ImpulseGenerator;
import nandcat.model.element.Lamp;
import nandcat.model.element.NotGate;
import nandcat.model.element.OrGate;
import nandcat.model.element.factory.ModuleBuilderFactory;
import nandcat.view.StandardElementDrawer;
import org.junit.Before;

public class ElementDrawerViewer extends JFrame {

    private MyWorkbench workbench;

    private StandardElementDrawer elementDrawer;

    private Circuit mainCircuit;

    private ModuleBuilderFactory factory;

    @Before
    public void setup() throws IOException {
        factory = new ModuleBuilderFactory();
        factory.setDefaults(new ModelElementDefaults());
    }

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
        this.setSize(new Dimension(300, 500));
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
        mainCircuit = (Circuit) factory.getCircuitBuilder().build();
        mainCircuit.addModule(factory.getAndGateBuilder().setLocation(new Point(5, 5)).build());
        mainCircuit.addModule(factory.getAndGateBuilder().setLocation(new Point(100, 5))
                .setAnnotation("Meine Annotation").build());
        AndGate andGate3 = (AndGate) factory.getAndGateBuilder().setLocation(new Point(200, 5))
                .setAnnotation("Meine Annotation").build();
        andGate3.getInPorts().get(0).setState(true, null);
        andGate3.getInPorts().get(1).setState(true, null);
        andGate3.getOutPorts().get(0).setState(true, null);
        andGate3.setSelected(true);
        mainCircuit.addModule(andGate3);

        // OR Gates
        mainCircuit.addModule(factory.getOrGateBuilder().setLocation(new Point(5, 80))
                .setAnnotation("Meine Annotation").build());

        OrGate orGate2 = (OrGate) factory.getOrGateBuilder().setLocation(new Point(100, 80))
                .setAnnotation("Meine Annotation").build();
        orGate2.getInPorts().get(0).setState(true, null);
        mainCircuit.addModule(orGate2);

        OrGate orGate3 = (OrGate) factory.getOrGateBuilder().setLocation(new Point(200, 80))
                .setAnnotation("Meine Annotation").build();
        orGate3.getInPorts().get(0).setState(true, null);
        orGate3.getInPorts().get(1).setState(true, null);
        orGate3.getOutPorts().get(0).setState(true, null);
        orGate3.setSelected(true);
        mainCircuit.addModule(orGate3);
        // OrGate orGate4 = new OrGate(2, 2);
        // orGate4.getInPorts().get(0).setState(true, null);
        // orGate4.getInPorts().get(1).setState(true, null);
        // orGate4.getOutPorts().get(0).setState(true, null);
        // orGate4.setRectangle(new Rectangle(300, 80, 60, 40));
        // elements.add(orGate4);
        // Connection between and2 and or3
        // Connection con1 = new Connection(orGate3.getInPorts().get(0), andGate2.getOutPorts().get(0));
        mainCircuit.addConnection(orGate2.getOutPorts().get(0), orGate3.getInPorts().get(0));

        mainCircuit.addModule(factory.getLampBuilder().setLocation(new Point(5, 160)).setAnnotation("Eine Lampe")
                .build());

        Lamp lamp2 = (Lamp) factory.getLampBuilder().setLocation(new Point(100, 160)).setAnnotation("Eine Lampe")
                .build();
        lamp2.getInPorts().get(0).setState(true, null);
        mainCircuit.addModule(lamp2);

        Lamp lamp3 = (Lamp) factory.getLampBuilder().setLocation(new Point(200, 160)).setAnnotation("Eine Lampe")
                .build();
        lamp3.getInPorts().get(0).setState(true, null);
        lamp3.setSelected(true);
        mainCircuit.addModule(lamp3);

        NotGate notGate1 = (NotGate) factory.getNotGateBuilder().setLocation(new Point(5, 240))
                .setAnnotation("Ein Not").build();
        mainCircuit.addModule(notGate1);

        NotGate notGate2 = (NotGate) factory.getNotGateBuilder().setLocation(new Point(100, 240))
                .setAnnotation("Ein Not").build();
        notGate2.getInPorts().get(0).setState(true, null);
        mainCircuit.addModule(notGate2);

        NotGate notGate3 = (NotGate) factory.getNotGateBuilder().setLocation(new Point(200, 240))
                .setAnnotation("Ein Not").build();
        notGate3.getOutPorts().get(0).setState(true, null);
        notGate3.setSelected(true);
        mainCircuit.addModule(notGate3);
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
        ImpulseGenerator ig1 = (ImpulseGenerator) factory.getSwitchBuilder().setLocation(new Point(5, 320))
                .setAnnotation("Ein Schalter").build();
        mainCircuit.addModule(ig1);

        ImpulseGenerator ig2 = (ImpulseGenerator) factory.getSwitchBuilder().setLocation(new Point(100, 320))
                .setAnnotation("Ein Schalter").build();
        ig2.toggleState();
        ig2.getOutPorts().get(0).setState(true, null);
        mainCircuit.addModule(ig2);

        ImpulseGenerator ig3 = (ImpulseGenerator) factory.getClockBuilder().setLocation(new Point(200, 320))
                .setAnnotation("Eine Clock").build();
        ig3.toggleState();
        ig3.getOutPorts().get(0).setState(true, null);
        ig3.setSelected(true);
        mainCircuit.addModule(ig3);

        FlipFlop ff1 = (FlipFlop) factory.getFlipFlopBuilder().setLocation(new Point(200, 400)).setAnnotation("Ein FF")
                .build();
        mainCircuit.addModule(ff1);

        Circuit circuit1 = (Circuit) factory.getCircuitBuilder().build();
        circuit1.addModule(factory.getAndGateBuilder().build());
        circuit1.addModule(factory.getOrGateBuilder().build());
        circuit1.setRectangle(new Rectangle(5, 400, 60, 40));
        BufferedImage symbol;
        try {
            symbol = ImageIO.read(NandcatTest.class.getResourceAsStream("../images/cat.png"));
            circuit1.setSymbol(symbol);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mainCircuit.addModule(circuit1);
    }

    public void drawElements(Graphics g) {
        elementDrawer.setGraphics(g);
        LinkedList<Connection> cons = new LinkedList<Connection>();
        for (Element element : mainCircuit.getElements()) {
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
                cons.add((Connection) element);
        }

        for (Connection connection : cons) {
            elementDrawer.draw(connection);
        }
    }

    public class MyWorkbench extends JPanel {

        private ElementDrawerViewer viewer;

        public MyWorkbench(ElementDrawerViewer viewer) {
            // setSize(new Dimension(300, 300));
            setBackground(Color.white);
            this.viewer = viewer;
        }

        public void paint(Graphics g) {
            super.paint(g);
            viewer.drawElements(g);
        }
    }
}

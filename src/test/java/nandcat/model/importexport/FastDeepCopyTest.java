package nandcat.model.importexport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import nandcat.model.ModelElementDefaults;
import nandcat.model.element.AndGate;
import nandcat.model.element.Circuit;
import nandcat.model.element.Connection;
import nandcat.model.element.Element;
import nandcat.model.element.FlipFlop;
import nandcat.model.element.IdentityGate;
import nandcat.model.element.ImpulseGenerator;
import nandcat.model.element.Lamp;
import nandcat.model.element.NotGate;
import nandcat.model.element.OrGate;
import nandcat.model.element.factory.ModuleBuilderFactory;
import nandcat.model.importexport.sepaf.FastDeepCopy;
import org.junit.Before;
import org.junit.Test;

public class FastDeepCopyTest {

    private ModuleBuilderFactory factory;

    @Before
    public void setUp() {
        factory = new ModuleBuilderFactory();
        factory.setDefaults(new ModelElementDefaults());
    }

    @Test
    public void testSimple() {
        Circuit obj = (Circuit) factory.getCircuitBuilder().build();
        obj.setName("name");
        Circuit copy = (Circuit) FastDeepCopy.copy(obj);
        assertTrue(obj.getName() != null);
        assertEquals(obj.getName(), copy.getName());
    }

    private Circuit createCircuit() {
        Circuit c = (Circuit) factory.getCircuitBuilder().build();
        AndGate andGate = (AndGate) factory.getAndGateBuilder().build();
        andGate.setName("andgate");
        OrGate orGate = new OrGate();
        orGate.setName("orgate");

        c.addModule(andGate);
        c.addModule(orGate);
        c.addConnection(andGate.getOutPorts().get(0), orGate.getInPorts().get(0));

        return c;
    }

    private boolean verifyCircuit(Circuit c) {
        boolean andGateFound = false;
        boolean orGateFound = false;
        boolean connectionFound = false;
        AndGate andGate = null;
        OrGate orGate = null;
        Object connectionGate1 = null;
        Object connectionGate2 = null;
        for (Element element : c.getElements()) {
            if (element instanceof AndGate) {
                if (((AndGate) element).getName().equals("andgate")) {
                    if (!andGateFound) {
                        andGateFound = true;
                        andGate = (AndGate) element;
                    } else {
                        return false;
                    }
                }
            } else if (element instanceof OrGate) {
                if (((OrGate) element).getName().equals("orgate")) {
                    if (!orGateFound) {
                        orGateFound = true;
                        orGate = (OrGate) element;
                    } else {
                        return false;
                    }
                }
            } else if (element instanceof Connection) {
                if (!connectionFound) {
                    connectionFound = true;
                    connectionGate1 = ((Connection) element).getPreviousModule();
                    connectionGate2 = ((Connection) element).getNextModule();
                } else {
                    return false;
                }
            }
        }
        if (!orGateFound || !andGateFound || !connectionFound) {
            // nicht alles gefunden
            return false;
        }
        if (connectionGate1 != andGate || connectionGate2 != orGate) {
            // gates nicht richtig verbunden
            return false;
        }
        return true;
    }

    @Test
    public void testDeepCircuit() {
        Circuit c1 = createCircuit();
        Circuit c2 = createCircuit();
        c1.addModule(c2);

        // Test reference circuit.
        assertTrue(verifyCircuit(c1));
        for (Element e : c1.getElements()) {
            if (e instanceof Circuit) {
                assertTrue(verifyCircuit((Circuit) e));
            }
        }

        // Test cloned
        Circuit newC = (Circuit) FastDeepCopy.copy(c1);
        assertTrue(verifyCircuit(newC));
        for (Element e : newC.getElements()) {
            if (e instanceof Circuit) {
                assertTrue(verifyCircuit((Circuit) e));
            }
        }
    }

    @Test
    public void testAllElements() {
        FastDeepCopy.copy((AndGate) factory.getAndGateBuilder().build());
        FastDeepCopy.copy((OrGate) factory.getOrGateBuilder().build());
        FastDeepCopy.copy((FlipFlop) factory.getFlipFlopBuilder().build());
        FastDeepCopy.copy((IdentityGate) factory.getIdentityGateBuilder().build());
        FastDeepCopy.copy((ImpulseGenerator) factory.getClockBuilder().setFrequency(10).build());
        FastDeepCopy.copy((ImpulseGenerator) factory.getSwitchBuilder().build());
        FastDeepCopy.copy((Lamp) factory.getLampBuilder().build());
        FastDeepCopy.copy((NotGate) factory.getNotGateBuilder().build());
        AndGate gate = (AndGate) factory.getAndGateBuilder().build();
        OrGate gate2 = (OrGate) factory.getOrGateBuilder().build();
        FastDeepCopy.copy(gate.getInPorts().get(0));
        FastDeepCopy.copy(new Connection(gate.getOutPorts().get(0), gate2.getInPorts().get(0)));
        // FastDeepCopy.copy(new Connection(null, null));
    }
}

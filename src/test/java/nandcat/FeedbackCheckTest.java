package nandcat;

import static org.junit.Assert.fail;
import java.awt.Point;
import java.util.List;
import nandcat.model.Model;
import nandcat.model.ModelElementDefaults;
import nandcat.model.check.CheckEvent;
import nandcat.model.check.CheckListener;
import nandcat.model.check.CircuitCheck;
import nandcat.model.check.FeedbackCheck;
import nandcat.model.element.NotGate;
import nandcat.model.element.Port;
import nandcat.model.element.factory.ModuleBuilderFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * FeedbackCheckTest.
 * 
 * Tests a circuit with an not-gate self connected to its input to fail the feedback check.
 */
public class FeedbackCheckTest {

    /**
     * Model used for testing.
     */
    private Model model;

    /**
     * Sets the model and components up.
     * 
     * @throws Exception
     *             Any Exception should fail the test.
     */
    @Ignore
    @Before
    public void setUp() throws Exception {
        model = new Model();
        ModuleBuilderFactory factory = new ModuleBuilderFactory();
        factory.setDefaults(new ModelElementDefaults());
        NotGate notgate = (NotGate) factory.getNotGateBuilder().setOutPorts(1).build();
        List<Port> inPorts = notgate.getInPorts();
        List<Port> outPorts = notgate.getOutPorts();
        model.addConnection(inPorts.get(0), outPorts.get(0));
        model.addModule(notgate, new Point(1, 1));
    }

    /**
     * Tests if FeedbackCheck fails.
     */
    @Ignore
    @Test
    public void test() {
        CircuitCheck check = null;
        for (CircuitCheck c : model.getChecks()) {
            if (c instanceof FeedbackCheck) {
                check = c;
            }
        }
        if (check == null) {
            fail("FeedbackCheck not found in model");
        }
        check.addListener(new CheckListener() {

            public void checkStarted() {
                System.out.println("Check started");
            }

            public void checkChanged(CheckEvent e) {
                if (e.getState() == CheckEvent.State.SUCCEEDED) {
                    fail("Check succeeded.");
                }
            }
        });
        model.startChecks();
    }
}

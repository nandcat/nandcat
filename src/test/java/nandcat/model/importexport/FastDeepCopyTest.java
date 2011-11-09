package nandcat.model.importexport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import nandcat.model.element.Circuit;
import nandcat.model.importexport.sepaf.FastDeepCopy;
import org.junit.Test;

public class FastDeepCopyTest {

    @Test
    public void testSimple() {
        Circuit obj = new Circuit();
        obj.setName("name");
        Circuit copy = (Circuit) FastDeepCopy.copy(obj);
        assertTrue(obj.getName() != null);
        assertEquals(obj.getName(), copy.getName());
    }

}

package nandcat;

import static org.junit.Assert.assertEquals;
import java.text.DateFormat;
import java.util.Date;
import nandcat.I18N.I18NBundle;
import org.junit.Test;

public class ResourcesTest {

    @Test
    public void testMessageArguments() {
        I18NBundle bundle = I18N.getBundle("TestRes");
        assertEquals("Simple String", bundle.getString("simpletest"));
        Date date = new Date();
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, I18N.getLocale());
        String timeStr = timeFormat.format(date);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG, I18N.getLocale());
        String dateStr = dateFormat.format(date);
        assertEquals("At " + timeStr + " on " + dateStr + ", we detected 7 spaceships on the planet Mars.",
                bundle.getString("fancytest", "Mars", new Integer(7), date));
    }
}

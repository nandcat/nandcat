package nandcat.view.elementdrawer;

import java.awt.Color;
import nandcat.ReflectionUtil;
import nandcat.view.StandardElementDrawer;
import org.junit.Before;

/**
 * Base for element drawer tests.
 * 
 */
public abstract class AbstractElementDrawerTest {

    int PORT_MARGIN_LEFT;

    int PORT_MARGIN_RIGHT;

    int PORT_MARGIN_TOP;

    int PORT_DIAMETER = 2;

    StandardElementDrawer drawer;

    Color PORT_COLOR_ACTIVE;

    Color PORT_COLOR_DEFAULT;

    int PORT_MARGIN_BOTTOM;

    Color CONNECTION_COLOR_DEFAULT;

    Color CONNECTION_COLOR_ACTIVE;

    Color GATE_COLOR;

    String LABEL_ANDGATE;

    String LABEL_ORGATE;

    String LABEL_NOTGATE;

    Color LABEL_COLOR;

    Color IG_COLOR_DEFAULT = Color.WHITE;

    Color IG_COLOR_ACTIVE = Color.YELLOW;

    Color LAMP_COLOR_ACTIVE;

    String LABEL_IDENTITYGATE;

    Color GATE_COLOR_SELECTED;

    Color ANNOTATION_COLOR;

    Color LAMP_COLOR_DEFAULT;

    void applyFields() throws Exception {
        PORT_COLOR_ACTIVE = (Color) ReflectionUtil.getPrivateField(StandardElementDrawer.class, drawer,
                "PORT_COLOR_ACTIVE");
        PORT_COLOR_DEFAULT = (Color) ReflectionUtil.getPrivateField(StandardElementDrawer.class, drawer,
                "PORT_COLOR_DEFAULT");
        PORT_MARGIN_BOTTOM = (Integer) ReflectionUtil.getPrivateField(StandardElementDrawer.class, drawer,
                "PORT_MARGIN_BOTTOM");
        PORT_MARGIN_TOP = (Integer) ReflectionUtil.getPrivateField(StandardElementDrawer.class, drawer,
                "PORT_MARGIN_TOP");
        PORT_MARGIN_LEFT = (Integer) ReflectionUtil.getPrivateField(StandardElementDrawer.class, drawer,
                "PORT_MARGIN_LEFT");
        PORT_MARGIN_RIGHT = (Integer) ReflectionUtil.getPrivateField(StandardElementDrawer.class, drawer,
                "PORT_MARGIN_RIGHT");
        PORT_DIAMETER = (Integer) ReflectionUtil.getPrivateField(StandardElementDrawer.class, drawer, "PORT_DIAMETER");
        CONNECTION_COLOR_DEFAULT = (Color) ReflectionUtil.getPrivateField(StandardElementDrawer.class, drawer,
                "CONNECTION_COLOR_DEFAULT");
        CONNECTION_COLOR_ACTIVE = (Color) ReflectionUtil.getPrivateField(StandardElementDrawer.class, drawer,
                "CONNECTION_COLOR_ACTIVE");
        GATE_COLOR = (Color) ReflectionUtil.getPrivateField(StandardElementDrawer.class, drawer, "GATE_COLOR");
        LABEL_ORGATE = (String) ReflectionUtil.getPrivateField(StandardElementDrawer.class, drawer, "LABEL_ORGATE");
        LABEL_ANDGATE = (String) ReflectionUtil.getPrivateField(StandardElementDrawer.class, drawer, "LABEL_ANDGATE");
        LABEL_NOTGATE = (String) ReflectionUtil.getPrivateField(StandardElementDrawer.class, drawer, "LABEL_NOTGATE");
        LABEL_IDENTITYGATE = (String) ReflectionUtil.getPrivateField(StandardElementDrawer.class, drawer,
                "LABEL_IDENTITYGATE");
        LABEL_COLOR = (Color) ReflectionUtil.getPrivateField(StandardElementDrawer.class, drawer, "LABEL_COLOR");
        ANNOTATION_COLOR = (Color) ReflectionUtil.getPrivateField(StandardElementDrawer.class, drawer,
                "ANNOTATION_COLOR");
        IG_COLOR_DEFAULT = (Color) ReflectionUtil.getPrivateField(StandardElementDrawer.class, drawer,
                "IG_COLOR_DEFAULT");
        IG_COLOR_ACTIVE = (Color) ReflectionUtil
                .getPrivateField(StandardElementDrawer.class, drawer, "IG_COLOR_ACTIVE");
        LAMP_COLOR_ACTIVE = (Color) ReflectionUtil.getPrivateField(StandardElementDrawer.class, drawer,
                "LAMP_COLOR_ACTIVE");
        LAMP_COLOR_DEFAULT = (Color) ReflectionUtil.getPrivateField(StandardElementDrawer.class, drawer,
                "LAMP_COLOR_DEFAULT");
        GATE_COLOR_SELECTED = (Color) ReflectionUtil.getPrivateField(StandardElementDrawer.class, drawer,
                "GATE_COLOR_SELECTED");
    }

    /**
     * Create drawer and setup fields.
     * 
     * @throws Exception
     *             Fail on all exceptions.
     */
    @Before
    public void setUpDrawerAndFields() throws Exception {
        drawer = new StandardElementDrawer();
        applyFields();
    }
}

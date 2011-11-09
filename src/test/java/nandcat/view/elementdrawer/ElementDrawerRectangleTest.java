package nandcat.view.elementdrawer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

public class ElementDrawerRectangleTest extends AbstractElementDrawerTest {

    private Graphics graphicMock;

    private Rectangle rec;

    @Before
    public void setUp() {
        rec = new Rectangle();
        rec.x = 1;
        rec.y = 2;
        rec.width = 3;
        rec.height = 4;

        graphicMock = mock(Graphics.class);
        drawer.setGraphics(graphicMock);
    }

    @Test
    public void testDraw() {
        drawer.draw(rec);
        InOrder inOrder = inOrder(graphicMock);
        inOrder.verify(graphicMock).setColor(any(Color.class));
        inOrder.verify(graphicMock).drawRect(rec.x, rec.y, rec.width, rec.height);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDrawNullRectangle() {
        drawer.draw((Rectangle) null);
    }
}

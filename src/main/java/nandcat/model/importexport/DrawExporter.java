package nandcat.model.importexport;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import nandcat.model.element.AndGate;
import nandcat.model.element.Circuit;
import nandcat.model.element.Connection;
import nandcat.model.element.Element;
import nandcat.model.element.FlipFlop;
import nandcat.model.element.IdentityGate;
import nandcat.model.element.ImpulseGenerator;
import nandcat.model.element.Lamp;
import nandcat.model.element.Module;
import nandcat.model.element.NotGate;
import nandcat.model.element.OrGate;
import nandcat.view.ElementDrawer;
import org.apache.log4j.Logger;

/**
 * Draws an image using the given ElementDrawer.
 */
public class DrawExporter implements Exporter {

    /**
     * File format to export image as.
     */
    private static final String FILE_FORMAT = "png";

    /**
     * Margin used to separate border from image.
     */
    private static final int MARGIN = 10;

    /**
     * Image height threshold to throw warning.
     */
    private static final int WARN_HEIGHT_THRESHOLD = 720;

    /**
     * Image width threshold to throw warning.
     */
    private static final int WARN_WIDTH_THRESHOLD = 1280;

    /**
     * Class logger instance.
     */
    private static final Logger LOG = Logger.getLogger(DrawExporter.class);

    /**
     * File to write.
     */
    private File file = null;

    /**
     * Drawer to use.
     */
    private ElementDrawer drawer;

    /**
     * Circuit to draw.
     */
    private Circuit circuit;

    /**
     * Error handler called if an error occur.
     */
    private FormatErrorHandler errorHandler;

    /**
     * Map of supported file extensions connected with the format description.
     */
    private static final Map<String, String> SUPPORTED_FORMATS = new HashMap<String, String>();
    static {
        SUPPORTED_FORMATS.put(FILE_FORMAT, "Image");
    }

    /**
     * {@inheritDoc}
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Sets the element drawer to use.
     * 
     * @param drawer
     *            ElementDrawer.
     */
    public void setElementDrawer(ElementDrawer drawer) {
        this.drawer = drawer;
    }

    /**
     * Not used.
     * 
     * @param circuits
     *            Not Used.
     */
    public void setExternalCircuits(Map<String, String> circuits) {

    }

    /**
     * Calculates the last point to draw. The right bottom corner.
     * 
     * @param elements
     *            Elements to draw.
     * @return Point of right bottom corner.
     */
    private Dimension getRightBottom(List<Element> elements) {
        Dimension dim = new Dimension(0, 0);
        for (Element element : elements) {
            if (element instanceof Module) {
                int moduleX = ((Module) element).getRectangle().x + ((Module) element).getRectangle().width;
                int moduleY = ((Module) element).getRectangle().y + ((Module) element).getRectangle().height;
                dim.width = Math.max(dim.width, moduleX);
                dim.height = Math.max(dim.height, moduleY);
            }
        }
        return dim;
    }

    /**
     * Calculates the first point to draw. The left upper corner.
     * 
     * @param elements
     *            Elements to draw.
     * @return Point of left upper corner.
     */
    private Point getLeftTop(List<Element> elements) {
        Integer minX = null;
        Integer minY = null;
        for (Element element : elements) {
            if (element instanceof Module) {
                int moduleX = ((Module) element).getRectangle().x;
                int moduleY = ((Module) element).getRectangle().y;
                if (minX == null && minY == null) {
                    minX = moduleX;
                    minY = moduleY;
                } else {
                    minX = Math.min(minX, moduleX);
                    minY = Math.min(minY, moduleY);
                }
            }
        }
        return new Point(minX, minY);
    }

    /**
     * Exports the circuit as an image.
     * 
     * @return True iff export was succesful.
     */
    public boolean exportCircuit() {
        if (file == null) {
            throw new IllegalArgumentException();
        }
        if (drawer == null) {
            throw new IllegalArgumentException();
        }
        if (circuit == null) {
            throw new IllegalArgumentException();
        }
        try {
            Point minPoint = getLeftTop(circuit.getElements());
            Dimension dim = getRightBottom(circuit.getElements());
            dim.height = dim.height + MARGIN;
            dim.width = dim.width + MARGIN;
            minPoint.x = minPoint.x - MARGIN;
            minPoint.y = minPoint.y - MARGIN;
            BufferedImage bufferedImage = new BufferedImage(dim.width - minPoint.x, dim.height - minPoint.y,
                    BufferedImage.TYPE_INT_RGB);
            if ((dim.width - minPoint.x) > WARN_WIDTH_THRESHOLD) {
                throwWarning(new FormatException("Image width > " + WARN_WIDTH_THRESHOLD));
            }
            if ((dim.height - minPoint.y) > WARN_HEIGHT_THRESHOLD) {
                throwWarning(new FormatException("Image height > " + WARN_HEIGHT_THRESHOLD));
            }
            Graphics2D g2d = bufferedImage.createGraphics();
            g2d.translate(-minPoint.x, -minPoint.y);

            g2d.setBackground(Color.white);
            g2d.setColor(Color.white);
            g2d.fillRect(minPoint.x, minPoint.y, dim.width - minPoint.x, dim.height - minPoint.y);
            List<Element> elements = circuit.getElements();
            drawer.setGraphics(g2d);
            List<Connection> cachedConnections = new LinkedList<Connection>();
            for (Element elem : elements) {
                if (elem instanceof Connection) {
                    cachedConnections.add((Connection) elem);
                } else if (elem instanceof AndGate) {
                    drawer.draw((AndGate) elem);
                } else if (elem instanceof FlipFlop) {
                    drawer.draw((FlipFlop) elem);
                } else if (elem instanceof Circuit) {
                    drawer.draw((Circuit) elem);
                } else if (elem instanceof ImpulseGenerator) {
                    drawer.draw((ImpulseGenerator) elem);
                } else if (elem instanceof IdentityGate) {
                    drawer.draw((IdentityGate) elem);
                } else if (elem instanceof NotGate) {
                    drawer.draw((NotGate) elem);
                } else if (elem instanceof OrGate) {
                    drawer.draw((OrGate) elem);
                } else if (elem instanceof Lamp) {
                    drawer.draw((Lamp) elem);
                }
            }
            for (Connection connection : cachedConnections) {
                drawer.draw(connection);
            }
            RenderedImage image = bufferedImage;
            try {
                ImageIO.write(image, FILE_FORMAT, file);
            } catch (IOException e) {
                throwFatalError(new FormatException("File couldn't be written.", e));
            }
        } catch (FormatException e) {
            LOG.debug("FormatException caught handled using ErrorHandler");
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public void setCircuit(Circuit c) {
        this.circuit = c;
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, String> getFileFormats() {
        return SUPPORTED_FORMATS;
    }

    /**
     * Throws a warning using the Error Handler. If error handler decides to throw exception, processing is stopped by
     * this exception.
     * 
     * @param e
     *            Exception with information about warning.
     * @throws FormatException
     *             FormatException, reason for stop processing.
     */
    private void throwWarning(FormatException e) throws FormatException {
        if (this.errorHandler != null) {
            this.errorHandler.warning(e);
        }
    }

    /**
     * Throws a fatal error using the Error Handler. If error handler decides to throw exception, processing is stopped
     * by this exception.
     * 
     * @param e
     *            Exception with information about fatal error.
     * @throws FormatException
     *             FormatException, reason for stop processing.
     */
    private void throwFatalError(FormatException e) throws FormatException {
        if (this.errorHandler != null) {
            this.errorHandler.warning(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setErrorHandler(FormatErrorHandler h) {
        if (h == null) {
            throw new IllegalArgumentException();
        }
        this.errorHandler = h;
    }

}

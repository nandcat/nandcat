package nandcat.controller;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.swing.JFileChooser;
import nandcat.view.View;
import org.apache.log4j.Logger;

/**
 * Utilities for import export functionality.
 */
public final class ImportExportUtils {

    /**
     * Class logger instance.
     */
    private static final Logger LOG = Logger.getLogger(ImportExportUtils.class);

    /**
     * Private constructor for utility class.
     */
    private ImportExportUtils() {
        throw new IllegalStateException();
    }

    /**
     * Adds file filter to the given file chooser depending on the given map of formats and descriptions.
     * 
     * @param fc
     *            JFileChooser to set filter on.
     * @param formats
     *            Formats as a map of extension and description.
     */
    public static void addFileFilterToChooser(JFileChooser fc, Map<String, String> formats) {
        if (fc == null) {
            throw new IllegalArgumentException();
        }
        if (formats == null) {
            throw new IllegalArgumentException();
        }

        for (Map.Entry<String, String> entry : formats.entrySet()) {
            fc.addChoosableFileFilter(new ExtensionFileFilter(entry.getKey(), entry.getValue()));
        }
    }

    /**
     * Sets a new title to the view with the filename.
     * 
     * @param view
     *            View to set title of.
     * @param file
     *            File to get filename to set from.
     */
    public static void setViewTitle(View view, File file) {
        try {
            if (file != null) {
                view.setNewTitle(file.getCanonicalPath());
            } else {
                view.setNewTitle(null);
            }
        } catch (IOException e) {
            LOG.debug("Title cannot be set because of exception", e);
        }
    }

    /**
     * Get the extension of a file.
     * 
     * @param f
     *            File to get extension from.
     * @return Extension of the file.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();

        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    /**
     * Gets the path of a file without the filename appended.
     * 
     * @param file
     *            File to get path of.
     * @return Path of a file.
     */
    public static String getFilePath(File file) {
        int sep = file.getAbsolutePath().lastIndexOf(File.separator);
        if (sep == -1) {
            return file.getAbsolutePath();
        } else {
            return file.getAbsolutePath().substring(0, sep);
        }
    }
}

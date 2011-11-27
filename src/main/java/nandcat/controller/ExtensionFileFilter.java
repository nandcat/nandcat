package nandcat.controller;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * Filefilter used to select files based on file extensions.
 */
public class ExtensionFileFilter extends FileFilter {

    /**
     * Description of the filter shown in a file dialog.
     */
    private String description;

    /**
     * Extension used to filter.
     */
    private String fileExtension;

    /**
     * Construct ExtensionFileFilter.
     * 
     * @param extension
     *            Extension to created filter for.
     * @param description
     *            Description of the filter shown in a file dialog.
     */
    public ExtensionFileFilter(String extension, String description) {
        this.description = description;
        this.fileExtension = extension;

    }

    /**
     * Tests if file is accepted by filter. Accepts directories by default to show them in file dialog.
     * 
     * @param f
     *            File to check.
     * @return True iff file was accepted.
     */
    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String extension = ImportExportUtils.getExtension(f);
        if (extension != null) {
            if (extension.equals(fileExtension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the description of the filter, shown in a file dialog.
     * 
     * @return the description of the filter.
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Gets the extension of the filter, used to filter files.
     * 
     * @return the filter's file extension.
     */
    public String getExtension() {
        return fileExtension;
    }
}

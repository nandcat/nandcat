package nandcat.controller;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class ExtensionFileFilter extends FileFilter {

    private String description;

    private String fileExtension;

    public ExtensionFileFilter(String extension, String description) {
        this.description = description;
        this.fileExtension = extension;

    }

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String extension = getExtension(f);
        if (extension != null) {
            if (extension.equals(fileExtension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the extension of a file.
     * 
     * @param f
     *            File to get extension from.
     */
    private static String getExtension(File f) {
        String ext = null;
        String s = f.getName();

        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    @Override
    public String getDescription() {
        return description;
    }

}

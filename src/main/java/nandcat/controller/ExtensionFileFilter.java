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
        String extension = ImportExportUtils.getExtension(f);
        if (extension != null) {
            if (extension.equals(fileExtension)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return description;
    }

}

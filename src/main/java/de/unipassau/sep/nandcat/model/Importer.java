package de.unipassau.sep.nandcat.model;

import java.io.File;

/**
 * Importer.
 * 
 * @version 0.1
 * 
 */
public interface Importer {
    void setFile(File file);

    void getElements();

    // NOTE: import is key
    boolean importElements();

    String getFileExtension();

    String getFileDescription();

}

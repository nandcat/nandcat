package de.unipassau.sep.nandcat.model;

import java.io.File;

/**
 * Exporter.
 * 
 * @version 0.1
 * 
 */
public interface Exporter {
    void setFile(File file);

    boolean exportElements();

    String getFileExtension();

    String getFileDescription();

}
package de.unipassau.sep.nandcat.model;

import java.io.File;
import de.unipassau.sep.nandcat.model.element.Circuit;

/**
 * StandardExporter.
 * 
 * Supports standard SEP format.
 * 
 * @version 0.1
 */
public class StandardExporter implements Exporter {

    /**
     * {@inheritDoc}
     */
    public void setFile(File file) {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     */
    public boolean exportCircuit() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public void setCircuit(Circuit c) {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     */
    public String[] getFileExtension() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getFileDescription() {
        // TODO Auto-generated method stub
        return null;
    }
}

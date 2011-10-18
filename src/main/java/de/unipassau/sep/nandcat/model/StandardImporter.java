package de.unipassau.sep.nandcat.model;

import java.io.File;
import de.unipassau.sep.nandcat.model.element.Circuit;

/**
 * StandardImporter.
 * 
 * Supports standard SEP format.
 * @version 0.1
 */
public class StandardImporter implements Importer {

    /**
     * {@inheritDoc}
     */
    public void setFile(File file) {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     */
    public Circuit getCircuit() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public boolean importCircuit() {
        // TODO Auto-generated method stub
        return false;
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

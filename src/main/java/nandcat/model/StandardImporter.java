package nandcat.model;

import java.io.File;
import java.util.Map;
import nandcat.model.element.Circuit;

/**
 * StandardImporter.
 * 
 * Supports standard SEP format SEPAF.
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
    public String getErrorMessage() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, String> getFileFormats() {
        // TODO Auto-generated method stub
        return null;
    }
}

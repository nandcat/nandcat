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
     * Handle of file to import from.
     */
    private File file = null;

    /**
     * {@inheritDoc}
     */
    public void setFile(File file) {
        this.file = file;
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

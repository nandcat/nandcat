package nandcat.model.importexport.sepaf;

import java.io.File;
import java.util.Map;
import nandcat.model.element.Circuit;
import nandcat.model.importexport.Exporter;

/**
 * StandardExporter.
 * 
 * Supports standard SEP format SEPAF.
 */
public class SEPAFExporter implements Exporter {

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

package nandcat.model;

import nandcat.model.element.Module;
import org.apache.log4j.Logger;

/**
 * View-friendly representation of all available module objects. Contains no logic.
 * 
 * Used to display the representation of a module object, e.g. in a tool bar. Not used to display the module on the
 * workspace!
 */
public class ViewModule {

    /**
     * ViewModule's name.
     */
    private String name;

    /**
     * ViewModule's filename (may be null in case of standard gates). This has to be used to reference
     * circuit-definition files.
     */
    private final String fileName;

    /**
     * Module reference to the gate represented by this ViewModule. May be null in case of circuits.
     */
    private final Module module;

    /**
     * Class logger instance.
     */
    private static final Logger LOG = Logger.getLogger(ViewModule.class);

    /**
     * Default constructor.
     * 
     * @param name
     *            String containing ViewModule's name
     * @param module
     *            Module this viewmodule represents. May be null. (for circuits)
     * @param fileName
     *            String ViewModule's file name. May be empty. (for standard gates)
     */
    protected ViewModule(String name, Module module, String fileName) {
        this.name = name;
        this.module = module;
        if (fileName == null) {
            this.fileName = "";
        } else {
            this.fileName = fileName;
        }
    }

    /**
     * Return ViewModule's name.
     * 
     * @return String containing ViewModule's name
     */
    public String getName() {
        return name;
    }

    /**
     * Set ViewModule's name.
     * 
     * @param name
     *            String containing ViewModule's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get Filename (may be empty in case of circuits).
     * 
     * @return String containing the filename of the circuit this ViewModule represents
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Return Module this ViewModule represents.
     * 
     * @return Module represented by this ViewModule
     */
    public Module getModule() {
        try {
            return (Module) FastDeepCopy.copy(module);
        } catch (Exception e) {
            LOG.warn("Fast deep copy exception", e);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return new String(name);
    }
}

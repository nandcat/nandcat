package nandcat.model;

import java.lang.reflect.Constructor;
import nandcat.model.element.Module;

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
     * ViewModule's symbol. <b>Note:</b> may be null
     */
    private byte[] symbol;

    /**
     * ViewModule's filename (may be null in case of standard gates). This has to be used to reference
     * circuit-definition files.
     */
    private final String fileName; // TODO data-type: Filename/File?

    /**
     * Module reference to the gate represented by this ViewModule. May be null in case of circuits.
     */
    private final Module module;

    /**
     * Default constructor.
     * 
     * @param name
     *            String containing ViewModule's name
     * @param module
     *            Module this viewmodule represents. May be null. (for circuits)
     * @param fileName
     *            String ViewModule's file name. May be empty. (for standard gates)
     * @param symbol
     *            bytearray containing ViewModule's symbol
     */
    protected ViewModule(String name, Module module, String fileName, byte[] symbol) {
        this.name = name;
        this.module = module;
        if (fileName == null) {
            this.fileName = "";
        } else {
            this.fileName = fileName;
        }
        this.symbol = symbol;
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
     * Returns the file extension.
     * 
     * @return String containing the file extension
     */
    public String getFileExtension() {
        return fileName;
    }

    /**
     * Return ViewModule's symbol.
     * 
     * @return byte[] containing ViewModule's symbol
     */
    public byte[] getSymbol() {
        return symbol;
    }

    /**
     * Set ViewModule's symbol.
     * 
     * @param symbol
     *            byte[] containing ViewModule's symbol
     */
    public void setSymbol(byte[] symbol) {
        this.symbol = symbol;
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
        Module m = null;
        Constructor<? extends Module> x = null;
        try {
            x = module.getClass().getConstructor(new Class[] { int.class, int.class });
            m = x.newInstance(module.getInPorts().size(), module.getOutPorts().size());
            return m;
        } catch (Exception e) {
        }
        try {
            x = module.getClass().getConstructor();
            m = x.newInstance();
        } catch (Exception e) {
        }
        return m;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return new String(name);
    }
}

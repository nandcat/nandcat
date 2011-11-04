package nandcat.model;

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
     * ViewModule's file extension.
     */
    private String fileExtension;

    /**
     * Default constructor.
     * 
     * @param name
     *            String containing ViewModule's name
     * @param fileExtension
     *            String ViewModule's file extension
     * @param symbol
     *            bytearray containing ViewModule's symbol
     */
    public ViewModule(String name, String fileExtension, byte[] symbol) {
        this.name = name;
        this.fileExtension = fileExtension;
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
     * Sets the file extension for the symbol of this ViewModule.
     * 
     * @param s
     *            String containing the file extension
     */
    public void setFileExtension(String s) {
        fileExtension = s;
    }

    /**
     * Returns the file extension.
     * 
     * @return String containing the file extension
     */
    public String getFileExtension() {
        return fileExtension;
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
}

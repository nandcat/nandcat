package nandcat.model;

/**
 * View-friendly representation of all available module Objects. Contains no logic.
 * 
 */
public class ViewModule {

    /**
     * ViewModule's name.
     */
    private String name;

    /**
     * ViewModule's symbol.
     */
    private byte[] symbol;

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
     * Sets the file extension for this ViewModule.
     * 
     * @param s
     *            String containing the file extension
     */
    public void setFileExtension(String s) {
    }

    /**
     * Returns the file extension.
     * 
     * @return String containing the file extension
     */
    public String getFileExtension() {
        return null;
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

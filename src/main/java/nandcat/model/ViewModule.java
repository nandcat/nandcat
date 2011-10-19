package nandcat.model;

/**
 * View-friendly representation of all available module Objects.
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

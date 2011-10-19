package nandcat.view;

import javax.swing.ImageIcon;

/**
 * ViewElements represents a ViewElement means a Element (e.g. AND-Gate) available to select.
 * 
 * @version 0.1
 * 
 */
public class ViewElement {

    /**
     * Name of the Element.
     */
    private String name;

    /**
     * Index number of the Element.
     */
    private int index;

    /**
     * Symbol representing the Element.
     */
    private ImageIcon symbol;

    /**
     * Returns the Name String.
     * 
     * @return String, the Name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the Name of the Element.
     * 
     * @param name
     *            String the Name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the Index Number of the Element.
     * 
     * @return int the index.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Set the Index of the Element.
     * 
     * @param index
     *            int the index number.
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Returns the Symbol of the Element.
     * 
     * @return ImageIcon the Elementsymbol.
     */
    public ImageIcon getSymbol() {
        return symbol;
    }

    /**
     * sets the Elementsymbol.
     * 
     * @param symbole
     *            ImageIcon the Elementsymbol.
     */
    public void setSymbol(ImageIcon symbole) {
        this.symbol = symbole;
    }
}

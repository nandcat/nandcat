package nandcat.model.element;

/**
 * Element interface. An Element contains getters and setters for the most basic attributes a visible (or under specific
 * circumstances invisible) on the workspave component can have.
 */
public interface Element {

    /**
     * Set element's name.
     * 
     * @param name
     *            set element's name to name
     */
    void setName(String name);

    /**
     * Return element's name.
     * 
     * @return element's name
     */
    String getName();

    /**
     * Sets the Element's selected state.
     * 
     * @param b
     *            new state
     */
    void setSelected(boolean b);

    /**
     * Returns the Element's selected state.
     * 
     * @return true if the Element is selected, false if not
     */
    boolean isSelected();
}

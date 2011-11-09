package nandcat.model.element;

/**
 * RoElement interface. A read-only Element contains getters for the most basic attributes a visible (or under specific
 * circumstances invisible) on the workspave component can have.
 */
public interface DrawElement {

    /**
     * Return element's name.
     * 
     * @return element's name
     */
    String getName();

    /**
     * Returns the Element's selected state.
     * 
     * @return true if the Element is selected, false if not
     */
    boolean isSelected();
}

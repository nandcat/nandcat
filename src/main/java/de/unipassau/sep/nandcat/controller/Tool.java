package de.unipassau.sep.nandcat.controller;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;

/**
 * Tool is an Interface implemented by the Tools given in the Program. The user
 * can interact with the Tools. If someone wants to add new Tools he has to
 * implement this Interface.
 * 
 * @version 0.1
 * 
 */
public interface Tool {

	/**
	 * Activates or deactivates tool.
	 * 
	 * @param active
	 *            true to activate tool.
	 */
	void setActive(boolean active);

	/**
	 * Returns the Listener of the Tool-Class.
	 * 
	 * @return ActionListener implemented by the specific tool
	 */
	ActionListener getListener();

	/**
	 * Returns a String representation of the Tool.
	 * 
	 * @return String representing the Tool
	 */
	List<String> getText();

	/**
	 * Returns a Icon representation of the Tool.
	 * 
	 * @return ImageIcon which represents the Tool
	 */
	ImageIcon getIcon();
}

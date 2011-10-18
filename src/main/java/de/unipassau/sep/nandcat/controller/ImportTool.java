package de.unipassau.sep.nandcat.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import de.unipassau.sep.nandcat.model.Model;
import de.unipassau.sep.nandcat.view.WorkspaceListener;

/**
 * Tool for importing files. Includes loading.
 * 
 * @version 0.1
 * 
 */
public class ImportTool implements Tool {

	/**
	 * Current Model instance.
	 */
	private Model model;

	/**
	 * Current Controller instance.
	 */
	private Controller controller;

	/**
	 * Icon representation of the Tool.
	 */
	private ImageIcon icon; // TODO icon setzen

	/**
	 * String representation of the Tool.
	 */
	private List<String> represent; // TODO beschreibung schreiben

	/**
	 * ActionListerner of the Tool on the Buttons.
	 */
	private ActionListener buttonListener;

	/**
	 * WorkspaceListener of the Tool.
	 */
	private WorkspaceListener workspaceListener;

	/**
	 * Constructs the ImportTool.
	 * 
	 * @param controller
	 *            Controller component of the application.
	 */
	public ImportTool(Controller controller) {
		this.controller = controller;
		this.model = controller.getModel();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setActive(boolean active) {
		// Always active
	}

	/**
	 * {@inheritDoc}
	 */
	public Map<String, ActionListener> getFunctionalities() {
		buttonListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		};
		Map<String, ActionListener> map = new HashMap<String, ActionListener>();
		for (String functionality : represent) {
			map.put(functionality, buttonListener);
		}
		return map;
	}

	/**
	 * {@inheritDoc}
	 */
	public ImageIcon getIcon() {
		return icon;
	}
}

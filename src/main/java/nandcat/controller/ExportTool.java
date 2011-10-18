package nandcat.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import nandcat.model.Model;
import nandcat.view.WorkspaceListener;

/**
 * Tool for exporting files. Includes saving.
 * 
 * @version 0.1
 * 
 */
public class ExportTool implements Tool {

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
	 * Constructs the ExportTool.
	 * 
	 * @param controller
	 *            Controller component of the application.
	 */
	public ExportTool(Controller controller) {
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

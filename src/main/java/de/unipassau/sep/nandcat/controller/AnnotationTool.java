package de.unipassau.sep.nandcat.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import de.unipassau.sep.nandcat.model.Model;
import de.unipassau.sep.nandcat.view.View;
import de.unipassau.sep.nandcat.view.WorkspaceEvent;
import de.unipassau.sep.nandcat.view.WorkspaceListener;

/**
 * The AnnotationTool is responsible for the naming of modules.
 * 
 * @version 0.1
 * 
 */
public class AnnotationTool implements Tool {

	/**
	 * Current Model instance.
	 */
	private Model model;

	/**
	 * Current Controller instance.
	 */
	private Controller controller;

	/**
	 * Current View instance.
	 */
	private View view;

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
	 * Constructs the SelectTool.
	 * 
	 * @param controller
	 *            Controller component of the application.
	 */
	public AnnotationTool(Controller controller) {
		this.controller = controller;
		this.view = controller.getView();
		this.model = controller.getModel();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setActive(boolean active) {
		if (active) {
			if (workspaceListener == null) {
				workspaceListener = new WorkspaceListener() {

					public void mouseReleased(WorkspaceEvent e) {
						// TODO Auto-generated method stub
					}

					public void mousePressed(WorkspaceEvent e) {
						// TODO Auto-generated method stub
					}

					public void mouseMoved(WorkspaceEvent e) {
						// TODO Auto-generated method stub
					}

					public void mouseDragged(WorkspaceEvent e) {
						// TODO Auto-generated method stub
					}

					public void mouseClicked(WorkspaceEvent e) {
						// TODO Auto-generated method stub
					}
				};
			}
			view.getWorkspace().addListener(workspaceListener);
		} else {
			view.getWorkspace().removeListener(workspaceListener);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public ImageIcon getIcon() {
		return icon;
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
}

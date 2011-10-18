package nandcat.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import nandcat.view.View;
import nandcat.view.WorkspaceEvent;
import nandcat.view.WorkspaceListener;

/**
 * The ViewTool is responsible for moving the display range over the Workspace.
 * 
 * @version 0.4
 * 
 */
public class ViewTool implements Tool {

	/**
	 * Current View instance.
	 */
	private View view = null;

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
	 * WorkspaceListener of the Tools on the Workspace.
	 */
	private WorkspaceListener workspaceListener;

	/**
	 * Constructs the ViewTool.
	 * 
	 * @param controller
	 *            Controller component of the application.
	 */
	public ViewTool(Controller controller) {
		this.controller = controller;
		view = controller.getView();
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

package de.unipassau.sep.nandcat.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.*;

import javax.swing.ImageIcon;
import javax.swing.plaf.basic.BasicComboBoxUI.ItemHandler;

import de.unipassau.sep.nandcat.model.Model;
import de.unipassau.sep.nandcat.model.ModelEvent;
import de.unipassau.sep.nandcat.model.ModelListener;
import de.unipassau.sep.nandcat.view.CheckManager;

/**
 * The SimulateTool is responsible for handling the Simulation and Checks.
 * 
 * @version 0.1
 * 
 */
public class SimulateTool implements Tool {

	/**
	 * Current Model instance.
	 */
	private Model model;

	/**
	 * Current Controller instance.
	 */
	private Controller controller;

	/**
	 * Current CheckManager instance.
	 */
	private CheckManager checkManager;

	/**
	 * Icon representation of the Tool.
	 */
	private ImageIcon icon; // TODO icon setzen

	/**
	 * String representation of the Tool.
	 */
	private List<String> represent; // TODO beschreibung schreiben

	/**
	 * ActionListener of the Tool on the Buttons.
	 */
	private ActionListener buttonListener;

	/**
	 * ModelListener of the Tool on the Model.
	 */
	private ModelListener modelListener;

	/**
	 * ItemHanlder of the Tool the the ComboBox in the CheckManager.
	 */
	private ItemHandler comboboxListener;

	/**
	 * Constructs the SimulateTool.
	 * 
	 * @param controller
	 *            Controller component of the application.
	 */
	public SimulateTool(Controller controller) {
		this.controller = controller;
		model = controller.getModel();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setActive(boolean active) {
		if (active) {
			if (modelListener == null) {
				modelListener = new ModelListener() {

					public void elementsChanged(ModelEvent e) {
					}

					public void checksChanged(ModelEvent e) {
						// TODO Auto-generated method stub

					}

					public void simulationChanged(ModelEvent e) {
						// TODO Auto-generated method stub

					}
				};
			}
			model.addListener(modelListener);
		} else {
			model.removeListener(modelListener);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public ActionListener getListener() {
		if (buttonListener != null) {
			return buttonListener;
		} else {
			buttonListener = new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					// nur zum beispiel actionCommands sind defaultmäßig die
					// namen der Buttons/menüpunkte
					if (e.getActionCommand() == "checkstating")
						;
					checkManager = new CheckManager(model.getChecks());
				}
			};
		}
		return buttonListener;
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

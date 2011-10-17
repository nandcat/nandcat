package de.unipassau.sep.nandcat.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

import de.unipassau.sep.nandcat.model.Model;
import de.unipassau.sep.nandcat.model.ModelEvent;
import de.unipassau.sep.nandcat.model.ModelListener;
import de.unipassau.sep.nandcat.view.CheckManager;

public class CheckTool implements Tool{
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
	private String represent; // TODO beschreibung schreiben

	/**
	 * ActionListener of the Tool on the Buttons.
	 */
	private ActionListener buttonListener;

	/**
	 * ModelListener of the Tool on the Model.
	 */
	private ModelListener modelListener;

	/**
	 * Constructs the SimulateTool.
	 * 
	 * @param controller
	 *            Controller component of the application.
	 */
	public CheckTool(Controller controller) {
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

					// TODO wirklich elementsChanged? was ist mit checks
					// started, simulation started etc.?
					public void elementsChanged(ModelEvent e) {
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
					// TODO Auto-generated method stub
				}
			};
		}
		return buttonListener;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getText() {
		return represent;
	}

	/**
	 * {@inheritDoc}
	 */
	public ImageIcon getIcon() {
		return icon;
	}

}

package nandcat.controller;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import nandcat.model.Model;
import nandcat.view.View;

/**
 * Controller.
 * 
 * Maps user actions to model updates and defines application behavior. It holds all tools which are responsible for the
 * application functionality available in the GUI.
 */
public class Controller {

    /**
     * Collection of available tools.
     */
    // TODO: protected für Tests
    protected Set<Tool> tools = new LinkedHashSet<Tool>();

    /**
     * Currently active tool.
     */
    private Tool activeTool = null;

    /**
     * Current View instance.
     */
    private View view = null;

    /**
     * Current Model instance.
     */
    private Model model = null;

    /**
     * Constructs the Controller.
     * 
     * @param view
     *            View component of the application.
     * @param model
     *            Model component of the application.
     */
    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        initTools();
    }

    /**
     * Initializes the Tool Classes.
     */
    protected void initTools() {
        AnnotationTool annotationTool = new AnnotationTool(this);
        tools.add(annotationTool);
        // TODO Liste Fertig
    }

    /**
     * Requests activation for a given tool.
     * 
     * If available, the given tool will be activated after the current tools is deactivated.
     * 
     * @param tool
     *            Tool to be activated.
     */
    public void requestActivation(Tool tool) {
        if (tool == null) {
            throw new IllegalArgumentException();
        }
        if (activeTool != null) {
            activeTool.setActive(false);
        }
        activeTool = tool;
        activeTool.setActive(true);
    }

    /**
     * Returns the current Model instance.
     * 
     * @return model the current Model instance
     */
    public Model getModel() {
        return model;
    }

    /**
     * Returns the current View instance.
     * 
     * @return view the current View instance
     */
    public View getView() {
        return view;
    }

    /**
     * Gives the Functionalities and Listeners of the Tools to the View.
     */
    public void giveFunctionalities() {
        Map<String, ActionListener> map = new HashMap<String, ActionListener>();
        for (Tool tool : tools) {
            map.putAll(tool.getFunctionalities());
        }
        view.setFunctionalities(map);
    }
}

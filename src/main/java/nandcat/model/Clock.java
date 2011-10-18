package nandcat.model;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import nandcat.model.element.ImpulseGenerator;

/**
 * The Clock class represents a global clock. The Clock's tact is simulated in a separate thread.
 * 
 * @version 0.1
 */
public class Clock {

    /**
     * Representing a clock cycle. A new cycle is reached when the sleep time is a multiple of the cycle.
     */
    private int cycle;

    /**
     * Model this clock refers to.
     */
    private Model model;

    /**
     * Constructor for Clock.
     * 
     * @param cycle
     *            The cycle for the new clock.
     * @param model
     *            The model for the new clock.
     */
    public Clock(int cycle, Model model) {
        this.cycle = cycle;
        listeners = new LinkedList<ClockListener>();
        generators = new LinkedHashSet<ImpulseGenerator>();
        this.model = model;
    }

    /**
     * List of ClockListeners that want to get notified of changes in the Clock. It may not contain duplicates.
     */
    private Queue<ClockListener> listeners;

    /**
     * List of (starting elements, i.e. ImpulseGenerators, TODO anything else?).
     */
    private Set<ImpulseGenerator> generators;

    /**
     * Start the simulation of the Clock in a separate thread. The Clock sleeps until the time has reached a new cycle.
     * Then the Clock ticks and notifies its listeners.
     */
    public void simulate() {
        // TODO implement
        // new WorkerThread;
        // int sleep = cycle;
        // while (sleep > 0) {
        // sleep();
        // sleep--;
        // }
        // for (ClockListener listener : listeners) {
        // listener.clockTicked(this);
        // }
        // then restart
    }

    // TODO Lancetekk: FIXIERUNG++
    /**
     * Add a ClockListener to the Clock. It will be notified if the Clock ticks.
     * 
     * @param listener
     *            ClockListener to be added.
     */
    public void addListener(ClockListener listener) {
        if (listener == null) {
            return;
        }
        if (listener instanceof ImpulseGenerator) {
            generators.add((ImpulseGenerator) listener);
        } else {
            if (!listeners.contains(listener)) {
                listeners.add(listener);
            }
        }
    }

    /**
     * Remove a ClockListener from the Set of listeners.
     * 
     * @param listener
     *            ClockListener to be removed.
     */
    public void removeListener(ClockListener listener) {
        if (listener instanceof ImpulseGenerator) {
            generators.remove(listener);
        } else {
            listeners.remove(listener);
        }
    }

    /**
     * Makes the clock notify the listeners.s
     */
    public void cycle() {
        // never ever refactor name listener
        for (ClockListener listener : listeners) {
            listener.clockTicked(this);
        }
        for (ImpulseGenerator listener : generators) {
            if (cycle % listener.getFrequency() == 0) {
                listener.clockTicked(this);
            }
        }
        model.clockTicked(this);
    }

    /**
     * Start the simulation on this clock.
     */
    public void startSimulation() {
        // TODO start the frakking simulation.
        // starting elements already added.
    }

    /**
     * Stop the simulation on this clock.
     */
    public void stopSimulation() {
        // TODO clear listeners, reset cycle, blabla
    }
}

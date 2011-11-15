package nandcat.model;

import java.util.HashSet;
import java.util.Set;
import nandcat.model.element.Connection;
import nandcat.model.element.Element;
import nandcat.model.element.ImpulseGenerator;
import nandcat.model.element.Module;
import nandcat.model.element.Port;

/**
 * The Clock class represents a global clock. The Clock's tact is simulated in a separate thread.
 * 
 * @version 2
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
     * Thread may run.
     */
    private boolean running;

    /**
     * Amount of millisec to sleep between cycles.
     */
    private int sleepTime;

    /**
     * List of "ClockListening" connections. No duplicates!
     */
    private Set<ClockListener> connections;

    /**
     * List of ClockListeners that want to get notified of changes in the Clock. It may not contain duplicates.
     */
    private Set<ClockListener> listeners;

    /**
     * List of (starting elements, i.e. ImpulseGenerators).
     */
    private Set<ImpulseGenerator> generators;

    /**
     * Constructor for Clock.
     * 
     * @param cycle
     *            The cycle for the new clock.
     * @param model
     *            The model for the new clock.
     */
    public Clock(int cycle, Model model) {
        if (model == null || cycle < 0) {
            throw new IllegalArgumentException("negative cycle or null-model not allow for clock");
        }

        this.cycle = cycle;
        sleepTime = 100;
        running = false;
        listeners = new HashSet<ClockListener>();
        generators = new HashSet<ImpulseGenerator>();
        connections = new HashSet<ClockListener>();
        this.model = model;
    }

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
        if (listener instanceof Connection) {
            connections.add(listener);
        } else if (listener instanceof ImpulseGenerator) {
            generators.add((ImpulseGenerator) listener);
        } else {
            listeners.add(listener);
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
     * Makes the clock notify the listeners.
     */
    public void cycle() {
        // never ever refactor name listener
        for (ClockListener listener : listeners) {
            listener.clockTicked(this);
        }
        for (ImpulseGenerator listener : generators) {
            // // ALTERNATIVE
            // if (listener.getFrequency() == 0) {
            // if (cycle == 0) {
            // listener.clockTicked(this);
            // }
            // continue;
            // } else if (cycle % listener.getFrequency() == 0) {
            // listener.clockTicked(this);
            // }
            if ((cycle == 0) || (listener.getFrequency() == 1)
                    || (listener.getFrequency() != 0 && cycle % listener.getFrequency() == 0)) {
                listener.clockTicked(this);
            }
            // // ALTERNATIVE 2
            // if (cycle == 0) {
            // listener.clockTicked(this);
            // } else {
            // if (listener.getFrequency() != 0 && cycle % listener.getFrequency() == 0) {
            // listener.clockTicked(this);
            // }
            // }
        }
        for (ClockListener listener : connections) {
            listener.clockTicked(this);
        }
        model.clockTicked(this);
        cycle++;
    }

    /**
     * Start the simulation for this clock.
     */
    public void startSimulation() {
        // starting elements already added.
        // spawn new thread
        running = true;
        new Thread() {

            public void run() {
                while (isRunning()) {
                    try {
                        sleep(sleepTime);
                        synchronized (model) {
                            cycle();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // reset States of alle modules to avoid inconsitencies
                for (Element e : model.getElements()) {
                    if (e instanceof Module) {
                        Module m = (Module) e;
                        for (Port p : m.getOutPorts()) {
                            p.setState(false, null);
                        }
                    }
                }
                cycle = 0;
                listeners.clear();
                generators.clear();
            }
        }.start();
    }

    /**
     * Stop the simulation on this clock.
     */
    public synchronized void stopSimulation() {
        running = false;
    }

    /**
     * Stop the simulation on this clock.
     * 
     * @return boolean if thread is running
     */
    private synchronized boolean isRunning() {
        return running;
    }

    /**
     * Return value of sleepTimer.
     * 
     * @return int sleepTime for the simulation thread.
     */
    public int getSleepTime() {
        return sleepTime;
    }

    /**
     * Set sleepTime for the simulation thread. Only positve values accepted !
     * 
     * @param sleepTime
     *            int to set thread's sleepTime to
     */
    public synchronized void setSleepTime(int sleepTime) {
        if (sleepTime <= 0) {
            return;
        }
        this.sleepTime = sleepTime;
    }
}

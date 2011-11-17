package nandcat.model;

import java.util.HashSet;
import java.util.Set;
import nandcat.model.element.Connection;
import nandcat.model.element.Element;
import nandcat.model.element.ImpulseGenerator;
import nandcat.model.element.Module;
import nandcat.model.element.Port;
import org.apache.log4j.Logger;

/**
 * The Clock class represents a global clock. The Clock's tact is simulated in a separate thread.
 * 
 * @version 2
 */
public class Clock implements Runnable {

    /**
     * Class logger instance.
     */
    private static final Logger LOG = Logger.getLogger(Clock.class);

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
        sleepTime = 1000;
        running = false;
        listeners = new HashSet<ClockListener>();
        generators = new HashSet<ImpulseGenerator>();
        connections = new HashSet<ClockListener>();
        this.model = model;
    }

    /**
     * Get the _current_ cycle-number.
     * 
     * @return the current cycle-number
     */
    protected int getCycle() {
        return cycle;
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

        // Added debug code !
        EXTRACT_THE_DEBUGINFO();
        long before = System.nanoTime();

        // never ever refactor name listener
        for (ClockListener listener : listeners) {
            listener.clockTicked(this);
        }
        for (ImpulseGenerator listener : generators) {
            if ((cycle == 0) || (listener.getFrequency() == 1)
                    || (listener.getFrequency() != 0 && cycle % listener.getFrequency() == 0)) {
                listener.clockTicked(this);
            }
        }
        for (ClockListener listener : connections) {
            listener.clockTicked(this);
        }
        model.clockTicked(this);

        // Added debug code !
        long after = System.nanoTime();
        LOG.debug("Cycle " + cycle + " took " + (after - before) + " ns");

        cycle++;
    }

    /**
     * Start the simulation for this clock.
     */
    public void startSimulation() {
        running = true;
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

    public void run() {
        // Added debug code !
        LOG.debug("new Thread started, Cycle = " + cycle);
        while (isRunning()) {
            try {
                Thread.sleep(sleepTime);
                synchronized (model) {
                    cycle();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // reset States of EVERYTHING to avoid inconsitencies
        reset();
        model.notifyForStoppedSim();
        // Added debug code !
        LOG.debug("Thread died, listeners notified!");
    }

    /**
     * Reset all listeners, clears the lists and resets the cycle.
     */
    private void reset() {
        for (Element e : model.getElements()) {
            if (e instanceof Module) {
                Module m = (Module) e;
                for (Port p : m.getOutPorts()) {
                    p.setState(false, null);
                }
                for (Port p : m.getInPorts()) {
                    p.setState(false, null);
                }
            }
            if (e instanceof ImpulseGenerator) {
                ImpulseGenerator i = (ImpulseGenerator) e;
                if (i.getState()) {
                    i.toggleState();
                }
            }
            if (e instanceof Connection) {
                ((Connection) e).setState(false, null);
            }
        }
        cycle = 0;
        listeners.clear();
        generators.clear();
        connections.clear();
    }

    // TODO - entfernen wenn richtigkeit sichergestellt !
    private void EXTRACT_THE_DEBUGINFO() {
        String imps = "\nactive impulseGenerators:\n";
        for (ImpulseGenerator listener : generators) {
            if ((cycle == 0) || (listener.getFrequency() == 1)
                    || (listener.getFrequency() != 0 && cycle % listener.getFrequency() == 0)) {
                imps += (listener.toString() + "\n");
            }
        }
        imps += "modules in queue:\n";
        for (ClockListener l : listeners) {
            imps += l.toString() + "\n";
        }
        imps += "connections in queue:\n";
        for (ClockListener c : connections) {
            imps += c.toString() + "\n";
        }
        LOG.debug(imps);
    }
}

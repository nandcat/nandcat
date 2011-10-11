package de.unipassau.sep.nandcat.model.element;

import de.unipassau.sep.nandcat.model.Clock;

/**
 * Connection.
 * 
 * @version 0.1
 * 
 */
public class Connection implements Element { // Connection meldet Baustein bei Clock an! Über Port.

    public void setState(boolean state, Clock clock) {
        // TODO methode schreiben
    }

    public void setName(String name) {
        // TODO Auto-generated method stub

    }

    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    public void clockTicked(Clock clock) { // entspricht notify()
        // TODO Auto-generated method stub

    }
}

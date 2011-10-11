package de.unipassau.sep.nandcat.model.check;

/**
 * @(#) CheckListener.java
 */
public interface CheckListener {

    void checkStarted();

    void checkChanged(CheckEvent e);
}

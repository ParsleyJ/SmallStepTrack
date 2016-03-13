package com.parsleyj.smallsteptrack;

import com.parsleyj.smallsteptrack.command.Command;


/**
 * This class represents a Program written in While language.
 */
public class Program {
    private Command c;

    /**
     * Creates a new Program class, wrapped around the specified "root" Command.
     * @param c the command
     */
    public Program(Command c) {
        this.c = c;
    }

    /**
     * Makes a computational step.
     *
     * @param store the store
     * @return true if the program reached a "ended" state; false otherwise.
     */
    public boolean step(Store store) {

        if (!c.isTerminal()) {
            c = c.step(store);
            return false;
        } else {
            return true;
        }

    }

    /**
     * Returns the string representation of the Program object.
     * This is done by returning the string representation of the
     * Command object initially specified via {@link #Program(Command)}
     * @return the string representation of the Program object.
     */
    @Override
    public String toString() {
        return "" + c.toString();
    }
}

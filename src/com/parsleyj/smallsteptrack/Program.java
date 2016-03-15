package com.parsleyj.smallsteptrack;

import com.parsleyj.smallsteptrack.command.Command;
import com.parsleyj.smallsteptrack.configuration.Configuration;


/**
 * This class represents a Program written in While language.
 */
public class Program {
    private String programName;

    private Command c;

    /**
     * Creates a new Program class, wrapped around the specified "root" Command.
     * @param name the program's name
     * @param c the command
     */
    public Program(String name, Command c) {
        programName = name;
        this.c = c;
    }

    /**
     * Makes a computational step.
     *
     * @param configuration the configuration
     * @return true if the program reached a "ended" state; false otherwise.
     */
    public boolean step(Configuration configuration) {

        if (!c.isTerminal()) {
            c = c.step(configuration);
            return false;
        } else {
            return true;
        }

    }

    /**
     * Returns the string representation of the Program object.
     * This is done by returning the string representation of the
     * Command object initially specified via {@link #Program(String,Command)}
     * @return the string representation of the Program object.
     */
    @Override
    public String toString() {
        return "" + c.toString();
    }
}

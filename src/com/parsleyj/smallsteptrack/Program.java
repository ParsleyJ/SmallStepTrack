package com.parsleyj.smallsteptrack;

import com.parsleyj.smallsteptrack.command.Command;

/**
 * Created by Giuseppe on 09/03/16.
 */
public class Program {
    private Command c;

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

    @Override
    public String toString() {
        return "" + c.toString();
    }
}

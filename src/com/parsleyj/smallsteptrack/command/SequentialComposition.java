package com.parsleyj.smallsteptrack.command;

import com.parsleyj.smallsteptrack.Store;

/**
 * Created by Giuseppe on 10/03/16.
 */
public class SequentialComposition implements Command {
    private final Command a;
    private final Command b;

    public SequentialComposition(Command a, Command b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public Command step(Store x) {
        if (!a.isTerminal()) { //if a can make a step of computation
            Command a1 = a.step(x); // then let it do it

            // then return the new sequential composition made of the modified a command and the original b command.
            return new SequentialComposition(a1, b);

        } else { //else, a is terminal (skip)
            //then return the original b command.
            return b;

        }
    }

    @Override
    public boolean isTerminal() {
        return false;
    }


    @Override
    public String toString() {
        return "(" + a + "; " + b + ")";
    }
}

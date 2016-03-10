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
        if (!a.isTerminal()) {
            Command a1 = a.step(x);
            return new SequentialComposition(a1, b);

        } else if (!b.isTerminal()) { //a is terminal (numeral) but b is not terminal
            return b;

        } else { // both are terminal
            return new Skip();
        }
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public Integer getIntValue() {
        return null;
    }

    @Override
    public Boolean getBooleanValue() {
        return null;
    }

    @Override
    public String toString() {
        return "(" + a + "; " + b + ")";
    }
}

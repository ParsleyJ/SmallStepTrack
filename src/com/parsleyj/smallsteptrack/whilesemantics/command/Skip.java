package com.parsleyj.smallsteptrack.whilesemantics.command;

import com.parsleyj.smallsteptrack.configurations.Configuration;

/**
 * Semantic object representing the {@code skip} terminal command
 */
public class Skip implements Command {

    @Override
    public Command step(Configuration c) {
        return null;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }



    @Override
    public String toString() {
        return "skip";
    }
}

package com.parsleyj.smallsteptrack.command;

import com.parsleyj.toolparser.configuration.Configuration;

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

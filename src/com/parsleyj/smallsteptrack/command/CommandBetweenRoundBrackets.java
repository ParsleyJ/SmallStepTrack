package com.parsleyj.smallsteptrack.command;

import com.parsleyj.smallsteptrack.booleanexpr.BooleanExpression;
import com.parsleyj.smallsteptrack.configuration.Configuration;

/**
 * Semantic object representing a boolean expression surrounded by round brackets.
 */
public class CommandBetweenRoundBrackets implements Command {
    private Command com;

    public CommandBetweenRoundBrackets(Command c) {
        this.com = c;
    }

    @Override
    public Command step(Configuration c) {
        if(!com.isTerminal()){
            Command com1 = com.step(c);
            return new CommandBetweenRoundBrackets(com1);
        }else return com;
    }

    @Override
    public String toString() {
        return "(" + com + ")";
    }
}

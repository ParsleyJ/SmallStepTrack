package com.parsleyj.smallsteptrack.command;

import com.parsleyj.smallsteptrack.configuration.Configuration;
import com.parsleyj.smallsteptrack.booleanexpr.BooleanExpression;

/**
 * Semantic object representing the {@code while} statement
 */
public class WhileCommand implements Command {
    private final BooleanExpression condition;
    private final Command com;

    public WhileCommand(BooleanExpression condition, Command com) {
        this.condition = condition;
        this.com = com;
    }

    @Override
    public Command step(Configuration c) {
        return new IfThenElseCommand(condition,
                new SequentialComposition(
                        com,
                        new WhileCommand(condition, com)),
                new Skip());
    }

    @Override
    public boolean isTerminal() {
        return false;
    }


    @Override
    public String toString() {
        return "while " + condition + " do " + com;
    }
}

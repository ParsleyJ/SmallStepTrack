package com.parsleyj.smallsteptrack.command;

import com.parsleyj.smallsteptrack.booleanexpr.BooleanExpression;
import com.parsleyj.smallsteptrack.Store;

/**
 * Created by Giuseppe on 10/03/16.
 */
public class WhileCommand implements Command {
    private final BooleanExpression condition;
    private final Command com;

    public WhileCommand(BooleanExpression condition, Command com) {
        this.condition = condition;
        this.com = com;
    }

    @Override
    public Command step(Store x) {
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
    public Integer getIntValue() {
        return null;
    }

    @Override
    public Boolean getBooleanValue() {
        return null;
    }

    @Override
    public String toString() {
        return "while " + condition + " do " + com;
    }
}

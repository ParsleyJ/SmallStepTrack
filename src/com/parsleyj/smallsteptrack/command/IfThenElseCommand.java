package com.parsleyj.smallsteptrack.command;

import com.parsleyj.smallsteptrack.booleanexpr.BooleanExpression;
import com.parsleyj.smallsteptrack.configuration.Configuration;

/**
 * Semantic object representing the if-then-else statement
 */
public class IfThenElseCommand implements Command {

    private final BooleanExpression cond;
    private final Command thenComm;
    private final Command elseComm;

    public IfThenElseCommand(BooleanExpression cond, Command thenComm, Command elseComm) {
        this.cond = cond;
        this.thenComm = thenComm;
        this.elseComm = elseComm;
    }

    @Override
    public Command step(Configuration c) {
        if (!cond.isTerminal()) {
            BooleanExpression cond1 = cond.step(c);
            return new IfThenElseCommand(cond1, thenComm, elseComm);
        } else if (cond.getBooleanValue()) {
            return thenComm;
        } else {
            return elseComm;
        }
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public String toString() {
        return "if " + cond + " then " + thenComm + " else " + elseComm;
    }
}

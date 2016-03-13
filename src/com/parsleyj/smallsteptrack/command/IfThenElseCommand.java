package com.parsleyj.smallsteptrack.command;

import com.parsleyj.smallsteptrack.booleanexpr.BooleanExpression;
import com.parsleyj.smallsteptrack.Store;

/**
 * Created by Giuseppe on 10/03/16.
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
    public Command step(Store x) {
        if (!cond.isTerminal()) {
            BooleanExpression cond1 = cond.step(x);
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

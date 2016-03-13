package com.parsleyj.smallsteptrack.command;

import com.parsleyj.smallsteptrack.integerexpr.IntegerExpression;
import com.parsleyj.smallsteptrack.Store;
import com.parsleyj.smallsteptrack.integerexpr.Variable;

/**
 * Semantic object representing the assignment operation
 */
public class Assignment implements Command {
    private final Variable a;
    private final IntegerExpression y;

    public Assignment(Variable a, IntegerExpression y) {
        this.a = a;
        this.y = y;
    }

    @Override
    public Command step(Store x) {
        if (!y.isTerminal()) {
            IntegerExpression y1 = y.step(x);
            return new Assignment(a, y1);

        } else { // y is terminal
            x.write(a.getName(), y.getIntValue());
            return new Skip();
        }
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public String toString() {
        return a + " := " + y;
    }
}

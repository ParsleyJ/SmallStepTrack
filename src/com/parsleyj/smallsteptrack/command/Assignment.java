package com.parsleyj.smallsteptrack.command;

import com.parsleyj.toolparser.configuration.Configuration;
import com.parsleyj.smallsteptrack.configurations.IntegerStore;
import com.parsleyj.smallsteptrack.integerexpr.IntegerExpression;
import com.parsleyj.smallsteptrack.integerexpr.Variable;

/**
 * Semantic object representing the assignment operation
 */
public class Assignment implements Command {
    private String storeName;
    private final Variable a;
    private final IntegerExpression y;

    public Assignment(String storeName, Variable a, IntegerExpression y) {
        this.storeName = storeName;
        this.a = a;
        this.y = y;
    }

    @Override
    public Command step(Configuration c) {
        if (!y.isTerminal()) {
            IntegerExpression y1 = y.step(c);
            return new Assignment(storeName, a, y1);

        } else { // y is terminal
            ((IntegerStore)c.getConfigurationElement(storeName)).write(a.getName(), y.getIntValue());
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

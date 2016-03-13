package com.parsleyj.smallsteptrack.integerexpr;

import com.parsleyj.smallsteptrack.configuration.Configuration;

/**
 * Semantic object representing the sum operation
 */
public class Sum implements IntegerExpression {
    private IntegerExpression a;
    private IntegerExpression b;

    public Sum(IntegerExpression a, IntegerExpression b) {
        this.a = a;
        this.b = b;
    }


    @Override
    public IntegerExpression step(Configuration c) {
        if (!a.isTerminal()) {
            IntegerExpression a1 = a.step(c);
            return new Sum(a1, b);

        } else if (!b.isTerminal()) { //a is terminal (numeral) but b is not terminal
            IntegerExpression b1 = b.step(c);
            return new Sum(a, b1);

        } else { // both are terminal
            return new Numeral(a.getIntValue() + b.getIntValue());
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
    public String toString() {
        return a + " + " + b;
    }
}

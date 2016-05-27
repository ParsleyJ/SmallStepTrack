package com.parsleyj.smallsteptrack.integerexpr;

import com.parsleyj.toolparser.configuration.Configuration;

/**
 * Semantic object representing the subtraction operation
 */
public class Subtraction implements IntegerExpression {
    private final IntegerExpression a;
    private final IntegerExpression b;

    public Subtraction(IntegerExpression a, IntegerExpression b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public IntegerExpression step(Configuration c) {
        if (!a.isTerminal()) {
            IntegerExpression a1 = a.step(c);
            return new Subtraction(a1, b);

        } else if (!b.isTerminal()) { //a is terminal (numeral) but b is not terminal
            IntegerExpression b1 = b.step(c);
            return new Subtraction(a, b1);

        } else { // both are terminal
            return new Numeral(a.getIntValue() - b.getIntValue());
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
        return a + " - " + b;
    }
}

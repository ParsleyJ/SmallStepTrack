package com.parsleyj.smallsteptrack.whilesemantics.integerexpr;

import com.parsleyj.smallsteptrack.configurations.Configuration;

/**
 * Semantic object representing the multiplication operation
 */
public class Multiplication implements IntegerExpression {
    private IntegerExpression a;
    private IntegerExpression b;

    public Multiplication(IntegerExpression a, IntegerExpression b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public IntegerExpression step(Configuration c) {
        if (!a.isTerminal()) {
            IntegerExpression a1 = a.step(c);
            return new Multiplication(a1, b);

        } else if (!b.isTerminal()) { //a is terminal (numeral) but b is not terminal
            IntegerExpression b1 = b.step(c);
            return new Multiplication(a, b1);

        } else { // both are terminal
            return new Numeral(a.getIntValue() * b.getIntValue());
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
        return a + " * " + b;
    }
}

package com.parsleyj.smallsteptrack.whilesemantics.integerexpr;

import com.parsleyj.smallsteptrack.configurations.Configuration;

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
        if (!a.isTerminal()) { // if a can make a step of computation:
            IntegerExpression a1 = a.step(c); // a makes a step
            return new Sum(a1, b); // return a new Sum with a(modified) and b.

        } else if (!b.isTerminal()) { //if a cannot make a step, but b can:
            IntegerExpression b1 = b.step(c); // b makes a step
            return new Sum(a, b1); // return a new Sum with a and b(modified)

        } else { // neither a or b can make a step: they are both numerals!

            //return a new numeral corresponding to the sum of the two numeral operands:
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

package com.parsleyj.smallsteptrack.booleanexpr;

import com.parsleyj.smallsteptrack.configuration.Configuration;
import com.parsleyj.smallsteptrack.integerexpr.IntegerExpression;

/**
 * Semantic object representing a 'is greater than' comparison between integers.
 */
public class GreaterIntegerComparison implements BooleanExpression {
    private final IntegerExpression a;
    private final IntegerExpression b;

    public GreaterIntegerComparison(IntegerExpression a, IntegerExpression b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public BooleanExpression step(Configuration c) {
        if (!a.isTerminal()) {
            IntegerExpression a1 = a.step(c);
            return new GreaterIntegerComparison(a1, b);

        } else if (!b.isTerminal()) {
            IntegerExpression b1 = b.step(c);
            return new GreaterIntegerComparison(a, b1);

        } else {
            if (a.getIntValue() > b.getIntValue()) {
                return new True();
            } else {
                return new False();
            }
        }
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public Boolean getBooleanValue() {
        return null;
    }

    @Override
    public String toString() {
        return a + " > " + b;
    }
}

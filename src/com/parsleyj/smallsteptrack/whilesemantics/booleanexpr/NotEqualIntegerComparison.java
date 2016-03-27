package com.parsleyj.smallsteptrack.whilesemantics.booleanexpr;

import com.parsleyj.smallsteptrack.configurations.Configuration;
import com.parsleyj.smallsteptrack.whilesemantics.integerexpr.IntegerExpression;

import java.util.Objects;

/**
 * Semantic object representing a 'is not equal to' comparison between integers.
 */
public class NotEqualIntegerComparison implements BooleanExpression {
    private final IntegerExpression a;
    private final IntegerExpression b;

    public NotEqualIntegerComparison(IntegerExpression a, IntegerExpression b) {
        this.a = a;
        this.b = b;
    }


    @Override
    public BooleanExpression step(Configuration c) {
        if (!a.isTerminal()) {
            IntegerExpression a1 = a.step(c);
            return new NotEqualIntegerComparison(a1, b);

        } else if (!b.isTerminal()) {
            IntegerExpression b1 = b.step(c);
            return new NotEqualIntegerComparison(a, b1);

        } else {
            if (!Objects.equals(a.getIntValue(), b.getIntValue())) {
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
        return a + " != " + b;
    }
}

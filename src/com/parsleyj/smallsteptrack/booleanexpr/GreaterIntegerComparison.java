package com.parsleyj.smallsteptrack.booleanexpr;

import com.parsleyj.smallsteptrack.integerexpr.IntegerExpression;
import com.parsleyj.smallsteptrack.Store;

/**
 * Created by Giuseppe on 10/03/16.
 */
public class GreaterIntegerComparison implements BooleanExpression {
    private final IntegerExpression a;
    private final IntegerExpression b;

    public GreaterIntegerComparison(IntegerExpression a, IntegerExpression b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public BooleanExpression step(Store x) {
        if (!a.isTerminal()) {
            IntegerExpression a1 = a.step(x);
            return new GreaterIntegerComparison(a1, b);

        } else if (!b.isTerminal()) {
            IntegerExpression b1 = b.step(x);
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
    public Integer getIntValue() {
        return null;
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

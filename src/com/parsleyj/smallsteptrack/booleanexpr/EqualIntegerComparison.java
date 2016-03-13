package com.parsleyj.smallsteptrack.booleanexpr;

import com.parsleyj.smallsteptrack.Store;
import com.parsleyj.smallsteptrack.integerexpr.IntegerExpression;

import java.util.Objects;

/**
 * Created by Giuseppe on 10/03/16.
 */
public class EqualIntegerComparison implements BooleanExpression {
    private final IntegerExpression a;
    private final IntegerExpression b;

    public EqualIntegerComparison(IntegerExpression a, IntegerExpression b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public BooleanExpression step(Store x) {
        if (!a.isTerminal()) {
            IntegerExpression a1 = a.step(x);
            return new EqualIntegerComparison(a1, b);

        } else if (!b.isTerminal()) {
            IntegerExpression b1 = b.step(x);
            return new EqualIntegerComparison(a, b1);

        } else {
            if (Objects.equals(a.getIntValue(), b.getIntValue())) {
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
        return a + " = " + b;
    }
}

package com.parsleyj.smallsteptrack.booleanexpr;

import com.parsleyj.smallsteptrack.Store;

/**
 *  Semantic object representing the {@code false} constant value
 */
public class False implements BooleanExpression {

    @Override
    public BooleanExpression step(Store x) {
        return null;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    @Override
    public Boolean getBooleanValue() {
        return false;
    }

    @Override
    public String toString() {
        return "false";
    }
}

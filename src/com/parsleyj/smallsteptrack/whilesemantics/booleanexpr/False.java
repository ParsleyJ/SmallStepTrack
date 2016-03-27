package com.parsleyj.smallsteptrack.whilesemantics.booleanexpr;

import com.parsleyj.smallsteptrack.configurations.Configuration;

/**
 *  Semantic object representing the {@code false} constant value
 */
public class False implements BooleanExpression {

    @Override
    public BooleanExpression step(Configuration x) {
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

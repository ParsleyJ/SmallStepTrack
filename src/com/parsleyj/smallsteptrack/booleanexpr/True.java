package com.parsleyj.smallsteptrack.booleanexpr;

import com.parsleyj.smallsteptrack.configuration.Configuration;

/**
 * Semantic object representing the {@code true} constant value
 */
public class True implements BooleanExpression {

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
        return true;
    }

    @Override
    public String toString() {
        return "true";
    }
}

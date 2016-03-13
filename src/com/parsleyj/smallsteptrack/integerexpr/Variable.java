package com.parsleyj.smallsteptrack.integerexpr;

import com.parsleyj.smallsteptrack.Store;

/**
 * Semantic object representing a variable.
 */
public class Variable implements IntegerExpression {
    private String name;

    public Variable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public IntegerExpression step(Store x) {
        return new Numeral(x.read(name));
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
        return name;
    }
}

package com.parsleyj.smallsteptrack.integerexpr;

import com.parsleyj.smallsteptrack.configuration.Configuration;

/**
 * Semantic object representing a numeral.
 */
public class Numeral implements IntegerExpression {
    private int i;

    public Numeral(int i) {
        this.i = i;
    }

    @Override
    public IntegerExpression step(Configuration c) {
        return null;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    @Override
    public Integer getIntValue() {
        return i;
    }



    @Override
    public String toString() {
        return "" + i;
    }
}

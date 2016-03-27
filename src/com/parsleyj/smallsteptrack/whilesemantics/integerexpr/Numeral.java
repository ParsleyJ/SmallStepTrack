package com.parsleyj.smallsteptrack.whilesemantics.integerexpr;

import com.parsleyj.smallsteptrack.configurations.Configuration;

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

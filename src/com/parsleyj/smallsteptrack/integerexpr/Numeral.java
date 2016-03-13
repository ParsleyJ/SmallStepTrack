package com.parsleyj.smallsteptrack.integerexpr;

import com.parsleyj.smallsteptrack.Store;

/**
 * Semantic object representing a numeral.
 */
public class Numeral implements IntegerExpression {
    private int i;

    public Numeral(int i) {
        this.i = i;
    }

    @Override
    public IntegerExpression step(Store x) {
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

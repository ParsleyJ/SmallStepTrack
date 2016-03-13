package com.parsleyj.smallsteptrack.booleanexpr;

import com.parsleyj.smallsteptrack.Store;

/**
 * Created by Giuseppe on 10/03/16.
 */
public class True implements BooleanExpression {

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
        return true;
    }

    @Override
    public String toString() {
        return "true";
    }
}

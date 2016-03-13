package com.parsleyj.smallsteptrack.booleanexpr;

import com.parsleyj.smallsteptrack.Store;

/**
 * Created by Giuseppe on 10/03/16.
 */
public class ShortCircuitOr implements BooleanExpression {
    private final BooleanExpression a;
    private final BooleanExpression b;

    public ShortCircuitOr(BooleanExpression a, BooleanExpression b){

        this.a = a;
        this.b = b;
    }


    @Override
    public BooleanExpression step(Store x) {
        if(!a.isTerminal()){
            BooleanExpression a1 = a.step(x);
            return new ShortCircuitOr(a1, b);
        } else {
            if(a.getBooleanValue()){
                return new True();
            } else {
                return b;
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
        return a + " | " + b;
    }
}

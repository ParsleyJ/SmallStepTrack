package com.parsleyj.smallsteptrack.booleanexpr;

import com.parsleyj.smallsteptrack.Store;

/**
 * Semantic object representing a the logical short-circuit 'and' boolean operator.
 */
public class ShortCircuitAnd implements BooleanExpression {
    private final BooleanExpression a;
    private final BooleanExpression b;

    public ShortCircuitAnd(BooleanExpression a, BooleanExpression b){

        this.a = a;
        this.b = b;
    }


    @Override
    public BooleanExpression step(Store x) {
        if(!a.isTerminal()){
            BooleanExpression a1 = a.step(x);
            return new ShortCircuitAnd(a1, b);
        } else {
            if(a.getBooleanValue()){
                return b;
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
        return a + " & " + b;
    }
}

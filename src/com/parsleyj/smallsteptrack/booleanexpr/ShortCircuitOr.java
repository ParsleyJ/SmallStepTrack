package com.parsleyj.smallsteptrack.booleanexpr;

import com.parsleyj.toolparser.configuration.Configuration;

/**
 * Semantic object representing a the logical short-circuit 'or' boolean operator.
 */
public class ShortCircuitOr implements BooleanExpression {
    private final BooleanExpression a;
    private final BooleanExpression b;

    public ShortCircuitOr(BooleanExpression a, BooleanExpression b){

        this.a = a;
        this.b = b;
    }


    @Override
    public BooleanExpression step(Configuration c) {
        if(!a.isTerminal()){
            BooleanExpression a1 = a.step(c);
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

package com.parsleyj.smallsteptrack.booleanexpr;

import com.parsleyj.smallsteptrack.Store;

/**
 * Created by Giuseppe on 10/03/16.
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
    public Integer getIntValue() {
        return null;
    }

    @Override
    public Boolean getBooleanValue() {
        return null;
    }
}

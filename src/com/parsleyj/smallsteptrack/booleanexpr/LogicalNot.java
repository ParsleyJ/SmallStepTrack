package com.parsleyj.smallsteptrack.booleanexpr;

import com.parsleyj.smallsteptrack.Store;

/**
 * Created by Giuseppe on 10/03/16.
 */
public class LogicalNot implements BooleanExpression {

    private BooleanExpression a;

    public LogicalNot(BooleanExpression a) {
        this.a = a;
    }

    @Override
    public BooleanExpression step(Store x) {
        if(!a.isTerminal()){
            BooleanExpression a1 = a.step(x);
            return new LogicalNot(a1);
        }else{
            if(a.getBooleanValue()){
                return new False();
            } else {
                return new True();
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

    @Override
    public String toString() {
        return "!"+a;
    }
}

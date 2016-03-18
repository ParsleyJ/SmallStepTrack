package com.parsleyj.smallsteptrack.booleanexpr;

import com.parsleyj.smallsteptrack.configuration.Configuration;

/**
 * Semantic object representing a the logical 'not' boolean operator.
 */
public class LogicalNot implements BooleanExpression {

    private BooleanExpression a;

    public LogicalNot(BooleanExpression a) {
        this.a = a;
    }

    @Override
    public BooleanExpression step(Configuration c) {
        if(!a.isTerminal()){
            BooleanExpression a1 = a.step(c);
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
    public Boolean getBooleanValue() {
        return null;
    }

    @Override
    public String toString() {
        return "!"+a;
    }


}

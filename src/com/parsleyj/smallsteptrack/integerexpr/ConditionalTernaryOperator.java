package com.parsleyj.smallsteptrack.integerexpr;

import com.parsleyj.smallsteptrack.booleanexpr.BooleanExpression;
import com.parsleyj.smallsteptrack.configuration.Configuration;

/**
 * Semantic object representing the conditional ternary operator ('condition ? a : b')
 */
public class ConditionalTernaryOperator implements IntegerExpression {
    private BooleanExpression condition;
    private IntegerExpression thenExp;
    private IntegerExpression elseExp;

    public ConditionalTernaryOperator(BooleanExpression condition, IntegerExpression thenExp, IntegerExpression elseExp) {
        this.condition = condition;
        this.thenExp = thenExp;
        this.elseExp = elseExp;
    }

    @Override
    public IntegerExpression step(Configuration c) {
        if(!condition.isTerminal()){
            BooleanExpression cond1 = condition.step(c);
            return new ConditionalTernaryOperator(cond1, thenExp, elseExp);
        } else {
            if(condition.getBooleanValue()){
                return thenExp;
            }else{
                return elseExp;
            }
        }
    }

    @Override
    public Integer getIntValue() {
        return null;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public String toString() {
        return "" + condition + " ? " + thenExp + " : " + elseExp;
    }
}

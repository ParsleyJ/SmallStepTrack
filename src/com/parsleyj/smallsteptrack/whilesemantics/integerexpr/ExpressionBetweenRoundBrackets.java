package com.parsleyj.smallsteptrack.whilesemantics.integerexpr;

import com.parsleyj.smallsteptrack.configurations.Configuration;

/**
 * Semantic object representing a boolean expression surrounded by round brackets.
 */
public class ExpressionBetweenRoundBrackets implements IntegerExpression{
    private IntegerExpression e;

    public ExpressionBetweenRoundBrackets(IntegerExpression e){
        this.e = e;
    }
    @Override
    public IntegerExpression step(Configuration c) {
        if(!e.isTerminal()){
            IntegerExpression e1 = e.step(c);
            return new ExpressionBetweenRoundBrackets(e1);
        } else {
            return e;
        }
    }

    @Override
    public String toString() {
        return "(" + e + ")";
    }
}

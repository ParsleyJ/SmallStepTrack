package com.parsleyj.smallsteptrack.booleanexpr;

import com.parsleyj.toolparser.configuration.Configuration;

/**
 * Semantic object representing a boolean expression surrounded by round brackets.
 */
public class BooleanExpressionBetweenRoundBrackets implements BooleanExpression {
    private BooleanExpression e;

    public BooleanExpressionBetweenRoundBrackets(BooleanExpression e) {
        this.e = e;
    }

    @Override
    public BooleanExpression step(Configuration c) {
        if(!e.isTerminal()){
            BooleanExpression e1 = e.step(c);
            return new BooleanExpressionBetweenRoundBrackets(e1);
        }else return e;
    }

    @Override
    public String toString() {
        return "(" + e + ")";
    }
}

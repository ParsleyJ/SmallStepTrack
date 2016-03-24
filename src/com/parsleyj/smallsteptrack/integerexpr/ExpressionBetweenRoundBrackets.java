package com.parsleyj.smallsteptrack.integerexpr;

import com.parsleyj.smallsteptrack.configuration.Configuration;

/**
 * Created by Giuseppe on 24/03/16.
 * TODO: javadoc
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

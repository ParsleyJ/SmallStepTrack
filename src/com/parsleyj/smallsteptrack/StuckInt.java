package com.parsleyj.smallsteptrack;

import com.parsleyj.toolparser.configuration.Configuration;
import com.parsleyj.smallsteptrack.integerexpr.IntegerExpression;

/**
 * Semantic object representing a stuck configuration where should be an integer expression
 */
public class StuckInt implements IntegerExpression {
    @Override
    public IntegerExpression step(Configuration c) {
        throw new StuckConfigurationException();
    }

    @Override
    public String toString() {
        return "?STUCK?";
    }
}

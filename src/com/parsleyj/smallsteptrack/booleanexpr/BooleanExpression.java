package com.parsleyj.smallsteptrack.booleanexpr;

import com.parsleyj.toolparser.configuration.Configuration;
import com.parsleyj.toolparser.semanticsconverter.SemanticObject;

/**
 * Common interface of all boolean expressions.
 */
public interface BooleanExpression extends SemanticObject {

    /**
     * Make a computational step.
     * This method should return {@code null} if the {@link #isTerminal()} method returns {@code true}.
     * @param c the configuration, that may change during computation.
     * @return a {@link BooleanExpression} representing the original expression modified by the step of computation.
     */
    BooleanExpression step(Configuration c);

    /**
     * Returns the actual Boolean value of the object.
     * This must return {@code null} if {@link #isTerminal()} returns {@code false}.
     * @return the actual Boolean value of this semantic object, or {@code null}.
     */
    default Boolean getBooleanValue(){
        return null;
    }
}

package com.parsleyj.smallsteptrack.whilesemantics.integerexpr;

import com.parsleyj.smallsteptrack.configurations.Configuration;
import com.parsleyj.smallsteptrack.semanticsconverter.SemanticObject;

/**
 * Common interface of all integer expressions.
 */
public interface IntegerExpression extends SemanticObject {

    /**
     * Make a computational step.
     * This method should return {@code null} if the {@link #isTerminal()} method returns {@code true}.
     * @param c the configuration, that may change during computation.
     * @return a {@link IntegerExpression} representing the original expression modified by the step of computation.
     */
    IntegerExpression step(Configuration c);

    /**
     * Returns the actual Integer value of the object.
     * This must return {@code null} if {@link #isTerminal()} returns {@code false}.
     * @return the actual Integer value of this semantic object, or {@code null}.
     */
    default Integer getIntValue(){
        return null;
    }
}
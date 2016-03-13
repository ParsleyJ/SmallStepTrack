package com.parsleyj.smallsteptrack.integerexpr;

import com.parsleyj.smallsteptrack.SmallStepSemanticObject;
import com.parsleyj.smallsteptrack.Store;

/**
 * Common interface of all integer expressions.
 */
public interface IntegerExpression extends SmallStepSemanticObject {

    /**
     * Make a computational step.
     * This method should return {@code null} if the {@link #isTerminal()} method returns {@code true}.
     * @param x the Store
     * @return a {@link IntegerExpression} representing the original expression modified by the step of computation.
     */
    IntegerExpression step(Store x);

    /**
     * Returns the actual Integer value of the object.
     * This must return {@code null} if {@link #isTerminal()} returns {@code false}.
     * @return the actual Integer value of this semantic object, or {@code null}.
     */
    Integer getIntValue();
}

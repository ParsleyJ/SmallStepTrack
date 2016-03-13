package com.parsleyj.smallsteptrack.booleanexpr;

import com.parsleyj.smallsteptrack.SmallStepSemanticObject;
import com.parsleyj.smallsteptrack.Store;

/**
 * Common interface of all boolean expressions.
 */
public interface BooleanExpression extends SmallStepSemanticObject {

    /**
     * Make a computational step.
     * This method should return {@code null} if the {@link #isTerminal()} method returns {@code true}.
     * @param x the Store
     * @return a {@link BooleanExpression} representing the original expression modified by the step of computation.
     */
    BooleanExpression step(Store x);

    /**
     * Returns the actual Boolean value of the object.
     * This must return {@code null} if {@link #isTerminal()} returns {@code false}.
     * @return the actual Boolean value of this semantic object, or {@code null}.
     */
    Boolean getBooleanValue();
}

package com.parsleyj.smallsteptrack;

/**
 * Common interface of all the semantic definitions of the language.
 */
public interface SmallStepSemanticObject {

    /**
     * Used to determine if the object is terminal ( = it cannot do other steps of computation).
     * @return {@code false} if the object cannot do other step of computation, {@code true} otherwise.
     */
    boolean isTerminal();

}

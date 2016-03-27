package com.parsleyj.smallsteptrack.semanticsconverter;

/**
 * Common interface of all the semantic definitions of the language.
 */
public interface SemanticObject {

    /**
     * Used to determine if the object is terminal ( = it cannot do other steps of computation).
     * This is 'java8-default-implemented' in the interface as always returning {@code false}, because it's the
     * behavior of most of the implementing classes. You actually only need to implement this if you want to
     * define a terminal semantic object.
     * @return {@code false} if the object cannot do other step of computation, {@code true} otherwise.
     */
    default boolean isTerminal(){
        return false;
    }

}

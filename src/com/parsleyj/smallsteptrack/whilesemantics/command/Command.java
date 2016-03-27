package com.parsleyj.smallsteptrack.whilesemantics.command;

import com.parsleyj.smallsteptrack.configurations.Configuration;
import com.parsleyj.smallsteptrack.semanticsconverter.SemanticObject;

/**
 * Common interface of all commands of the While language.
 */
public interface Command extends SemanticObject {
    /**
     * Make a computational step.
     * This method should return {@code null} if the {@link #isTerminal()} method returns {@code true}.
     * @param c the configuration, that may change during computation.
     * @return a {@link Command} representing the original command modified by the step of computation.
     */
    Command step(Configuration c);
}

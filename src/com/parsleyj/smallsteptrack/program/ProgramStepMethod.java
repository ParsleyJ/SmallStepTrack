package com.parsleyj.smallsteptrack.program;

import com.parsleyj.smallsteptrack.configuration.Configuration;

/**
 * Functional interface used to define how a {@link Program} makes
 * a computational step.
 */
@FunctionalInterface
public interface ProgramStepMethod {
    boolean step(Program program, Configuration configuration);
}

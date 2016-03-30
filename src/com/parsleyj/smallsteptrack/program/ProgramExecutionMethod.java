package com.parsleyj.smallsteptrack.program;

import com.parsleyj.smallsteptrack.configurations.Configuration;

/**
 * Functional interface used to define how a {@link Program} makes
 * a computational step.
 */
@FunctionalInterface
public interface ProgramExecutionMethod {
    boolean step(Program program, Configuration configuration);
}

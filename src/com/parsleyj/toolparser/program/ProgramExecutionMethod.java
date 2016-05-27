package com.parsleyj.toolparser.program;

import com.parsleyj.toolparser.configuration.Configuration;

/**
 * Functional interface used to defineParameter how a {@link Program} makes
 * a computational execute.
 */
@FunctionalInterface
public interface ProgramExecutionMethod {
    boolean execute(Program program, Configuration configuration);
}

package com.parsleyj.smallsteptrack.program;

import com.parsleyj.smallsteptrack.configuration.Configuration;

/**
 * Created by Giuseppe on 23/03/16.
 * TODO: javadoc
 */
public interface ProgramStepMethod {
    boolean step(Program program, Configuration configuration);
}

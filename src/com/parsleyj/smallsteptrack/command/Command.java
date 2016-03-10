package com.parsleyj.smallsteptrack.command;

import com.parsleyj.smallsteptrack.SemanticObject;
import com.parsleyj.smallsteptrack.Store;

/**
 * Created by Giuseppe on 09/03/16.
 */
public interface Command extends SemanticObject {
    Command step(Store x);
}

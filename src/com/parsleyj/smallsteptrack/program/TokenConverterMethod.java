package com.parsleyj.smallsteptrack.program;

import com.parsleyj.smallsteptrack.SmallStepSemanticObject;

/**
 * Created by Giuseppe on 24/03/16.
 * TODO: javadoc
 */
@FunctionalInterface
public interface TokenConverterMethod {
    SmallStepSemanticObject convert(String generatingString, Semantics s);
}

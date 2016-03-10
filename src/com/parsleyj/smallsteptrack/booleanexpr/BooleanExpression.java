package com.parsleyj.smallsteptrack.booleanexpr;

import com.parsleyj.smallsteptrack.SemanticObject;
import com.parsleyj.smallsteptrack.Store;

/**
 * Created by Giuseppe on 09/03/16.
 */
public interface BooleanExpression extends SemanticObject {
    BooleanExpression step(Store x);
}

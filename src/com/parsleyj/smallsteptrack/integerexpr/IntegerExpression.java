package com.parsleyj.smallsteptrack.integerexpr;

import com.parsleyj.smallsteptrack.SemanticObject;
import com.parsleyj.smallsteptrack.Store;

/**
 * Created by Giuseppe on 09/03/16.
 */
public interface IntegerExpression extends SemanticObject {
    IntegerExpression step(Store x);
}

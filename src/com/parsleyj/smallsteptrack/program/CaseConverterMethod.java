package com.parsleyj.smallsteptrack.program;

import com.parsleyj.smallsteptrack.SmallStepSemanticObject;
import com.parsleyj.smallsteptrack.parser.SyntaxTreeNode;

/**
 * Created by Giuseppe on 24/03/16.
 * TODO: javadoc
 */
@FunctionalInterface
public interface CaseConverterMethod {
    SmallStepSemanticObject convert(SyntaxTreeNode node, Semantics s);
}

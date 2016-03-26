package com.parsleyj.smallsteptrack.program;

import com.parsleyj.smallsteptrack.SemanticObject;
import com.parsleyj.smallsteptrack.parser.ParseTreeNode;

/**
 * A functional interface used to define a method to convert a
 * non-terminal {@link ParseTreeNode} to a {@link SemanticObject}.
 */
@FunctionalInterface
public interface CaseConverterMethod {
    SemanticObject convert(ParseTreeNode node, SemanticsConverter s);
}

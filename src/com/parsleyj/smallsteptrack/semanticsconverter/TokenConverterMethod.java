package com.parsleyj.smallsteptrack.semanticsconverter;

import com.parsleyj.smallsteptrack.parser.ParseTreeNode;

/**
 * A functional interface used to define a method to convert a
 * terminal {@link ParseTreeNode} to a {@link SemanticObject}.
 */
@FunctionalInterface
public interface TokenConverterMethod {
    SemanticObject convert(String generatingString, SemanticsConverter s);
}

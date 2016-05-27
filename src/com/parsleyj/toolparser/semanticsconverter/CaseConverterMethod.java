package com.parsleyj.toolparser.semanticsconverter;

import com.parsleyj.toolparser.parser.ParseTreeNode;

/**
 * A functional interface used to defineParameter a method to convert a
 * non-terminal {@link ParseTreeNode} to a {@link SemanticObject}.
 */
@FunctionalInterface
public interface CaseConverterMethod {
    SemanticObject convert(ParseTreeNode node, SemanticsConverter s);
}

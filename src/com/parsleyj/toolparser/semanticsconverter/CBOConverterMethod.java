package com.parsleyj.toolparser.semanticsconverter;

import com.parsleyj.toolparser.parser.ParseTreeNode;

/**
 * Helper class used to easily interpret a {@link CaseConverterMethod} for
 * closed binary operations, i.e. those binary operations which result is
 * of the same semantic type of its operands.
 */
public class CBOConverterMethod<T extends SemanticObject>  implements CaseConverterMethod {
    private BinaryOperationConverterMethod<T> method;
    public CBOConverterMethod(BinaryOperationConverterMethod<T> method) {
        this.method = method;
    }

    @Override
    public SemanticObject convert(ParseTreeNode node, SemanticsConverter s) {
        return method.convert(s.convert(node.get(0)), s.convert(node.get(2)));
    }

    @FunctionalInterface
    public interface BinaryOperationConverterMethod<T>{
        T convert(T a, T b);
    }
}

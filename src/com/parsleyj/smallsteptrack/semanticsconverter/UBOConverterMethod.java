package com.parsleyj.smallsteptrack.semanticsconverter;

import com.parsleyj.smallsteptrack.parser.ParseTreeNode;

/**
 * Helper class used to easily generate a {@link CaseConverterMethod} for
 * not closed (unclosed) binary operations, i.e. those binary operations which
 * result can be of a different semantic type of its operands.
 */
public class UBOConverterMethod
        <R extends SemanticObject, T1 extends SemanticObject, T2 extends SemanticObject>
        implements CaseConverterMethod{
    private BinaryOperationConverterMethod<R, T1, T2> method;
    public UBOConverterMethod(BinaryOperationConverterMethod<R, T1, T2> method) {
        this.method = method;
    }

    @Override
    public SemanticObject convert(ParseTreeNode node, SemanticsConverter s) {
        return method.convert(s.convert(node.get(0)), s.convert(node.get(2)));
    }

    @FunctionalInterface
    public interface BinaryOperationConverterMethod<R, T1, T2>{
        R convert(T1 a, T2 b);
    }
}

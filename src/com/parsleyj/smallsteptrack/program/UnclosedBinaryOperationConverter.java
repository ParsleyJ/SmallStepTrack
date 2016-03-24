package com.parsleyj.smallsteptrack.program;

import com.parsleyj.smallsteptrack.SmallStepSemanticObject;
import com.parsleyj.smallsteptrack.parser.SyntaxCase;

/**
 * Created by Giuseppe on 24/03/16.
 * TODO: javadoc
 */
public class UnclosedBinaryOperationConverter
        <R extends SmallStepSemanticObject, T1 extends SmallStepSemanticObject, T2 extends SmallStepSemanticObject>  extends CheckedCaseConverter {
    public UnclosedBinaryOperationConverter(SyntaxCase casE, BinaryOperationConverterMethod<R, T1, T2> method) {
        super(casE, ((node, s) -> method.convert(s.resolve(node.get(0)), s.resolve(node.get(2)))));
    }

    @FunctionalInterface
    public interface BinaryOperationConverterMethod<R, T1, T2>{
        R convert(T1 a, T2 b);
    }
}

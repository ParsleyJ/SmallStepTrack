package com.parsleyj.smallsteptrack.program;

import com.parsleyj.smallsteptrack.SmallStepSemanticObject;
import com.parsleyj.smallsteptrack.parser.SyntaxCase;

/**
 * Created by Giuseppe on 24/03/16.
 * TODO: javadoc
 */
public class ClosedBinaryOperationConverter<T extends SmallStepSemanticObject>  extends CheckedCaseConverter {
    public ClosedBinaryOperationConverter(SyntaxCase casE, BinaryOperationConverterMethod<T> method) {
        super(casE, ((node, s) -> method.convert(s.resolve(node.get(0)), s.resolve(node.get(2)))));
    }

    @FunctionalInterface
    public interface BinaryOperationConverterMethod<T>{
        T convert(T a, T b);
    }
}

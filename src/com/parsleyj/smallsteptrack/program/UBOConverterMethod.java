package com.parsleyj.smallsteptrack.program;

import com.parsleyj.smallsteptrack.SmallStepSemanticObject;
import com.parsleyj.smallsteptrack.parser.ParseTreeNode;

/**
 * Created by Giuseppe on 24/03/16.
 * TODO: javadoc
 */
public class UBOConverterMethod
        <R extends SmallStepSemanticObject, T1 extends SmallStepSemanticObject, T2 extends SmallStepSemanticObject>
        implements CaseConverterMethod{
    private BinaryOperationConverterMethod<R, T1, T2> method;
    public UBOConverterMethod(BinaryOperationConverterMethod<R, T1, T2> method) {
        this.method = method;
    }

    @Override
    public SmallStepSemanticObject convert(ParseTreeNode node, Semantics s) {
        return method.convert(s.resolve(node.get(0)), s.resolve(node.get(2)));
    }

    @FunctionalInterface
    public interface BinaryOperationConverterMethod<R, T1, T2>{
        R convert(T1 a, T2 b);
    }
}
